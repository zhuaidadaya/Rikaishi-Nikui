package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import org.json.JSONObject;

public class FabricMinecraftDownloadParser extends Parser {
    private final JSONObject download;
    private final FabricMinecraftArtifactParser artifactParser;

    public FabricMinecraftDownloadParser(JSONObject json, String gameVersion, String loaderVersion) {
        download = json;
        artifactParser = new FabricMinecraftArtifactParser(json, gameVersion, loaderVersion);
    }

    public FabricMinecraftArtifactParser getArtifactParser() {
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
}
