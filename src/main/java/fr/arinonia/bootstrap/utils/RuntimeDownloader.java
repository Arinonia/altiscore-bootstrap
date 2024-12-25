package fr.arinonia.bootstrap.utils;

import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.logger.Logger;
import fr.flowarg.azuljavadownloader.*;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class RuntimeDownloader {

    private final AzulJavaDownloader downloader;
    private final Path downloadPath;
    private final FileManager fileManager;

    //test

    public RuntimeDownloader(final Path downloadPath, final FileManager fileManager, final Callback callback) {
        this.downloader = new AzulJavaDownloader(callback);
        this.downloadPath = downloadPath;
        this.fileManager = fileManager;
    }

    //! I should rework this all part
    public CompletableFuture<Path> downloadJava21() {
        Logger.info(String.format("Starting Java 21 download to path: %s", this.downloadPath));

        return CompletableFuture.supplyAsync(() -> {
            try {
                Logger.info(String.format("Initiating download for platform: %s", OSDetector.getCurrentPlatform()));
                final AzulJavaBuildInfo buildInfoWindows = this.downloader.getBuildInfo(new RequestedJavaInfo("21", AzulJavaType.JDK, OSDetector.getCurrentPlatform(), AzulJavaArch.X64).setJavaFxBundled(true));
                final Path javaPath = this.downloader.downloadAndInstall(buildInfoWindows, this.downloadPath);
                this.fileManager.moveExtractedContent();
                return javaPath;
            } catch (final Exception e) {
                throw new RuntimeException("Failed to download Java runtime", e);
            }
        });
    }

}