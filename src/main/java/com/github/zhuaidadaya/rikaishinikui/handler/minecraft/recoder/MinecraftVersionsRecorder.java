package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashSet;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class MinecraftVersionsRecorder {
    private Object2ObjectLinkedOpenHashMap<String, MinecraftVersionInformation> versions = new Object2ObjectLinkedOpenHashMap<>();
    private Object2ObjectLinkedOpenHashMap<String, String> versionNames = new Object2ObjectLinkedOpenHashMap<>();

    public MinecraftVersionsRecorder() {

    }

    public MinecraftVersionsRecorder(JSONObject json) {
        try {
            apply(json);
        } catch (Exception e) {

        }
    }

    public void add(MinecraftVersionInformation information) {
        versions.put(information.getId(), information);
        versionNames.put(information.getId(), information.getName());
        config.set("versions", toJSONObject());
    }

    public void remove(MinecraftVersionInformation information) {
        versions.remove(information.getId());
        versionNames.remove(information.getId());
        config.set("versions", toJSONObject());
    }

        public Collection<String> getVersionNames() {
        return versionNames.values();
    }

    public Collection<MinecraftVersionInformation> getVersions() {
        return versions.values();
    }

    public Collection<MinecraftVersionInformation> getVersions(String search) {
        Collection<MinecraftVersionInformation> result = new LinkedHashSet<>();
        for(MinecraftVersionInformation information : versions.values()) {
            if(information.getName().contains(search)) {
                result.add(information);
            }
        }
        return result;
    }

    public MinecraftVersionInformation getVersionAsName(String name) {
        for(MinecraftVersionInformation information : versions.values()) {
            if(information.getName().equals(name)) {
                return information;
            }
        }
        return null;
    }

    public MinecraftVersionInformation getVersionAsId(String id) {
        return versions.get(id);
    }

    public void apply(JSONObject json) throws Exception {
        versions = new Object2ObjectLinkedOpenHashMap<>();
        versionNames = new Object2ObjectLinkedOpenHashMap<>();
        for(String id : json.keySet()) {
            MinecraftVersionInformation information = new MinecraftVersionInformation(json.getJSONObject(id));
            versions.put(information.getId(), information);
            versionNames.put(information.getId(), information.getName());
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        for(String id : versions.keySet()) {
            json.put(id, versions.get(id).toJSONObject());
        }

        return json;
    }
}
