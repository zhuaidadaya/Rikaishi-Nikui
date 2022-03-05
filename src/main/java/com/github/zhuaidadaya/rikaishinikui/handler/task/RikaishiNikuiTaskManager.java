package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter.RikaishiNikuiSubmitter;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RikaishiNikuiTaskManager {
    private final Object2ObjectRBTreeMap<UUID, RikaishiNikuiTask> tasks = new Object2ObjectRBTreeMap<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public RikaishiNikuiTaskManager() {

    }

    public boolean hasTask(UUID task) {
        return tasks.containsKey(task);
    }

    public void join(RikaishiNikuiTask task) {
        RikaishiNikuiTask old = tasks.get(task.getId());
        if (old == null || !old.isRunning()) {
            tasks.put(task.getId(), task);
            Thread thread = new Thread(() -> {
                task.preJoin();
                task.join();
            });
            threadPool.execute(thread);
        }
    }

    public void quit(RikaishiNikuiTask task) {
        if (task.isRunning())
            task.stop();
        tasks.remove(task.getId());
    }

    public void quit(UUID task) {
        quit(tasks.get(task));
    }

    public StringBuilder getLog(RikaishiNikuiTask task) {
        return task.getLog();
    }

    public StringBuilder getLog(UUID task) {
        return getLog(tasks.get(task));
    }

    public void done(RikaishiNikuiTask task) {
        task.done();
    }

    public void log(UUID id, String log) {
        tasks.get(id).log(log);
    }

    public void log(String id, String log) {
        log(UUID.fromString(id), log);
    }

    public void log(UUID id, String log, LogLevel level) {
        tasks.get(id).log(log, level);
    }

    public void log(String id, String log,  LogLevel level) {
        log(UUID.fromString(id), log, level);
    }

    public Collection<RikaishiNikuiTask> getTasks() {
        return tasks.values();
    }

    public Map<UUID, RikaishiNikuiTask> getTasksMap() {
        return tasks;
    }

    public void submitter(RikaishiNikuiLogComponent component, RikaishiNikuiTask task) {
        submitter(component, task.getId());
    }

    public void submitter(RikaishiNikuiLogComponent component, UUID task) {
        tasks.get(task).getPaginateCachedLog().setComponent(component);
        tasks.get(task).setSubmitter(new RikaishiNikuiSubmitter(tasks.get(task).getPaginateCachedLog().getTextManager()));
    }

    public void clearSubmitter() {
        for (RikaishiNikuiTask task : tasks.values()) {
            task.setSubmitter(null);
        }
    }

    public void quitAll() {
        for (RikaishiNikuiTask task : tasks.values()) {
            task.done();
        }
    }

    public void quitAll(RikaishiNikuiTask matcher) {
        for (RikaishiNikuiTask task : tasks.values()) {
            if (matcher.getTaskTypeName().equals(task.getTaskTypeName())) {
                task.done();
            }
        }
    }

    public RikaishiNikuiTaskStatus getStatus(RikaishiNikuiTask task) {
        return getStatus(task.getId());
    }

    public RikaishiNikuiTaskStatus getStatus(UUID task) {
        return tasks.get(task).getStatus();
    }
}
