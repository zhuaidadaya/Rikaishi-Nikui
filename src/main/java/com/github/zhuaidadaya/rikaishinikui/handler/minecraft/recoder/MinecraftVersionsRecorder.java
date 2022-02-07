package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.json.JSONObject;

import java.util.Collection;

public class MinecraftVersionsRecorder {
    private Object2ObjectLinkedOpenHashMap<String, MinecraftVersionInformation> versions = new Object2ObjectLinkedOpenHashMap<>();
    private Object2ObjectLinkedOpenHashMap<String,String> versionNames = new Object2ObjectLinkedOpenHashMap<>();

    public MinecraftVersionsRecorder() {

    }

    public MinecraftVersionsRecorder(JSONObject json) {
        try {
            apply(json);
        } catch (Exception e) {

        }
    }

    public Collection<String> getVersionNames() {
        return versionNames.values();
    }

    public void apply(JSONObject json) throws Exception {
        versions = new Object2ObjectLinkedOpenHashMap<>();
        versionNames = new Object2ObjectLinkedOpenHashMap<>();
        for(String id : json.keySet()) {
            MinecraftVersionInformation information = new MinecraftVersionInformation(json.getJSONObject(id));
            versions.put(information.getId(),information);
            versionNames.put(information.getId(),information.getName());
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        for(String id : versions.keySet()) {
            json.put(id,versions.get(id).toJSONObject());
        }

        return json;
    }
}
