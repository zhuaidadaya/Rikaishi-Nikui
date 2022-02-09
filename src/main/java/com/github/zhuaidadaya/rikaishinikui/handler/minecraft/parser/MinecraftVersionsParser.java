package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class MinecraftVersionsParser {
    private final JSONArray versions;
    private final Object2ObjectLinkedOpenHashMap<String, MinecraftVersionParser> versionsMap = new Object2ObjectLinkedOpenHashMap<>();
    private final String latestRelease;
    private final String latestSnapshot;

    public MinecraftVersionsParser(JSONObject json) {
        versions = json.getJSONArray("versions");
        JSONObject latest = json.getJSONObject("latest");
        latestRelease = latest.getString("release");
        latestSnapshot = latest.getString("snapshot");
        apply(versions);
    }

    public MinecraftVersionsParser(JSONArray json, String latestRelease, String latestSnapshot) {
        versions = json;
        this.latestRelease = latestRelease;
        this.latestSnapshot = latestSnapshot;
        apply(versions);
    }

    public void apply(JSONArray json) {
        for(Object o : json) {
            JSONObject version = new JSONObject(o.toString());
            versionsMap.put(version.getString("id"), new MinecraftVersionParser(version));
        }
    }

    public Collection<String> getVersionIds() {
        return versionsMap.keySet();
    }

    public MinecraftVersionParser getVersion(String id) {
        return versionsMap.get(id);
    }

    public MinecraftVersionParser getLatestRelease() {
        return getVersion(latestRelease);
    }

    public MinecraftVersionParser getLatestSnapshot() {
        return getVersion(latestSnapshot);
    }

    public String getLatestReleaseId() {
        return latestRelease;
    }

    public String getLatestSnapshotId() {
        return latestSnapshot;
    }

    public Collection<MinecraftVersionInformation> getVersionsInformation(String search) {
        Collection<MinecraftVersionInformation> versionsInformation = new LinkedHashSet<>();
        for(MinecraftVersionParser parser : versionsMap.values()) {
            if(parser.getId().contains(search)) {
                MinecraftVersionInformation information = new MinecraftVersionInformation(parser.getId(), parser.getId());
                information.setUrl(parser.getUrl());
                information.setType("Vanilla");
                information.setVersion(parser.getId());
                information.setStatus("status.download.ready");
                versionsInformation.add(information);
            }
        }
        return versionsInformation;
    }

    public Collection<MinecraftVersionInformation> getVersionsInformation() {
        return getVersionsInformation("");
    }
}
