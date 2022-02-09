package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import com.github.zhuaidadaya.rikaishinikui.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MinecraftAssetsParser {
    private JSONObject assets;
    private String url;
    private String area;

    public MinecraftAssetsParser(String url,String area,JSONObject json) {
        try {
            this.assets = json.getJSONObject("objects");
        } catch (Exception ex) {
            this.assets = json;
        }
        this.area = area;
        this.url = url;
    }

    public Map<String, MinecraftAssetParser> getAssets() {
        Object2ObjectRBTreeMap<String, MinecraftAssetParser> libs = new Object2ObjectRBTreeMap<>();
        for(String asset : assets.keySet()) {
            JSONObject json = assets.getJSONObject(asset);

            MinecraftAssetParser parser = new MinecraftAssetParser(url, json);

            libs.put(parser.getUrl(), parser);
        }
        return libs;
    }

    public Set<NetworkFileInformation> getAssetsDownloads() {
        Map<String, MinecraftAssetParser> assets = getAssets();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for(String s : assets.keySet()) {
            MinecraftAssetParser asset = assets.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/assets/objects/%s", area, asset.getPath()), asset.getHash()));
        }
        return downloads;
    }
}
