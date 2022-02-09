package com.github.zhuaidadaya.rikaishinikui.handler.task;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;

import java.util.Collection;
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
        tasks.put(task.getId(), task);
        task.preJoin();
        Thread thread = new Thread(task :: join);
        threadPool.execute(thread);
    }

    public void quit(RikaishiNikuiTask task) {
        if(task.isRunning())
            task.stop();
        tasks.remove(task.getId());
    }

    public void quit(UUID task) {
        quit(tasks.get(task));
    }

    public void done(RikaishiNikuiTask task) {
        task.done();
    }

    public void task(UUID task) {
        synchronized(tasks) {
            if(tasks.containsKey(task))
                tasks.get(task).join();
            else
                throw new IllegalArgumentException("task " + task.toString() + " not found");
        }
    }

    public void task(RikaishiNikuiTask task) {
        task.join();
    }

    public Collection<RikaishiNikuiTask> getTasks() {
        return tasks.values();
    }
}
