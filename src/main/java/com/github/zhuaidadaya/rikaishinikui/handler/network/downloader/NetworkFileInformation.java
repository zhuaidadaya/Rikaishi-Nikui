package com.github.zhuaidadaya.rikaishinikui.handler.network.downloader;

public class NetworkFileInformation {
    private String sha1 = "";
    private String url;
    private String filePath;
    private int size = -1;
    private String name = "";

    public NetworkFileInformation(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    public NetworkFileInformation(String url, String filePath, String sha1) {
        this.url = url;
        if (filePath.startsWith("/") || filePath.startsWith("\\")) {
            this.filePath = filePath.substring(1);
        } else {
            this.filePath = filePath;
        }
        this.sha1 = sha1;
    }

    public NetworkFileInformation(String url, String filePath, String sha1, int size) {
        this.url = url;
        this.filePath = filePath;
        this.sha1 = sha1;
        this.size = size;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return filePath;
    }

    public String getSha1() {
        return sha1;
    }

    public int getSize() {
        return size;
    }
}
