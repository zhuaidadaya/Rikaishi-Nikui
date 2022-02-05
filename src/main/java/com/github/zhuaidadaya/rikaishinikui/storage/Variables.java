package com.github.zhuaidadaya.rikaishinikui.storage;

import com.github.zhuaidadaya.rikaishinikui.RikaishiNikuiLauncher;
import com.github.zhuaidadaya.rikaishinikui.language.Language;
import com.github.zhuaidadaya.rikaishinikui.language.TextFormat;
import com.github.zhuaidadaya.rikaishinikui.logger.RikaishiNikuiLogger;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.utils.config.ObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.apache.logging.log4j.LogManager;

public class Variables {
    public static RikaishiNikuiLauncher launcher = new RikaishiNikuiLauncher();
    public static TextFormat textFormat;
    public static Language language = Language.CHINESE;
    public static final String version = "1.0.0";
    public static final String entrust = "RikaishiNikui";
    public static RikaishiNikuiLogger logger = new RikaishiNikuiLogger(LogManager.getLogger(entrust),"[%d] [%c/%l] %m%n","yyyy-mm-dd HH:mm:ss:SSS");
    public static ObjectConfigUtil configUi;
    public static ObjectConfigUtil config;
    public static Object2ObjectRBTreeMap<String, RikaishiNikuiFrame> frames = new Object2ObjectRBTreeMap<>();
}
