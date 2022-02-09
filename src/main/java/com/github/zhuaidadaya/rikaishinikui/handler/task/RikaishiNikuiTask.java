package com.github.zhuaidadaya.rikaishinikui.handler.task;

import java.util.UUID;

public abstract class RikaishiNikuiTask {
    private final UUID id;

    public RikaishiNikuiTask(UUID id) {
        this.id = id;
    }

    public void join(RikaishiNikuiTaskManager manager) {
        manager.join(this);
    }

    public void stop(RikaishiNikuiTaskManager manager) {
        manager.quit(this);
    }

    public void done(RikaishiNikuiTaskManager manager) {
        manager.done(this);
    }

    public UUID getId() {
        return id;
    }

    public abstract boolean isRunning();

    protected abstract void preJoin();

    protected abstract void join();

    protected abstract void stop();

    protected abstract void done();
}
