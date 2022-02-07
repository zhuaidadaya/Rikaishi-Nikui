package com.github.zhuaidadaya.rikaishinikui.network;

import org.jetbrains.annotations.NotNull;

public class NetworkFileInformation {
    private String sha1 = "";
    private String url = "";
    private String filePath = "";

    public NetworkFileInformation(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    public NetworkFileInformation(String url, String filePath, String sha1) {
        this.url = url;
        this.filePath = filePath;
        this.sha1 = sha1;
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
}
