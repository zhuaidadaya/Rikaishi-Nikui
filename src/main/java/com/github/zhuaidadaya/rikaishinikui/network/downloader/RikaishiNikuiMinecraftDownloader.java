package com.github.zhuaidadaya.rikaishinikui.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.*;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.utils.integer.IntegerUtil;
import com.github.zhuaidadaya.utils.string.checker.StringCheckUtil;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiMinecraftDownloader {
    private final int taskThreads = 0;
    private final ResourceDownloader downloader = new ResourceDownloader();
    private final String DEFAULT_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private final String DEFAULT_RESOURCE = "http://resources.download.minecraft.net";
    private int lastTaskThreads = 0;
    private int threads = - 1;
    private boolean running = true;

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

    public boolean downloadVanilla(String area, String gameId, String name, String versionId, boolean isFix, String taskId) {
        MinecraftVersionInformation information = new MinecraftVersionInformation(versionId, name, "downloading");
        information.setTaskId(taskId);
        information.setArea(area);
        information.setVersion(gameId);
        return downloadVanilla(information, isFix);
    }

    public boolean downloadVanilla(MinecraftVersionInformation information, boolean isFix) {
        try {
            running = true;

            information.setType("minecraft.type.vanilla");

            String versionId = information.getId();
            if(! information.isIdFormatted()) {
                versionId = information.getName();
            }
            String area = information.getArea();
            String gameId = information.getVersion();
            if(isFix) {
                information.setStatus("status.checking");
            } else {
                information.setStatus("status.downloading");
            }

            information.setUrl("status.getting");

            minecraftVersions.add(information);

            ExecutorService threadPool = Executors.newCachedThreadPool();

            String url;
            url = StringCheckUtil.getNotNull(config.getConfigString("minecraft-versions-manifest-url"), DEFAULT_MANIFEST);
            config.set("minecraft-versions-manifest-url", url);

            String resourceUrl;
            resourceUrl = StringCheckUtil.getNotNull(config.getConfigString("minecraft-versions-resource-url"), DEFAULT_RESOURCE);
            config.set("minecraft-versions-resource-url", resourceUrl);

            information.setStatus("status.parsing");

            information.setUrl(information.formatManifest());

            minecraftVersions.add(information);

            MinecraftVersionsParser versionsParser = getVersions(url, downloader);

            String manifestPath = String.format("%s/versions/%s/%s.json", area, versionId, versionId);
            downloader.downloadFile(new NetworkFileInformation(versionsParser.getVersion(gameId).getUrl(), manifestPath), 0);
            JSONObject version = new JSONObject(downloader.downloadFileToStringBuilder(manifestPath).toString());

            MinecraftAssetIndexParser assetsIndexParser = new MinecraftAssetIndexParser(version, area);

            downloader.downloadFile(new NetworkFileInformation(assetsIndexParser.getUrl(), assetsIndexParser.getPath(), assetsIndexParser.getSha1()), - 1);

            MinecraftLibrariesParser downloadParser = new MinecraftLibrariesParser(version, area, os);

            String finalOs = os;
            MinecraftClassifiersParser classifiersParser = new MinecraftClassifiersParser(version, area, finalOs);

            MinecraftAssetsParser assetsParser = new MinecraftAssetsParser(resourceUrl, area, new JSONObject(downloader.downloadFileToStringBuilder(assetsIndexParser.getPath()).toString()));

            MinecraftServiceJarParser serviceJarParser = new MinecraftServiceJarParser(version.getJSONObject("downloads"), area, versionId);

            Thread classifiersThread = new Thread(() -> {
                try {
                    downloader.downloadFiles(classifiersParser.getClassifiersDownloads());
                } catch (Exception ex) {

                }
                done();
            });

            Thread serviceJarThread = new Thread(() -> {
                try {
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getClientUrl(), serviceJarParser.getClientPath(), serviceJarParser.getClientSha1()), threads);
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getServerUrl(), serviceJarParser.getServerPath(), serviceJarParser.getServerSha1()), threads);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
            });

            Thread librariesThread = new Thread(() -> {
                try {
                    downloader.downloadFiles(downloadParser.getLibrariesDownloads());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
            });

            Thread resourcesThread = new Thread(() -> {
                try {
                    assetsParser.getAssetsDownloads();
                    downloader.downloadFiles(assetsParser.getAssetsDownloads());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
            });

            lastTaskThreads = 4;

            if(running) {
                threadPool.execute(serviceJarThread);
                threadPool.execute(librariesThread);
                threadPool.execute(resourcesThread);
                threadPool.execute(classifiersThread);
            }

            threadPool.shutdown();

            if(isFix)
                information.setStatus("status.checking");
            else
                information.setStatus("status.downloading");

            information.setUrl(information.formatManifest());

            minecraftVersions.add(information);

            while(lastTaskThreads != 0) {
                if(! running & ! isFix) {
                    information.setStatus("status.interrupting");
                    minecraftVersions.add(information);
                }
                Thread.sleep(25);
            }

            if(! running & ! isFix) {
                information.setStatus("status.interrupted");
                information.setLockStatus("lock.interrupted");
                minecraftVersions.add(information);
                return false;
            }

            information.setStatus("status.ready");
            minecraftVersions.add(information);
        } catch (Exception e) {
            return false;
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
}
