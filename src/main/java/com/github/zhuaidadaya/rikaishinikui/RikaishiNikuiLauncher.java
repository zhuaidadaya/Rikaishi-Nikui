package com.github.zhuaidadaya.rikaishinikui;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.language.TextFormat;
import com.github.zhuaidadaya.rikaishinikui.language.Language;
import com.github.zhuaidadaya.rikaishinikui.language.LanguageResource;
import com.github.zhuaidadaya.rikaishinikui.ui.button.RikaishiNikuiButton;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiTextFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.list.RikaishiNikuiScrollStringList;
import com.github.zhuaidadaya.rikaishinikui.ui.list.RikaishiNikuiStringList;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiButtonPanel;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiPanel;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiTextPanel;
import com.github.zhuaidadaya.utils.config.EncryptionType;
import com.github.zhuaidadaya.utils.config.ObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiLauncher {
    private final int tickInterval = 100;

    public RikaishiNikuiFrame mainFrame;
    public RikaishiNikuiTextFrame errorFrame;
    public RikaishiNikuiTextFrame logFrame;

    public RikaishiNikuiPanel mainPanel;
    public RikaishiNikuiPanel mainInformationPanel;
    public RikaishiNikuiButtonPanel mainButtonPanel;

    public RikaishiNikuiPanel mainVersionPanel;
    public RikaishiNikuiScrollStringList versionList;
    public RikaishiNikuiTextPanel versionDetailsPanel;

    public ObjectRBTreeSet<String> options = new ObjectRBTreeSet<>();
    private boolean running = false;
    private boolean shutdown = false;

    public void init(String[] options) {
        try {
            errorFrame = new RikaishiNikuiTextFrame(1000, 800, "error in rikaishi nikui launcher");
            logFrame = new RikaishiNikuiTextFrame(1000, 800, "logs of rikaishi nikui launcher");

            logger.info("loading for {} {}", entrust, version);

            this.options.addAll(Arrays.asList(options));

            LanguageResource resource = new LanguageResource();
            resource.set(Language.CHINESE, "/assets/lang/zh_cn.json");
            resource.set(Language.ENGLISH, "/assets/lang/en_us.json");
            textFormat = new TextFormat(resource);

            initConfig();

            running = true;

            mainFrame = new RikaishiNikuiFrame(1000, 800, "main", true, "main.frame");

            mainPanel = new RikaishiNikuiPanel("main");
            mainButtonPanel = new RikaishiNikuiButtonPanel("main");
            mainInformationPanel = new RikaishiNikuiPanel("main");

            mainVersionPanel = new RikaishiNikuiPanel();
            versionList = new RikaishiNikuiScrollStringList(new RikaishiNikuiStringList("main"));
            versionDetailsPanel = new RikaishiNikuiTextPanel("main");

            try {
                loadUiAsConfig();

                rending();
            } catch (Exception ex) {
                ex.printStackTrace();

                defaultMainInformationPanel();
                defaultMainPanelButtons();
                defaultUiConfig();

                loadUiAsConfig();

                rending();
            }

            Collection<Object> strings = new ObjectRBTreeSet<>();
            for(int i = 100; i > 0; i--) {
                strings.add("1.18.2-" + new Random().nextInt(100));
            }
            versionList.setListData(strings);

            mainFrame.getContentPane().setBackground(RikaishiNikuiColor.BLACK);
            mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            mainFrame.setResizable(false);
            frames.put(mainFrame.getName(), mainFrame);

            initCloseEvent();

            mainFrame.setVisible(true);

            new Thread(() -> {
                while(running) {
                    long tickStart = System.currentTimeMillis();

                    synchronized(this) {
                        try {
                            tick();
                        } catch (Exception e) {
                            parseError(e, "error in tick thread, stop tick task");
                            break;
                        }
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

            if(this.options.contains("-DlogFrameVisible"))
                logFrame.setVisible(true);
        } catch (Exception e) {
            parseError(e, "error in rikaishi nikui launcher initializing");
        }
    }

    public void initConfig() {
        configUi = new ObjectConfigUtil(entrust + "UI", System.getProperty("user.dir") + "/rikaishi_nikui", "rikaishi_nikui_ui.mhf").setNote(textFormat.getText("config.note.ui")).setEncryption(false).setEncryptionHead(false).setEncryptionType(EncryptionType.COMPOSITE_SEQUENCE).setLibraryOffset(50);
        config = new ObjectConfigUtil(entrust, System.getProperty("user.dir") + "/rikaishi_nikui", "rikaishi_nikui.mhf").setNote(textFormat.getText("config.note"));
    }

    public void tick() throws Exception {
        configUi.readConfig(false);
        rending();
    }

    public void parseError(Throwable throwable, String source) {
        parseError(throwable, source, true);
    }

    public void parseError(Throwable throwable, String source, boolean shutdownCU) {
        running = false;

        logger.error(source, throwable);

        mainFrame.setVisible(false);
        errorFrame.setVisible(true);
        if(shutdownCU) {
            logger.error("invaliding (UI)CU, protection the config file");
            configUi.invalid();
        }
        logger.error("showing error");
        appendErrorFrameText(textFormat.format("happened.error").setColor(new Color(0, 0, 0)), true);
        errorFrame.appendText(throwable.getMessage() + "\n", new Color(152, 12, 10));
        for(StackTraceElement s : throwable.getStackTrace())
            appendErrorFrameText(textFormat.format("happened.error.at", s.toString() + "\n"), false);
        appendErrorFrameText(textFormat.format("happened.error.tip"), false);
    }

    public void rending() {
        applyUi();
    }

    public void defaultUiConfig() {
        RikaishiNikuiPanel panel = new RikaishiNikuiPanel();
        panel.setHeight(800);
        panel.setWidth(1000);
        panel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("panel-main", panel.toJSONObject());
        RikaishiNikuiFrame frame = new RikaishiNikuiFrame();
        frame.setHeight(800);
        frame.setWidth(1000);
        frame.setText("main.frame");
        frame.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("frame-main", frame.toJSONObject());
    }

    public void applyUi() {
        mainButtonPanel.addButtons(configUi.getConfigJSONObject("button-panel-main").getJSONObject("buttons"));

        mainPanel.apply(configUi.getConfigJSONObject("panel-main"));
        mainFrame.apply(configUi.getConfigJSONObject("frame-main"));
        mainButtonPanel.apply(configUi.getConfigJSONObject("button-panel-main"));
        mainInformationPanel.apply(configUi.getConfigJSONObject("information-panel-main"));
        mainVersionPanel.apply(configUi.getConfigJSONObject("information-panel-main-version"));
        versionList.apply(configUi.getConfigJSONObject("list-panel-versions"));
        versionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-main-version-details"));
    }

    public void loadUiAsConfig() {
        mainFrame.setLayout(null);
        mainPanel.setLayout(null);
        mainButtonPanel.setLayout(null);
        mainInformationPanel.setLayout(null);
        mainVersionPanel.setLayout(null);

        mainFrame.add(mainPanel);
        mainPanel.add(mainInformationPanel);
        mainPanel.add(mainButtonPanel);
        mainPanel.add(versionDetailsPanel);

        mainVersionPanel.add(versionList);
        mainInformationPanel.add(mainVersionPanel);

        functionButtons();
    }

    public void defaultMainInformationPanel() {
        mainInformationPanel = new RikaishiNikuiButtonPanel(1000, 730, "main");
        mainInformationPanel.setXY(0, 0);
        mainInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main", mainInformationPanel.toJSONObject());
        versionList = new RikaishiNikuiScrollStringList(new RikaishiNikuiStringList(350, 720, "main"));
        versionList.disableBorder();
        versionList.setXY(0, 0);
        versionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        versionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions", versionList.toJSONObject());
        mainVersionPanel = new RikaishiNikuiPanel(300, 700, "main");
        mainVersionPanel.setXY(0, 0);
        mainVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main-version", mainVersionPanel.toJSONObject());
        versionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "main");
        versionDetailsPanel.setXY(400, 0);
        versionDetailsPanel.setText("az");
        configUi.set("information-panel-main-version-details", versionDetailsPanel.toJSONObject());
    }

    public void functionButtons() {
        mainButtonPanel.addButtons(new RikaishiNikuiButton(100, 40, "main", "main.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), true).setId(0));
        mainButtonPanel.addButtons(new RikaishiNikuiButton(100, 40, "function#1", "某功能#1", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1));
        mainButtonPanel.addButtons(new RikaishiNikuiButton(100, 40, "function#2", "某功能#2", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2));
        mainButtonPanel.addButtons(new RikaishiNikuiButton(100, 40, "function#3", "某功能#3", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(3));
        mainButtonPanel.addButtons(new RikaishiNikuiButton(100, 40, "function#4", "某功能#4", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(4));
    }

    public void defaultMainPanelButtons() {
        mainButtonPanel = new RikaishiNikuiButtonPanel(1000, 40, "main");
        mainButtonPanel.setXY(0, 730);
        mainButtonPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        functionButtons();
        configUi.set("button-panel-main", mainButtonPanel.toJSONObject());
    }

    public void appendErrorFrameText(Text text, boolean clear) {
        errorFrame.appendText(text, clear);
    }

    public void appendLogFrameText(Text text, boolean clear) {
        logFrame.appendText(text, clear);
    }

    public void initCloseEvent() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                launcher.shutdown();
            }
        });
        errorFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                launcher.shutdown();
            }
        });
    }

    public void shutdown() {
        logger.info("rikaishi nikui launcher shutting down");

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

        try {
            configUi.shutdown();
        } catch (Exception e) {
            logger.error("(UI)CU already shutdown, failed to save config");
        }

        try {
            config.shutdown();
        } catch (Exception e) {
            logger.error("CU already shutdown, failed to save config");
        }

        shutdown = true;

        logger.info("rikaishi nikui launcher are shutdown");

        System.exit(0);
    }
}
