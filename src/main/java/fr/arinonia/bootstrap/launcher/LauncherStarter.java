package fr.arinonia.bootstrap.launcher;

import fr.arinonia.bootstrap.logger.Logger;
import fr.arinonia.bootstrap.updater.LauncherUpdater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LauncherStarter {

    private final Path runtimePath;
    private final LauncherUpdater updater; //! Maybe use main class and "classPath" instead of launcherUpdater

    public LauncherStarter(final Path runtimePath, final LauncherUpdater launcherUpdater) {
        this.runtimePath = runtimePath;
        this.updater = launcherUpdater;
    }

    public void startLauncher() throws IOException {
        final List<String> command = new ArrayList<>();

        final String javaExecutable = System.getProperty("os.name").toLowerCase().contains("win") ? "javaw.exe" : "java";
        command.add(runtimePath.resolve("bin").resolve(javaExecutable).toString());

        command.add("-Xmx2G");

        command.add("-cp");
        command.add(String.join(File.pathSeparator, updater.getClasspath()));

        command.add(updater.getMainClass());

        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(updater.getRootDir().toFile());

        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Logger.info("Starting launcher with command: " + String.join(" ", command));

        try {
            final Process process = processBuilder.start();
            Thread.sleep(1000);

            if (!process.isAlive()) {
                int exitCode = process.exitValue();
                throw new IOException("Launcher process terminated immediately with exit code: " + exitCode);
            }

            System.exit(0);
        } catch (final InterruptedException e) {
            throw new IOException("Failed to start launcher", e);
        }
    }
}
