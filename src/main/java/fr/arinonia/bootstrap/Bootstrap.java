package fr.arinonia.bootstrap;

import fr.arinonia.bootstrap.file.FileManager;
import fr.arinonia.bootstrap.logger.Logger;
import fr.arinonia.bootstrap.services.ApplicationService;
import fr.arinonia.bootstrap.utils.GlobalErrorHandler;

public class Bootstrap {
    private static Bootstrap instance;
    private final ApplicationService applicationService;


    public Bootstrap() {
        instance = this;
        final FileManager fileManager = new FileManager();
        fileManager.createDirectories();
        Logger.init(fileManager);
        this.applicationService = new ApplicationService(fileManager);
    }

    public void init() {
        GlobalErrorHandler.setup();
        this.applicationService.start();
    }

    public static Bootstrap getInstance() {
        return instance;
    }

    public ApplicationService getApplicationService() {
        return this.applicationService;
    }

}
