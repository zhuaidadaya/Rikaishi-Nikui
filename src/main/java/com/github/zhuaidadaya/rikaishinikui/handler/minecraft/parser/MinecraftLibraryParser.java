package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

public class MinecraftLibraryParser {
    private JSONObject library;
    private MinecraftDownloadParser downloadParser;

    public MinecraftLibraryParser(JSONObject json) {
        this.library = json;
        this.downloadParser = new MinecraftDownloadParser(json.getJSONObject("downloads"));
    }

    public String getUrl() {
        return downloadParser.getUrl();
    }

    public String getPath() {
        return downloadParser.getPath();
    }

    public String getSha1() {
        return downloadParser.getSha1();
    }

    public String getName() {
        return library.getString("name");
    }
}
