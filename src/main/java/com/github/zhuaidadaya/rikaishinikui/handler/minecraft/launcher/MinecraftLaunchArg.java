package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.launcher;

import com.github.zhuaidadaya.rikaishinikui.handler.account.Account;
import com.github.zhuaidadaya.rikaishinikui.handler.information.java.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.LibrariesParser;
import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.LibraryParser;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.language.MultipleText;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.json.JSONObject;

import java.io.File;
import java.util.Collection;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.minecraftVersions;

public class MinecraftLaunchArg {
    private String username = "";
    private String uuid = "";
    private String version = "";
    private Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions = new Object2ObjectLinkedOpenHashMap<>();
    private JavaVersionInformation java = JavaVersionInformation.unknownJava();
    private String nativePath = "native/";
    private Collection<LibrariesParser> libraries = new ObjectArrayList<>();
    private String area = "";
    private int frameWidth = 854;
    private Object2ObjectLinkedOpenHashMap<String, String> userProperties = new Object2ObjectLinkedOpenHashMap<>();
    private String userType = "Legacy";
    private int frameHeight = 480;
    private JSONObject gameSource;
    private Account account;
    private String gamePath = "";
    private String os = "windows";
    private boolean offline = true;
    private MinecraftVersionInformation versionInformation;

    public Object2ObjectLinkedOpenHashMap<String, String> getUserProperties() {
        return userProperties;
    }

    public String getLockStatus() {
        return versionInformation.getLockStatus();
    }

    public void setLockStatus(String lockStatus) {
        versionInformation.setLockStatus(lockStatus);
    }

    public String getLastLaunch() {
        return versionInformation.getLastLaunch();
    }

    public void setLastLaunch(String lastLaunch) {
        versionInformation.setLastLaunch(lastLaunch);
    }

    public MultipleText getTaskFeedback() {
        return versionInformation.getTaskFeedback();
    }

    public void setTaskFeedback(MultipleText taskFeedback) {
        versionInformation.setTaskFeedback(taskFeedback);
    }

    public void setTaskFeedback(String source,Object... args) {
        versionInformation.setTaskFeedback(source, args);
    }

    public String getId() {
        return versionInformation.getId();
    }

    public void setId(String id) {
        versionInformation.setId(id);
    }

    public String getStatus() {
        return versionInformation.getStatus();
    }

    public void setStatus(String status) {
        versionInformation.setStatus(status);
    }

    public void update() {
        minecraftVersions.update(versionInformation);
    }

