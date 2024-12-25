package fr.arinonia.bootstrap.utils;

import fr.arinonia.bootstrap.Bootstrap;
import fr.arinonia.bootstrap.logger.Logger;
import fr.arinonia.bootstrap.ui.ErrorDialog;

public class GlobalErrorHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        Logger.error("Uncaught exception in thread " + t.getName(), e);
        ErrorDialog.showError(
                Bootstrap.getInstance().getApplicationService().getUiService().getFrame(),
                "Erreur inattendue",
                "Une erreur inattendue s'est produite : " + e.getMessage(),
                e
        );
    }

    public static void setup() {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalErrorHandler());
        System.setProperty("sun.awt.exception.handler", GlobalErrorHandler.class.getName());
    }
}