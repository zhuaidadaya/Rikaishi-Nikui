package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedString;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationCachedText;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter.RikaishiNikuiSubmitter;
import com.github.zhuaidadaya.rikaishinikui.handler.text.Text;
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

    /**
     * a prepared for join
     */
    protected synchronized void prepare() {
        RikaishiNikuiTask parent = getParentTask();
        running = true;
        status = RikaishiNikuiTaskStatus.RUNNING;
        logger.info(taskTypeName + " " + getId() + " pre join");
        if (parent != null & !stop) {
            parent.run();
            done = false;
        }

        if (stop) {
            running = false;
        }
    }

    /**
     * what should do when task join
     * it is needing Override
     */
    protected abstract void join();

    /**
     * do prepare and join task
     */
    protected void run() {
        prepare();
        join();
    }

    public synchronized void rebuild(RikaishiNikuiTaskManager manager) {
        if (stop & !running) {
            logger.info(taskTypeName + " " + getId() + " rebuilding");
            done = false;
            stop = false;
            manager.join(this);
        } else {
            throw new IllegalStateException(taskTypeName + " " + getId() + " cannot rebuild");
        }
    }

    /**
     * exit after performing the desired actions <br>
     * such as confirm or stop action <br>
     * <br>
     * <b>need task override to performing exit actions <br></b>
     */
    public synchronized boolean quit() {
        exit();
        return true;
    }

    /**
     * manager task, need a manager to destroy
     *
     * @param manager entrust to manager, delete task
     */
    public synchronized void destroy(RikaishiNikuiTaskManager manager) {
        manager.destroy(this);
    }

    /**
     * direct exit task
     */
    public synchronized void exit() {
        if (running) {
            stop = true;
            RikaishiNikuiTask parent = getParentTask();
            if (parent != null) {
                parent.exit();
            }
            logger.info(taskTypeName + " " + getId() + "destroyed");
            running = false;
            done = true;
        }
    }

    protected void done() {
        status = RikaishiNikuiTaskStatus.DONE;
        if (!running) {
            logger.info(taskTypeName + " " + getId() + " done");
        } else {
            exit();
        }
    }

    protected void fail() {
        status = RikaishiNikuiTaskStatus.FAILED;
        if (!running) {
            logger.warn(taskTypeName + " " + getId() + " failed");
        } else {
            exit();
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

    protected void cover() {
        if (submitter != null) {
            submitter.cover();
        }
    }

    protected void submit(Text submit) {
        if (submitter != null) {
            submitter.submit(submit);
        }
    }

    protected RikaishiNikuiTaskStatus getStatus() {
        return status;
    }

    public boolean canExit() {
        return true;
    }

    public String getInformation() {
        return "";
    }

    protected String getTaskTypeName() {
        return taskTypeName;
    }

    protected String getProgress() {
        return "1/1";
    }
}
