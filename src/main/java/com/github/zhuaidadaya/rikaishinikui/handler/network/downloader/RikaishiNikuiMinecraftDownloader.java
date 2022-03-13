package com.github.zhuaidadaya.rikaishinikui.handler.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.MinecraftLoaderType;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric.FabricMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.*;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.task.download.MinecraftDownloadEntrustType;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsConcurrentWaiting;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsDoneCondition;
import com.github.zhuaidadaya.rikaishinikui.logger.RikaishiNikuiLogger;
import com.github.zhuaidadaya.utils.integer.IntegerUtil;
import com.github.zhuaidadaya.utils.resource.Resources;
import com.github.zhuaidadaya.utils.string.checker.StringCheckUtil;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

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

    public boolean downloadVanilla(String area, String gameId, String name, String versionId, MinecraftDownloadEntrustType entrustType, String taskId) {
        MinecraftVersionInformation information = new MinecraftVersionInformation(UUID.randomUUID().toString(), name, "downloading");
        information.setTaskId(taskId);
        information.setArea(area);
        information.setVersion(gameId);
        return downloadVanilla(information, entrustType);
    }

    public boolean download(MinecraftVersionInformation information, MinecraftDownloadEntrustType entrustType, MinecraftLoaderType type) {
        String manifestPath = information.formatManifest();
        Resources.createFile(new File(manifestPath).getParent() + "/status.lock");

        boolean result = switch (type) {
            case VANILLA -> downloadVanilla(information, entrustType);
            case FABRIC -> downloadFabric(information, entrustType);
            case FORGE -> downloadVanilla(information, entrustType);
        };

        information.setLockStatus("lock.not");
        information.setStatus("status.ready");
        minecraftVersions.update(information);

        Resources.deleteFile(new File(manifestPath).getParent() + "/status.lock");

        return result;
    }

    public boolean downloadFabric(MinecraftVersionInformation information, MinecraftDownloadEntrustType entrustType) {
        Thread vanilla = new Thread(() -> {
            downloadVanilla(information, entrustType);
        });

        Thread fabric = new Thread(() -> {
            String parse = NetworkUtil.downloadToStringBuilder(information.formatManifest()).toString();
            FabricMinecraftLibrariesParser parser = new FabricMinecraftLibrariesParser(new JSONObject(parse), information.getArea(), information.getVersion());
            Set<NetworkFileInformation> downloads = parser.getLibrariesDownloads();
            logger.debug("(check) fabric thread for " + information.getTaskId() + " started");
            logger.debug("fabric thread checking 2 files");

            downloader.downloadFiles(downloads, "fabric");

            logger.info("(check) fabric thread for " + information.getTaskId() + " done");
        });

        ThreadsConcurrentWaiting threads = new ThreadsConcurrentWaiting(ThreadsDoneCondition.ALIVE, vanilla, fabric);

        threads.start();

        return true;
    }

    public boolean downloadVanilla(MinecraftVersionInformation information, MinecraftDownloadEntrustType entrustType) {
        try {
            String identity = switch (entrustType) {
                case DOWNLOAD -> "download";
                case FIX -> "fix";
                case LAUNCH -> "check";
            };

            String operation = switch (entrustType) {
                case DOWNLOAD -> "downloading";
                case FIX -> "fixing";
                case LAUNCH -> "checking";
            };

            logger.debug(identity + " thread " + information.getTaskId() + "#0 started");

            minecraftVersions.update(information);

            running = true;

            information.setType("minecraft.type.vanilla");

            String versionId = information.getId();
            if (!information.isIdFormatted()) {
                versionId = information.getName();
            }
            String area = information.getArea();
            String gameId = information.getVersion();
            information.setStatus("status." + operation);

            information.setUrl("status.getting");

            minecraftVersions.update(information);

            ThreadsConcurrentWaiting threads = new ThreadsConcurrentWaiting(ThreadsDoneCondition.ALIVE);

            String url;
            url = StringCheckUtil.getNotNull(config.getConfigString("minecraft-versions-manifest-url"), DEFAULT_MANIFEST);
            config.set("minecraft-versions-manifest-url", url);

            String resourceUrl;
            resourceUrl = StringCheckUtil.getNotNull(config.getConfigString("minecraft-versions-resource-url"), DEFAULT_RESOURCE);
            config.set("minecraft-versions-resource-url", resourceUrl);

            information.setStatus("status.parsing");

            information.setUrl(information.formatManifest());

            information.setArea(area);

            minecraftVersions.update(information);

            VanillaMinecraftVersionsParser versionsParser = getVersions(url, downloader);

            String manifestPath = information.formatManifest();

            information.setPath(String.format("%s/versions/%s", area, versionId));

            VanillaMinecraftVersionParser versionParser = versionsParser.getVersion(gameId);

            if (entrustType != MinecraftDownloadEntrustType.LAUNCH) {
                downloader.downloadFile(new NetworkFileInformation(versionParser.getUrl(), manifestPath), -1);
            }

            JSONObject version = new JSONObject(downloader.downloadFileToStringBuilder(manifestPath).toString());

            VanillaMinecraftAssetIndexParser assetsIndexParser = new VanillaMinecraftAssetIndexParser(version, area);

            downloader.downloadFile(new NetworkFileInformation(assetsIndexParser.getUrl(), assetsIndexParser.getPath(), assetsIndexParser.getSha1()), -1);

            VanillaMinecraftLibrariesParser downloadParser = new VanillaMinecraftLibrariesParser(version, area, os);

            String finalOs = os;
            VanillaMinecraftClassifiersParser classifiersParser = new VanillaMinecraftClassifiersParser(version, area, finalOs);

            VanillaMinecraftAssetsParser assetsParser = new VanillaMinecraftAssetsParser(resourceUrl, area, new JSONObject(downloader.downloadFileToStringBuilder(assetsIndexParser.getPath()).toString()));

            VanillaMinecraftServiceJarParser serviceJarParser = new VanillaMinecraftServiceJarParser(version.getJSONObject("downloads"), area, versionId);

            Thread classifiersThread = new Thread(() -> {
                increase();
                Set<NetworkFileInformation> downloads = classifiersParser.getClassifiersDownloads();
                logger.debug("(" + identity + ") classifiers thread for " + information.getTaskId() + " started");
                logger.debug("classifiers thread " + operation + " " + downloads.size() + " files");
                try {
                    downloader.downloadFiles(downloads, "classifiers");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                logger.info("(" + identity + ") classifiers thread for " + information.getTaskId() + " done");
                done();
            });

            Thread serviceJarThread = new Thread(() -> {
                increase();
                logger.debug("(" + identity + ") service#1 thread for " + information.getTaskId() + " started");
                logger.debug("service thread#1 " + operation + " 1 files");
                try {
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getClientUrl(), serviceJarParser.getClientPath(), serviceJarParser.getClientSha1()), this.threads, "service#1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
                logger.info("(" + identity + ") service thread#1 for " + information.getTaskId() + " done");
            });

            Thread serviceJarThread2 = new Thread(() -> {
                increase();
                logger.debug("(" + identity + ") service#2 thread for " + information.getTaskId() + " started");
                logger.debug("service thread#2 " + operation + " 1 files");
                try {
                    downloader.downloadFile(new NetworkFileInformation(serviceJarParser.getServerUrl(), serviceJarParser.getServerPath(), serviceJarParser.getServerSha1()), this.threads, "service#2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                done();
                logger.info("(" + identity + ") service#2 thread for " + information.getTaskId() + " done");
            });

            Thread librariesThread = new Thread(() -> {
                increase();
                Set<NetworkFileInformation> downloads = downloadParser.getLibrariesDownloads();
                logger.debug("(" + identity + ") libraries thread for " + information.getTaskId() + " started");
                logger.debug("libraries thread " + operation + " 2 files");
                try {
                    downloader.downloadFiles(downloads, "libraries");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("(" + identity + ") libraries thread for " + information.getTaskId() + " done");
                done();
            });

            Thread resourcesThread = new Thread(() -> {
                increase();
                Set<NetworkFileInformation> downloads = assetsParser.getFasterAssetsDownloads();
                logger.debug("(" + identity + ") resources thread for " + information.getTaskId() + " started");
                logger.debug("resources thread " + operation + " 2 files");
                try {
                    downloader.downloadFiles(downloads, "resources");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("(" + identity + ") resources thread for " + information.getTaskId() + " done");
                done();
            });

            if (running) {
                threads.add(serviceJarThread);
                threads.add(serviceJarThread2);
                threads.add(librariesThread);
                threads.add(resourcesThread);
                threads.add(classifiersThread);
            }

            information.setStatus("status." + operation);

            information.setUrl(information.formatManifest());
            information.setReleaseTime(versionParser.getReleaseTime());
            information.setReleaseType(versionParser.getType());

            minecraftVersions.update(information);

            threads.start();

            if (!running & entrustType != MinecraftDownloadEntrustType.LAUNCH) {
                logger.warn("interrupting download task: " + information.getTaskId());
                information.setStatus("status.interrupting");
                minecraftVersions.update(information);
            }

            if (!running & entrustType != MinecraftDownloadEntrustType.LAUNCH) {
                information.setStatus("status.interrupted");
                information.setLockStatus("lock.interrupted");
                minecraftVersions.update(information);
                return false;
            }

            if (entrustType == MinecraftDownloadEntrustType.DOWNLOAD) {
                information.addVmOptions(new VmOption("-server").setNote("note.default"));
                // log4j2 - jndi inject bug
                information.addVmOptions(new VmOption("-Dlog4j2.formatMsgNoLookups=true").setNote("note.default.jndi.bug"), new VmOption("-Dcom.sun.jndi.ldap.object.trustURLCodebase=false").setNote("note.default.jndi.bug"));
                information.addVmOption("-Duser.dir=\"" + information.getAbsolutePath() + "\"");
            }

            logger.info(identity + " thread " + information.getTaskId() + "#0 done");
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public LinkedHashMap<String, String> getProgress() {
        return downloader.getProgress();
    }

    public void stop() {
        running = false;
        downloader.stop();
    }

    public void increase() {
        synchronized (this) {
            lastTaskThreads++;
        }
    }

    public void done() {
        synchronized (this) {
            lastTaskThreads--;
        }
    }
}
