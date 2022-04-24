package com.github.zhuaidadaya.rikaishinikui.handler.task;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustExecution;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter.RikaishiNikuiSubmitter;
import com.github.zhuaidadaya.rikaishinikui.logger.RikaishiNikuiLogger;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.apache.logging.log4j.LogManager;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiTaskManager {
    public static RikaishiNikuiLogger logger = new RikaishiNikuiLogger(rikaishiNikuiLauncherTaskId, taskManager, "%t=s [%c/%level] %msg", LogManager.getLogger("TaskManager"));
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
            threadPool.execute(new Thread(task::run));
        }
    }

    public void destroy(RikaishiNikuiTask task) {
        if (task.isRunning()) task.exit();
        tasks.remove(task.getId());
    }

    public void destroy(UUID task) {
        destroy(tasks.get(task));
    }

    public boolean quit(RikaishiNikuiTask task) {
        if (task.canExit()) {
            return task.quit();
        } else {
            return false;
        }
    }

    public boolean quit(UUID task) {
        return quit(tasks.get(task));
    }

    public void exit(UUID task) {
        exit(tasks.get(task));
    }

    public void exit(RikaishiNikuiTask task) {
        if (task.canExit()) task.exit();
    }

    public void rebuild(UUID task) {
        rebuild(tasks.get(task));
    }

    public void rebuild(RikaishiNikuiTask task) {
        task.rebuild(this);
    }

    public StringBuilder getLog(RikaishiNikuiTask task) {
        return task.getLog();
    }

    public StringBuilder getLog(UUID task) {
        return getLog(tasks.get(task));
    }

    public void log(UUID id, String log) {
        EntrustExecution.notNull(tasks.get(id), e -> {
            e.log(log);
        });
    }

    public void log(String id, String log) {
        log(UUID.fromString(id), log);
    }

    public void log(UUID id, String log, LogLevel level) {
        EntrustExecution.notNull(tasks.get(id), e -> {
            e.log(log, level);
        });
//        tasks.get(id).log(log, level);
    }

    public void log(String id, String log, LogLevel level) {
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
        try {
            return tasks.get(task).getStatus();
        } catch (Exception e) {
            return RikaishiNikuiTaskStatus.INACTIVE;
        }
    }

    public String getProgress(RikaishiNikuiTask task) {
        return getProgress(task.getId());
    }

    public String getProgress(UUID id) {
        try {
            return tasks.get(id).getProgress();
        } catch (Exception e) {
            return "1/1";
        }
    }
}
