package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiTextComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.log.submitter.RikaishiNikuiSubmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RikaishiNikuiTaskManager {
    private final Object2ObjectRBTreeMap<UUID, RikaishiNikuiTask> tasks = new Object2ObjectRBTreeMap<>();
    private final Object2ObjectRBTreeMap<UUID, Thread> tasksThread = new Object2ObjectRBTreeMap<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public RikaishiNikuiTaskManager() {

    }

    public boolean hasTask(UUID task) {
        return tasks.containsKey(task);
    }

    public void join(RikaishiNikuiTask task) {
        RikaishiNikuiTask old = tasks.get(task.getId());
        if(old == null || ! old.isRunning()) {
            tasks.put(task.getId(), task);
            Thread thread = new Thread(() -> {
                task.preJoin();
                task.join();
            });
            threadPool.execute(thread);
        }
    }

    public void quit(RikaishiNikuiTask task) {
        if(task.isRunning())
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

    public Collection<RikaishiNikuiTask> getTasks() {
        return tasks.values();
    }

    public Map<UUID, RikaishiNikuiTask> getTasksMap() {
        return tasks;
    }

    public void submitter(RikaishiNikuiTextComponent component, RikaishiNikuiTask task) {
        submitter(component, task.getId());
    }

    public void submitter(RikaishiNikuiTextComponent component, UUID task) {
        tasks.get(task).setSubmitter(new RikaishiNikuiSubmitter(component));
    }

    public void clearSubmitter() {
        for(RikaishiNikuiTask task : tasks.values()) {
            task.setSubmitter(null);
        }
    }

    public void quitAll() {
        for(RikaishiNikuiTask task : tasks.values()) {
            task.done();
        }
    }

    public RikaishiNikuiTaskStatus getStatus(RikaishiNikuiTask task) {
        return getStatus(task.getId());
    }

    public RikaishiNikuiTaskStatus getStatus(UUID task) {
        return tasks.get(task).getStatus();
    }
}
