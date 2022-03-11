package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

public class VanillaMinecraftAssetsParser extends Parser {
    private JSONObject assets;
    private final String url;
    private final String area;

    public VanillaMinecraftAssetsParser(String url, String area, JSONObject json) {
        try {
            this.assets = json.getJSONObject("objects");
        } catch (Exception ex) {
            this.assets = json;
        }
        this.area = area;
        this.url = url;
    }

    public Object2ObjectRBTreeMap<String, VanillaMinecraftAssetParser> getAssets() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftAssetParser> libs = new Object2ObjectRBTreeMap<>();
        for (String asset : assets.keySet()) {
            JSONObject json = assets.getJSONObject(asset);

            VanillaMinecraftAssetParser parser = new VanillaMinecraftAssetParser(url, json);

            libs.put(parser.getUrl(), parser);
        }
        return libs;
    }

    public List<NetworkFileInformation> getSmallerAssetsDownloads() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftAssetParser> assets = getAssets();
        ObjectArrayList<NetworkFileInformation> downloads = new ObjectArrayList<>();
        for (String s : assets.keySet()) {
            VanillaMinecraftAssetParser asset = assets.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/assets/objects/%s", area, asset.getPath()), asset.getHash()));
        }
        return downloads;
    }

    public Set<NetworkFileInformation> getFasterAssetsDownloads() {
        Object2ObjectRBTreeMap<String, VanillaMinecraftAssetParser> assets = getAssets();
        ObjectArraySet<NetworkFileInformation> downloads = new ObjectArraySet<>();
        for (String s : assets.keySet()) {
            VanillaMinecraftAssetParser asset = assets.get(s);
            downloads.add(new NetworkFileInformation(s, String.format("%s/assets/objects/%s", area, asset.getPath()), asset.getHash()));
        }
        return downloads;
    }
}
