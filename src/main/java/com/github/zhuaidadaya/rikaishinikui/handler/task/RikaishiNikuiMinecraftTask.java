package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher.MinecraftLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedSingleText;
import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftTask extends RikaishiNikuiTask {
    private final PaginationCachedSingleText logs;
    private final MinecraftLauncher launcher;
    private LogLevel appendLoglevel = LogLevel.INFO;

    public RikaishiNikuiMinecraftTask(MinecraftLauncher launcher) {
        super(UUID.randomUUID(), "MinecraftTask(TS)");
        logs = new PaginationCachedSingleText(getId());
        this.launcher = launcher;
    }

    @Override
    protected void join() {
        if (running) {
            logger.info(getTaskTypeName() + " " + getId() + " join to manager");
            synchronized (this) {
                if (launcher.getJava().getVersion() >= launcher.getVersionInformation().getJavaRequires()) {
                    launcher.launch(getId().toString());
                } else {
                    launcher.setTaskFeedback("task.feedback.java.version.error");
                    launcher.fail();
                }
            }
        }
        done = true;
        running = false;
        if (launcher.isFailed()) {
            fail();
        } else {
            done();
        }
    }

    protected void stop() {
        stop = true;
        RikaishiNikuiTask parent = super.getParentTask();
        if (parent != null) {
            parent.stop();
        }
        logger.info("stopping " + getTaskTypeName() + " " + getId());
        try {
            launcher.stop();
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
            launcher.stop();
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
        switch (level) {
            case ERROR -> {
                logs.append(new SingleText(log, new RikaishiNikuiColor(246, 55, 65)));
            }
            case WARN -> {
                logs.append(new SingleText(log,new RikaishiNikuiColor(217,163,67)));
            }
            case CHAT, INFO -> {
                logs.append(new SingleText(log,new RikaishiNikuiColor(106,169,89)));
            }
        }
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
                } else if (match.contains("[chat]")) {
                    appendLoglevel = LogLevel.CHAT;
                } else {
                    appendLoglevel = LogLevel.INFO;
                }
            }
        } catch (Exception e) {

        }

        log(log, appendLoglevel);
    }

    public PaginationCachedSingleText getPaginateCachedLog() {
        return logs;
    }

    public StringBuilder getLog() {
        return logs.readStringBuilder();
    }

    public StringBuilder getLog(int page) {
        return logs.readStringBuilder(page);
    }
}
