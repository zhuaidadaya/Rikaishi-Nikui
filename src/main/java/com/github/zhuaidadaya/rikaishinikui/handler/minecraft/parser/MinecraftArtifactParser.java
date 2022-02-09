package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

public class MinecraftArtifactParser {
    private JSONObject artifact;

    public MinecraftArtifactParser(JSONObject json) {
        this.artifact = json;
    }

    public String getUrl() {
        return artifact.getString("url");
    }

    public String getPath() {
        return artifact.getString("path");
    }

    public String getSha1() {
        return artifact.getString("sha1");
    }

    public int getSize() {
        return artifact.getInt("size");
    }
}
