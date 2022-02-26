package com.github.zhuaidadaya.rikaishinikui.storage;

import com.github.zhuaidadaya.rikaishinikui.RikaishiNikuiLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.RikaishiNikuiMinecraftDownloader;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiTaskManager;
import com.github.zhuaidadaya.rikaishinikui.language.Language;
import com.github.zhuaidadaya.rikaishinikui.language.TextFormat;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.utils.config.DiskObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Variables {
    public static RikaishiNikuiLauncher launcher = new RikaishiNikuiLauncher();
    public static TextFormat textFormat;
    public static Language language = Language.CHINESE;
    public static final String version = "1.0.0";
    public static final String entrust = "RikaishiNikui";
    public static Logger logger = LogManager.getLogger(entrust);
    public static DiskObjectConfigUtil configUi;
    public static DiskObjectConfigUtil config;
    public static Object2ObjectRBTreeMap<String, RikaishiNikuiFrame> frames = new Object2ObjectRBTreeMap<>();
    public static RikaishiNikuiMinecraftDownloader minecraftDownloader = new RikaishiNikuiMinecraftDownloader();
    public static MinecraftVersionsRecorder minecraftVersions = new MinecraftVersionsRecorder();
    public static JavaVersionsRecorder javaVersions = new JavaVersionsRecorder();
    public static JavaVersionInformation usedJava;
    public static RikaishiNikuiTaskManager taskManager = new RikaishiNikuiTaskManager();
    public static String os;

    static {
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows"))
            os = "windows";
        else if(osName.contains("Linux"))
            os = "linux";
        else if(osName.contains("Macos"))
            os = "macos";
    }
}
