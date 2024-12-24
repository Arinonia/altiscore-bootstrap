package fr.arinonia.bootstrap.services;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.file.FileManager;
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
        System.out.printf("Starting %s version %s%n",
                BootstrapConfig.getInstance().getAppName(),
                BootstrapConfig.getInstance().getAppVersion());

        if (this.runtimeService.needsRuntimeDownload()) {
            handleRuntimeSetup();
        } else {
            System.out.println("Runtime requirements met, proceeding with launcher update");
            uiService.display();
            handleLauncherUpdate();
        }
    }

    private void handleRuntimeSetup() {
        this.uiService.display();
        UIService.ProgressCallback callback = uiService.createProgressCallback("Installation de Java");

        this.runtimeService.downloadRuntime(
                callback::onProgress
        ).thenAccept(javaPath -> {
            System.out.println("Runtime setup completed at: " + javaPath);
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
        final UIService.ProgressCallback callback = uiService.createProgressCallback("Mise à jour du launcher");

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
