package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

public class MinecraftAssetIndexParser {
    private final JSONObject asset;
    private final String version;
    private String area;

    public MinecraftAssetIndexParser(JSONObject json,String area) {
        this.version = json.getString("assets");
        this.asset = json.getJSONObject("assetIndex");
        this.area = area;
    }

    public String getUrl() {
        return asset.getString("url");
    }

    public String getPath() {
        return String.format("%s/assets/indexes/%s.json", area, version);
    }

    public String getSha1() {
        return asset.getString("sha1");
    }
}
