package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.PaginateCachedLog;
import com.github.zhuaidadaya.rikaishinikui.network.downloader.RikaishiNikuiMinecraftDownloader;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftDownloadTask extends RikaishiNikuiTask {
    private final RikaishiNikuiMinecraftDownloader downloader = new RikaishiNikuiMinecraftDownloader();
    private String gameId;
    private String name;
    private String versionId;
    private String area;
    private boolean running = false;
    private boolean isFix = false;
    private boolean done = false;
    private boolean stop = false;
    private MinecraftVersionInformation information;

    public RikaishiNikuiMinecraftDownloadTask(MinecraftVersionInformation information, UUID id, boolean isFix) {
        super(id);
        this.information = information;
        this.isFix = isFix;
    }

    public RikaishiNikuiMinecraftDownloadTask(String gameId, String name, UUID id, String versionId, String area, boolean isFix) {
        super(id);
        this.gameId = gameId;
        this.name = name;
        this.versionId = versionId;
        this.area = area;
        this.isFix = isFix;
    }

    public RikaishiNikuiMinecraftDownloadTask(String gameId, String name, UUID id, String versionId, String area) {
        super(id);
        this.gameId = gameId;
        this.name = name;
        this.versionId = versionId;
        this.area = area;
    }

    @Override
    public void preJoin() {
        RikaishiNikuiTask parent = super.getParentTask();
        running = true;
        logger.info("RikaishiNikuiMinecraftDownloadTask " + getId() + " pre join");
        if(parent != null) {
            parent.preJoin();
            parent.join();
        }
        if(! stop) {
            done = false;
            logger.info("RikaishiNikuiMinecraftDownloadTask " + getId() + " pre join done");
        } else {
            running = false;
        }
    }

    @Override
    protected void join() {
        if(running) {
            logger.info("RikaishiNikuiMinecraftDownloadTask " + getId() + " join to manager");
            synchronized(this) {
                try {
                    downloader.apply(config.getConfigJSONObject("minecraft-downloader"));
                } catch (Exception e) {
                    config.set("version-manifest", downloader.toJSONObject());
                }
                if(information == null) {
                    downloader.downloadVanilla(area, gameId, name, versionId, isFix, getParentTask() != null ? getParentTask().getId().toString() : getId().toString());
                } else {
                    if(getParentTask() != null) {
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

    @Override
    protected void stop() {
        stop = true;
        RikaishiNikuiTask parent = super.getParentTask();
        if(parent != null) {
            parent.stop();
        }
        logger.info("stopping RikaishiNikuiMinecraftDownloadTask " + getId());
        running = false;
        downloader.stop();
        done = true;
    }

    @Override
    protected void done() {
        if(! running) {
            logger.info("RikaishiNikuiMinecraftDownloadTask " + getId() + " done");
        } else {
            stop();
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
        logs.append(log);
        submit(logs.read());
    }

    public PaginateCachedLog getPaginateCachedLog() {
        return logs;
    }

    public StringBuilder getLog() {
        return logs.read();
    }

    public StringBuilder getLog(int page) {
        return logs.read(page);
    }
}
