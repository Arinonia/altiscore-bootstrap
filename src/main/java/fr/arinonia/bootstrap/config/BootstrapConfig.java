package fr.arinonia.bootstrap.config;

import java.awt.*;

/**
 * I'm trying a new way to organize my constants
 */
public class BootstrapConfig {

    private static final BootstrapConfig INSTANCE = new BootstrapConfig();

    // Application
    private final String appName = "AltisCore Bootstrap";
    private final String appVersion = "1.0.1";
    private final String discordUrl = "https://discord.gg/your-discord";

    // Runtime
    private final String requiredJavaVersion = "21";
    private final String manifestUrl = "https://launcher.altiscore.fr/bootstrap/launcher.json";
    private final String mavenCentralUrl = "https://repo1.maven.org/maven2";

    // UI
    private final Color backgroundColor = new Color(36, 17, 70);
    private final Color textColor = new Color(222, 222, 222);
    private final Color progressBarBackground = new Color(48, 25, 88);
    private final Color progressBarForeground = new Color(149, 128, 255);
    private final int windowWidth = 540;
    private final int windowHeight = 710;

    // Files and Paths
    private final String altiscoreFolderName = ".altiscore";

    private BootstrapConfig() {}

    public static BootstrapConfig getInstance() {
        return INSTANCE;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String getDiscordUrl() {
        return this.discordUrl;
    }

    public String getRequiredJavaVersion() {
        return this.requiredJavaVersion;
    }

    public String getManifestUrl() {
        return this.manifestUrl;
    }

    public String getMavenCentralUrl() {
        return this.mavenCentralUrl;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public Color getProgressBarBackground() {
        return this.progressBarBackground;
    }

    public Color getProgressBarForeground() {
        return this.progressBarForeground;
    }

    public int getWindowWidth() {
        return this.windowWidth;
    }

    public int getWindowHeight() {
        return this.windowHeight;
    }

    public String getAltiscoreFolderName() {
        return this.altiscoreFolderName;
    }
}
