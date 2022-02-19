package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launch.MinecraftLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.PaginateCachedLog;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftTask extends RikaishiNikuiTask {
    private final MinecraftLauncher launcher;

    public RikaishiNikuiMinecraftTask(MinecraftLauncher launcher) {
        super(UUID.randomUUID(),"MinecraftTask(TS)");
        this.launcher = launcher;
    }

    @Override
    protected void join() {
        if(running) {
            logger.info(getTaskTypeName() + " " + getId() + " join to manager");
            synchronized(this) {
                launcher.launch(getId().toString());
            }
        }
        done = true;
        running = false;
        if(launcher.isFailed()) {
            fail();
        } else {
            done();
        }
    }

    protected void stop() {
        stop = true;
        RikaishiNikuiTask parent = super.getParentTask();
        if(parent != null) {
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
        if(! running) {
            logger.info(getTaskTypeName() + " " + getId() + " failed");
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
