package com.github.zhuaidadaya.rikaishinikui;

import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiLauncherTask;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class EntryPoint {
    public static void main(String[] args) {
        taskManager.join(new RikaishiNikuiLauncherTask(rikaishiNikuiLauncherTaskId, launcher));
    }
}
