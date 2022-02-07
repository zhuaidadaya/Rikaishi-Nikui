package com.github.zhuaidadaya.rikaishinikui.handler.task;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;

import java.util.Collection;
import java.util.UUID;

public class RikaishiNikuiTaskManager {
    private final Object2ObjectRBTreeMap<UUID, RikaishiNikuiTask> tasks = new Object2ObjectRBTreeMap<>();

    public RikaishiNikuiTaskManager() {

    }

    public boolean hasTask(UUID task) {
        return tasks.containsKey(task);
    }

    public void join(RikaishiNikuiTask task) {
        tasks.put(task.getId(), task);
        task.join();
    }

    public void quit(RikaishiNikuiTask task) {
        tasks.remove(task.getId());
        task.stop();
    }

    public void quit(UUID task) {
        tasks.get(task).stop();
        tasks.remove(task);
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
