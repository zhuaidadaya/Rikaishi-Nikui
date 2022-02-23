package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MinecraftLibrariesParser {
    private final JSONArray libraries;
    private String area;
    private String os;

    public MinecraftLibrariesParser(JSONObject json,String area,String os) {
        libraries = json.getJSONArray("libraries");
        this.area = area;
        this.os = os;
    }

    public MinecraftLibrariesParser(JSONArray json,String area,String os) {
        libraries = json;
        this.area = area;
        this.os = os;
    }

    public Map<String, MinecraftLibraryParser> getLibraries() {
        Object2ObjectRBTreeMap<String, MinecraftLibraryParser> libs = new Object2ObjectRBTreeMap<>();
        for(Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());
            try {
                json.getJSONObject("classifiers");
            } catch (Exception e) {
                try {
                    MinecraftLibraryParser libraryParser = new MinecraftLibraryParser(json, os);

                    if(libraryParser.getLibrary() != null) {
                        libs.put(libraryParser.getUrl(), libraryParser);
                    }
                } catch (Exception e2) {

                }
            }
        }
        return libs;
    }

    public Set<NetworkFileInformation> getLibrariesDownloads() {
        Map<String, MinecraftLibraryParser> libs = getLibraries();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for(String s : libs.keySet()) {
            MinecraftLibraryParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/libraries/%s", area, lib.getPath()), lib.getSha1()));
        }
        return downloads;
    }
}
