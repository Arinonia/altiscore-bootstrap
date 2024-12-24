package fr.arinonia.bootstrap.updater.models;

public class MainJar {

    private String name;
    private String url;
    private String sha1;
    private long size;
    private String path;


    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSha1() {
        return this.sha1;
    }

    public long getSize() {
        return this.size;
    }

    public String getPath() {
        return this.path;
    }
}
