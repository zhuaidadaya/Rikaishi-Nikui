package com.github.zhuaidadaya.rikaishinikui.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launch.MinecraftLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.*;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.utils.integer.IntegerUtil;
import com.github.zhuaidadaya.utils.string.checker.StringCheckUtil;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.minecraftVersions;

public class RikaishiNikuiMinecraftDownloader {
    private final int taskThreads = 0;
    private final ResourceDownloader downloader = new ResourceDownloader();
    private int lastTaskThreads = 0;
    private int threads = - 1;
    private boolean running = true;
    private final String DEFAULT_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private final String DEFAULT_RESOURCE = "http://resources.download.minecraft.net";

    public RikaishiNikuiMinecraftDownloader() {

    }

    public void apply(JSONObject json) {
        threads = IntegerUtil.getIntFromJSON(json, "threads", - 1);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("threads", threads);
        return json;
    }

    public MinecraftVersionsParser getVersions() {
        String manifest = config.getConfigString("minecraft-versions-manifest-url");
        if(manifest == null) {
            config.set("minecraft-versions-manifest-url", DEFAULT_MANIFEST);
            return getVersions(DEFAULT_MANIFEST);
        } else {
            return getVersions(manifest);
        }
    }

    public MinecraftVersionsParser getVersions(String url) {
        ResourceDownloader downloader = new ResourceDownloader();
        return getVersions(url, downloader);
    }

    public MinecraftVersionsParser getVersions(String url, ResourceDownloader downloader) {
        String result = downloader.downloadFileToStringBuilder(url).toString();
        JSONObject versionManifest = new JSONObject(result);
        return new MinecraftVersionsParser(versionManifest);
    }

    public boolean download(String area, String id, String name) {
        try {
            String localId = UUID.randomUUID().toString();

            MinecraftVersionInformation information = new MinecraftVersionInformation(localId, name, area, "Vanilla", "status.downloading", id);

            minecraftVersions.add(information);

            ExecutorService threadPool = Executors.newCachedThreadPool();

            String url;
            url = StringCheckUtil.getOrDefault(config.getConfigString("minecraft-versions-manifest-url"), DEFAULT_MANIFEST);
            config.set("minecraft-versions-manifest-url", url);

            String resourceUrl;
            resourceUrl = StringCheckUtil.getOrDefault(config.getConfigString("minecraft-versions-resource-url"), DEFAULT_RESOURCE);
            config.set("minecraft-versions-resource-url", resourceUrl);

            MinecraftVersionsParser versionsParser = getVersions(url, downloader);

            information = new MinecraftVersionInformation(localId, name, area, "Vanilla", "status.downloading", id);
            information.setUrl(versionsParser.getVersion(id).getUrl());

            minecraftVersions.add(information);

            String manifestPath = String.format("%s/versions/%s/%s.json", area, localId, localId);
            downloader.downloadFile(new NetworkFileInformation(versionsParser.getVersion(id).getUrl(), manifestPath), 0);
            JSONObject version = new JSONObject(downloader.downloadFileToStringBuilder(manifestPath).toString());

            MinecraftAssetIndexParser assetsIndexParser = new MinecraftAssetIndexParser(version, area);

            downloader.downloadFile(new NetworkFileInformation(assetsIndexParser.getUrl(), assetsIndexParser.getPath(), assetsIndexParser.getSha1()), - 1);

            String os = System.getProperty("os.name");
            if(os.contains("Windows"))
                os = "windows";
            else if(os.contains("Linux"))
                os = "linux";
            else if(os.contains("Macos"))
                os = "macos";

            MinecraftLibrariesParser downloadParser = new MinecraftLibrariesParser(version, area, os);

            String finalOs = os;
            MinecraftClassifiersParser classifiersParser = new MinecraftClassifiersParser(version, area, finalOs);
            Thread classifiersThread = new Thread(() -> {
                try {
                    downloader.downloadFiles(classifiersParser.getClassifiersDownloads());
                    done();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            MinecraftServiceJarParser serviceJarParser = new MinecraftServiceJarParser(version.getJSONObject("downloads"), area, localId);
            Thread serviceJarThread = new Thread(() -> {
                try {
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getClientUrl(), serviceJarParser.getClientPath(), serviceJarParser.getClientSha1()), threads);
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getServerUrl(), serviceJarParser.getServerPath(), serviceJarParser.getServerSha1()), threads);
                    done();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread librariesThread = new Thread(() -> {
                try {
                    downloader.downloadFiles(downloadParser.getLibrariesDownloads());
                    done();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            MinecraftAssetsParser assetsParser = new MinecraftAssetsParser(resourceUrl, area, new JSONObject(downloader.downloadFileToStringBuilder(assetsIndexParser.getPath()).toString()));
            Thread resourcesThread = new Thread(() -> {
                try {
                    assetsParser.getAssetsDownloads();
                    downloader.downloadFiles(assetsParser.getAssetsDownloads());
                    done();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            lastTaskThreads = 4;

            threadPool.execute(serviceJarThread);
            threadPool.execute(librariesThread);
            threadPool.execute(resourcesThread);
            threadPool.execute(classifiersThread);

            threadPool.shutdown();

            while(lastTaskThreads != 0 & running) {
                Thread.sleep(25);
            }

            if(! running) {
                return false;
            }

            information.setStatus("status.ready");
            minecraftVersions.add(information);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void stop() {
        running = false;
        downloader.stop();
    }

    public void done() {
        synchronized(this) {
            lastTaskThreads--;
        }
    }

    public void download(String id, String name) {
        try {
            download(Objects.requireNonNull(config.getConfigString("area")), id, name);
        } catch (Exception ex) {
            config.set("area", "minecraft");
            download("minecraft", id, name);
        }
    }
}
