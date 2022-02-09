package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.network.downloader.RikaishiNikuiMinecraftDownloader;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftDownloadTask extends RikaishiNikuiTask {
    private final RikaishiNikuiMinecraftDownloader downloader = new RikaishiNikuiMinecraftDownloader();
    private final String gameId;
    private final String name;
    private boolean running = false;
    private boolean done = false;

    public RikaishiNikuiMinecraftDownloadTask(String gameId, String name) {
        super(UUID.randomUUID());
        this.gameId = gameId;
        this.name = name;
    }

    @Override
    public void preJoin() {
        running = true;
        done = false;
        logger.info("task " + getId() + " pre join done");
    }

    @Override
    protected void join() {
        logger.info("task " + getId() + " join to manager");
        synchronized(this) {
            try {
                downloader.apply(config.getConfigJSONObject("minecraft-downloader"));
            } catch (Exception e) {
                config.set("version-manifest", downloader.toJSONObject());
            }
            downloader.download(gameId, name);
        }
        done = true;
        running = false;
        done();
    }

    @Override
    protected void stop() {
        logger.info("stopping task " + getId());
        running = false;
        downloader.stop();
        done = true;
    }

    @Override
    protected void done() {
        if(!running) {
            logger.info("task " + getId() + " done");
        } else {
            stop();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
