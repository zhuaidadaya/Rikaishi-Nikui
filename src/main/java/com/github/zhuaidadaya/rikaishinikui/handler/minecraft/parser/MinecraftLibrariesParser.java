package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class MinecraftLibrariesParser {
    private JSONArray libraries;

    public MinecraftLibrariesParser(JSONObject json) {
        libraries = json.getJSONArray("libraries");
    }

    public MinecraftLibrariesParser(JSONArray json) {
        libraries = json;
    }

    public Map<String, MinecraftLibraryParser> getLibraries() {
        Object2ObjectRBTreeMap<String,MinecraftLibraryParser> libs = new Object2ObjectRBTreeMap<>();
        for(Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());

            MinecraftLibraryParser libraryParser = new MinecraftLibraryParser(json);

            libs.put(libraryParser.getUrl(), libraryParser);
        }
        return libs;
    }
}
