package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric;

import org.json.JSONObject;

public class FabricMinecraftLibraryParser {
    private final JSONObject library;
    private final FabricMinecraftDownloadParser downloadParser;

    public FabricMinecraftLibraryParser(JSONObject json, String gameVersion,String loaderVersion) {
        this.library = json;
        this.downloadParser = new FabricMinecraftDownloadParser(json,gameVersion,loaderVersion);
    }

    public FabricMinecraftArtifactParser getLibrary() {
        try {
            return downloadParser.getArtifactParser();
        } catch (Exception e) {

        }
        return null;
    }

    public String getUrl() {
        return downloadParser.getUrl();
    }

    public String getPath() {
        return downloadParser.getPath();
    }

    public void setArea(String area) {
        downloadParser.setArea(area);
    }

    public String getAbsolutePath() {
        return downloadParser.getAbsolutePath();
    }

    public String getName() {
        return library.getString("name");
    }
}
