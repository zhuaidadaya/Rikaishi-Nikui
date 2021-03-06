package com.github.zhuaidadaya.rikaishinikui.storage;

import com.github.zhuaidadaya.rikaishinikui.RikaishiNikuiLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.information.java.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.language.Language;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.RikaishiNikuiMinecraftDownloader;
import com.github.zhuaidadaya.rikaishinikui.handler.recoder.java.JavaVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.recoder.minecrtaft.MinecraftVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiTaskManager;
import com.github.zhuaidadaya.rikaishinikui.handler.text.formatter.TextFormatter;
import com.github.zhuaidadaya.rikaishinikui.logger.RikaishiNikuiLogger;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.rikaishinikui.handler.config.ObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

public class Variables {
    public static final String version = "1.0.0";
    public static final String entrust = "RikaishiNikui";
    public static final UUID rikaishiNikuiLauncherTaskId = UUID.randomUUID();
    public static RikaishiNikuiLauncher launcher = new RikaishiNikuiLauncher();
    public static TextFormatter textFormatter;
    public static Language language = Language.CHINESE;
    public static ObjectConfigUtil configUi;
    public static ObjectConfigUtil config;
    public static Object2ObjectRBTreeMap<String, RikaishiNikuiFrame> frames = new Object2ObjectRBTreeMap<>();
    public static RikaishiNikuiMinecraftDownloader minecraftDownloader = new RikaishiNikuiMinecraftDownloader();
    public static MinecraftVersionsRecorder minecraftVersions = new MinecraftVersionsRecorder();
    public static JavaVersionsRecorder javaVersions = new JavaVersionsRecorder();
    public static JavaVersionInformation usedJava;
    public static RikaishiNikuiTaskManager taskManager = new RikaishiNikuiTaskManager();
    public static RikaishiNikuiLogger logger = new RikaishiNikuiLogger(rikaishiNikuiLauncherTaskId, taskManager, "%t=s [%c/%level] %msg", LogManager.getLogger(entrust));
    public static String os;

    static {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) os = "windows";
        else if (osName.contains("Linux")) os = "linux";
        else if (osName.contains("Macos")) os = "macos";
    }
}
