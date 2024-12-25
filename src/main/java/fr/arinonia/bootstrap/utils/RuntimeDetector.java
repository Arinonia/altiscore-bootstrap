package fr.arinonia.bootstrap.utils;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.logger.Logger;

import java.io.File;

public class RuntimeDetector {
    private static final String[] JAVAFX_PACKAGES = {
            "javafx.application.Application",
            "javafx.scene.Scene",
            "javafx.stage.Stage"
    };

    public static class RuntimeInfo {
        private final boolean hasValidJava;
        private final boolean hasJavaFX;
        private final String currentJavaVersion;
        private final String javaHome;

        public RuntimeInfo(final boolean hasValidJava, final boolean hasJavaFX, final String currentJavaVersion, final String javaHome) {
            this.hasValidJava = hasValidJava;
            this.hasJavaFX = hasJavaFX;
            this.currentJavaVersion = currentJavaVersion;
            this.javaHome = javaHome;
        }

        public boolean hasValidJava() { return this.hasValidJava; }
        public boolean hasJavaFX() { return this.hasJavaFX; }
        public String getCurrentJavaVersion() { return this.currentJavaVersion; }
        public String getJavaHome() { return this.javaHome; }
    }

    public static RuntimeInfo detectRuntime() {
        final String javaVersion = System.getProperty("java.version");
        final String javaHome = System.getProperty("java.home");

        final boolean hasValidJava = isJava21OrHigher(javaVersion);

        final boolean hasJavaFX = checkJavaFXPresence();

        return new RuntimeInfo(hasValidJava, hasJavaFX, javaVersion, javaHome);
    }

    private static boolean isJava21OrHigher(final String version) {
        final String[] versionParts = version.split("\\.");
        try {
            final int majorVersion = Integer.parseInt(versionParts[0]);
            return majorVersion >= Integer.parseInt(BootstrapConfig.getInstance().getRequiredJavaVersion());
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean checkJavaFXPresence() {
        for (final String packageName : JAVAFX_PACKAGES) {
            try {
                Class.forName(packageName);
            } catch (final ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidJavaInstallation(final File javaPath) {
        final JavaValidator.ValidationResult result = JavaValidator.validateJavaInstallation(javaPath.toPath());
        if (!result.isValid()) {
            Logger.warning("Installation Java invalide : " + result.getErrorMessage());
            return false;
        }

        final String version = result.getVersion();
        if (version != null) {
            if (!version.startsWith("21.")) {//! change this to a config value
                Logger.warning("Version Java incompatible : " + version);
                return false;
            }
        }

        if (!result.hasJavaFX()) {
            Logger.warning("JavaFX non trouvé dans l'installation");
            return false;
        }

        return true;
    }
}