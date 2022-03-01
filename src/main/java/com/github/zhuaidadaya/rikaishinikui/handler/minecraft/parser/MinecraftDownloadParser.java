package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

public class MinecraftDownloadParser {
    private JSONObject download;
    private MinecraftArtifactParser artifactParser;

    public MinecraftDownloadParser(JSONObject json) {
        download = json;
        artifactParser = new MinecraftArtifactParser(json.getJSONObject("artifact"));
    }

    public MinecraftArtifactParser getArtifactParser() {
        return artifactParser;
    }

    public String getUrl() {
        return artifactParser.getUrl();
    }

    public String getPath() {
        return artifactParser.getPath();
    }

    public void setArea(String area) {
        artifactParser.setArea(area);
    }

    public String getAbsolutePath() {
        return artifactParser.getAbsolutePath();
    }

    public String getSha1() {
        return artifactParser.getSha1();
    }
}
