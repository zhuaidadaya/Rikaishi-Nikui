package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedString;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedText;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter.RikaishiNikuiSubmitter;
import org.json.JSONObject;

import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public abstract class RikaishiNikuiTask {
    protected final UUID id;
    protected PaginationCachedString logs;
    protected RikaishiNikuiTaskStatus status = RikaishiNikuiTaskStatus.INACTIVE;
    protected boolean running = false;
    protected boolean done = false;
    protected boolean stop = false;
    protected RikaishiNikuiSubmitter submitter;
    protected RikaishiNikuiTask parent;
    private String taskTypeName = "RikaishiNikuiTask(Ab)";

    public RikaishiNikuiTask(UUID id, String taskTypeName) {
        this.id = id;
        this.taskTypeName = taskTypeName;
        logs = new PaginationCachedString(getId(), -1, "rikaishi_nikui");
    }

    public RikaishiNikuiTask(UUID id) {
        this.id = id;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("status", getStatus().name());
        json.put("parent", getParentTask().toJSONObject());
        json.put("type", getTaskTypeName());
        return json;
    }

    public String toString() {
        return toJSONObject().toString();
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

    protected void preJoin() {
        RikaishiNikuiTask parent = getParentTask();
        running = true;
        status = RikaishiNikuiTaskStatus.RUNNING;
        logger.info(taskTypeName + " " + getId() + " pre join");
        if (parent != null) {
            parent.preJoin();
            parent.join();
        }
        if (!stop) {
            done = false;
        } else {
            running = false;
        }
    }

    protected abstract void join();

    protected void stop() {
        stop = true;
        RikaishiNikuiTask parent = getParentTask();
        if (parent != null) {
            parent.stop();
        }
        logger.info("stopping " + taskTypeName + " " + getId());
        running = false;
        done = true;
    }

    protected void done() {
        status = RikaishiNikuiTaskStatus.DONE;
        if (!running) {
            logger.info(taskTypeName + " " + getId() + " done");
        } else {
            stop();
        }
    }

    protected void fail() {
        status = RikaishiNikuiTaskStatus.FAILED;
        if (!running) {
            logger.warn(taskTypeName + " " + getId() + " failed");
        } else {
            stop();
        }
    }

    protected abstract void log(String log);

    protected abstract void log(String log, LogLevel level);

    protected abstract PaginationCachedText<?,?> getPaginateCachedLog();

    protected abstract StringBuilder getLog();

    protected abstract StringBuilder getLog(int page);

    protected RikaishiNikuiSubmitter getSubmitter() {
        return this.submitter;
    }

    protected void setSubmitter(RikaishiNikuiSubmitter submitter) {
        try {
            getPaginateCachedLog().setComponent(submitter.getComponent());
        } catch (Exception e) {

        }
        this.submitter = submitter;
    }

    protected void submit() {
        if (submitter != null) {
            submitter.submit();
        }
    }

    protected RikaishiNikuiTaskStatus getStatus() {
        return status;
    }

    protected String getTaskTypeName() {
        return taskTypeName;
    }

    protected String getProgress() {
        return "1/1";
    }
}
