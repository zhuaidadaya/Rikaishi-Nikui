package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launch.MinecraftLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.PaginateCachedLog;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class RikaishiNikuiMinecraftTask extends RikaishiNikuiTask {
    private final MinecraftLauncher launcher;
    private boolean running = false;
    private boolean done = false;
    private boolean stop = false;

    public RikaishiNikuiMinecraftTask(MinecraftLauncher launcher) {
        super(UUID.randomUUID());
        this.launcher = launcher;
    }

    @Override
    public void preJoin() {
        RikaishiNikuiTask parent = super.getParentTask();
        running = true;
        logger.info("RikaishiNikuiMinecraftTask " + getId() + " pre join");
        if(parent != null) {
            parent.preJoin();
            parent.join();
        }
        if(! stop) {
            done = false;
            logger.info("RikaishiNikuiMinecraftTask " + getId() + " pre join done");
        } else {
            running = false;
        }
    }

    @Override
    protected void join() {
        if(running) {
            logger.info("RikaishiNikuiMinecraftTask " + getId() + " join to manager");
            synchronized(this) {
                launcher.launch(getId().toString());
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
        logger.info("stopping RikaishiNikuiMinecraftTask " + getId());
        running = false;
        try {
            launcher.stop();
        } catch (Exception e) {

        }
        done = true;
    }

    @Override
    protected void done() {
        if(! running) {
            logger.info("RikaishiNikuiMinecraftTask " + getId() + " done");
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
