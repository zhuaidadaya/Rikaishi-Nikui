package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.PaginateCachedLog;
import com.github.zhuaidadaya.rikaishinikui.network.downloader.RikaishiNikuiMinecraftDownloader;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.launcher;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiDownloadRefreshTask extends RikaishiNikuiTask {
    private final RikaishiNikuiMinecraftDownloader downloader;
    private boolean running = false;
    private boolean done = false;
    private boolean stop = false;

    public RikaishiNikuiDownloadRefreshTask(UUID id, RikaishiNikuiMinecraftDownloader downloader) {
        super(id);
        this.downloader = downloader;
    }

    public RikaishiNikuiDownloadRefreshTask(RikaishiNikuiMinecraftDownloader downloader) {
        super(UUID.randomUUID());
        this.downloader = downloader;
    }

    @Override
    public void preJoin() {
        RikaishiNikuiTask parent = super.getParentTask();
        running = true;
        logger.info("RikaishiNikuiDownloadRefreshTask " + getId() + " pre join");
        if(parent != null) {
            parent.preJoin();
            parent.join();
        }
        if(! stop) {
            done = false;
            logger.info("RikaishiNikuiDownloadRefreshTask " + getId() + " pre join done");
        } else {
            running = false;
        }
    }

    @Override
    protected void join() {
        if(running) {
            logger.info("RikaishiNikuiDownloadRefreshTask " + getId() + " join to manager");
            synchronized(this) {
                launcher.downloadVersions = downloader.getVersions();
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
        logger.info("stopping RikaishiNikuiDownloadRefreshTask " + getId());
        running = false;
        done = true;
    }

    @Override
    protected void done() {
        if(! running) {
            logger.info("RikaishiNikuiDownloadRefreshTask " + getId() + " done");
        } else {
            stop();
        }
    }

    @Override
    public void setParentTask(RikaishiNikuiTask task) {
        super.parentTask(task);
    }

    @Override
    public boolean isRunning() {
        return running;
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
