package com.github.zhuaidadaya.rikaishinikui.handler.java.recorder;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class JavaVersionsRecorder {
    private Object2ObjectLinkedOpenHashMap<String, JavaVersionInformation> versions = new Object2ObjectLinkedOpenHashMap<>();
    private Object2ObjectLinkedOpenHashMap<String, String> versionNames = new Object2ObjectLinkedOpenHashMap<>();
    private JavaVersionInformation defaultInformation;

    public JavaVersionsRecorder() {

    }

    public JavaVersionsRecorder(JSONObject json) {
        try {
            apply(json);
        } catch (Exception e) {

        }
    }

    public void add(JavaVersionInformation information) {
        versions.put(information.getName(), information);
        versionNames.put(information.getName(), information.getName());
        config.set("java-versions", toJSONObject());
    }

    public void remove(JavaVersionInformation information) {
        versions.remove(information.getName());
        versionNames.remove(information.getName());
        if (information.getName().equals(defaultInformation.getName())) {
            defaultInformation = null;
        }
        config.set("java-versions", toJSONObject());
    }

    public Collection<String> getVersionNames() {
        return versionNames.values();
    }

    public Collection<JavaVersionInformation> getVersions() {
        return versions.values();
    }

    public Collection<JavaVersionInformation> getVersions(String search) {
        String filter = search.toLowerCase();
        Collection<JavaVersionInformation> result = new LinkedHashSet<>();
        for (JavaVersionInformation information : versions.values()) {
            if (information.getName().toLowerCase().contains(filter) || information.getType().toLowerCase().contains(filter) || information.getJavaId().toLowerCase().contains(filter) || (information.isIs64Bit()) & search.equals("64")) {
                result.add(information);
            }
        }
        return result;
    }

    public JavaVersionInformation getVersionAsName(String name) {
        for (JavaVersionInformation information : versions.values()) {
            if (information.getName().equals(name)) {
                return information;
            }
        }
        return null;
    }

    public JavaVersionInformation getVersionAsId(String id) {
        return versions.get(id);
    }

    public void apply(JSONObject json) throws Exception {
        versions = new Object2ObjectLinkedOpenHashMap<>();
        versionNames = new Object2ObjectLinkedOpenHashMap<>();
        defaultInformation = null;
        for (String name : json.keySet()) {
            JavaVersionInformation information = new JavaVersionInformation(json.getJSONObject(name));
            if (information.getStatus().equals("status.default")) {
                if (defaultInformation == null)
                    defaultInformation = information;
                else
                    continue;
            }
            versions.put(information.getName(), information);
            versionNames.put(information.getName(), information.getName());
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        for (String name : versions.keySet()) {
            json.put(name, versions.get(name).toJSONObject());
        }

        return json;
    }
}
