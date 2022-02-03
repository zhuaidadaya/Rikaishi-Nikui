package com.github.zhuaidadaya.rikaishinikui;

import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiFrame;
import com.github.zhuaidadaya.utils.config.ObjectConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.locks.Lock;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiLauncher {
    private final int tickInterval = 100;
    public RikaishiNikuiFrame mainFrame;
    private boolean running = false;
    private boolean shutdown = false;

    public void init() {
        logger.info("loading for {} {}", entrust, version);

        config = new ObjectConfigUtil(entrust, System.getProperty("user.dir") + "/rikaishi_nikui", "rikaishi_nikui.conf");

        running = true;

        mainFrame = new RikaishiNikuiFrame(1000, 800, "main");
        mainFrame.getContentPane().setBackground(Color.BLACK);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frames.put(mainFrame.getName(), mainFrame);

        initCloseEvent();

        new Thread(() -> {
            while(running) {
                long tickStart = System.currentTimeMillis();

                synchronized(this) {
                    tick();
                }

                long tickTime = System.currentTimeMillis() - tickStart;
                if(tickTime < tickInterval) {
                    try {
                        Thread.sleep(tickInterval - tickTime);
                    } catch (InterruptedException e) {

                    }
                }
            }
            shutdown = true;
        }).start();

        mainFrame.setVisible(true);
    }

    public void tick() {

    }

    public void initCloseEvent() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                launcher.shutdown();
            }
        });
    }

    public void shutdown() {
        running = false;

        int waiting = 0;

        while(! shutdown) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }

            waiting += 50;

            if(waiting > 1000) {
                break;
            }
        }

        config.shutdown();
        shutdown = true;

        System.exit(0);
    }
}
