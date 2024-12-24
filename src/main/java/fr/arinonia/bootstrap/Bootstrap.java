package fr.arinonia.bootstrap;

import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.services.ApplicationService;
import fr.arinonia.bootstrap.utils.GlobalErrorHandler;

public class Bootstrap {
    private static Bootstrap instance;
    private final ApplicationService applicationService;


    public Bootstrap() {
        instance = this;
        final FileManager fileManager = new FileManager();
        fileManager.createDirectories();

        this.applicationService = new ApplicationService(fileManager);
    }

    public void init() {
        GlobalErrorHandler.setup();
        this.applicationService.start();
    }

    public void exit() {
        System.exit(0);
    }

    public static Bootstrap getInstance() {
        return instance;
    }

    public ApplicationService getApplicationService() {
        return this.applicationService;
    }

}
