package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class MinecraftAssetsParser {
    private JSONObject assets;
    private String url;

    public MinecraftAssetsParser(String url,JSONObject json) {
        try {
            this.assets = json.getJSONObject("objects");
        } catch (Exception ex) {
            this.assets = json;
        }
        this.url = url;
    }

    public Map<String, MinecraftAssetParser> getAssets() {
        Object2ObjectRBTreeMap<String, MinecraftAssetParser> libs = new Object2ObjectRBTreeMap<>();
        for(String asset : assets.keySet()) {
            JSONObject json = assets.getJSONObject(asset);

            MinecraftAssetParser parser = new MinecraftAssetParser(url, json);

            libs.put(parser.getUrl(), parser);
        }
        return libs;
    }
}
