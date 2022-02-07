package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONObject;

import java.util.Map;

public class MinecraftAssetParser {
    private final JSONObject asset;
    private final String url;

    public MinecraftAssetParser(String url, JSONObject json) {
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
