package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class VanillaMinecraftAssetsParser {
    private JSONObject assets;
    private String url;
    private String area;

    public VanillaMinecraftAssetsParser(String url, String area, JSONObject json) {
        try {
            this.assets = json.getJSONObject("objects");
        } catch (Exception ex) {
            this.assets = json;
        }
        this.area = area;
        this.url = url;
    }

    public Map<String, VanillaMinecraftAssetParser> getAssets() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftAssetParser> libs = new Object2ObjectRBTreeMap<>();
        for(String asset : assets.keySet()) {
            JSONObject json = assets.getJSONObject(asset);

            VanillaMinecraftAssetParser parser = new VanillaMinecraftAssetParser(url, json);

            libs.put(parser.getUrl(), parser);
        }
        return libs;
    }

    public Set<NetworkFileInformation> getAssetsDownloads() {
        Map<String, VanillaMinecraftAssetParser> assets = getAssets();
        Set<NetworkFileInformation> downloads = new LinkedHashSet<>();
        for(String s : assets.keySet()) {
            VanillaMinecraftAssetParser asset = assets.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/assets/objects/%s", area, asset.getPath()), asset.getHash()));
        }
        return downloads;
    }
}
