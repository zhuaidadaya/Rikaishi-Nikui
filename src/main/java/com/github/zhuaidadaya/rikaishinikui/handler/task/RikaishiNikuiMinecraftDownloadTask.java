package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.RikaishiNikuiMinecraftDownloader;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedString;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftDownloadTask extends RikaishiNikuiTask {
    private final RikaishiNikuiMinecraftDownloader downloader = new RikaishiNikuiMinecraftDownloader();
    private String gameId;
    private String name;
    private String versionId;
    private String area;
    private boolean isFix = false;
    private MinecraftVersionInformation information;

    public RikaishiNikuiMinecraftDownloadTask(MinecraftVersionInformation information, UUID id, boolean isFix) {
        super(id, "DownloadTask(TS)");
        this.information = information;
        this.isFix = isFix;
    }

    public RikaishiNikuiMinecraftDownloadTask(String gameId, String name, UUID id, String versionId, String area, boolean isFix) {
        super(id, "DownloadTask(TS)");
        this.gameId = gameId;
        this.name = name;
        this.versionId = versionId;
        this.area = area;
        this.isFix = isFix;
    }

    public RikaishiNikuiMinecraftDownloadTask(String gameId, String name, UUID id, String versionId, String area) {
        super(id, "DownloadTask(TS)");
        this.gameId = gameId;
        this.name = name;
        this.versionId = versionId;
        this.area = area;
    }

    @Override
    protected void join() {
        if (running) {
            logger.info(getTaskTypeName() + " " + getId() + " join to manager");
            synchronized (this) {
                try {
                    downloader.apply(config.getConfigJSONObject("minecraft-downloader"));
                } catch (Exception e) {
                    config.set("version-manifest", downloader.toJSONObject());
                }
                if (information == null) {
                    downloader.downloadVanilla(area, gameId, name, versionId, isFix, getParentTask() != null ? getParentTask().getId().toString() : getId().toString());
                } else {
                    if (getParentTask() != null) {
                        information.setTaskId(getParentTask().getId().toString());
                    }
                    downloader.downloadVanilla(information, isFix);
                }
            }
        }
        done = true;
        running = false;
        done();
    }

    public void stop() {
        stop = true;
        RikaishiNikuiTask parent = getParentTask();
        if (parent != null) {
            parent.stop();
        }
        logger.info("stopping " + getTaskTypeName() + " " + getId());
        running = false;
        done = true;
        try {
            downloader.stop();
        } catch (Exception e) {

        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public void setParentTask(RikaishiNikuiTask task) {
        super.parentTask(task);
    }

    public void log(String log) {
        log(log, LogLevel.INFO);
    }

    public void log(String log, LogLevel level) {
        logs.append(log);
        submit();
    }

    public PaginationCachedString getPaginateCachedLog() {
        return logs;
    }

    public StringBuilder getLog() {
        return logs.readAsStringBuilder();
    }

    public StringBuilder getLog(int page) {
        return logs.readAsStringBuilder(page);
    }
}
