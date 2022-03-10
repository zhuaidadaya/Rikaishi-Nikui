package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.RikaishiNikuiLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedMultipleText;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter.RikaishiNikuiSubmitter;
import com.github.zhuaidadaya.rikaishinikui.ui.style.RikaishiNikuiColorStyle;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiLauncherTask extends RikaishiNikuiTask {
    private final PaginationCachedMultipleText logs;
    private final RikaishiNikuiLauncher launcher;
    private final String[] args;
    private LogLevel appendLoglevel = LogLevel.INFO;

    public RikaishiNikuiLauncherTask(UUID id, RikaishiNikuiLauncher launcher, String... args) {
        super(id, "RikaishiNikui(TS)");
        logs = new PaginationCachedMultipleText(getId());
        this.launcher = launcher;
        this.args = args;
    }

    @Override
    protected void join() {
        if (running) {
            logger.info(getTaskTypeName() + " " + getId() + " join to manager");
            synchronized (this) {
                launcher.init(args);
            }
        }
        done = true;
        running = false;
    }

    protected void stop() {
        stop = true;
        RikaishiNikuiTask parent = super.getParentTask();
        if (parent != null) {
            parent.stop();
        }
        logger.info("stopping " + getTaskTypeName() + " " + getId());
        try {
            launcher.shutdown();
        } catch (Exception e) {

        }
        running = false;
        done = true;
    }

    @Override
    protected void fail() {
        status = RikaishiNikuiTaskStatus.FAILED;
        if (!running) {
            logger.warn(getTaskTypeName() + " " + getId() + " failed");
        } else {
            stop();
        }
        try {
            launcher.shutdown();
        } catch (Exception e) {

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

    public void log(String log, LogLevel level) {
        logs.append(RikaishiNikuiColorStyle.formatBlackLog(log, level));
        submit();
    }

    public void log(String log) {
        try {
            if (log.startsWith("[")) {
                String match = log.toLowerCase();
                if (match.contains("/error]")) {
                    appendLoglevel = LogLevel.ERROR;
                } else if (match.contains("/warn]")) {
                    appendLoglevel = LogLevel.WARN;
                } else if (match.contains("/debug]")) {
                    appendLoglevel = LogLevel.DEBUG;
                } else {
                    appendLoglevel = LogLevel.INFO;
                }
            }
        } catch (Exception e) {

        }

        log(log, appendLoglevel);
    }

    public PaginationCachedMultipleText getPaginateCachedLog() {
        return logs;
    }

    public StringBuilder getLog() {
        return logs.readStringBuilder();
    }

    public StringBuilder getLog(int page) {
        return logs.readStringBuilder(page);
    }

    public void setSubmitter(RikaishiNikuiSubmitter submitter) {
        this.submitter = submitter;
        submit();
    }
}
