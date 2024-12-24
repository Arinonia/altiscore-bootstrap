package fr.arinonia.bootstrap.services;

import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.utils.RuntimeDetector;
import fr.arinonia.bootstrap.utils.RuntimeDownloader;
import fr.flowarg.azuljavadownloader.Callback;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class RuntimeService {

    private final FileManager fileManager;
    private final RuntimeDetector.RuntimeInfo runtimeInfo;

    public RuntimeService(final FileManager fileManager, final RuntimeDetector.RuntimeInfo runtimeInfo) {
        this.fileManager = fileManager;
        this.runtimeInfo = runtimeInfo;
    }

    public boolean needsRuntimeDownload() {
        if (!RuntimeDetector.isValidJavaInstallation(this.fileManager.getRuntimePath().toFile())) {
            if (!this.runtimeInfo.hasValidJava()) {
                System.out.printf("Invalid Java version detected: %s%n", this.runtimeInfo.getCurrentJavaVersion());
                return true;
            }
            if (!this.runtimeInfo.hasJavaFX()) {
                System.out.println("JavaFX not found in runtime");
                return true;
            }
        }
        return false;
    }

    public CompletableFuture<Path> downloadRuntime(final Callback callback) {
        final RuntimeDownloader downloader = new RuntimeDownloader(
                this.fileManager.getRuntimePath(),
                this.fileManager,
                callback

        );
        return downloader.downloadJava21();
    }
}
