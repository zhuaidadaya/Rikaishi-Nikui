package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import org.json.JSONObject;

public class VanillaMinecraftAssetIndexParser extends Parser {
    private final JSONObject asset;
    private final String version;
    private final String area;

    public VanillaMinecraftAssetIndexParser(JSONObject json, String area) {
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
