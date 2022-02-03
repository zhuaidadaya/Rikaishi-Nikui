package com.github.zhuaidadaya.rikaishinikui.storage;

import com.github.zhuaidadaya.rikaishinikui.RikaishiNikuiLauncher;
import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiFrame;
import com.github.zhuaidadaya.utils.config.ObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Variables {
    public static RikaishiNikuiLauncher launcher = new RikaishiNikuiLauncher();
    public static final String version = "1.0.0";
    public static final String entrust = "RikaishiNikui";
    public static Logger logger = LogManager.getLogger(entrust);
    public static ObjectConfigUtil config;
    public static Object2ObjectRBTreeMap<String, RikaishiNikuiFrame> frames = new Object2ObjectRBTreeMap<>();
}
