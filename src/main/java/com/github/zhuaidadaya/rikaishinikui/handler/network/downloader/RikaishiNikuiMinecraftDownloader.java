package com.github.zhuaidadaya.rikaishinikui.handler.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.*;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.logger.RikaishiNikuiLogger;
import com.github.zhuaidadaya.utils.integer.IntegerUtil;
import com.github.zhuaidadaya.utils.string.checker.StringCheckUtil;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiMinecraftDownloader {
    private final ResourceDownloader downloader = new ResourceDownloader();
    private final String DEFAULT_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    private final String DEFAULT_RESOURCE = "http://resources.download.minecraft.net";
    private int lastTaskThreads = 0;
    private int threads = -1;
    private boolean running = true;

    public RikaishiNikuiMinecraftDownloader() {
        logger = new RikaishiNikuiLogger(rikaishiNikuiLauncherTaskId, taskManager, "%t=s [%c/%level] %msg", LogManager.getLogger(entrust + "-Downloader"));
    }

    public void apply(JSONObject json) {
        threads = IntegerUtil.getIntFromJSON(json, "threads", -1);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("threads", threads);
        return json;
    }

    public VanillaMinecraftVersionsParser getVersions() {
        String manifest = config.getConfigString("minecraft-versions-manifest-url");
        if (manifest == null) {
            config.set("minecraft-versions-manifest-url", DEFAULT_MANIFEST);
            return getVersions(DEFAULT_MANIFEST);
        } else {
            return getVersions(manifest);
        }
    }

    public VanillaMinecraftVersionsParser getVersions(String url) {
        ResourceDownloader downloader = new ResourceDownloader();
        return getVersions(url, downloader);
    }

    public VanillaMinecraftVersionsParser getVersions(String url, ResourceDownloader downloader) {
        String result = downloader.downloadFileToStringBuilder(url).toString();
        JSONObject versionManifest = new JSONObject(result);
        return new VanillaMinecraftVersionsParser(versionManifest);
    }

    public boolean downloadVanilla(String area, String gameId, String name, String versionId, boolean isFix, String taskId) {
        MinecraftVersionInformation information = new MinecraftVersionInformation(versionId, name, "downloading");
        information.setTaskId(taskId);
        information.setArea(area);
        information.setVersion(gameId);
        return downloadVanilla(information, isFix);
    }

    public boolean downloadVanilla(MinecraftVersionInformation information, boolean checking) {
        try {
            String identity = (checking ? "check" : "download");

            String operation = (checking ? "checking" : "downloading");

            logger.debug(identity + " thread " + information.getTaskId() + "#0 started");

            minecraftVersions.add(information);

            lastTaskThreads = 4;

            running = true;

            information.setType("minecraft.type.vanilla");

            String versionId = information.getId();
            if (!information.isIdFormatted()) {
                versionId = information.getName();
            }
            String area = information.getArea();
            String gameId = information.getVersion();
            if (checking) {
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

            information.setArea(area);
            information.setId(versionId);

            minecraftVersions.add(information);

            VanillaMinecraftVersionsParser versionsParser = getVersions(url, downloader);

            String manifestPath = String.format("%s/versions/%s/%s.json", area, versionId, versionId);

            information.setPath(String.format("%s/versions/%s", area, versionId));

            VanillaMinecraftVersionParser versionParser = versionsParser.getVersion(gameId);

            downloader.downloadFile(new NetworkFileInformation(versionParser.getUrl(), manifestPath), 0);
            JSONObject version = new JSONObject(downloader.downloadFileToStringBuilder(manifestPath).toString());

            VanillaMinecraftAssetIndexParser assetsIndexParser = new VanillaMinecraftAssetIndexParser(version, area);

            downloader.downloadFile(new NetworkFileInformation(assetsIndexParser.getUrl(), assetsIndexParser.getPath(), assetsIndexParser.getSha1()), -1);

            VanillaMinecraftLibrariesParser downloadParser = new VanillaMinecraftLibrariesParser(version, area, os);

            String finalOs = os;
            VanillaMinecraftClassifiersParser classifiersParser = new VanillaMinecraftClassifiersParser(version, area, finalOs);

            VanillaMinecraftAssetsParser assetsParser = new VanillaMinecraftAssetsParser(resourceUrl, area, new JSONObject(downloader.downloadFileToStringBuilder(assetsIndexParser.getPath()).toString()));

            VanillaMinecraftServiceJarParser serviceJarParser = new VanillaMinecraftServiceJarParser(version.getJSONObject("downloads"), area, versionId);

            Thread classifiersThread = new Thread(() -> {
                logger.debug(identity + " thread " + information.getTaskId() + "#1 started");
                logger.debug("thread #1 " + operation + " " + classifiersParser.getClassifiersDownloads().size() + " files");
                try {
                    downloader.downloadFiles(classifiersParser.getClassifiersDownloads());
                } catch (Exception ex) {

                }
                logger.info((checking ? "check" : "download") + " thread " + information.getTaskId() + "#1 done");
                done();
            });

            Thread serviceJarThread = new Thread(() -> {
                logger.debug((checking ? "check" : "download") + " thread " + information.getTaskId() + "#2 started");
                logger.debug("thread #2 " + operation + " 2 files");
                try {
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getClientUrl(), serviceJarParser.getClientPath(), serviceJarParser.getClientSha1()), threads);
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getServerUrl(), serviceJarParser.getServerPath(), serviceJarParser.getServerSha1()), threads);
                } catch (Exception e) {

                }
                done();
                logger.info((checking ? "check" : "download") + " thread " + information.getTaskId() + "#2 done");
            });

            Thread librariesThread = new Thread(() -> {
                logger.debug((checking ? "check" : "download") + " thread " + information.getTaskId() + "#3 started");
                logger.debug("thread #3 " + operation + " " + downloadParser.getLibrariesDownloads().size() + " files");
                try {
                    downloader.downloadFiles(downloadParser.getLibrariesDownloads());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info((checking ? "check" : "download") + " thread " + information.getTaskId() + "#3 done");
                done();
            });

            Thread resourcesThread = new Thread(() -> {
                logger.debug((checking ? "check" : "download") + " thread " + information.getTaskId() + "#4 started");
                logger.debug("thread #4 " + operation + " " + assetsParser.getAssetsDownloads().size() + " files");
                try {
                    assetsParser.getAssetsDownloads();
                    downloader.downloadFiles(assetsParser.getAssetsDownloads());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info((checking ? "check" : "download") + " thread " + information.getTaskId() + "#4 done");
                done();
            });

            if (running) {
                threadPool.execute(serviceJarThread);
                threadPool.execute(librariesThread);
                threadPool.execute(resourcesThread);
                threadPool.execute(classifiersThread);
            }

            threadPool.shutdown();

            if (checking) information.setStatus("status.checking");
            else information.setStatus("status.downloading");

            information.setUrl(information.formatManifest());
            information.setReleaseTime(versionParser.getReleaseTime());
            information.setReleaseType(versionParser.getType());

            minecraftVersions.add(information);

            while (lastTaskThreads != 0) {
                if (!running & !checking) {
                    logger.warn("interrupting download task: " + information.getTaskId());
                    information.setStatus("status.interrupting");
                    minecraftVersions.add(information);
                }
                Thread.sleep(25);
            }

            if (!running & !checking) {
                information.setStatus("status.interrupted");
                information.setLockStatus("lock.interrupted");
                minecraftVersions.add(information);
                return false;
            }

            if (!checking) {
                information.addVmOptions("-server");
                // log4j2 - jndi inject bug
                information.addVmOptions("-Dlog4j2.formatMsgNoLookups=true", "-Dcom.sun.jndi.ldap.object.trustURLCodebase=false");
                information.addVmOption("-Duser.dir=\"" + information.getAbsolutePath() + "\"");
            }

            information.setStatus("status.ready");
            minecraftVersions.add(information);

            logger.info((checking ? "check" : "download") + " thread " + information.getTaskId() + "#0 done");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void stop() {
        running = false;
        downloader.stop();
    }

    public void done() {
        synchronized (this) {
            lastTaskThreads--;
        }
    }
}
