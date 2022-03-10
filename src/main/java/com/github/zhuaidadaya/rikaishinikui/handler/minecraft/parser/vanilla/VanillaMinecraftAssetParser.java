package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import org.json.JSONObject;

public class VanillaMinecraftAssetParser extends Parser {
    private final JSONObject asset;
    private final String url;

    public VanillaMinecraftAssetParser(String url, JSONObject json) {
        this.asset = json;
        this.url = url;
    }

    public String getUrl() {
        return String.format("%s/%s", url, getPath());
    }

    public String getPath() {
        String hash = getHash();
        return String.format("%s/%s", hash.substring(0, 2), hash);
    }

    public String getHash() {
        return asset.getString("hash");
    }
}
