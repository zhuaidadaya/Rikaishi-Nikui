package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.information.java.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftLaunchInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifierParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifiersParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsConcurrentWaiting;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsDoneCondition;
import com.github.zhuaidadaya.rikaishinikui.language.MultipleText;
import com.github.zhuaidadaya.utils.times.TimeType;
import com.github.zhuaidadaya.utils.times.Times;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class MinecraftLauncher {
    private boolean failed = false;
    private Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions;
    private MinecraftVersionInformation versionInformation;
    private String nativePath;
    private String os;
    private Account account;
    private JSONObject gameSource;
    private String area;
    private String gamePath;
    private VanillaMinecraftClassifiersParser classifiers;
    private JavaVersionInformation java = new JavaVersionInformation("java", "", true);
    private Process minecraft;
    private VanillaMinecraftLibrariesParser libraries;

    public MinecraftLauncher(MinecraftLaunchInformation information) {
        apply(information);
    }

    public MinecraftVersionInformation getVersionInformation() {
        return versionInformation;
    }

    public void setVersionInformation(MinecraftVersionInformation versionInformation) {
        this.versionInformation = versionInformation;
    }

    public void setVmOptions(Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions) {
        this.vmOptions = vmOptions;
    }

    public void apply(MinecraftLaunchInformation information) {
        versionInformation = information.getVersionInformation();
        String name = versionInformation.getName();
        setArea(versionInformation.getArea());
        setOs(information.getOs());
        setJava(information.getJava());
        setAccount(information.getAccount());
        String gamePath = versionInformation.getAbsolutePath();
        setGamePath(gamePath);
        setNativePath(gamePath + "/natives/");
        setGameSource(new JSONObject(NetworkUtil.downloadToStringBuilder(versionInformation.formatManifest()).toString()));
        setClassifiers(new VanillaMinecraftClassifiersParser(gameSource, area, os));

        setVmOptions(information.getVmOptions());

        VanillaMinecraftLibrariesParser librariesParser = new VanillaMinecraftLibrariesParser(gameSource, area, os);
        setLibraries(librariesParser);
    }

    public void setLibraries(VanillaMinecraftLibrariesParser libraries) {
        this.libraries = libraries;
    }

    public String getNativePath() {
        return nativePath;
    }

    public void setNativePath(String nativePath) {
        this.nativePath = nativePath;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public JSONObject getGameSource() {
        return gameSource;
    }

    public void setGameSource(JSONObject gameSource) {
        this.gameSource = gameSource;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGamePath() {
        return gamePath;
    }

    public void setGamePath(String gamePath) {
        this.gamePath = gamePath;
    }

    public VanillaMinecraftClassifiersParser getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(VanillaMinecraftClassifiersParser classifiers) {
        this.classifiers = classifiers;
    }

    public JavaVersionInformation getJava() {
        return java;
    }

    public void setJava(JavaVersionInformation java) {
        this.java = java;
    }

    public MinecraftLaunchArg formatArg() {
        MinecraftLaunchArg arg = new MinecraftLaunchArg();
        arg.setJava(java.getPath());
        arg.setVmOptions(vmOptions);
        arg.setNativePath(nativePath);
        arg.setLibraries(libraries);
        arg.setGameSource(gameSource);
        arg.setAccount(account);
        arg.setArea(area);
        arg.setGamePath(gamePath);
        arg.setVersionInformation(versionInformation);
        return arg;
    }

    public void launch(String taskId) {
        versionInformation.setTaskId(taskId);
        minecraftVersions.update(versionInformation);

        try {
            FileUtil.deleteFiles(nativePath + "/");
        } catch (Exception e) {

        }
        for (VanillaMinecraftClassifierParser classifier : classifiers.getClassifiers().values()) {
            try {
                FileUtil.unzip(area + "/libraries/" + classifier.getNative().getPath(), nativePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MinecraftLaunchArg arg = new MinecraftLaunchArg();
        if (account.getType().equals("offline")) {
            arg = formatArg();
        }

        try {
            System.out.println(arg.formatRuntime());
            logger.info("launching minecraft " + versionInformation.getTaskId() + " with args: " + arg.formatVisual());

            minecraft = Runtime.getRuntime().exec(arg.formatRuntime());
            BufferedReader minecraftLog = new BufferedReader(new InputStreamReader(minecraft.getInputStream(), "GBK"));
            BufferedReader minecraftError = new BufferedReader(new InputStreamReader(minecraft.getErrorStream(), "GBK"));

            versionInformation.setLastLaunch(Times.getTime(TimeType.AS_SECOND));
            versionInformation.setLockStatus("lock.not");
            versionInformation.setStatus("status.ready");

            minecraftVersions.update(versionInformation);

            AtomicInteger logLines = new AtomicInteger();
            AtomicBoolean unknownError = new AtomicBoolean(true);

            Thread logThread = new Thread(() -> {
                String readLog;
                try {
                    while ((readLog = minecraftLog.readLine()) != null) {
                        logLines.getAndIncrement();
                        taskManager.log(taskId, readLog);
                        if (failed) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    fail();
                }
            });

            Thread errThread = new Thread(() -> {
                String readErr;
                try {
                    while ((readErr = minecraftError.readLine()) != null) {
                        logLines.getAndIncrement();
                        taskManager.log(taskId, readErr);
                        if (readErr.contains("java.lang.UnsupportedClassVersionError:")) {
                            setTaskFeedback("task.feedback.java.version.error");
                            fail();
                            unknownError.set(false);
                        }
                        if (readErr.contains("java.lang.ClassNotFoundException")) {
                            if (readErr.contains(gameSource.getString("mainClass"))) {
                                setTaskFeedback("task.feedback.main.class.not.found");
                            } else {
                                setTaskFeedback("task.feedback.class.not.found");
                            }
                            fail();
                            unknownError.set(false);
                        }
                        if (failed) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    fail();
                }
            });

            errThread.setName(String.format("err submitter(%s)", versionInformation.getId()));
            logThread.setName(String.format("log submitter(%s)", versionInformation.getId()));

            ThreadsConcurrentWaiting waiting = new ThreadsConcurrentWaiting(ThreadsDoneCondition.ALIVE, logThread, errThread);
            waiting.start();

            if (logLines.get() < 5 & unknownError.get() || minecraft.exitValue() != 0 & versionInformation.getTaskFeedback().getText().equals("none")) {
                logger.warn("minecraft " + versionInformation.getTaskId() + " exit with unknown error");
                setTaskFeedback("task.feedback.unknown.error");
                fail();
            }
        } catch (Exception e) {
            fail();
        }
    }

    public void fail() {
        versionInformation.setLockStatus("lock.not");
        versionInformation.setStatus("status.ready");

        minecraftVersions.update(versionInformation);

        this.failed = true;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setTaskFeedback(MultipleText feedback) {
        versionInformation.setTaskFeedback(feedback);
        minecraftVersions.update(versionInformation);
    }

    public void setTaskFeedback(String feedback) {
        versionInformation.setTaskFeedback(feedback);
        minecraftVersions.update(versionInformation);
    }

    public void stop() {
        minecraft.destroy();
        versionInformation.setLockStatus("lock.not");
        versionInformation.setStatus("status.ready");

        minecraftVersions.update(versionInformation);
    }
}
