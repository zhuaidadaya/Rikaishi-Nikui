package com.github.zhuaidadaya.rikaishinikui.handler.task;

public enum RikaishiNikuiTaskStatus {
    INACTIVE, RUNNING, DONE, FAILED;

    public static RikaishiNikuiTaskStatus of(String name) {
        return switch(name.toLowerCase()) {
            case "running" -> RUNNING;
            case "done" -> DONE;
            case "failed" -> FAILED;
            default -> INACTIVE;
        };
    }

    public String toString() {
        return "task.status." + name().toLowerCase();
    }
}