    public void setUserProperties(Object2ObjectLinkedOpenHashMap<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public String getGamePath() {
        return gamePath;
    }

    public void setGamePath(String gamePath) {
        this.gamePath = gamePath;
    }

    public JSONObject getGameSource() {
        return gameSource;
    }

    public void setGameSource(JSONObject gameSource) {
        this.gameSource = gameSource;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.username = account.getName();
        this.uuid = account.getUuid();
    }

    public void setTaskId(String id) {
        versionInformation.setTaskId(id);
    }

    public String getTaskId() {
        return versionInformation.getTaskId();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNativePath() {
        return nativePath;
    }

    public void setNativePath(String nativePath) {
        this.nativePath = nativePath;
    }

    public Collection<LibrariesParser> getLibraries() {
        return libraries;
    }

    public void setLibraries(Collection<LibrariesParser> libraries) {
        this.libraries = libraries;
    }

    public void addLibraries(LibrariesParser libraries) {
        this.libraries.add(libraries);
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JavaVersionInformation getJava() {
        return java;
    }

    public void setJava(JavaVersionInformation java) {
        this.java = java;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object2ObjectLinkedOpenHashMap<String, VmOption> getVmOptions() {
        return vmOptions;
    }

    public void setVmOptions(Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions) {
        this.vmOptions = vmOptions;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public String formatVmOptionsRuntime() {
        StringBuilder vmOptionString = new StringBuilder();
        int last = vmOptions.size();
        if (last > 0) {
            for (VmOption option : vmOptions.values()) {
                last--;
                if (last > 0) {
                    vmOptionString = appendVmOption(vmOptionString, option.getDetail(), null);
                } else {
                    vmOptionString = appendVmOption(vmOptionString, option.getDetail(), "");
                }
            }
        }
        return vmOptionString.toString();
    }

    public MinecraftVersionInformation getVersionInformation() {
        return versionInformation;
    }

    public void setVersionInformation(MinecraftVersionInformation versionInformation) {
        this.versionInformation = versionInformation;
    }

    public StringBuilder appendVmOption(StringBuilder source, String option, String suffix) {
        source.append(option).append(suffix == null ? " " : suffix);
        return source;
    }

    public String formatClassesPathRuntime() {
        StringBuilder classPathString = new StringBuilder();
        for (LibrariesParser parser : libraries) {
            for (LibraryParser lib : parser.getLibraries().values()) {
                lib.setArea(area);
                classPathString.append(lib.getAbsolutePath()).append(os.equals("windows") ? ";" : ":");
            }
        }
        classPathString.append(versionInformation.formatAbsoluteClientPath()).append(os.equals("windows") ? ";" : ":");
//        System.out.println(classPathString);
        return classPathString.toString();
    }

    public String formatVisual() {
        StringBuilder builder = new StringBuilder("\n");
        String baseSpc = "";
        builder = appendSpc(builder, "(" + java.getName() + ") \"" + java.getPath() + "\"", baseSpc);
        baseSpc = "    ";
        for (VmOption vmOption : vmOptions.values()) {
            if (vmOption.isPair()) {
                builder = appendSpc(builder, vmOption.getPairHead(), baseSpc);
                baseSpc = "        ";
                builder = appendSpc(builder, vmOption.getPairEnd(), baseSpc);
                baseSpc = "    ";
            } else {
                builder = appendSpc(builder, vmOption.getDetail(), baseSpc);
            }
        }
        builder = appendSpc(builder, "-Djava.library.path", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, "\"" + nativePath + "\"", baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "-cp", baseSpc);
        baseSpc = "        ";
        for (LibrariesParser parser : libraries) {
            for (LibraryParser lib : parser.getLibraries().values()) {
                lib.setArea(area);
                builder = appendSpc(builder, lib.getAbsolutePath(), baseSpc);
            }
        }
        builder = appendSpc(builder,versionInformation.formatAbsoluteClientPath(), baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--username", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, username, baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--gameDir", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, "\"" + gamePath + "\"", baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--version", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, gameSource.getString("id"), baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--assetsDir", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, "\"" + new File(area + "/assets").getAbsolutePath() + "\"", baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--assetIndex", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, gameSource.getString("assets"), baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--uuid", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, account.getUuid(), baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--accessToken", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, offline ? account.getUuid() : account.getToken(), baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--userProperties", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, "{", baseSpc);
        baseSpc = "            ";
        for (String s : userProperties.keySet()) {
            builder = appendSpc(builder, s + "=" + userProperties.get(s), baseSpc);
        }
        baseSpc = "        ";
        builder = appendSpc(builder, "}", baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--userType", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, userType, baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--width", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, frameWidth, baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "--height", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, frameHeight, baseSpc);
        baseSpc = "    ";
        builder = appendSpc(builder, "mainClass", baseSpc);
        baseSpc = "        ";
        builder = appendSpc(builder, gameSource.getString("mainClass"), baseSpc);
        return builder.substring(0, builder.length() - 1);
    }

    public StringBuilder appendSpc(StringBuilder builder, Object details, String spc) {
        return builder.append(spc).append(details.toString()).append("\n");
    }

    public String formatRuntime() {
        return String.format("\"%s\" %s -Djava.library.path=\"%s\" -cp \"%s\" %s --username %s --version \"%s\" --gameDir \"%s\" --assetsDir \"%s\" --assetIndex %s --uuid %s --accessToken %s --userProperties %s --userType %s --width %s --height %s", //stu
                java.getPath(), //java
                formatVmOptionsRuntime(), //java vm options
                nativePath, //game native path
                formatClassesPathRuntime(), //game libraries
                gameSource.getString("mainClass"), //entry point class
                account.getName(), //account name
                gameSource.getString("id"), //game version
                gamePath, //game path
                new File(area + "/assets").getAbsolutePath(), //asset path
                gameSource.getString("assets"), //asset id
                account.getUuid(), //account uuid
                offline ? account.getUuid() : account.getToken(), //token
                userProperties.toString(), //user properties
                userType, //uer type
                frameWidth, //frame width
                frameHeight //frame height
        );
    }
}
