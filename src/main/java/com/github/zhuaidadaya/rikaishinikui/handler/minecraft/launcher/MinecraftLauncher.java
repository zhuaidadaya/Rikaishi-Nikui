package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.information.java.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifierParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftClassifiersParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftLibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla.VanillaMinecraftLibraryParser;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftLaunchInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.network.NetworkUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsConcurrentWaiting;
import com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting.ThreadsDoneCondition;
import com.github.zhuaidadaya.utils.times.TimeType;
import com.github.zhuaidadaya.utils.times.Times;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.*;

public class MinecraftLauncher {
    private boolean failed = false;
    private LinkedHashMap<String, VmOption> vmOptions;
    private StringBuilder vmOptionString = new StringBuilder();
    private StringBuilder classPathString = new StringBuilder();
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

    public MinecraftLauncher(MinecraftLaunchInformation information) {
        apply(information);
    }

    public MinecraftVersionInformation getVersionInformation() {
        return versionInformation;
    }

    public void setVersionInformation(MinecraftVersionInformation versionInformation) {
        this.versionInformation = versionInformation;
    }

    public void setVmOptions(LinkedHashMap<String, VmOption> vmOptions) {
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
        setVmOptionString(vmOptions);

        VanillaMinecraftLibrariesParser librariesParser = new VanillaMinecraftLibrariesParser(gameSource, area, os);
        setClassPathString(librariesParser);
        appendClassPath(versionInformation.formatAbsoluteClientPath() + (os.equals("windows") ? ";" : ":"));
    }

    public void appendClassPath(String path) {
        this.classPathString.append(path);
    }

    public StringBuilder getVmOptionString() {
        return vmOptionString;
    }

    public void setVmOptionString(LinkedHashMap<String, VmOption> options) {
        vmOptionString = new StringBuilder();
        int last = options.size();
        if(last > 0) {
            for(VmOption option : options.values()) {
                last--;
                if(last > 0) {
                    appendVmOption(option.getDetail(), null);
                } else {
                    appendVmOption(option.getDetail(), "");
                }
            }
        }
    }

    public StringBuilder getClassPathString() {
        return classPathString;
    }

    public void setClassPathString(VanillaMinecraftLibrariesParser libraries) {
        classPathString = new StringBuilder();
        for(VanillaMinecraftLibraryParser lib : libraries.getLibraries().values()) {
            lib.setArea(area);
            classPathString.append(lib.getAbsolutePath()).append(os.equals("windows") ? ";" : ":");
//            classPathString.append(area).append("/").append("libraries").append("/").append(lib.getPath()).append(os.equals("windows") ? ";" : ":");
        }
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

    public void appendVmOption(String option, String suffix) {
        vmOptionString.append(option).append(suffix == null ? " " : suffix);
    }

    public String formatArg() {
        return String.format("\"%s\" %s -Djava.library.path=\"%s\" -cp \"%s\" %s --username %s --version \"%s\" --gameDir \"%s\" --assetsDir \"%s\" --assetIndex %s --uuid %s --accessToken %s --userProperties {%s} --userType %s --width %s --height %s", //stu
                java.getPath(), //java
                vmOptionString, //java vm options
                nativePath, //game native path
                classPathString, //game libraries
                gameSource.getString("mainClass"), //main
                account.getName(), //account name
                gameSource.getString("id"), //game version
                gamePath, //game path
                area + "/assets", //asset path
                gameSource.getString("assets"), //asset id
                account.getUuid(), //account uuid
                account.getUuid(), //token
                "", //user properties
                "Legacy", //uer type
                854, //frame width
                480 //frame height
        );
    }

    public void launch(String taskId) {
        versionInformation = minecraftVersions.getVersionAsId(versionInformation.getId());
        versionInformation.setTaskId(taskId);

        try {
            FileUtil.deleteFiles(nativePath + "/");
        } catch (Exception e) {

        }
        for(VanillaMinecraftClassifierParser classifier : classifiers.getClassifiers().values()) {
            try {
                FileUtil.unzip(area + "/libraries/" + classifier.getNative().getPath(), nativePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String arg = "";
        if(account.getType().equals("offline")) {
            arg = formatArg();
        }

        try {
            logger.info("launching minecraft " + versionInformation.getTaskId() + " with arg: " + arg);

            minecraft = Runtime.getRuntime().exec(arg);
            BufferedReader minecraftLog = new BufferedReader(new InputStreamReader(minecraft.getInputStream(), "GBK"));
            BufferedReader minecraftError = new BufferedReader(new InputStreamReader(minecraft.getErrorStream(), "GBK"));

            versionInformation.setLastLaunch(Times.getTime(TimeType.AS_SECOND));
            versionInformation.setLockStatus("lock.not");
            versionInformation.setStatus("status.ready");

            minecraftVersions.add(versionInformation);

            AtomicInteger logLines = new AtomicInteger();
            AtomicBoolean unknownError = new AtomicBoolean(true);

            Thread logThread = new Thread(() -> {
                String readLog;
                try {
                    while((readLog = minecraftLog.readLine()) != null) {
                        logLines.getAndIncrement();
                        taskManager.log(taskId, readLog);
                        if(failed) {
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
                    while((readErr = minecraftError.readLine()) != null) {
                        logLines.getAndIncrement();
                        taskManager.log(taskId, readErr);
                        if(readErr.contains("java.lang.UnsupportedClassVersionError:")) {
                            setTaskFeedback("task.feedback.java.version.error");
                            fail();
                            unknownError.set(false);
                        }
                        if(failed) {
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

            if(logLines.get() < 5 & unknownError.get() || minecraft.exitValue() != 0) {
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

        minecraftVersions.add(versionInformation);

        this.failed = true;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setTaskFeedback(String feedback) {
        versionInformation.setTaskFeedback(feedback);
        minecraftVersions.add(versionInformation);
    }

    public void stop() {
        minecraft.destroy();
        versionInformation.setLockStatus("lock.not");
        versionInformation.setStatus("status.ready");

        minecraftVersions.add(versionInformation);
    }
}
