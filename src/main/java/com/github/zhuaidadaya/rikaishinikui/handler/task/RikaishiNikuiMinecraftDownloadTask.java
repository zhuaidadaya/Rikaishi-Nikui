package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.MinecraftVersionManifest;
import com.github.zhuaidadaya.rikaishinikui.network.RikaishiNikuiMinecraftDownloader;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class RikaishiNikuiMinecraftDownloadTask extends RikaishiNikuiTask {
    private RikaishiNikuiMinecraftDownloader downloader = new RikaishiNikuiMinecraftDownloader();

    public RikaishiNikuiMinecraftDownloadTask() {
        super(UUID.randomUUID());
    }

    @Override
    public void join() {
        synchronized(this) {
            try {
                downloader.init(config.getConfigJSONObject("minecraft-downloader"));
            } catch (Exception e) {
                config.set("version-manifest", downloader.toJSONObject());
            }
        }
    }

    @Override
    public void stop() {

    }

    public String toString() {
        return "az-" + getId();
    }
}
