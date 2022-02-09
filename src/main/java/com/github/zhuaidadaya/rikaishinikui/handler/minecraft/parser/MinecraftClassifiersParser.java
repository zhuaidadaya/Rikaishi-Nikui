package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import com.github.zhuaidadaya.rikaishinikui.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MinecraftClassifiersParser {
    private final JSONArray libraries;
    private String area;
    private String os;

    public MinecraftClassifiersParser(JSONObject json, String area,String os) {
        libraries = json.getJSONArray("libraries");
        this.area = area;
        this.os = os;
    }

    public MinecraftClassifiersParser(JSONArray json, String area,String os) {
        libraries = json;
        this.area = area;
        this.os = os;
    }

    public Map<String, MinecraftClassifierParser> getClassifiers() {
        Object2ObjectRBTreeMap<String, MinecraftClassifierParser> libs = new Object2ObjectRBTreeMap<>();
        for(Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());

            try {
                MinecraftClassifierParser libraryParser = new MinecraftClassifierParser(json, os);
                libs.put(libraryParser.getNative().getUrl(), libraryParser);
            } catch (Exception e) {

            }
        }
        return libs;
    }

    public Set<NetworkFileInformation> getClassifiersDownloads() {
        Map<String, MinecraftClassifierParser> libs = getClassifiers();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for(String s : libs.keySet()) {
            MinecraftClassifierParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/libraries/%s", area, lib.getNative().getPath()), lib.getNative().getUrl()));
        }
        return downloads;
    }
}
