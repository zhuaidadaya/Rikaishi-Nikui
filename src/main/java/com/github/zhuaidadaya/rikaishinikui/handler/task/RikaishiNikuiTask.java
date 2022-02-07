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

    public UUID getId() {
        return id;
    }

    public abstract void join();

    public abstract void stop();
}
