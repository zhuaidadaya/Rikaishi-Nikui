package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

import java.io.File;

public class MinecraftArtifactParser {
    private JSONObject artifact;
    private String area = "";

    public MinecraftArtifactParser(JSONObject json) {
        this.artifact = json;
    }

    public String getUrl() {
        return artifact.getString("url");
    }

    public String getPath() {
        return artifact.getString("path");
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAbsolutePath() {
        return String.format("%s/libraries/%s",new File(area).getAbsolutePath(), artifact.getString("path"));
    }

    public String getSha1() {
        return artifact.getString("sha1");
    }

    public int getSize() {
        return artifact.getInt("size");
    }
}
