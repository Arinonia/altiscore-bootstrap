package fr.arinonia.bootstrap.file;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.utils.OSDetector;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileManager {

    private final Path rootPath;
    private final Path runtimePath;

    public FileManager() {
        this.rootPath = this.initRootPath();
        this.runtimePath = this.rootPath.resolve("runtime");
        System.out.println(this.runtimePath);
    }

    public void createDirectories() {
        try {
            if (!Files.exists(this.rootPath)) {
                Files.createDirectories(this.rootPath);
            }

            if (!Files.exists(this.runtimePath)) {
                Files.createDirectories(this.runtimePath);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private Path initRootPath() {
        switch (OSDetector.getCurrentPlatform()) {
            case WINDOWS:
                return Paths.get(System.getenv("APPDATA"), BootstrapConfig.getInstance().getAltiscoreFolderName());
            case MACOS:
                return Paths.get(System.getProperty("user.home"), "Library", "Application Support", BootstrapConfig.getInstance().getAltiscoreFolderName().replaceAll("\\.", ""));
            case LINUX:
            default:
                return Paths.get(System.getProperty("user.home"), BootstrapConfig.getInstance().getAltiscoreFolderName());
        }
    }

    public void moveExtractedContent() throws IOException {
        try (final Stream<Path> paths = Files.list(this.runtimePath)) {
            final Path extractedDir = paths
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().contains("jdk") ||
                            p.getFileName().toString().contains("zulu"))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Dossier JDK extrait non trouvé"));

            System.out.println("Moving content from: " + extractedDir);

            try (final Stream<Path> files = Files.walk(extractedDir)) {
                files.filter(Files::isRegularFile).forEach(file -> {
                    try {
                        final Path relativePath = extractedDir.relativize(file);
                        final Path targetPath = this.runtimePath.resolve(relativePath);
                        Files.createDirectories(targetPath.getParent());
                        Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            try (final Stream<Path> pathsToDelete = Files.walk(extractedDir)
                    .sorted(Comparator.reverseOrder())) {
                for (final Path path : (Iterable<Path>) pathsToDelete::iterator) {
                    if (Files.isDirectory(path)) {
                        try (final Stream<Path> contents = Files.list(path)) {
                            if (!contents.findFirst().isPresent()) {
                                Files.delete(path);
                            }
                        }
                    }
                }
            }
        }
    }

    public Path getRootPath() {
        return this.rootPath;
    }

    public Path getRuntimePath() {
        return this.runtimePath;
    }
}