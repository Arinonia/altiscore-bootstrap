package fr.arinonia.bootstrap.services;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.launcher.LauncherStarter;
import fr.arinonia.bootstrap.updater.LauncherUpdater;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LauncherService {

    private final FileManager fileManager;
    private LauncherUpdater updater;

    public LauncherService(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public CompletableFuture<Void> updateLauncher(final ProgressCallback progressCallback) {
        this.updater = new LauncherUpdater(
                BootstrapConfig.getInstance().getManifestUrl(),
                this.fileManager.getRootPath()
        );

        return this.updater.update(progress ->
                SwingUtilities.invokeLater(() -> {
                    final int progressValue = (int) (progress * 100);
                    progressCallback.onProgress(progressValue);
                })
        );
    }

    public void startLauncher() throws IOException {
        if (this.updater == null) {
            throw new IllegalStateException("Launcher update must be performed before starting");
        }

        final LauncherStarter starter = new LauncherStarter(fileManager.getRuntimePath(), updater);
        starter.startLauncher();
    }

    @FunctionalInterface
    public interface ProgressCallback {
        void onProgress(int progressValue);
    }
}
