package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import org.json.JSONObject;

public class VanillaMinecraftDownloadParser {
    private final JSONObject download;
    private final VanillaMinecraftArtifactParser artifactParser;

    public VanillaMinecraftDownloadParser(JSONObject json) {
        download = json;
        artifactParser = new VanillaMinecraftArtifactParser(json.getJSONObject("artifact"));
    }

    public VanillaMinecraftArtifactParser getArtifactParser() {
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
