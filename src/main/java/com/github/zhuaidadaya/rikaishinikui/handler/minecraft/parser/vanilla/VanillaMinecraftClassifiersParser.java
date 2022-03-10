package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class VanillaMinecraftClassifiersParser {
    private final JSONArray libraries;
    private final String area;
    private final String os;

    public VanillaMinecraftClassifiersParser(JSONObject json, String area, String os) {
        libraries = json.getJSONArray("libraries");
        this.area = area;
        this.os = os;
    }

    public VanillaMinecraftClassifiersParser(JSONArray json, String area, String os) {
        libraries = json;
        this.area = area;
        this.os = os;
    }

    public Map<String, VanillaMinecraftClassifierParser> getClassifiers() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftClassifierParser> libs = new Object2ObjectRBTreeMap<>();
        for (Object library : libraries) {
            JSONObject json = new JSONObject(library.toString());

            try {
                VanillaMinecraftClassifierParser libraryParser = new VanillaMinecraftClassifierParser(json, os);
                libs.put(libraryParser.getNative().getUrl(), libraryParser);
            } catch (Exception e) {

            }
        }
        return libs;
    }

    public Set<NetworkFileInformation> getClassifiersDownloads() {
        Map<String, VanillaMinecraftClassifierParser> libs = getClassifiers();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for (String s : libs.keySet()) {
            VanillaMinecraftClassifierParser lib = libs.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/libraries/%s", area, lib.getNative().getPath()), lib.getNative().getUrl()));
        }
        return downloads;
    }
}