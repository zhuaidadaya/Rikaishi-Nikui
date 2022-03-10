package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class VanillaMinecraftLibrariesParser {
    private final JSONArray libraries;
    private String area;
    private String os;

    public VanillaMinecraftLibrariesParser(JSONObject json, String area, String os) {
        libraries = json.getJSONArray("libraries");
        this.area = area;
        this.os = os;
    }

    public VanillaMinecraftLibrariesParser(JSONArray json, String area, String os) {
        libraries = json;
        this.area = area;
        this.os = os;
    }

    public Map<String, VanillaMinecraftLibraryParser> getLibraries() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftLibraryParser> libs = new Object2ObjectRBTreeMap<>();
        for(Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());
            try {
                json.getJSONObject("classifiers");
            } catch (Exception e) {
                try {
                    VanillaMinecraftLibraryParser libraryParser = new VanillaMinecraftLibraryParser(json, os);

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
        Map<String, VanillaMinecraftLibraryParser> libs = getLibraries();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for(String s : libs.keySet()) {
            VanillaMinecraftLibraryParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/libraries/%s", area, lib.getPath()), lib.getSha1()));
        }
        return downloads;
    }
}
