package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.PaginateCachedLog;
import com.github.zhuaidadaya.rikaishinikui.ui.log.submitter.RikaishiNikuiSubmitter;

import java.util.UUID;

public abstract class RikaishiNikuiTask {
    private final UUID id;
    protected RikaishiNikuiSubmitter submitter;
    protected final PaginateCachedLog logs = new PaginateCachedLog(getId());
    private RikaishiNikuiTask parent;

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

    protected void parentTask(RikaishiNikuiTask task) {
        this.parent = task;
    }

    public RikaishiNikuiTask getParentTask() {
        return parent;
    }

    public abstract void setParentTask(RikaishiNikuiTask task);

    public abstract boolean isRunning();

    protected abstract void preJoin();

    protected abstract void join();

    protected abstract void stop();

    protected abstract void done();

    protected abstract void log(String log);

    protected abstract PaginateCachedLog getPaginateCachedLog();

    protected abstract StringBuilder getLog();

    protected abstract StringBuilder getLog(int page);

    protected void setSubmitter(RikaishiNikuiSubmitter submitter) {
        this.submitter = submitter;
    }

    protected RikaishiNikuiSubmitter getSubmitter() {
        return this.submitter;
    }

    protected void submit(StringBuilder log) {
        if(submitter != null) {
            submitter.submit(log.toString());
        }
    }
}
