package fr.arinonia.bootstrap.updater.models;

import java.util.List;

public class LauncherManifest {
    private String version;
    private String mainClass;
    private MainJar mainJar;
    private List<Dependency> dependencies;

    public String getVersion() {
        return this.version;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public MainJar getMainJar() {
        return this.mainJar;
    }

    public List<Dependency> getDependencies() {
        return this.dependencies;
    }
}
