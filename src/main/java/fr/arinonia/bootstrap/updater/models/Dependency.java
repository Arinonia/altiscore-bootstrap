package fr.arinonia.bootstrap.updater.models;

import com.google.gson.annotations.SerializedName;

public class Dependency {

    private DependencyType type;
    private String name;
    private String url;
    private String sha1;
    private long size;
    private String path;
    //@SerializedName("groupId")
    private String groupId;
    //@SerializedName("artifactId")
    private String artifactId;
    private String version;
    private String repository;

    public DependencyType getType() {
        return this.type;
    }

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

    public String getGroupId() {
        return this.groupId;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public String getVersion() {
        return this.version;
    }

    public String getRepository() {
        return this.repository;
    }

    public enum  DependencyType {
        @SerializedName("maven")
        MAVEN,
        @SerializedName("direct")
        DIRECT
    }
}
