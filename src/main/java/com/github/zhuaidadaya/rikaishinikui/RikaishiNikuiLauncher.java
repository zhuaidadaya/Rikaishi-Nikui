package com.github.zhuaidadaya.rikaishinikui;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.file.IllegalFileName;
import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.java.version.JavaVersionChecker;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher.MinecraftLauncher;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.MinecraftVersionsParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftLaunchInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionsRecorder;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiDownloadRefreshTask;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiMinecraftDownloadTask;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiMinecraftTask;
import com.github.zhuaidadaya.rikaishinikui.language.*;
import com.github.zhuaidadaya.rikaishinikui.ui.button.RikaishiNikuiButton;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.frame.RikaishiNikuiLogFrame;
import com.github.zhuaidadaya.rikaishinikui.ui.list.*;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.*;
import com.github.zhuaidadaya.utils.config.DiskObjectConfigUtil;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class RikaishiNikuiLauncher {
    private final int standardInterval = 50;
    public RikaishiNikuiFrame mainFrame;
    public RikaishiNikuiLogFrame errorFrame;
    public RikaishiNikuiLogFrame logFrame;
    public RikaishiNikuiHorizontalButtonPanel buttons;
    public RikaishiNikuiPanel mainPanel;
    public RikaishiNikuiPanel mainInformationPanel;
    public RikaishiNikuiPanel mainVersionPanel;
    public RikaishiNikuiScrollMinecraftList mainVersionList;
    public RikaishiNikuiTextPanel mainVersionDetailsPanel;
    public RikaishiNikuiTipPanel searchLocalTip;
    public RikaishiNikuiEditingTextPanel searchLocalVersion;
    public RikaishiNikuiHorizontalButtonPanel mainOperationButtons;
    public RikaishiNikuiPanel downloadInformationPanel;
    public RikaishiNikuiPanel downloadVersionPanel;
    public RikaishiNikuiScrollMinecraftList downloadVersionList;
    public RikaishiNikuiTextPanel downloadVersionDetailsPanel;
    public RikaishiNikuiTipPanel searchDownloadTip;
    public RikaishiNikuiEditingTextPanel searchDownloadVersion;
    public RikaishiNikuiTipPanel downloadSaveAsTip;
    public RikaishiNikuiEditingTextPanel downloadSaveAs;
    public RikaishiNikuiHorizontalButtonPanel downloadOperationButtons;
    public RikaishiNikuiPanel javaInformationPanel;
    public RikaishiNikuiPanel javaVersionPanel;
    public RikaishiNikuiScrollJavaList javaVersionList;
    public RikaishiNikuiTextPanel javaVersionDetailsPanel;
    public RikaishiNikuiTipPanel searchJavaTip;
    public RikaishiNikuiEditingTextPanel searchJavaVersion;
    public RikaishiNikuiTipPanel javaPathTip;
    public RikaishiNikuiEditingTextPanel javaPathEditing;
    public RikaishiNikuiHorizontalButtonPanel javaOperationButtons;
    public RikaishiNikuiPanel vmOptionsInformationPanel;
    public RikaishiNikuiPanel vmOptionsPanel;
    public RikaishiNikuiScrollVmOptionList vmOptionsList;
    public RikaishiNikuiTextPanel vmOptionDetailsPanel;
    public RikaishiNikuiScrollMinecraftList versionVmOptionList;
    public RikaishiNikuiPanel versionVmOptionListPanel;
    public RikaishiNikuiTipPanel searchVmOptionsTip;
    public RikaishiNikuiEditingTextPanel searchVmOptions;
    public RikaishiNikuiTipPanel searchLocalMinecraftVmTip;
    public RikaishiNikuiEditingTextPanel searchLocalMinecraftVmVersion;
    public RikaishiNikuiTipPanel editVmOptionsTip;
    public RikaishiNikuiEditingTextPanel editVmOptionsKey;
    public RikaishiNikuiEditingTextPanel editVmOptionsValue;
    public RikaishiNikuiTipPanel addVmOptionsTip;
    public RikaishiNikuiEditingTextPanel addVmOptions;
    public RikaishiNikuiHorizontalButtonPanel vmOptionsOperationButtons;
    public RikaishiNikuiHorizontalButtonPanel vmOptionsOperationButtons2;
    public MinecraftVersionsParser downloadVersions;
    public UUID REFRESH_TASK_ID = UUID.randomUUID();
    public UUID LAUNCH_DOWNLOAD_TASK_ID = UUID.randomUUID();
    public ObjectRBTreeSet<String> options = new ObjectRBTreeSet<>();
    private long lastTick = -1;
    private long lastTickMainFrame = -1;
    private boolean running = false;
    private boolean shutdown = false;

    public void init(String[] options) {
        try {
            errorFrame = new RikaishiNikuiLogFrame(1000, 800, "error in rikaishi nikui launcher");
            logFrame = new RikaishiNikuiLogFrame(1000, 800, "logs of rikaishi nikui launcher");

            logFrame.setColor();
            errorFrame.setColor();

            logger.info(String.format("loading for %s %s", entrust, version));

            this.options.addAll(Arrays.asList(options));

            LanguageResource resource = new LanguageResource();
            resource.set(Language.CHINESE, "/assets/lang/zh_cn.json");
            resource.set(Language.ENGLISH, "/assets/lang/en_us.json");
            textFormatter = new TextFormatter(resource);

            initConfig();

            setJavaInformationList();

            try {
                usedJava = new JavaVersionInformation(config.getConfigJSONObject("used_java"));
            } catch (Exception ex) {
                JavaVersionInformation javaInformation = new JavaVersionChecker().check("java");
                if (javaInformation.isAvailable()) javaVersions.add(javaInformation);
                usedJava = javaInformation;
                config.set("used_java", usedJava.toJSONObject());
            }

            running = true;

            mainFrame = new RikaishiNikuiFrame(1000, 800, "main", true, "main.frame");
            mainFrame.setAlwaysOnTop(false);

            mainPanel = new RikaishiNikuiPanel("main");
            buttons = new RikaishiNikuiHorizontalButtonPanel("main");
            mainInformationPanel = new RikaishiNikuiPanel("main");

            mainVersionPanel = new RikaishiNikuiPanel("main");
            mainVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList("main"));
            mainVersionDetailsPanel = new RikaishiNikuiTextPanel("main");
            searchLocalVersion = new RikaishiNikuiEditingTextPanel("main");
            searchLocalTip = new RikaishiNikuiTipPanel();
            mainOperationButtons = new RikaishiNikuiHorizontalButtonPanel("main");

            downloadInformationPanel = new RikaishiNikuiPanel("download");
            downloadVersionPanel = new RikaishiNikuiPanel("download");
            downloadVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList("download"));
            downloadVersionDetailsPanel = new RikaishiNikuiTextPanel("download");
            searchDownloadVersion = new RikaishiNikuiEditingTextPanel("download");
            searchDownloadTip = new RikaishiNikuiTipPanel("download");
            downloadOperationButtons = new RikaishiNikuiHorizontalButtonPanel("download");
            downloadSaveAs = new RikaishiNikuiEditingTextPanel("download");
            downloadSaveAsTip = new RikaishiNikuiTipPanel();

            javaInformationPanel = new RikaishiNikuiPanel("java");
            javaVersionDetailsPanel = new RikaishiNikuiTextPanel("java");
            javaVersionList = new RikaishiNikuiScrollJavaList(new RikaishiNikuiJavaList("java"));
            javaVersionPanel = new RikaishiNikuiHorizontalButtonPanel("java");
            searchJavaVersion = new RikaishiNikuiEditingTextPanel("java");
            searchJavaTip = new RikaishiNikuiTipPanel();
            javaOperationButtons = new RikaishiNikuiHorizontalButtonPanel("java");
            javaPathEditing = new RikaishiNikuiEditingTextPanel("java");
            javaPathTip = new RikaishiNikuiTipPanel();

            vmOptionDetailsPanel = new RikaishiNikuiTextPanel("vm");
            vmOptionsInformationPanel = new RikaishiNikuiPanel("vm");
            vmOptionsList = new RikaishiNikuiScrollVmOptionList(new RikaishiNikuiVmOptionList("vm"));
            versionVmOptionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList("vm"));
            versionVmOptionListPanel = new RikaishiNikuiPanel("vm");
            editVmOptionsKey = new RikaishiNikuiEditingTextPanel("vm");
            editVmOptionsValue = new RikaishiNikuiEditingTextPanel("vm");
            editVmOptionsTip = new RikaishiNikuiTipPanel("vm");
            vmOptionsPanel = new RikaishiNikuiPanel("vm");
            searchVmOptions = new RikaishiNikuiEditingTextPanel("vm");
            searchVmOptionsTip = new RikaishiNikuiTipPanel("vm");
            searchLocalMinecraftVmVersion = new RikaishiNikuiEditingTextPanel("vm");
            searchLocalMinecraftVmTip = new RikaishiNikuiTipPanel("vm");
            vmOptionsOperationButtons = new RikaishiNikuiHorizontalButtonPanel("vm");
            vmOptionsOperationButtons2 = new RikaishiNikuiHorizontalButtonPanel("vm");
            addVmOptions = new RikaishiNikuiEditingTextPanel("vm");
            addVmOptionsTip = new RikaishiNikuiTipPanel("vm");

            try {
                functionButtons();

                initUi();

                testRending();
            } catch (Exception ex) {
                defaultButtonsUi();

                defaultDownloadInformationPanel();
                defaultMainInformationPanel();
                defaultJavaInformationPanel();
                defaultVmInformationPanel();

                defaultUiConfig();

                defaultConfig();

                initUi();

                testRending();
            }

            recoveryVersionsStatus();

            mainFrame.getContentPane().setBackground(RikaishiNikuiColor.BLACK);
            mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            mainFrame.setResizable(false);
            frames.put(mainFrame.getName(), mainFrame);

            initCloseEvent();

            config.set("minecraft-downloader", minecraftDownloader.toJSONObject());

            String cache = "rikaishi_nikui/logs/cached/";
            try {
                FileUtil.deleteFiles(cache);
            } catch (Exception e) {

            }

            new Thread(() -> {
                Thread.currentThread().setName("UI tick thread");

                while (running) {
                    long tickStart = System.currentTimeMillis();

                    try {
                        tickUI();
                    } catch (Exception e) {
                        parseError(e, "error in tick thread, stop tick task");
                        break;
                    }

                    lastTick = System.currentTimeMillis();
                    long tickTime = lastTick - tickStart;
                    if (tickTime < standardInterval) {
                        try {
                            Thread.sleep(standardInterval - tickTime);
                        } catch (InterruptedException e) {

                        }
                    }
                }
                shutdown = true;
            }).start();

            mainFrame.setVisible(true);

            logFrame.setVisible(true);

            taskManager.submitter(logFrame, rikaishiNikuiLauncherTaskId);

            try {
                downloadVersions = minecraftDownloader.getVersions();
            } catch (Exception e) {

            }

            JavaVersionInformation javaInformation = new JavaVersionChecker().check("java");
            if (javaInformation.isAvailable()) javaVersions.add(javaInformation);
        } catch (Exception e) {
            parseError(e, "error in rikaishi nikui launcher initializing");
        }
    }

    public void recoveryVersionsStatus() {
        setLocalInformationList();
        detectAreaVersions();

        for (MinecraftVersionInformation information : minecraftVersions.getVersions()) {
            if (!information.getLockStatus().equals("lock.not")) {
                information.setLockStatus("lock.not");
            }
            if (!information.getStatus().equals("status.undefined") & !information.getStatus().equals("status.interrupted") & !information.getStatus().equals("status.interrupting")) {
                if (!information.getStatus().equals("status.ready")) {
                    information.setStatus("status.ready");
                }
            }
            information.setTaskFeedback("none");
            minecraftVersions.add(information);
        }
    }

    public void initConfig() {
//        configUi = new DiskObjectConfigUtil(entrust + "UI", System.getProperty("user.dir") + "/rikaishi_nikui/config/launcher/ui/", "rikaishi_nikui_ui.mhf").setNote(textFormat.getText("config.note.ui")).setEncryption(true).setEncryptionHead(false).setEncryptionType(EncryptionType.COMPOSITE_SEQUENCE).setLibraryOffset(50);
//        config = new DiskObjectConfigUtil(entrust, System.getProperty("user.dir") + "/rikaishi_nikui/config/launcher/", "rikaishi_nikui.mhf").setNote(textFormat.getText("config.note")).setEncryption(true);
        configUi = new DiskObjectConfigUtil(entrust + "UI", System.getProperty("user.dir") + "/rikaishi_nikui/config/launcher/ui/");
        config = new DiskObjectConfigUtil(entrust, System.getProperty("user.dir") + "/rikaishi_nikui/config/launcher/");
    }

    public void tickUI() {
        if (mainFrame.isFocused() || System.currentTimeMillis() - lastTickMainFrame > 500) {
            rendingMainFrame();
            lastTickMainFrame = System.currentTimeMillis();
        }
    }

    public void parseError(Throwable throwable, String source) {
        parseError(throwable, source, true, configUi);
    }

    public void parseError(Throwable throwable, String source, boolean shutdownCU, DiskObjectConfigUtil configUtil) {
        logger.error(source, throwable);

        mainFrame.setVisible(false);
        errorFrame.setVisible(true);
        logger.error("showing error");
        errorFrame.appendText(textFormatter.formatSingleText("happened.error"), true);
        errorFrame.appendText(new SingleText("\n\n"), false);
        errorFrame.appendText(new SingleText("---------- STACK TRACE ----------\n"), false);
        errorFrame.appendText(new SingleText(source + ": \n", new RikaishiNikuiColor(198, 55, 65)), false);
        errorFrame.appendText(textFormatter.formatTrace(throwable), false);
        errorFrame.appendText(new SingleText("\n---------- THREADS STACK TRACE ----------\n"), false);
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getStackTrace().length > 0) {
                errorFrame.appendText(textFormatter.format(thread.getName() + "\n"), false);
                for (StackTraceElement s : thread.getStackTrace()) {
                    errorFrame.appendText(textFormatter.format("happened.error.at", s.toString() + "\n"), false);
                }
            }
        }
        errorFrame.updateText();

        if (shutdownCU) {
            logger.error("invaliding (UI)CU, protection the config file");
            configUtil.shutdown();
        }

        running = false;
    }

    public void rendingMainFrame() {
        applyUi();
    }

    public void detectAreaVersions() {
        try {
            String area = config.getConfigString("area");
            for (File f : new File(area + "/versions/").listFiles()) {
                if (minecraftVersions.getVersionAsId(f.getName()) == null) {
                    if (minecraftVersions.getVersionAsName(f.getName()) == null) {
                        String name = f.getName();
                        String id = name;
                        MinecraftVersionInformation information = new MinecraftVersionInformation(id, name, "status.undefined");
                        try {
                            UUID.fromString(id.substring(id.length() - 36));
                            information.setIdFormatted(true);
                        } catch (Exception ex) {
                            id = UUID.randomUUID().toString();
                            information.setIdFormatted(false);
                        }
                        if (information.isIdFormatted()) {
                            information.setId(id.substring(id.length() - 36));
                        } else {
                            information.setId(id);
                        }
                        information.setName(name);
                        information.setArea(area);
                        information.setType("minecraft.type.vanilla");
                        minecraftVersions.add(information);
                    }
                }
            }

            for (MinecraftVersionInformation information : minecraftVersions.getVersions()) {
                if (!new File(information.getPath()).exists()) {
                    minecraftVersions.remove(information);
                }

                information.setJavaSatisfy(usedJava.getVersion() >= information.getJavaRequires());
            }
        } catch (Exception e) {

        }
    }

    public void detectJavaVersions() {
        try {
            if (javaVersionList.getSelectedIndex() == -1) {
                JavaVersionInformation javaInformation = new JavaVersionChecker().check("java");
                if (javaInformation.isAvailable()) javaVersions.add(javaInformation);
            }
            if (javaVersions.getVersions().size() == 1) {
                javaVersions.getDefault().setUsed(true);
            }
            JavaVersionInformation information = javaVersionList.getSelectedValue();

            information.setUsed(usedJava.getName().equals(information.getName()));

            JavaVersionChecker checker = new JavaVersionChecker();
            JavaVersionInformation checkInformation = checker.check(information.getPath());
            information.setAvailable(!checkInformation.isUnknown());
        } catch (Exception e) {

        }
    }

    public void setLocalInformationList() {
        try {
            try {
                minecraftVersions.apply(config.getConfigJSONObject("minecraft-versions"));
            } catch (Exception ex) {
                minecraftVersions = new MinecraftVersionsRecorder();
                config.set("minecraft-versions", minecraftVersions.toJSONObject());
            }

            detectAreaVersions();

            Collection<MinecraftVersionInformation> names;
            if (buttons.getActiveButton().getId() == 0)
                names = minecraftVersions.getVersions(searchLocalVersion.getText());
            else names = minecraftVersions.getVersions(searchLocalMinecraftVmVersion.getText());

            if (names.size() < 1) {
                if (buttons.getActiveButton().getId() == 0) {
                    mainVersionList.setListData(new MinecraftVersionInformation[0]);
                } else {
                    versionVmOptionList.setListData(new MinecraftVersionInformation[0]);
                    vmOptionsOperationButtons2.setButtonVisible(0, false);
                }
            } else {
                if (buttons.getActiveButton().getId() == 0) {
                    mainVersionList.setListData(names);
                } else {
                    versionVmOptionList.setListData(names);
                }
            }
        } catch (Exception e) {

        }
    }

    public void setDownloadInformationList() {
        try {
            Collection<MinecraftVersionInformation> names = downloadVersions.getVersionsInformation(searchDownloadVersion.getText());

            if (names.size() < 1) {
                downloadVersionList.setListData(new MinecraftVersionInformation[0]);
            } else {
                downloadVersionList.setListData(names);
            }
        } catch (Exception e) {

        }
    }

    public void setJavaInformationList() {
        try {
            try {
                javaVersions.apply(config.getConfigJSONObject("java-versions"));
            } catch (Exception ex) {
                javaVersions = new JavaVersionsRecorder();
                config.set("java-versions", javaVersions.toJSONObject());
            }
            Collection<JavaVersionInformation> names = javaVersions.getVersions(searchJavaVersion.getText());

            if (names.size() < 1) {
                javaVersionList.setListData(new JavaVersionInformation[0]);
            } else {
                javaVersionList.setListData(names);
            }
        } catch (Exception e) {

        }
    }

    public void setVmOptionsList() {
        try {
            try {
                versionVmOptionList.apply(config.getConfigJSONObject("minecraft-versions"));
            } catch (Exception ex) {

            }

            try {
                LinkedHashMap<String, VmOption> names = versionVmOptionList.getSelectedValue().getVmOptions(searchVmOptions.getText());

                if (names.size() < 1) {
                    vmOptionsList.setListData(new VmOption[0]);
                } else {
                    vmOptionsList.setListData(names.values());
                }
            } catch (Exception e) {
                vmOptionsList.setListData(new VmOption[0]);
            }
        } catch (Exception e) {

        }
    }

    public void setLocalInformationText() {
        try {
            config.setIfNoExist("area", "rikaishi_nikui/minecraft/");
            String area = config.getConfigString("area");
            MinecraftVersionInformation information = mainVersionList.getSelectedValue();
            mainVersionDetailsPanel.setText("");
            if (information == null) {
//                mainVersionDetailsPanel.appendText(textFormat.format("tip.versions.not.found.in.area", area));
                mainVersionDetailsPanel.appendText(textFormatter.format("tip.versions.not.found", area));
//                mainVersionDetailsPanel.appendText(textFormat.format("tip.versions.not.found.import"));
                mainOperationButtons.setButtonVisible(4, true);
                throw new Exception();
            }
            if (new File(information.formatManifest()).isFile()) {
                if (information.getStatus().equals("status.undefined")) {
                    if (!information.getStatus().equals("status.destroyed")) {
                        information.setStatus("status.ready");
                    }

                    try {
                        JSONObject gameSource = new JSONObject(NetworkUtil.downloadToStringBuilder(information.formatManifest()).toString());
                        information.setVersion(gameSource.getString("id"));
                        information.setReleaseTime(gameSource.getString("releaseTime"));
                        information.setReleaseType(gameSource.getString("type"));
                        information.setUrl(information.formatManifest());
                    } catch (Exception ex) {

                    }
                }
            } else {
                if (!information.getStatus().equals("status.checking") & !information.getStatus().equals("status.parsing"))
                    information.setStatus("status.undefined");
            }

            if (information.getLockStatus().equals("lock.launching")) {
                try {
                    if (!taskManager.hasTask(UUID.fromString(information.getTaskId()))) {
                        information.setLockStatus("lock.not");
                        information.setStatus("status.ready");
                    }
                } catch (Exception e) {
                    information.setLockStatus("lock.not");
                    information.setStatus("status.ready");
                }
            }

            LinkedHashMap<String, String> inf = information.getInformation();
            inf.remove("options");
            MultipleText texts = new MultipleText();
            for (String s : inf.keySet()) {
                try {
                    new JSONObject(inf.get(s));
                } catch (Exception e) {
                    mainVersionDetailsPanel.appendText(new PairText(textFormatter.format("minecraft.information." + s), ((SingleText) textFormatter.format(inf.get(s))).append("\n"), new SingleText(": ")));
                }
            }
            mainVersionDetailsPanel.appendText(texts, true);

//            mainVersionDetailsPanel.updateText();

            if (information.getStatus().equals("status.interrupting")) throw new Exception();
            mainOperationButtons.setButtonVisible(0, information.getStatus().equals("status.ready") & information.getLockStatus().equals("lock.not"));
            mainOperationButtons.setButtonVisible(1, information.getLockStatus().equals("lock.launching") || information.getStatus().equals("status.downloading"));
            mainOperationButtons.setButtonVisible(2, true);
            mainOperationButtons.setButtonVisible(3, information.getStatus().equals("status.interrupted") || information.getStatus().equals("status.undefined"));
            mainOperationButtons.setButtonVisible(4, false);
        } catch (Exception ex) {
            mainOperationButtons.setButtonVisible(0, false);
            mainOperationButtons.setButtonVisible(1, false);
            mainOperationButtons.setButtonVisible(2, false);
            mainOperationButtons.setButtonVisible(3, false);
        }

        mainVersionDetailsPanel.updateText();
    }

    public void setDownloadInformationText() {
        try {
            String area = config.getConfigString("area");
            MinecraftVersionInformation information = downloadVersionList.getSelectedValue();
            downloadVersionDetailsPanel.setText("");
            if (information == null) {
                String search = searchDownloadVersion.getText();
                if (search.equals("")) {
                    downloadVersionDetailsPanel.appendText(textFormatter.format("error.version.not.found.in.manifest"));
                } else {
                    downloadVersionDetailsPanel.appendText(textFormatter.format("tip.versions.not.found.in.manifest", search));
                }
                downloadVersionDetailsPanel.updateText();
                throw new Exception();
            }
            LinkedHashMap<String, String> inf = information.getInformation();
            inf.remove("last-launch");
            inf.remove("formatted-by-id");
            inf.remove("task-id");
            inf.remove("last-task-status");
            inf.remove("task-feedback");
            inf.remove("lock-status");
            inf.remove("options");
            inf.remove("java-satisfy");
            boolean canDownload = true;
            if (downloadSaveAs.getText().equals("")) {
                inf.put("save-as", "save.auto");
            } else {
                String name = downloadSaveAs.getText();
                MinecraftVersionInformation detect = new MinecraftVersionInformation(name, name);
                detect.setArea(area);
                IllegalFileName illegal = FileUtil.legally(name);
                if (!illegal.isIllegal()) {
                    if (new File(detect.formatPath()).exists()) {
                        inf.put("save-as", textFormatter.formatSingleText("save.name.dump", name).toJSONObject().toString());
                        canDownload = false;
                    } else {
                        inf.put("save-as", textFormatter.formatSingleText("save.name", name).toJSONObject().toString());
                    }
                } else {
                    inf.put("save-as", textFormatter.formatSingleText("save.illegal", name, illegal.getIllegals()).toJSONObject().toString());
                    canDownload = false;
                }
            }
            for (String s : inf.keySet()) {
                try {
                    JSONObject json = new JSONObject(inf.get(s));
                    Text text = new SingleText(json);
                    downloadVersionDetailsPanel.appendText(textFormatter.format("minecraft.information." + s));
                    downloadVersionDetailsPanel.appendText(": ");
                    downloadVersionDetailsPanel.appendText(text, false);
                    downloadVersionDetailsPanel.appendText("\n");
                } catch (Exception e) {
                    downloadVersionDetailsPanel.appendText(textFormatter.format("minecraft.information." + s));
                    downloadVersionDetailsPanel.appendText(": ");
                    downloadVersionDetailsPanel.appendText(textFormatter.format(inf.get(s)), false);
                    downloadVersionDetailsPanel.appendText("\n");
                }
            }

            downloadOperationButtons.getButton(1).setVisible(information.getStatus().equals("status.download.ready") & canDownload);
        } catch (Exception ex) {
            downloadOperationButtons.getButton(1).setVisible(false);
        }

        downloadVersionDetailsPanel.updateText();
    }

    public void setJavaInformationText() {
        try {
            JavaVersionInformation information = javaVersionList.getSelectedValue();
            javaVersionDetailsPanel.setText("");
            if (information == null) {
                String search = searchJavaVersion.getText();
                if (search.equals("")) {
                    javaVersionDetailsPanel.appendText(textFormatter.format("error.java.not.found.in.manifest"));
                } else {
                    javaVersionDetailsPanel.appendText(textFormatter.format("tip.java.not.found.in.config", search));
                }
                javaVersionDetailsPanel.updateText();
                throw new Exception();
            }
            LinkedHashMap<String, String> inf = information.getInformation();
            for (String s : inf.keySet()) {
                try {
                    JSONObject json = new JSONObject(inf.get(s));
                    Text text = new SingleText(json);
                    javaVersionDetailsPanel.appendText(textFormatter.format("java.information." + s));
                    javaVersionDetailsPanel.appendText(": ");
                    javaVersionDetailsPanel.appendText(text, false);
                    javaVersionDetailsPanel.appendText("\n");
                } catch (Exception e) {
                    javaVersionDetailsPanel.appendText(textFormatter.format("java.information." + s));
                    javaVersionDetailsPanel.appendText(": ");
                    javaVersionDetailsPanel.appendText(textFormatter.format(inf.get(s)), false);
                    javaVersionDetailsPanel.appendText("\n");
                }
            }

            JavaVersionInformation checkInformation = new JavaVersionChecker().check(new File(javaPathEditing.getText()).getAbsolutePath());
            javaOperationButtons.setButtonVisible(0, checkInformation.isAvailable());
            javaOperationButtons.setButtonVisible(1, !information.getStatus().equals("status.default"));
            javaOperationButtons.setButtonVisible(2, !information.isUsed());
        } catch (Exception ex) {
            javaOperationButtons.setButtonVisible(0, false);
            javaOperationButtons.setButtonVisible(2, false);
        }

        javaVersionDetailsPanel.updateText();
    }

    public void setVmOptionEditing() {
        try {
            VmOption information = vmOptionsList.getSelectedValue();
            vmOptionsOperationButtons2.setButtonVisible(0, !addVmOptions.getText().equals(""));
            if (information == null) {
                addVmOptionsTip.setVisible(false);
                addVmOptions.setVisible(false);
                editVmOptionsKey.setVisible(false);
                editVmOptionsValue.setVisible(false);
                editVmOptionsTip.setVisible(false);
                vmOptionsOperationButtons.setVisible(false);
            } else {
                addVmOptionsTip.setVisible(true);
                addVmOptions.setVisible(true);
                vmOptionsOperationButtons.setVisible(true);
                editVmOptionsKey.setVisible(true);
                editVmOptionsTip.setVisible(true);
                boolean editKeyFocus = true;
                boolean editValueFocus = true;
                boolean badKey = false;
                String head = editVmOptionsKey.getText();
                if (head.contains("=")) {
                    badKey = head.substring(head.indexOf("=")).length() > 1;
                }
                if (!editVmOptionsKey.isFocusOwner() || badKey) {
                    editVmOptionsKey.setText(information.getName());
                    editVmOptionsKey.updateText();
                    editKeyFocus = false;
                }
                if (information.isPair()) {
                    editVmOptionsValue.setVisible(true);
                    if (!editVmOptionsValue.isFocusOwner()) {
                        editVmOptionsValue.setText(information.getPairEnd());
                        editVmOptionsValue.updateText();
                        editValueFocus = false;
                    }
                } else {
                    editVmOptionsValue.setVisible(false);
                }

                if (editKeyFocus || editValueFocus) {
                    String newKey = editVmOptionsKey.getText();

                    if (information.isPair()) {
                        information.setPairHead(newKey);
                        information.setPairEnd(editVmOptionsValue.getText());
                        information.setDetail(information.getDetail());
                    } else {
                        information.setDetail(newKey);
                    }

                    minecraftVersions.getVersionAsId(versionVmOptionList.getSelectedValue().getId()).addVmOption(information);
                    vmOptionsList.setSelectValue(information);
                    config.set("minecraft-versions", minecraftVersions.toJSONObject());
                }
            }
        } catch (Exception e) {

        }
    }

    public void setVmOptionInformationText() {
        try {
            if (vmOptionDetailsPanel.getCaret().isSelectionVisible()) {
                return;
            }
            VmOption information = vmOptionsList.getSelectedValue();
            vmOptionDetailsPanel.setText("");
            if (information == null) {
                String search = searchVmOptions.getText();
                if (!search.equals("")) {
                    vmOptionDetailsPanel.appendText(textFormatter.format("tip.vm.options.not.found", search));
                } else {
                    vmOptionDetailsPanel.setText("");
                    vmOptionDetailsPanel.appendText(textFormatter.format("error.vm.options.not.found"));
                }
                vmOptionDetailsPanel.updateText();
                throw new Exception();
            }
            JSONObject inf = information.toJSONObject();
            inf.remove("opt");
            inf.remove("id");
            for (String s : inf.keySet()) {
                try {
                    JSONObject json = new JSONObject(inf.getString(s));
                    Text text = new SingleText(json);
                    vmOptionDetailsPanel.appendText(textFormatter.format("vm.options.information." + s));
                    vmOptionDetailsPanel.appendText(": ");
                    vmOptionDetailsPanel.appendText(text, false);
                    vmOptionDetailsPanel.appendText("\n");
                } catch (Exception e) {
                    String value = inf.get(s).toString();
                    if (value.equals("")) continue;
                    Text t = textFormatter.format("vm.options.information." + s);
                    vmOptionDetailsPanel.appendText(t);
                    vmOptionDetailsPanel.appendText(": ");
                    if (!value.equals("true") & !value.equals("false") || s.equals("pair") || s.equals("enable")) {
                        if (s.equals("description")) {
                            if (information.getName().matches("((-Xss)|(-Xm[xns]))[0-9]+[TGMKB]")) {
                                value = "vm.option." + information.getName().substring(0, 4);
                            } else {
                                value = "vm.option." + information.getName() + (information.getName().equals("-client") ? (usedJava.isIs64Bit() ? ".64" : ".32") : "");
                            } if (textFormatter.hasFormat(value)) {
                                vmOptionDetailsPanel.appendText(textFormatter.format(value), false);
                            } else {
                                vmOptionDetailsPanel.appendText(new SingleText(value.substring(10)), false);
                            }
                        } else {
                            if (s.equals("pair-head")) value = value.replace("=", "");
                            vmOptionDetailsPanel.appendText(textFormatter.format(value), false);
                        }
                    } else {
                        vmOptionDetailsPanel.appendText(new SingleText(value), false);
                    }

                    vmOptionDetailsPanel.appendText("\n");
                }
            } vmOptionDetailsPanel.updateText();
        } catch (Exception ex) {

        }
    }

    public void defaultConfig() {
        config.setIfNoExist("area", "rikaishi_nikui/minecraft");
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

    public void applyUi(RikaishiNikuiComponent component, String config) {
        component.apply(configUi.getConfigJSONObject(config));
    }

    public void applyUi() {
        switch (buttons.getActiveButton().getId()) {
            case 0 -> {
                setMainVisible();

                setLocalInformationList();
                setLocalInformationText();

                applyUi(mainPanel, "panel-main");

                applyUi(mainInformationPanel, "information-panel-main");
                applyUi(mainVersionPanel, "information-panel-main-version");
                applyUi(mainVersionList, "list-panel-versions");
                applyUi(mainVersionDetailsPanel, "information-panel-main-version-details");

                applyUi(searchLocalVersion, "search-local");
                applyUi(searchLocalTip, "search-local-tip");

                mainOperationButtons.applyButtons(configUi.getConfigJSONObject("button-panel-main-operation").getJSONObject("buttons"));
                applyUi(mainOperationButtons, "button-panel-main-operation");
            }
            case 1 -> {
                setDownloadVisible();

                setDownloadInformationList();
                setDownloadInformationText();

                downloadInformationPanel.apply(configUi.getConfigJSONObject("information-panel-download"));
                downloadVersionPanel.apply(configUi.getConfigJSONObject("information-panel-download-version"));
                downloadVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-download"));
                downloadVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-download-version-details"));

                searchDownloadVersion.apply(configUi.getConfigJSONObject("search-download"));
                searchDownloadTip.apply(configUi.getConfigJSONObject("search-download-tip"));

                downloadOperationButtons.applyButtons(configUi.getConfigJSONObject("button-panel-download-operation").getJSONObject("buttons"));
                downloadOperationButtons.apply(configUi.getConfigJSONObject("button-panel-download-operation"));

                downloadSaveAs.apply(configUi.getConfigJSONObject("download-save-as-editing"));
                downloadSaveAsTip.apply(configUi.getConfigJSONObject("download-save-as-editing-tip"));
            }
            case 2 -> {
                setJavaVisible();

                setJavaInformationList();
                detectJavaVersions();
                setJavaInformationText();

                javaInformationPanel.apply(configUi.getConfigJSONObject("information-panel-java"));

                javaVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-java"));
                javaVersionPanel.apply(configUi.getConfigJSONObject("information-panel-java-version"));

                javaVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-java-version-details"));

                searchJavaVersion.apply(configUi.getConfigJSONObject("search-java"));
                searchJavaTip.apply(configUi.getConfigJSONObject("search-java-tip"));

                javaPathEditing.apply(configUi.getConfigJSONObject("java-path-editing"));
                javaPathTip.apply(configUi.getConfigJSONObject("java-path-editing-tip"));

                javaOperationButtons.apply(configUi.getConfigJSONObject("button-panel-java-operation"));
            }
            case 3 -> {
                setVmVisible();

                setLocalInformationList();
                setVmOptionsList();
                setVmOptionEditing();
                setVmOptionInformationText();

                vmOptionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options-details"));
                vmOptionsInformationPanel.apply(configUi.getConfigJSONObject("information-panel-vm"));

                vmOptionsList.apply(configUi.getConfigJSONObject("list-panel-vm-options"));
                vmOptionsPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options"));

                searchVmOptions.apply(configUi.getConfigJSONObject("search-vm-options"));
                searchVmOptionsTip.apply(configUi.getConfigJSONObject("search-vm-options-tip"));

                searchLocalMinecraftVmVersion.apply(configUi.getConfigJSONObject("search-minecraft-vm-options"));
                searchLocalMinecraftVmTip.apply(configUi.getConfigJSONObject("search-minecraft-vm-options-tip"));

                mainVersionPanel.apply(configUi.getConfigJSONObject("information-panel-main-version"));
                mainVersionList.apply(configUi.getConfigJSONObject("list-panel-versions"));

                versionVmOptionList.apply(configUi.getConfigJSONObject("list-panel-vm-options-versions"));
                versionVmOptionListPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options-versions"));

                editVmOptionsKey.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-key"));
                editVmOptionsValue.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-value"));
                editVmOptionsTip.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-tip"));

                addVmOptions.apply(configUi.getConfigJSONObject("add-minecraft-vm-options"));
                addVmOptionsTip.apply(configUi.getConfigJSONObject("add-minecraft-vm-options-tip"));

                vmOptionsOperationButtons.apply(configUi.getConfigJSONObject("button-panel-vm-options-operation"));
                vmOptionsOperationButtons2.apply(configUi.getConfigJSONObject("button-panel-vm-options-operation#2"));
            }
        }

        buttons.applyButtons(configUi.getConfigJSONObject("button-panel-main").getJSONObject("buttons"));
        buttons.apply(configUi.getConfigJSONObject("button-panel-main"));
    }

    public void setMainVisible(boolean visible) {
        mainInformationPanel.setVisible(visible);
        mainVersionPanel.setVisible(visible);
        mainVersionList.setVisible(visible);
        mainVersionDetailsPanel.setVisible(visible);
        searchLocalTip.setVisible(visible);
        searchLocalVersion.setVisible(visible);
        mainOperationButtons.setVisible(visible);
    }

    public void setMainVisible() {
        setMainVisible(true);
        setDownloadVisible(false);
        setJavaVisible(false);
        setVmVisible(false);
    }

    public void setDownloadVisible(boolean visible) {
        downloadInformationPanel.setVisible(visible);
        downloadVersionPanel.setVisible(visible);
        downloadVersionList.setVisible(visible);
        downloadVersionDetailsPanel.setVisible(visible);
        searchDownloadVersion.setVisible(visible);
        searchDownloadTip.setVisible(visible);
        downloadOperationButtons.setVisible(visible);
        downloadSaveAs.setVisible(visible);
        downloadSaveAsTip.setVisible(visible);
    }

    public void setDownloadVisible() {
        setDownloadVisible(true);
        setMainVisible(false);
        setJavaVisible(false);
        setVmVisible(false);
    }

    public void setJavaVisible(boolean visible) {
        javaInformationPanel.setVisible(visible);
        javaVersionPanel.setVisible(visible);
        javaVersionList.setVisible(visible);
        javaVersionDetailsPanel.setVisible(visible);
        searchJavaTip.setVisible(visible);
        searchJavaVersion.setVisible(visible);
        //        javaOperationButtons.setVisible(visible);
    }

    public void setJavaVisible() {
        setJavaVisible(true);
        setDownloadVisible(false);
        setMainVisible(false);
        setVmVisible(false);
    }

    public void setVmVisible(boolean visible) {
        vmOptionDetailsPanel.setVisible(visible);
        vmOptionsInformationPanel.setVisible(visible);
        vmOptionsList.setVisible(visible);
        vmOptionsPanel.setVisible(visible);
        searchVmOptions.setVisible(visible);
        searchVmOptionsTip.setVisible(visible);
        //        javaOperationButtons.setVisible(visible);
    }

    public void setVmVisible() {
        setVmVisible(true);
        setJavaVisible(false);
        setDownloadVisible(false);
        setMainVisible(false);
    }

    public void setTaskVisible(boolean visible) {

    }

    public void setTaskVisible() {
        setTaskVisible(true);
        setVmVisible(false);
        setJavaVisible(false);
        setDownloadVisible(false);
        setMainVisible(false);
    }

    public void testRending() {
        mainPanel.apply(configUi.getConfigJSONObject("panel-main"));

        buttons.applyButtons(configUi.getConfigJSONObject("button-panel-main").getJSONObject("buttons"));
        buttons.apply(configUi.getConfigJSONObject("button-panel-main"));

        mainInformationPanel.apply(configUi.getConfigJSONObject("information-panel-main"));
        mainVersionPanel.apply(configUi.getConfigJSONObject("information-panel-main-version"));
        mainVersionList.apply(configUi.getConfigJSONObject("list-panel-versions"));
        mainVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-main-version-details"));
        mainOperationButtons.apply(configUi.getConfigJSONObject("button-panel-main-operation"));

        downloadInformationPanel.apply(configUi.getConfigJSONObject("information-panel-download"));
        downloadVersionPanel.apply(configUi.getConfigJSONObject("information-panel-download-version"));
        downloadVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-download"));
        downloadVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-download-version-details"));
        downloadOperationButtons.apply(configUi.getConfigJSONObject("button-panel-download-operation"));

        searchLocalVersion.apply(configUi.getConfigJSONObject("search-local"));
        searchLocalTip.apply(configUi.getConfigJSONObject("search-local-tip"));

        searchDownloadVersion.apply(configUi.getConfigJSONObject("search-download"));
        searchDownloadTip.apply(configUi.getConfigJSONObject("search-download-tip"));

        downloadSaveAs.apply(configUi.getConfigJSONObject("download-save-as-editing"));
        downloadSaveAsTip.apply(configUi.getConfigJSONObject("download-save-as-editing-tip"));

        javaInformationPanel.apply(configUi.getConfigJSONObject("information-panel-java"));

        javaVersionList.apply(configUi.getConfigJSONObject("list-panel-versions-java"));
        javaVersionPanel.apply(configUi.getConfigJSONObject("information-panel-java-version"));

        javaVersionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-java-version-details"));

        searchJavaVersion.apply(configUi.getConfigJSONObject("search-java"));
        searchJavaTip.apply(configUi.getConfigJSONObject("search-java-tip"));

        javaPathEditing.apply(configUi.getConfigJSONObject("java-path-editing"));
        javaPathTip.apply(configUi.getConfigJSONObject("java-path-editing-tip"));

        javaOperationButtons.apply(configUi.getConfigJSONObject("button-panel-java-operation"));

        vmOptionDetailsPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options-details"));
        vmOptionsInformationPanel.apply(configUi.getConfigJSONObject("information-panel-vm"));

        vmOptionsList.apply(configUi.getConfigJSONObject("list-panel-vm-options"));
        vmOptionsPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options"));

        searchVmOptions.apply(configUi.getConfigJSONObject("search-vm-options"));
        searchVmOptionsTip.apply(configUi.getConfigJSONObject("search-vm-options-tip"));

        searchLocalMinecraftVmVersion.apply(configUi.getConfigJSONObject("search-minecraft-vm-options"));
        searchLocalMinecraftVmTip.apply(configUi.getConfigJSONObject("search-minecraft-vm-options-tip"));

        versionVmOptionList.apply(configUi.getConfigJSONObject("list-panel-vm-options-versions"));
        versionVmOptionListPanel.apply(configUi.getConfigJSONObject("information-panel-vm-options-versions"));

        editVmOptionsKey.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-key"));
        editVmOptionsValue.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-value"));
        editVmOptionsTip.apply(configUi.getConfigJSONObject("edit-minecraft-vm-options-tip"));

        addVmOptions.apply(configUi.getConfigJSONObject("add-minecraft-vm-options"));
        addVmOptionsTip.apply(configUi.getConfigJSONObject("add-minecraft-vm-options-tip"));

        vmOptionsOperationButtons.apply(configUi.getConfigJSONObject("button-panel-vm-options-operation"));
        vmOptionsOperationButtons2.apply(configUi.getConfigJSONObject("button-panel-vm-options-operation#2"));
    }

    public void initUi() {
        mainFrame.setLayout(null);
        mainPanel.setLayout(null);
        buttons.setLayout(null);
        mainInformationPanel.setLayout(null);
        mainVersionPanel.setLayout(null);
        downloadInformationPanel.setLayout(null);
        downloadVersionPanel.setLayout(null);
        downloadVersionDetailsPanel.setLayout(null);
        mainOperationButtons.setLayout(null);
        downloadOperationButtons.setLayout(null);
        javaInformationPanel.setLayout(null);
        javaVersionPanel.setLayout(null);
        javaVersionDetailsPanel.setLayout(null);
        javaOperationButtons.setLayout(null);
        vmOptionsPanel.setLayout(null);
        vmOptionDetailsPanel.setLayout(null);
        vmOptionsInformationPanel.setLayout(null);
        versionVmOptionListPanel.setLayout(null);
        vmOptionsOperationButtons.setLayout(null);
        vmOptionsOperationButtons2.setLayout(null);

        mainFrame.add(mainPanel);
        mainPanel.add(buttons);


        mainPanel.add(mainInformationPanel);
        mainPanel.add(downloadInformationPanel);
        mainPanel.add(javaInformationPanel);
        mainPanel.add(vmOptionsInformationPanel);

        downloadVersionPanel.add(downloadVersionList);

        downloadInformationPanel.add(downloadVersionDetailsPanel);
        downloadInformationPanel.add(downloadVersionPanel);
        downloadInformationPanel.add(searchDownloadVersion);
        downloadInformationPanel.add(searchDownloadTip);
        downloadInformationPanel.add(downloadSaveAs);
        downloadInformationPanel.add(downloadSaveAsTip);
        downloadInformationPanel.add(downloadOperationButtons);

        javaVersionPanel.add(javaVersionList);

        javaInformationPanel.add(javaVersionPanel);
        javaInformationPanel.add(searchJavaVersion);
        javaInformationPanel.add(searchJavaTip);
        javaInformationPanel.add(javaPathEditing);
        javaInformationPanel.add(javaPathTip);
        javaInformationPanel.add(javaOperationButtons);
        javaInformationPanel.add(javaVersionDetailsPanel);

        mainVersionPanel.add(mainVersionList);

        mainInformationPanel.add(mainVersionDetailsPanel);
        mainInformationPanel.add(mainVersionPanel);
        mainInformationPanel.add(mainOperationButtons);
        mainInformationPanel.add(searchLocalVersion);
        mainInformationPanel.add(searchLocalTip);

        vmOptionsPanel.add(vmOptionsList);
        versionVmOptionListPanel.add(versionVmOptionList);

        vmOptionsInformationPanel.add(searchVmOptions);
        vmOptionsInformationPanel.add(searchVmOptionsTip);
        vmOptionsInformationPanel.add(searchLocalMinecraftVmVersion);
        vmOptionsInformationPanel.add(searchLocalMinecraftVmTip);
        //        vmOptionsInformationPanel.add(vmOperationButtons);
        vmOptionsInformationPanel.add(vmOptionDetailsPanel);
        vmOptionsInformationPanel.add(vmOptionsPanel);
        vmOptionsInformationPanel.add(versionVmOptionListPanel);
        vmOptionsInformationPanel.add(vmOptionsOperationButtons);
        vmOptionsInformationPanel.add(vmOptionsOperationButtons2);
        vmOptionsInformationPanel.add(editVmOptionsKey);
        vmOptionsInformationPanel.add(editVmOptionsValue);
        vmOptionsInformationPanel.add(editVmOptionsTip);
        vmOptionsInformationPanel.add(addVmOptions);
        vmOptionsInformationPanel.add(addVmOptionsTip);

        functionButtons();
    }

    public void defaultMainOperationButtons() {
        mainOperationButtons = new RikaishiNikuiHorizontalButtonPanel(350, 40, "main-operation");
        mainOperationButtons.setXY(0, 660);
        mainOperationButtons.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("button-panel-main-operation", mainOperationButtons.toJSONObject());
    }

    public void defaultDownloadOperationButtons() {
        downloadOperationButtons = new RikaishiNikuiHorizontalButtonPanel(350, 40, "main-operation");
        downloadOperationButtons.setXY(0, 630);
        downloadOperationButtons.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("button-panel-download-operation", downloadOperationButtons.toJSONObject());
    }

    public void defaultJavaOperationButtons() {
        javaOperationButtons = new RikaishiNikuiHorizontalButtonPanel(350, 40, "main-operation");
        javaOperationButtons.setXY(0, 630);
        javaOperationButtons.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("button-panel-java-operation", javaOperationButtons.toJSONObject());
    }

    public void defaultVmOptionsOperationButtons() {
        vmOptionsOperationButtons = new RikaishiNikuiHorizontalButtonPanel(350, 40, "vm-options-operation");
        vmOptionsOperationButtons.setXY(250, 630);
        vmOptionsOperationButtons.setBackground(new RikaishiNikuiColor(43, 43, 43));
        //        vmOptionsOperationButtons.setBackground(new RikaishiNikuiColor(0, 0, 0));
        configUi.set("button-panel-vm-options-operation", vmOptionsOperationButtons.toJSONObject());

        vmOptionsOperationButtons2 = new RikaishiNikuiHorizontalButtonPanel(350, 40, "vm-options-operation");
        vmOptionsOperationButtons2.setXY(860, 670);
        vmOptionsOperationButtons2.setBackground(new RikaishiNikuiColor(43, 43, 43));
        //        vmOptionsOperationButtons.setBackground(new RikaishiNikuiColor(0, 0, 0));
        configUi.set("button-panel-vm-options-operation#2", vmOptionsOperationButtons2.toJSONObject());

    }

    public void defaultMainInformationPanel() {
        mainInformationPanel = new RikaishiNikuiPanel(1000, 730, "main");
        mainInformationPanel.setXY(0, 0);
        mainInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main", mainInformationPanel.toJSONObject());
        mainVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList(1000, 660, "main"));
        mainVersionList.disableBorder();
        mainVersionList.setXY(0, 0);
        mainVersionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        mainVersionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions", mainVersionList.toJSONObject());
        mainVersionPanel = new RikaishiNikuiPanel(350, 660, "main");
        mainVersionPanel.setXY(0, 0);
        mainVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-main-version", mainVersionPanel.toJSONObject());
        mainVersionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "main");
        mainVersionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        mainVersionDetailsPanel.setXY(350, 0);
        configUi.set("information-panel-main-version-details", mainVersionDetailsPanel.toJSONObject());
        searchLocalVersion = new RikaishiNikuiEditingTextPanel("main");
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
        downloadInformationPanel = new RikaishiNikuiPanel(1000, 730, "download");
        downloadInformationPanel.setXY(0, 0);
        downloadInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-download", downloadInformationPanel.toJSONObject());

        downloadVersionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList(1000, 630, "download"));
        downloadVersionList.disableBorder();
        downloadVersionList.setXY(0, 0);
        downloadVersionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        downloadVersionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions-download", downloadVersionList.toJSONObject());

        downloadVersionPanel = new RikaishiNikuiPanel(350, 630, "download");
        downloadVersionPanel.setXY(0, 0);
        downloadVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-download-version", downloadVersionPanel.toJSONObject());

        downloadVersionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "download");
        downloadVersionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        downloadVersionDetailsPanel.setXY(350, 0);
        configUi.set("information-panel-download-version-details", downloadVersionDetailsPanel.toJSONObject());

        searchDownloadVersion = new RikaishiNikuiEditingTextPanel("download");
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

        downloadSaveAs = new RikaishiNikuiEditingTextPanel("download");
        downloadSaveAs.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        downloadSaveAs.setXY(100, 670);
        downloadSaveAs.setSize(250, 25);
        configUi.set("download-save-as-editing", downloadSaveAs.toJSONObject());

        downloadSaveAsTip = new RikaishiNikuiTipPanel();
        downloadSaveAsTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        downloadSaveAsTip.setXY(0, 670);
        downloadSaveAsTip.setSize(100, 25);
        downloadSaveAsTip.setText("tip.save.as");
        configUi.set("download-save-as-editing-tip", downloadSaveAsTip.toJSONObject());
    }

    public void defaultJavaInformationPanel() {
        javaInformationPanel = new RikaishiNikuiPanel(1000, 730, "java");
        javaInformationPanel.setXY(0, 0);
        javaInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-java", javaInformationPanel.toJSONObject());

        javaVersionList = new RikaishiNikuiScrollJavaList(new RikaishiNikuiJavaList(1000, 630, "java"));
        javaVersionList.disableBorder();
        javaVersionList.setXY(0, 0);
        javaVersionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        javaVersionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-versions-java", javaVersionList.toJSONObject());

        javaVersionPanel = new RikaishiNikuiPanel(350, 630, "java");
        javaVersionPanel.setXY(0, 0);
        javaVersionPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-java-version", javaVersionPanel.toJSONObject());

        javaVersionDetailsPanel = new RikaishiNikuiTextPanel(650, 720, "java");
        javaVersionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        javaVersionDetailsPanel.setXY(350, 0);
        configUi.set("information-panel-java-version-details", javaVersionDetailsPanel.toJSONObject());

        searchJavaVersion = new RikaishiNikuiEditingTextPanel("java");
        searchJavaVersion.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        searchJavaVersion.setXY(100, 700);
        searchJavaVersion.setSize(250, 25);
        configUi.set("search-java", searchJavaVersion.toJSONObject());

        searchJavaTip = new RikaishiNikuiTipPanel();
        searchJavaTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        searchJavaTip.setXY(0, 700);
        searchJavaTip.setSize(100, 25);
        searchJavaTip.setText("tip.search.java");
        configUi.set("search-java-tip", searchJavaTip.toJSONObject());

        javaPathEditing = new RikaishiNikuiEditingTextPanel("java");
        javaPathEditing.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        javaPathEditing.setXY(100, 670);
        javaPathEditing.setSize(250, 25);
        configUi.set("java-path-editing", javaPathEditing.toJSONObject());

        javaPathTip = new RikaishiNikuiTipPanel();
        javaPathTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        javaPathTip.setXY(0, 670);
        javaPathTip.setSize(100, 25);
        javaPathTip.setText("tip.search.java.path");
        configUi.set("java-path-editing-tip", javaPathTip.toJSONObject());
    }

    public void defaultVmInformationPanel() {
        vmOptionsInformationPanel = new RikaishiNikuiPanel(1000, 730, "vm");
        vmOptionsInformationPanel.setXY(0, 0);
        vmOptionsInformationPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-vm", vmOptionsInformationPanel.toJSONObject());

        versionVmOptionList = new RikaishiNikuiScrollMinecraftList(new RikaishiNikuiMinecraftList(1000, 630, "vm"));
        versionVmOptionList.disableBorder();
        versionVmOptionList.setXY(0, 0);
        versionVmOptionList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        versionVmOptionList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-vm-options-versions", versionVmOptionList.toJSONObject());

        versionVmOptionListPanel = new RikaishiNikuiPanel(250, 630, "java");
        versionVmOptionListPanel.setXY(0, 0);
        versionVmOptionListPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-vm-options-versions", versionVmOptionListPanel.toJSONObject());

        vmOptionsList = new RikaishiNikuiScrollVmOptionList(new RikaishiNikuiVmOptionList(1000, 630, "java"));
        vmOptionsList.disableBorder();
        vmOptionsList.setXY(0, 0);
        vmOptionsList.setListColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        vmOptionsList.setSelectionColor(new RikaishiNikuiColor(58, 58, 58), new RikaishiNikuiColor(214, 214, 214));
        configUi.set("list-panel-vm-options", vmOptionsList.toJSONObject());

        vmOptionsPanel = new RikaishiNikuiPanel(250, 550, "java");
        vmOptionsPanel.setXY(250, 0);
        vmOptionsPanel.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("information-panel-vm-options", vmOptionsPanel.toJSONObject());

        vmOptionDetailsPanel = new RikaishiNikuiTextPanel(650, 500, "java");
        //        vmOptionDetailsPanel.setColor(new RikaishiNikuiColor(0,0, 0), new RikaishiNikuiColor(214, 214, 214));
        vmOptionDetailsPanel.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        vmOptionDetailsPanel.setXY(500, 0);
        configUi.set("information-panel-vm-options-details", vmOptionDetailsPanel.toJSONObject());

        searchVmOptions = new RikaishiNikuiEditingTextPanel("java");
        searchVmOptions.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        searchVmOptions.setXY(100, 700);
        searchVmOptions.setSize(250, 25);
        configUi.set("search-vm-options", searchVmOptions.toJSONObject());

        searchVmOptionsTip = new RikaishiNikuiTipPanel();
        searchVmOptionsTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        searchVmOptionsTip.setXY(0, 700);
        searchVmOptionsTip.setSize(100, 25);
        searchVmOptionsTip.setText("tip.search.vm.option");
        configUi.set("search-vm-options-tip", searchVmOptionsTip.toJSONObject());

        searchLocalMinecraftVmVersion = new RikaishiNikuiEditingTextPanel("java");
        searchLocalMinecraftVmVersion.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        searchLocalMinecraftVmVersion.setXY(100, 670);
        searchLocalMinecraftVmVersion.setSize(250, 25);
        configUi.set("search-minecraft-vm-options", searchLocalMinecraftVmVersion.toJSONObject());

        searchLocalMinecraftVmTip = new RikaishiNikuiTipPanel();
        searchLocalMinecraftVmTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        searchLocalMinecraftVmTip.setXY(0, 670);
        searchLocalMinecraftVmTip.setSize(100, 25);
        searchLocalMinecraftVmTip.setText("tip.search");
        configUi.set("search-minecraft-vm-options-tip", searchLocalMinecraftVmTip.toJSONObject());

        editVmOptionsKey = new RikaishiNikuiEditingTextPanel("vm");
        editVmOptionsKey.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        editVmOptionsKey.setXY(600, 520);
        editVmOptionsKey.setSize(300, 25);
        configUi.set("edit-minecraft-vm-options-key", editVmOptionsKey.toJSONObject());

        editVmOptionsValue = new RikaishiNikuiEditingTextPanel("vm");
        editVmOptionsValue.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        editVmOptionsValue.setXY(600, 550);
        editVmOptionsValue.setSize(300, 25);
        configUi.set("edit-minecraft-vm-options-value", editVmOptionsValue.toJSONObject());

        editVmOptionsTip = new RikaishiNikuiTipPanel("vm");
        editVmOptionsTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        editVmOptionsTip.setXY(500, 520);
        editVmOptionsTip.setSize(100, 25);
        editVmOptionsTip.setText("tip.edit");
        configUi.set("edit-minecraft-vm-options-tip", editVmOptionsTip.toJSONObject());

        addVmOptions = new RikaishiNikuiEditingTextPanel("vm");
        addVmOptions.setColor(new RikaishiNikuiColor(49, 51, 53), new RikaishiNikuiColor(214, 214, 214), new RikaishiNikuiColor(200, 200, 200));
        addVmOptions.setXY(600, 580);
        addVmOptions.setSize(250, 25);
        configUi.set("add-minecraft-vm-options", addVmOptions.toJSONObject());

        addVmOptionsTip = new RikaishiNikuiTipPanel("vm");
        addVmOptionsTip.setColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        addVmOptionsTip.setXY(500, 580);
        addVmOptionsTip.setSize(100, 25);
        addVmOptionsTip.setText("tip.add");
        configUi.set("add-minecraft-vm-options-tip", addVmOptionsTip.toJSONObject());
    }

    public void defaultButtonsUi() {
        defaultBarButtons();
        defaultMainOperationButtons();
        defaultDownloadOperationButtons();
        defaultJavaOperationButtons();
        defaultVmOptionsOperationButtons();
        functionButtons();

        configUi.set("button-panel-main", buttons.toJSONObject());
        configUi.set("button-panel-main-operation", mainOperationButtons.toJSONObject());
        configUi.set("button-panel-download-operation", downloadOperationButtons.toJSONObject());
        configUi.set("button-panel-java-operation", javaOperationButtons.toJSONObject());
        configUi.set("button-panel-vm-options-operation", vmOptionsOperationButtons.toJSONObject());
        configUi.set("button-panel-vm-options-operation#2", vmOptionsOperationButtons2.toJSONObject());
    }

    public void defaultBarButtons() {
        buttons = new RikaishiNikuiHorizontalButtonPanel(1000, 40, "main");
        buttons.setXY(0, 730);
        buttons.setBackground(new RikaishiNikuiColor(43, 43, 43));
        configUi.set("button-panel-main", buttons.toJSONObject());
    }

    public void mainFunctionButtons() {
        RikaishiNikuiButton mainButton = new RikaishiNikuiButton(100, 40, "main", "main.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), true).setId(0);
        mainButton.addActionListener(e -> {
            buttons.cancelButtonsActive();
            buttons.getButton(0).setActive(true);
            tickUI();
        });
        buttons.applyButtons(mainButton);
        RikaishiNikuiButton downloadButton = new RikaishiNikuiButton(100, 40, "download", "download.vanilla.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        downloadButton.addActionListener(e -> {
            buttons.cancelButtonsActive();
            buttons.getButton(1).setActive(true);
            tickUI();
        });
        buttons.applyButtons(downloadButton);
        RikaishiNikuiButton javaButton = new RikaishiNikuiButton(100, 40, "vm", "vm.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2);
        javaButton.addActionListener(e -> {
            buttons.cancelButtonsActive();
            buttons.getButton(2).setActive(true);
            tickUI();
        });
        buttons.applyButtons(javaButton);
        RikaishiNikuiButton vmOptionButton = new RikaishiNikuiButton(100, 40, "vm-options", "vm-options.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(3);
        vmOptionButton.addActionListener(e -> {
            buttons.cancelButtonsActive();
            buttons.getButton(3).setActive(true);
            tickUI();
        });
        buttons.applyButtons(vmOptionButton);
        RikaishiNikuiButton taskButton = new RikaishiNikuiButton(100, 40, "task", "task.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(4);
        taskButton.addActionListener(e -> {
            buttons.cancelButtonsActive();
            buttons.getButton(4).setActive(true);
            tickUI();
        });
        buttons.applyButtons(taskButton);

        RikaishiNikuiButton launch = new RikaishiNikuiButton(100, 40, "launch", "launch.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(0);
        launch.addActionListener(e -> {
            MinecraftVersionInformation information = mainVersionList.getSelectedValue();
            try {
                if (!information.getLockStatus().equals("lock.launching")) {
                    information.setLockStatus("lock.launching");
                    information.setTaskFeedback("none");
                    minecraftVersions.add(information);
                    RikaishiNikuiMinecraftTask minecraftTask = new RikaishiNikuiMinecraftTask(new MinecraftLauncher(new MinecraftLaunchInformation(mainVersionList.getSelectedValue(), os, usedJava, new Account("zhuaidadaya", UUID.randomUUID().toString()))));
                    information.setTaskId(minecraftTask.getId().toString());
                    RikaishiNikuiMinecraftDownloadTask downloadTask = new RikaishiNikuiMinecraftDownloadTask(information, UUID.randomUUID(), true);
                    minecraftTask.setParentTask(downloadTask);
                    taskManager.join(minecraftTask);
                    taskManager.submitter(logFrame, minecraftTask.getId());
                    //                    subTask = minecraftTask.getId();
                }
            } catch (Exception ex) {
                information.setStatus("status.destroyed");
                minecraftVersions.add(information);
            }
        });

        mainOperationButtons.applyButtons(launch);

        RikaishiNikuiButton cancel = new RikaishiNikuiButton(100, 40, "cancel", "cancel.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        cancel.addActionListener(e -> {
            MinecraftVersionInformation information = mainVersionList.getSelectedValue();
            try {
                if (taskManager.hasTask(UUID.fromString(information.getTaskId()))) {
                    information.setLockStatus("lock.canceling");
                    information.setStatus("status.canceling");
                    minecraftVersions.add(information);
                    try {
                        taskManager.quit(UUID.fromString(information.getTaskId()));
                    } catch (Exception ex) {
                        information.setLockStatus("lock.not");
                        information.setStatus("status.undefined");
                        minecraftVersions.add(information);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        mainOperationButtons.applyButtons(cancel);

        RikaishiNikuiButton setting = new RikaishiNikuiButton(100, 40, "setting", "setting.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2);
        setting.addActionListener(e -> {

        });

        mainOperationButtons.applyButtons(setting);

        RikaishiNikuiButton delete = new RikaishiNikuiButton(100, 40, "delete", "delete.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(3);
        delete.addActionListener(e -> {
            MinecraftVersionInformation information = mainVersionList.getSelectedValue();
            try {
                FileUtil.deleteFiles(information.getPath());
            } catch (Exception ex) {

            }
            minecraftVersions.remove(information);
            mainVersionDetailsPanel.setText("");
        });

        mainOperationButtons.applyButtons(delete);

        RikaishiNikuiButton importMinecraft = new RikaishiNikuiButton(100, 40, "import", "import.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(4);
        importMinecraft.addActionListener(e -> {

        });

        mainOperationButtons.applyButtons(importMinecraft);
    }

    public void downloadFunctionButtons() {
        RikaishiNikuiButton download = new RikaishiNikuiButton(100, 40, "download", "download.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        download.addActionListener(e -> {
            String name = downloadSaveAs.getText();
            downloadSaveAs.setText("");
            downloadSaveAs.updateText();
            String id = UUID.randomUUID().toString();
            String area = config.getConfigString("area");
            if (area == null) {
                area = "minecraft";
                config.set("area", area);
            }
            String version = downloadVersionList.getSelectedValue().getVersion();
            UUID taskId = UUID.randomUUID();
            MinecraftVersionInformation information;
            if (name.equals("")) {
                information = new MinecraftVersionInformation(id, version + "-" + id, "status.downloading");
                information.setIdFormatted(true);
            } else {
                information = new MinecraftVersionInformation(id, name, "status.downloading");
                information.setIdFormatted(false);
            }
            information.setVersion(version);
            information.setTaskId(taskId.toString());
            information.setArea(area);
            RikaishiNikuiMinecraftDownloadTask downloadTask = new RikaishiNikuiMinecraftDownloadTask(information, taskId, false);
            taskManager.join(downloadTask);
        });

        downloadOperationButtons.applyButtons(download);

        RikaishiNikuiButton refresh = new RikaishiNikuiButton(100, 40, "download#refresh", "refresh.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(0);
        refresh.addActionListener(e -> {
            RikaishiNikuiDownloadRefreshTask refreshTask = new RikaishiNikuiDownloadRefreshTask(REFRESH_TASK_ID, minecraftDownloader);
            taskManager.join(refreshTask);
        });

        downloadOperationButtons.applyButtons(refresh);
    }

    public void javaFunctionButtons() {
        RikaishiNikuiButton addJava = new RikaishiNikuiButton(100, 40, "add", "add.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(0);
        addJava.addActionListener(e -> {
            javaVersions.add(new JavaVersionChecker().check(javaPathEditing.getText()));
            javaPathEditing.setText("");
            javaPathEditing.updateText();
        });

        javaOperationButtons.applyButtons(addJava);

        RikaishiNikuiButton removeJava = new RikaishiNikuiButton(100, 40, "remove", "remove.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        removeJava.addActionListener(e -> {
            javaVersions.remove(javaVersionList.getSelectedValue());
        });

        javaOperationButtons.applyButtons(removeJava);

        RikaishiNikuiButton selectJava = new RikaishiNikuiButton(100, 40, "select", "select.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2);
        selectJava.addActionListener(e -> {
            usedJava = javaVersionList.getSelectedValue();
            config.set("used_java", usedJava.toJSONObject());
        });

        javaOperationButtons.applyButtons(selectJava);
    }

    public void vmOptionsFunctionButtons() {
        RikaishiNikuiButton addOption = new RikaishiNikuiButton(100, 40, "add", "add.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(0);
        addOption.addActionListener(e -> {
            VmOption option = new VmOption(VmOption.getExample(addVmOptions.getText()));
            minecraftVersions.getVersionAsId(versionVmOptionList.getSelectedValue().getId()).addVmOption(option);
            config.set("minecraft-versions", minecraftVersions.toJSONObject());
            addVmOptions.setText("");
            addVmOptions.updateText();
        });

        vmOptionsOperationButtons2.applyButtons(addOption);

        RikaishiNikuiButton removeOption = new RikaishiNikuiButton(100, 40, "remove", "remove.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(1);
        removeOption.addActionListener(e -> {
            VmOption option = vmOptionsList.getSelectedValue();
            minecraftVersions.getVersionAsId(versionVmOptionList.getSelectedValue().getId()).removeVmOption(option.getId());
            config.set("minecraft-versions", minecraftVersions.toJSONObject());
        });

        vmOptionsOperationButtons.applyButtons(removeOption);

//        RikaishiNikuiButton selectJava = new RikaishiNikuiButton(100, 40, "select", "select.button", true).disableBorderPainted().setColor(new RikaishiNikuiColor(60, 63, 65), new RikaishiNikuiColor(214, 214, 214)).setActiveColor(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214), false).setId(2);
//        selectJava.addActionListener(e -> {
//            usedJava = javaVersionList.getSelectedValue();
//            config.set("used_java", usedJava.toJSONObject());
//        });

//        javaOperationButtons.applyButtons(selectJava);
    }

    public void functionButtons() {
        mainFunctionButtons();
        downloadFunctionButtons();
        javaFunctionButtons();
        vmOptionsFunctionButtons();
    }

    public void appendErrorFrameText(Text text, boolean clear) {
        errorFrame.appendText(text, clear);
        errorFrame.updateText();
    }

    public void appendLogFrameText(Text text, boolean clear) {
        logFrame.appendText(text, clear);
        logFrame.updateText();
        logFrame.setCaretPositionToFirst();
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

        while (!shutdown) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }

            waiting += 50;

            if (waiting > 1000) {
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
