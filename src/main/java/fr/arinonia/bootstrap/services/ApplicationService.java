package fr.arinonia.bootstrap.services;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.logger.Logger;
import fr.arinonia.bootstrap.utils.RuntimeDetector;

import java.io.IOException;

public class ApplicationService {
    private final RuntimeService runtimeService;
    private final LauncherService launcherService;
    private final UIService uiService;

    public ApplicationService(final FileManager fileManager) {
        this.runtimeService = new RuntimeService(fileManager, RuntimeDetector.detectRuntime());
        this.launcherService = new LauncherService(fileManager);
        this.uiService = new UIService();
    }

    public void start() {
        Logger.info(String.format("Starting %s version %s",
                BootstrapConfig.getInstance().getAppName(),
                BootstrapConfig.getInstance().getAppVersion()));

        if (this.runtimeService.needsRuntimeDownload()) {
            handleRuntimeSetup();
        } else {
            Logger.info("Runtime requirements met, proceeding with launcher update");
            this.uiService.display();
            handleLauncherUpdate();
        }
    }

    private void handleRuntimeSetup() {
        this.uiService.display();
        UIService.ProgressCallback callback = this.uiService.createProgressCallback("Installation de Java");

        this.runtimeService.downloadRuntime(
                callback::onProgress
        ).thenAccept(javaPath -> {
            Logger.info("Runtime setup completed at: " + javaPath);
            handleLauncherUpdate();
        }).exceptionally(throwable -> {
            callback.onError(
                    "Erreur d'installation",
                    "Impossible d'installer Java",
                    throwable
            );
            return null;
        });
    }

    private void handleLauncherUpdate() {
        final UIService.ProgressCallback callback = this.uiService.createProgressCallback("Mise à jour du launcher");

        this.launcherService.updateLauncher(
                progress -> callback.onProgress(progress, "")
        ).thenAccept(v -> {
            try {
                startLauncher();
            } catch (final IOException e) {
                callback.onError(
                        "Erreur de démarrage",
                        "Impossible de démarrer le launcher",
                        e
                );
            }
        }).exceptionally(throwable -> {
            callback.onError(
                    "Erreur de mise à jour",
                    "Impossible de mettre à jour le launcher",
                    throwable
            );
            return null;
        });
    }

    private void startLauncher() throws IOException {
        this.launcherService.startLauncher();
    }

    public void exit() {
        this.uiService.dispose();
        Logger.close();
        System.exit(0);
    }

    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }

    public LauncherService getLauncherService() {
        return this.launcherService;
    }

    public UIService getUiService() {
        return this.uiService;
    }
}
