package com.github.zhuaidadaya.rikaishinikui;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.MinecraftVersionsParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiMinecraftDownloadTask;
import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.language.TextFormat;
import com.github.zhuaidadaya.rikaishinikui.language.Language;
import com.github.zhuaidadaya.rikaishinikui.language.LanguageResource;
import com.github.zhuaidadaya.rikaishinikui.ui.button.RikaishiNikuiButton;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiTextFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.list.RikaishiNikuiMinecraftList;
import com.github.zhuaidadaya.rikaishinikui.ui.list.RikaishiNikuiScrollMinecraftList;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.*;
import com.github.zhuaidadaya.utils.config.EncryptionType;
import com.github.zhuaidadaya.utils.config.ObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiLauncher {
    private final int tickInterval = 100;

    public RikaishiNikuiFrame mainFrame;
    public RikaishiNikuiTextFrame errorFrame;
    public RikaishiNikuiTextFrame logFrame;

    public RikaishiNikuiButtonPanel buttonPanel;

    public RikaishiNikuiPanel mainPanel;

    public RikaishiNikuiPanel mainInformationPanel;
    public RikaishiNikuiPanel mainVersionPanel;
    public RikaishiNikuiScrollMinecraftList mainVersionList;
    public RikaishiNikuiTextPanel mainVersionDetailsPanel;
    public RikaishiNikuiTipPanel searchLocalTip;
    public RikaishiNikuiEditingTextPanel searchLocalVersion;

    public RikaishiNikuiPanel downloadInformationPanel;
    public RikaishiNikuiPanel downloadVersionPanel;
    public RikaishiNikuiScrollMinecraftList downloadVersionList;
    public RikaishiNikuiTextPanel downloadVersionDetailsPanel;
    public RikaishiNikuiTipPanel searchDownloadTip;
    public RikaishiNikuiEditingTextPanel searchDownloadVersion;

    public MinecraftVersionsParser downloadVersions;

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
            buttonPanel = new RikaishiNikuiButtonPanel("main");
            mainInformationPanel = new RikaishiNikuiPanel("main");

            mainVersionPanel = new RikaishiNikuiPanel("main");
            mainVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList("main"));
            mainVersionDetailsPanel = new RikaishiNikuiTextPanel("main");
            searchLocalVersion = new RikaishiNikuiEditingTextPanel();
            searchLocalTip = new RikaishiNikuiTipPanel();

            downloadInformationPanel = new RikaishiNikuiPanel("download");
            downloadVersionPanel = new RikaishiNikuiPanel("download");
            downloadVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList("download"));
            downloadVersionDetailsPanel = new RikaishiNikuiTextPanel("download");
            searchDownloadVersion = new RikaishiNikuiEditingTextPanel();
            searchDownloadTip = new RikaishiNikuiTipPanel();

            try {
                initUi();

                testRending();
            } catch (Exception ex) {
                defaultMainInformationPanel();
                defaultMainPanelButtons();

                defaultDownloadInformationPanel();

                defaultUiConfig();

                initUi();

                testRending();
            }

            try {
                try {
                    minecraftVersions = new MinecraftVersionsRecorder(config.getConfigJSONObject("versions"));
                } catch (Exception ex) {
                    minecraftVersions = new MinecraftVersionsRecorder();
                    config.set("versions", minecraftVersions.toJSONObject());
                }

                if(minecraftVersions.getVersionNames().size() < 1) {
                    //                    versionList.setListData(minecraftVersions.getBadVersion());
                } else {
                    mainVersionList.setListData(minecraftVersions.getVersions());
                }
            } catch (Exception e) {

            }

            mainFrame.getContentPane().setBackground(RikaishiNikuiColor.BLACK);
            mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            mainFrame.setResizable(false);
            frames.put(mainFrame.getName(), mainFrame);

            initCloseEvent();

            config.set("minecraft-downloader", minecraftDownloader.toJSONObject());

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

            //            RikaishiNikuiMinecraftDownloadTask downloadTask = new RikaishiNikuiMinecraftDownloadTask("1.17.1", "az1");
            //            downloadTask.join(taskManager);
            //            try {
            //                Thread.sleep(15000);
            //            } catch (InterruptedException e) {
            //
            //            }
            //            Collection<String> vmOption = new ObjectRBTreeSet<>();
            //            vmOption.add("-Xmx2G");
            //            vmOption.add("-Xms2G");
            //            MinecraftLauncher launcher = new MinecraftLauncher(new MinecraftLaunchInformation(minecraftVersions.getVersion("aedbc04c-2e19-40ec-9569-378218f44453"), "windows", "C:\\Users\\lx\\.jdks\\adopt-openjdk-17\\bin\\java.exe", new Account("zhuaidadaya", UUID.randomUUID().toString())));
            //            launcher.setVmOptions(vmOption);
            //            launcher.launch();

            downloadVersions = minecraftDownloader.getVersions();
        } catch (Exception e) {
            parseError(e, "error in rikaishi nikui launcher initializing");
        }

        //        minecraftDownloader.download("1.17.1", "test-1.17.1");
    }

    public void initConfig() {
        configUi = new ObjectConfigUtil(entrust + "UI", System.getProperty("user.dir") + "/rikaishi_nikui", "rikaishi_nikui_ui.mhf").setNote(textFormat.getText("config.note.ui")).setEncryption(false).setEncryptionHead(false).setEncryptionType(EncryptionType.COMPOSITE_SEQUENCE).setLibraryOffset(50);
        config = new ObjectConfigUtil(entrust, System.getProperty("user.dir") + "/rikaishi_nikui", "rikaishi_nikui.mhf").setNote(textFormat.getText("config.note"));
    }

    public void tick() throws Exception {
        configUi.readConfig(false);
        config.readConfig(false);
        rending();
        mainFrame.setVisible(true);
    }

    public void parseError(Throwable throwable, String source) {
        parseError(throwable, source, true, configUi);
    }

    public void parseError(Throwable throwable, String source, boolean shutdownCU, ObjectConfigUtil configUtil) {
        running = false;

        logger.error(source, throwable);

        mainFrame.setVisible(false);
        errorFrame.setVisible(true);
        if(shutdownCU) {
            logger.error("invaliding (UI)CU, protection the config file");
            configUtil.invalid();
        }
        logger.error("showing error");
        appendErrorFrameText(textFormat.format("happened.error").setColor(new Color(0, 0, 0)), true);
        errorFrame.appendText(throwable.getMessage() + "\n", new Color(152, 12, 10));
        for(StackTraceElement s : throwable.getStackTrace())
            appendErrorFrameText(textFormat.format("happened.error.at", s.toString() + "\n"), false);
        appendErrorFrameText(textFormat.format("happened.error.tip"), false);
        errorFrame.updateUI();
    }

    public void rending() {
        applyUi();
    }

    public void setLocalInformationList() {
        try {
            try {
                minecraftVersions.apply(config.getConfigJSONObject("versions"));
            } catch (Exception ex) {
                minecraftVersions = new MinecraftVersionsRecorder();
                config.set("versions", minecraftVersions.toJSONObject());
            }

            Collection<MinecraftVersionInformation> names = minecraftVersions.getVersions(searchLocalVersion.getText());

            if(names.size() < 1) {
                mainVersionList.setListData(new MinecraftVersionInformation[0]);
            } else {
                mainVersionList.setListData(names);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDownloadInformationList() {
        try {
            Collection<MinecraftVersionInformation> names = downloadVersions.getVersionsInformation(searchDownloadVersion.getText());

            if(names.size() < 1) {
                downloadVersionList.setListData(new MinecraftVersionInformation[0]);
            } else {
                downloadVersionList.setListData(names);
            }
        } catch (Exception e) {

        }
    }

    public void setLocalInformationText() {
        try {
            MinecraftVersionInformation information = mainVersionList.getSelectedValue();
            JSONObject inf = information.toJSONObject();
            mainVersionDetailsPanel.setText("");
            for(String s : inf.keySet()) {
                mainVersionDetailsPanel.appendText(textFormat.format("minecraft.information." + s));
                mainVersionDetailsPanel.appendText(": ");
                mainVersionDetailsPanel.appendText(textFormat.format(inf.getString(s)), false);
                mainVersionDetailsPanel.appendText("\n");
            }
            mainVersionDetailsPanel.updateText();
        } catch (Exception ex) {

        }
    }

    public void setDownloadInformationText() {
        try {
            MinecraftVersionInformation information = downloadVersionList.getSelectedValue();
            JSONObject inf = information.toJSONObject();
            downloadVersionDetailsPanel.setText("");
            for(String s : inf.keySet()) {
                downloadVersionDetailsPanel.appendText(textFormat.format("minecraft.information." + s));
                downloadVersionDetailsPanel.appendText(": ");
                downloadVersionDetailsPanel.appendText(textFormat.format(inf.getString(s)), false);
                downloadVersionDetailsPanel.appendText("\n");
            }
            downloadVersionDetailsPanel.updateText();
        } catch (Exception ex) {

        }
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
        mainFrame.apply(configUi.getConfigJSONObject("frame-main"));

        switch(buttonPanel.getActiveButton().getId()) {
            case 0 -> {
                setMainVisible();

                setLocalInformationList();
                setLocalInformationText();

                mainPanel.apply(configUi.getConfigJSONObject("panel-main"));

                mainInformationPanel.apply(configUi.getConfigJSONObject("information-panel-main"));
                mainVersionPanel.apply(configUi.getConfigJSONObject("information-panel-main-version"));
                mainVersionList.apply(configUi.getConfigJSONObject("list-panel-versions"));
                mainVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-main-version-details"));

                searchLocalVersion.apply(configUi.getConfigJSONObject("search-local"));
                searchLocalTip.apply(configUi.getConfigJSONObject("search-local-tip"));
            }
            case 1 -> {
                setDownloadInformationList();
                setDownloadVisible();

                setDownloadInformationText();

                downloadInformationPanel.apply(configUi.getConfigJSONObject("information-panel-download"));
                downloadVersionPanel.apply(configUi.getConfigJSONObject("information-panel-download-version"));
                downloadVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-download"));
                downloadVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-download-version-details"));

                searchDownloadVersion.apply(configUi.getConfigJSONObject("search-download"));
                searchDownloadTip.apply(configUi.getConfigJSONObject("search-download-tip"));
            }
        }

        buttonPanel.applyButtons(configUi.getConfigJSONObject("button-panel-main").getJSONObject("buttons"));
        buttonPanel.apply(configUi.getConfigJSONObject("button-panel-main"));
    }

    public void setMainVisible(boolean visible) {
        mainInformationPanel.setVisible(visible);
        mainVersionPanel.setVisible(visible);
        mainVersionList.setVisible(visible);
        mainVersionDetailsPanel.setVisible(visible);
        searchLocalTip.setVisible(visible);
        searchLocalVersion.setVisible(visible);
    }

    public void setMainVisible() {
        setMainVisible(true);
        setDownloadVisible(false);
    }

    public void setDownloadVisible(boolean visible) {
        downloadInformationPanel.setVisible(visible);
        downloadVersionPanel.setVisible(visible);
        downloadVersionList.setVisible(visible);
        downloadVersionDetailsPanel.setVisible(visible);
        searchDownloadVersion.setVisible(visible);
        searchDownloadTip.setVisible(visible);
    }

    public void setDownloadVisible() {
        setDownloadVisible(true);
        setMainVisible(false);
    }

    public void testRending() {
        mainPanel.apply(configUi.getConfigJSONObject("panel-main"));

        mainInformationPanel.apply(configUi.getConfigJSONObject("information-panel-main"));
        mainVersionPanel.apply(configUi.getConfigJSONObject("information-panel-main-version"));
        mainVersionList.apply(configUi.getConfigJSONObject("list-panel-versions"));
        mainVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-main-version-details"));

        downloadInformationPanel.apply(configUi.getConfigJSONObject("information-panel-download"));
        downloadVersionPanel.apply(configUi.getConfigJSONObject("information-panel-download-version"));
        downloadVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-download"));
        downloadVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-download-version-details"));

        searchLocalVersion.apply(configUi.getConfigJSONObject("search-local"));
        searchLocalTip.apply(configUi.getConfigJSONObject("search-local-tip"));

        searchDownloadVersion.apply(configUi.getConfigJSONObject("search-download"));
        searchDownloadTip.apply(configUi.getConfigJSONObject("search-download-tip"));
    }

    public void initUi() {
        mainFrame.setLayout(null);
        mainPanel.setLayout(null);
        buttonPanel.setLayout(null);
        mainInformationPanel.setLayout(null);
        mainVersionPanel.setLayout(null);
        downloadInformationPanel.setLayout(null);
        downloadVersionPanel.setLayout(null);
        downloadVersionDetailsPanel.setLayout(null);

        mainFrame.add(mainPanel);
        mainPanel.add(mainInformationPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(mainVersionDetailsPanel);

        mainPanel.add(downloadInformationPanel);

        downloadVersionPanel.add(downloadVersionList);
        downloadInformationPanel.add(downloadVersionPanel);
        mainPanel.add(downloadVersionDetailsPanel);

        mainVersionPanel.add(mainVersionList);
        mainInformationPanel.add(mainVersionPanel);

        mainInformationPanel.add(searchLocalVersion);
        mainInformationPanel.add(searchLocalTip);

        downloadInformationPanel.add(searchDownloadVersion);
        downloadInformationPanel.add(searchDownloadTip);

        functionButtons();
    }

    public void defaultMainInformationPanel() {
        mainInformationPanel = new RikaishiNikuiPanel(1000, 730, "main");
        mainInformationPanel.setXY(0, 0);
        mainInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main", mainInformationPanel.toJSONObject());
        mainVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList(1000, 1000, "main"));
        mainVersionList.disableBorder();
        mainVersionList.setXY(0, 0);
        mainVersionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        mainVersionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions", mainVersionList.toJSONObject());
        mainVersionPanel = new RikaishiNikuiPanel(350, 700, "main");
        mainVersionPanel.setXY(0, 0);
        mainVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main-version", mainVersionPanel.toJSONObject());
        mainVersionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "main");
        mainVersionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        mainVersionDetailsPanel.setXY(350, 0);
        configUi.set("information-panel-main-version-details", mainVersionDetailsPanel.toJSONObject());
        searchLocalVersion = new RikaishiNikuiEditingTextPanel();
        searchLocalVersion.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        searchLocalVersion.setXY(100, 700);
        searchLocalVersion.setSize(250, 25);
        configUi.set("search-local", searchLocalVersion.toJSONObject());
        searchLocalTip = new RikaishiNikuiTipPanel();
        searchLocalTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        searchLocalTip.setXY(0, 700);
        searchLocalTip.setSize(100, 25);
        searchLocalTip.setText("tip.search");
        configUi.set("search-local-tip", searchLocalTip.toJSONObject());
    }

    public void defaultDownloadInformationPanel() {
        downloadInformationPanel = new RikaishiNikuiPanel(1000, 730, "main");
        downloadInformationPanel.setXY(0, 0);
        downloadInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-download", downloadInformationPanel.toJSONObject());
        downloadVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList(1000, 1000, "main"));
        downloadVersionList.disableBorder();
        downloadVersionList.setXY(0, 0);
        downloadVersionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        downloadVersionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions-download", downloadVersionList.toJSONObject());
        downloadVersionPanel = new RikaishiNikuiPanel(350, 700, "main");
        downloadVersionPanel.setXY(0, 0);
        downloadVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-download-version", downloadVersionPanel.toJSONObject());
        downloadVersionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "main");
        downloadVersionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        downloadVersionDetailsPanel.setXY(350, 0);
        configUi.set("information-panel-download-version-details", downloadVersionDetailsPanel.toJSONObject());
        searchDownloadVersion = new RikaishiNikuiEditingTextPanel();
        searchDownloadVersion.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        searchDownloadVersion.setXY(100, 700);
        searchDownloadVersion.setSize(250, 25);
        configUi.set("search-download", searchDownloadVersion.toJSONObject());
        searchDownloadTip = new RikaishiNikuiTipPanel();
        searchDownloadTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        searchDownloadTip.setXY(0, 700);
        searchDownloadTip.setSize(100, 25);
        searchDownloadTip.setText("tip.search");
        configUi.set("search-download-tip", searchDownloadTip.toJSONObject());
    }

    public void defaultMainPanelButtons() {
        buttonPanel = new RikaishiNikuiButtonPanel(1000, 40, "main");
        buttonPanel.setXY(0, 730);
        buttonPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        functionButtons();
        configUi.set("button-panel-main", buttonPanel.toJSONObject());
    }

    public void functionButtons() {
        RikaishiNikuiButton mainButton = new RikaishiNikuiButton(100, 40, "main", "main.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), true).setId(0);
        mainButton.addActionListener(e -> {
            buttonPanel.cancelButtonsActive();
            buttonPanel.getButton(0).setActive(true);
        });
        buttonPanel.applyButtons(mainButton);
        RikaishiNikuiButton downloadButtons = new RikaishiNikuiButton(100, 40, "download", "download.vanilla.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        downloadButtons.addActionListener(e -> {
            buttonPanel.cancelButtonsActive();
            buttonPanel.getButton(1).setActive(true);
        });
        buttonPanel.applyButtons(downloadButtons);
        buttonPanel.applyButtons(new RikaishiNikuiButton(100, 40, "function#2", "某功能#2", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2));
        buttonPanel.applyButtons(new RikaishiNikuiButton(100, 40, "function#3", "某功能#3", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(3));
        buttonPanel.applyButtons(new RikaishiNikuiButton(100, 40, "function#4", "某功能#4", false).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(4));
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
