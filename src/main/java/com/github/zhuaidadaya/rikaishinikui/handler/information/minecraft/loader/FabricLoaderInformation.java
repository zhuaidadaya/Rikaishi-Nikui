package com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.loader;

import org.json.JSONObject;

public class FabricLoaderInformation {
    private String version = "0.13.3";
    private boolean isFabric = false;

    public FabricLoaderInformation() {

    }

    public FabricLoaderInformation(JSONObject json) {
        apply(json);
    }

    public String getVersion() {
        return version;
    }

    public boolean isFabric() {
        return isFabric;
    }

    public FabricLoaderInformation setFabric(boolean fabric) {
        isFabric = fabric;
        return this;
    }

    public FabricLoaderInformation setVersion(String version) {
        this.version = version;
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("version", version);
        json.put("is-fabric", isFabric);
        return json;
    }

    public void apply(JSONObject json) {
        this.version = json.getString("version");
        this.isFabric = json.getBoolean("is-fabric");
    }
}
