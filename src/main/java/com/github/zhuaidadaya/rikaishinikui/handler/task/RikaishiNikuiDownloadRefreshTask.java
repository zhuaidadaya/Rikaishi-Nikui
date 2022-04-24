package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.RikaishiNikuiMinecraftDownloader;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedString;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.launcher;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiDownloadRefreshTask extends RikaishiNikuiTask {
    private final RikaishiNikuiMinecraftDownloader downloader;

    public RikaishiNikuiDownloadRefreshTask(UUID id, RikaishiNikuiMinecraftDownloader downloader) {
        super(id, "DownloadTask(T: Refresh)");
        this.downloader = downloader;
    }

    public RikaishiNikuiDownloadRefreshTask(RikaishiNikuiMinecraftDownloader downloader) {
        super(UUID.randomUUID(), "DownloadTask(T: Refresh)");
        this.downloader = downloader;
    }

    @Override
    protected synchronized void join() {
        boolean failed = false;
        if(running) {
            logger.info(getTaskTypeName() + " " + getId() + " join to manager");
            synchronized(this) {
                try {
                    launcher.setDownloadVersions(downloader.getVersions());
                } catch (Exception e) {
                    failed = true;
                }
            }
        }
        done = true;
        running = false;
        if(! failed) {
            done();
        } else {
            fail();
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
        log(log, LogLevel.INFO);
    }

    public void log(String log, LogLevel level) {
        logs.append(log);
        submit(new SingleText(log));
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
