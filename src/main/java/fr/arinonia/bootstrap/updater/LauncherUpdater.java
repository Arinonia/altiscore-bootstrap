package fr.arinonia.bootstrap.updater;

import com.google.gson.Gson;
import fr.arinonia.bootstrap.updater.models.Dependency;
import fr.arinonia.bootstrap.updater.models.LauncherManifest;
import fr.arinonia.bootstrap.updater.utils.DownloadUtils;
import fr.arinonia.bootstrap.updater.utils.DownloadUtils.ProgressCallback;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LauncherUpdater {
    //why the fuck it's still here?
    private static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2";

    private final String manifestUrl;
    private final Path rootDir;
    private LauncherManifest manifest;
    private final Gson gson;

    public LauncherUpdater(final String manifestUrl, final Path rootDir) {
        this.manifestUrl = manifestUrl;
        this.rootDir = rootDir;
        this.gson = new Gson();
    }

    public CompletableFuture<Void> update(final ProgressCallback callback) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                downloadManifest();
                final int totalFiles = 1 + (this.manifest.getDependencies() != null ? this.manifest.getDependencies().size() : 0);
                int currentFile = 0;

                final Path mainJarPath = getMainJarPath();
                if (!DownloadUtils.verifyFile(mainJarPath,
                        this.manifest.getMainJar().getSha1(),
                        this.manifest.getMainJar().getSize())) {

                    System.out.println("Downloading main JAR: " + this.manifest.getMainJar().getName());
                    DownloadUtils.downloadFile(
                            this.manifest.getMainJar().getUrl(),
                            mainJarPath,
                            this.manifest.getMainJar().getSize(),
                            this.manifest.getMainJar().getSha1(),
                            progress -> callback.onProgress(progress / totalFiles)
                    );
                }
                currentFile++;

                if (this.manifest.getDependencies() != null) {
                    final int[] fileCounter = {currentFile}; // Mutable int for lambda

                    for (final Dependency dep : this.manifest.getDependencies()) {
                        final Path depPath = getDependencyPath(dep);
                        final String depUrl = getDependencyUrl(dep);

                        if (!DownloadUtils.verifyFile(depPath, dep.getSha1(), dep.getSize())) {
                            System.out.println("Downloading dependency: " + (dep.getName() != null ? dep.getName() : dep.getArtifactId()));
                            DownloadUtils.downloadFile(
                                    depUrl,
                                    depPath,
                                    dep.getSize(),
                                    dep.getSha1(),
                                    progress -> callback.onProgress((fileCounter[0] + progress) / totalFiles)
                            );
                        }
                        fileCounter[0]++;
                    }
                    currentFile = fileCounter[0];
                }

                return null;
            } catch (final Exception e) {
                throw new RuntimeException("Failed to update launcher", e);
            }
        });
    }

    private void downloadManifest() throws IOException {
        try (final InputStreamReader reader = new InputStreamReader(new URL(this.manifestUrl).openStream())) {
            this.manifest = gson.fromJson(reader, LauncherManifest.class);
        }
    }

    private Path getMainJarPath() {
        final String path = this.manifest.getMainJar().getPath();
        return path != null && !path.isEmpty()
                ? this.rootDir.resolve(path).resolve(this.manifest.getMainJar().getName())
                : this.rootDir.resolve(this.manifest.getMainJar().getName());
    }

    private Path getDependencyPath(final Dependency dep) {
       final  String basePath = dep.getPath() != null ? dep.getPath() : "libraries";

        if (dep.getType() == Dependency.DependencyType.MAVEN) {
            return this.rootDir.resolve(basePath).resolve(
                    String.format("%s/%s/%s/%s-%s.jar",
                            dep.getGroupId().replace('.', '/'),
                            dep.getArtifactId(),
                            dep.getVersion(),
                            dep.getArtifactId(),
                            dep.getVersion()
                    )
            );
        } else {
            return this.rootDir.resolve(basePath).resolve(dep.getName());
        }
    }

    private String getDependencyUrl(final Dependency dep) {
        if (dep.getType() == Dependency.DependencyType.MAVEN) {
            final String repo = dep.getRepository() != null ? dep.getRepository() : MAVEN_CENTRAL;
            return String.format("%s/%s/%s/%s/%s-%s.jar",
                    repo,
                    dep.getGroupId().replace('.', '/'),
                    dep.getArtifactId(),
                    dep.getVersion(),
                    dep.getArtifactId(),
                    dep.getVersion()
            );
        } else {
            return dep.getUrl();
        }
    }

    public List<String> getClasspath() {
        final List<String> classpath = new ArrayList<>();

        classpath.add(getMainJarPath().toAbsolutePath().toString());

        if (this.manifest.getDependencies() != null) {
            for (final Dependency dep : this.manifest.getDependencies()) {
                classpath.add(getDependencyPath(dep).toAbsolutePath().toString());
            }
        }

        return classpath;
    }

    public String getMainClass() {
        return manifest.getMainClass();
    }

    public String getVersion() {
        return manifest.getVersion();
    }

    public Path getRootDir() {
        return this.rootDir;
    }
}