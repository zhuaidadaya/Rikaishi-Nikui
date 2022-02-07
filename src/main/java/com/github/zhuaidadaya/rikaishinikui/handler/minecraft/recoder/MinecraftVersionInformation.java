package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder;

import org.json.JSONObject;

public class MinecraftVersionInformation {
    private String name;
    private String id;

    public MinecraftVersionInformation() {

    }

    public MinecraftVersionInformation(JSONObject json) {
        apply(json);
    }

    public void apply(JSONObject json) {
        this.name = json.getString("name");
        this.id = json.getString("id");
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);

        return json;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
