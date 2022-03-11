package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class VanillaMinecraftLibrariesParser extends Parser {
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

    public Object2ObjectRBTreeMap<String, VanillaMinecraftLibraryParser> getLibraries() {
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

    public ObjectLinkedOpenHashSet<NetworkFileInformation> getLibrariesDownloads() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftLibraryParser> libs = getLibraries();
        ObjectLinkedOpenHashSet<NetworkFileInformation> downloads = new ObjectLinkedOpenHashSet<>();
        for(String s : libs.keySet()) {
            VanillaMinecraftLibraryParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/libraries/%s", area, lib.getPath()), lib.getSha1()));
        }
        return downloads;
    }
}
