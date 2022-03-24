package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.information.java.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftLaunchInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.MinecraftLoaderType;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.LibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric.FabricMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifierParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifiersParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsConcurrentWaiting;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsDoneCondition;
import com.github.zhuaidadaya.rikaishinikui.handler.text.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.handler.times.TimeType;
import com.github.zhuaidadaya.rikaishinikui.handler.times.Times;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class MinecraftLauncher {
    private boolean failed = false;
    private String os;
    private Account account;
    private VanillaMinecraftClassifiersParser classifiers;
    private Process minecraft;
    private MinecraftLaunchArg arg = new MinecraftLaunchArg();
    private MinecraftVersionInformation versionInformation;

    public MinecraftLauncher(MinecraftLaunchInformation information) {
        apply(information);
    }

    public MinecraftLoaderType getLoaderType() {
        return versionInformation.getLoaderType();
    }

    public MinecraftVersionInformation getVersionInformation() {
        return versionInformation;
    }

    public void setVersionInformation(MinecraftVersionInformation versionInformation) {
        this.versionInformation = versionInformation;
        arg.setVersionInformation(versionInformation);
    }

    public void setVmOptions(Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions) {
        arg.setVmOptions(vmOptions);
    }

    public void apply(MinecraftLaunchInformation information) {
        setVersionInformation(information.getVersionInformation());
        String name = versionInformation.getName();
        setArea(versionInformation.getArea());
        setOs(information.getOs());
        setJava(information.getJava());
        setAccount(information.getAccount());
        String gamePath = versionInformation.getAbsolutePath();
        setGamePath(gamePath);
        setNativePath(gamePath + "/natives/");
        setGameSource(new JSONObject(NetworkUtil.downloadToStringBuilder(versionInformation.formatManifest()).toString()));
        setClassifiers(new VanillaMinecraftClassifiersParser(getGameSource(), getArea(), os));

        setVmOptions(information.getVmOptions());

        VanillaMinecraftLibrariesParser librariesParser = new VanillaMinecraftLibrariesParser(getGameSource(), getArea(), os);
        if (!information.getVersionInformation().getFabricLoader().isFabric())
            addLibraries(librariesParser);
    }

    public void addLibraries(LibrariesParser libraries) {
        arg.addLibraries(libraries);
    }

    public void setNativePath(String nativePath) {
        arg.setNativePath(nativePath);
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
        return arg.getGameSource();
    }

    public void setGameSource(JSONObject gameSource) {
        arg.setGameSource(gameSource);
    }

    public String getArea() {
        return arg.getArea();
    }

    public void setArea(String area) {
        arg.setArea(area);
    }

    public String getGamePath() {
        return arg.getGamePath();
    }

    public void setGamePath(String gamePath) {
        arg.setGamePath(gamePath);
    }

    public VanillaMinecraftClassifiersParser getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(VanillaMinecraftClassifiersParser classifiers) {
        this.classifiers = classifiers;
    }

    public JavaVersionInformation getJava() {
        return arg.getJava();
    }

    public void setJava(JavaVersionInformation java) {
        arg.setJava(java);
    }

    public void launch(String taskId) {
        if (getLoaderType() == MinecraftLoaderType.FABRIC) {
            String parse = NetworkUtil.downloadToStringBuilder(versionInformation.formatManifest()).toString();
            FabricMinecraftLibrariesParser parser = new FabricMinecraftLibrariesParser(new JSONObject(parse), versionInformation.getArea(), versionInformation.getVersion());
            versionInformation.setFabricLoader(parser.getFabricLoader());
            addLibraries(parser);
        }

        arg.setTaskId(taskId);
        minecraftVersions.update(versionInformation);

        try {
            FileUtil.deleteFiles(arg.getNativePath() + "/");
        } catch (Exception e) {

        }
        for (VanillaMinecraftClassifierParser classifier : classifiers.getClassifiers().values()) {
            try {
                FileUtil.unzip(arg.getArea() + "/libraries/" + classifier.getNative().getPath(), arg.getNativePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        arg.setAccount(account);

        try {
            logger.info("launching minecraft " + versionInformation.getTaskId() + " with args: " + arg.formatVisual());

            minecraft = Runtime.getRuntime().exec(arg.formatRuntime());
            BufferedReader minecraftLog = new BufferedReader(new InputStreamReader(minecraft.getInputStream(), "GBK"));
            BufferedReader minecraftError = new BufferedReader(new InputStreamReader(minecraft.getErrorStream(), "GBK"));

            /*
             * release memory
             */
            arg = new MinecraftLaunchArg();

            /*
             * call gc() to reduce memory
             */
            Runtime.getRuntime().gc();

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
                        if (readErr.contains("java.lang.UnsupportedClassVersionError")) {
                            setTaskFeedback("task.feedback.java.version.error");
                            fail();
                            unknownError.set(false);
                        }
                        if (readErr.contains("java.lang.ClassNotFoundException")) {
                            if (readErr.contains(arg.getGameSource().getString("mainClass"))) {
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

            versionInformation.setLockStatus("lock.not");
            versionInformation.setStatus("status.ready");

            minecraftVersions.update(versionInformation);

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

        versionInformation = new MinecraftVersionInformation("", "");

        System.gc();
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
