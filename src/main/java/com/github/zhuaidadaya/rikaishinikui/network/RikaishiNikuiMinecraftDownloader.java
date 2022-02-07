package com.github.zhuaidadaya.rikaishinikui.network;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.MinecraftVersionManifest;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.MinecraftVersionParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.MinecraftVersionsParser;
import com.github.zhuaidadaya.utils.string.checker.StringCheckUtil;
import org.json.JSONObject;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class RikaishiNikuiMinecraftDownloader {
    private final int files = 0;
    private final int lastFiles = 0;

    public RikaishiNikuiMinecraftDownloader() {

    }

    public void init(JSONObject manifest) {

    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        return json;
    }

    public MinecraftVersionsParser getVersions() {
        return getVersions(config.getConfigString("minecraft-versions-manifest"));
    }

    public MinecraftVersionsParser getVersions(String url) {
        ResourceDownloader downloader = new ResourceDownloader();
        return getVersions(url, downloader);
    }

    public MinecraftVersionsParser getVersions(String url, ResourceDownloader downloader) {
        JSONObject versionManifest = new JSONObject(downloader.downloadFileToStringBuilder(url).toString());
        return new MinecraftVersionsParser(versionManifest);
    }

    public void download(String id) {
        ResourceDownloader downloader = new ResourceDownloader();
        String url;
        url = StringCheckUtil.getOrDefault(config.getConfigString("minecraft-versions-manifest"), "https://launchermeta.mojang.com/mc/game/version_manifest.json");
        config.set("minecraft-versions-manifest", url);
        MinecraftVersionsParser versionsParser = getVersions(url, downloader);
        System.out.println(versionsParser.getVersion(id).getVersion());
    }
}
