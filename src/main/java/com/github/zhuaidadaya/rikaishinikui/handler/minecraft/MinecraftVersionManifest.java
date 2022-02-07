package com.github.zhuaidadaya.rikaishinikui.handler.minecraft;

import org.json.JSONObject;

public class MinecraftVersionManifest {
    private String manifestUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public MinecraftVersionManifest() {

    }

    public MinecraftVersionManifest(JSONObject json) {
        apply(json);
    }

    public void apply(JSONObject json) {
        manifestUrl = json.getString("manifest-versions-manifest");
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("manifest-versions-manifest", manifestUrl);
        return json;
    }
}
