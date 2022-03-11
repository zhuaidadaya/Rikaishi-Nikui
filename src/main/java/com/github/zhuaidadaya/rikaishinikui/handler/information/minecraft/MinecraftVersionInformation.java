package com.github.zhuaidadaya.rikaishinikui.handler.information.minecraft;

import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.handler.task.RikaishiNikuiTaskStatus;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.taskManager;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class MinecraftVersionInformation {
    private final Object2ObjectLinkedOpenHashMap<String, String> vmOptionsName = new Object2ObjectLinkedOpenHashMap<>();
    private String name = "unknown";
    private String id = UUID.randomUUID().toString();
    private String status = "unknown";
    private String area = "~";
    private String path = "/";
    private String type = "unknown";
    private String version = "unknown";
    private String url = "unknown";
    private String lastLaunch = "unknown";
    private String lockStatus = "lock.not";
    private boolean idFormatted = true;
    private String taskId = "unknown";
    private RikaishiNikuiTaskStatus lastTaskStatus = RikaishiNikuiTaskStatus.INACTIVE;
    private String taskFeedback = "none";
    private String releaseTime = "unknown";
    private String releaseType = "unknown";
    private int javaRequires = 17;
    private boolean javaSatisfy = true;
    private Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions = new Object2ObjectLinkedOpenHashMap<>();

    public MinecraftVersionInformation(String id, String name, String area, String type, String status, String version) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.area = area;
        this.type = type;
        this.version = version;
    }

    public MinecraftVersionInformation(String id, String name, String area, String type, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.area = area;
        this.type = type;
    }

    public MinecraftVersionInformation(String id, String name, String area, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.area = area;
    }

    public MinecraftVersionInformation(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public MinecraftVersionInformation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public MinecraftVersionInformation(JSONObject json) {
        apply(json);
    }

    public Object2ObjectLinkedOpenHashMap<String, String> getVmOptionsName() {
        return vmOptionsName;
    }

    public int getJavaRequires() {
        return javaRequires;
    }

    public void setJavaRequires(int javaRequires) {
        this.javaRequires = javaRequires;
    }

    public void addVmOptions(String... options) {
        for (String details : options) {
            addVmOption(details);
        }
    }

    public void addVmOption(String option) {
        addVmOption(new VmOption(option));
    }

    public void addVmOption(VmOption option) {
        if (!vmOptionsName.containsKey(option.getName())) {
            this.vmOptions.put(option.getId(), option);
        } else {
            this.vmOptions.get(vmOptionsName.get(option.getName())).apply(option.toJSONObject());
        }
    }

    public void removeVmOption(VmOption option) {
        this.vmOptions.remove(option.getId());
    }

    public void removeVmOption(String id) {
        this.vmOptions.remove(id);
    }

    public Object2ObjectLinkedOpenHashMap<String, VmOption> getVmOptions() {
        return vmOptions;
    }

    public void setVmOptions(Object2ObjectLinkedOpenHashMap<String, VmOption> vmOptions) {
        this.vmOptions = vmOptions;
    }

    public Object2ObjectLinkedOpenHashMap<String, VmOption> getVmOptions(String search) {
        String filter = search.toLowerCase();
        Object2ObjectLinkedOpenHashMap<String, VmOption> options = new Object2ObjectLinkedOpenHashMap<>();
        for (VmOption option : vmOptions.values()) {
            if (option.getDetail().equals("")) {
                continue;
            }
            if (option.getDetail().toLowerCase().contains(filter) || textFormatter.getText(option.getDescription()).contains(filter) || (textFormatter.getText("vm.options.information.enable").contains(filter) & option.isEnable())) {
                options.put(option.getId(), option);
            }
        }
        return options;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getTaskFeedback() {
        return taskFeedback;
    }

    public void setTaskFeedback(String taskFeedback) {
        this.taskFeedback = taskFeedback;
    }

    public RikaishiNikuiTaskStatus getLastTaskStatus() {
        return lastTaskStatus;
    }

    public void setLastTaskStatus(RikaishiNikuiTaskStatus lastTaskStatus) {
        this.lastTaskStatus = lastTaskStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void apply(JSONObject json) {
        this.name = json.getString("name");
        this.id = json.getString("id");
        this.status = json.getString("status");
        this.area = json.getString("area");
        this.path = json.getString("path");
        this.type = json.getString("type");
        this.version = json.getString("version");
        this.url = json.getString("url");
        this.lastLaunch = json.getString("last-launch");
        this.idFormatted = json.getBoolean("formatted-by-id");
        this.lockStatus = json.getString("lock-status");
        this.taskId = json.getString("task-id");
        this.lastTaskStatus = RikaishiNikuiTaskStatus.of(json.getString("last-task-status"));
        this.taskFeedback = json.getString("task-feedback");
        this.releaseTime = json.getString("release-time");
        this.releaseType = json.getString("release-type");
        this.vmOptions = new Object2ObjectLinkedOpenHashMap<>();
        this.javaRequires = json.getInt("java-requires");
        this.javaSatisfy = json.getBoolean("java-satisfy");

        JSONObject options = json.getJSONObject("options");
        for (String o : options.keySet()) {
            VmOption option = new VmOption(options.getJSONObject(o));
            option.setId(o);
            addVmOption(option);
        }

        LinkedHashSet<VmOption> removes = new LinkedHashSet<>();
        for (VmOption option : this.vmOptions.values()) {
            try {
                if (option.toString().equals("")) {
                    removes.add(option);
                }
                if (!vmOptionsName.containsKey(option.getName())) {
                    vmOptionsName.put(option.getName(), option.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (VmOption option : removes) {
            vmOptions.remove(option.getId());
        }
    }

    public String getAbsolutePath() {
        return new File(path).getAbsolutePath();
    }

    public String getLastLaunch() {
        return lastLaunch;
    }

    public void setLastLaunch(String lastLaunch) {
        this.lastLaunch = lastLaunch;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("status", status);
        json.put("area", area);
        json.put("path", formatPath());
        json.put("type", type);
        json.put("version", version);
        json.put("url", url);
        json.put("last-launch", lastLaunch);
        json.put("formatted-by-id", idFormatted);
        json.put("lock-status", lockStatus);
        json.put("task-id", taskId);
        try {
            json.put("last-task-status", taskManager.getStatus(UUID.fromString(taskId)).toString());
        } catch (Exception e) {
            json.put("last-task-status", "task.status.inactive");
        }
        json.put("task-feedback", taskFeedback);
        json.put("release-time", releaseTime);
        json.put("release-type", releaseType);
        json.put("java-requires", javaRequires);
        json.put("java-satisfy", javaSatisfy);

        JSONObject vmOptions = new JSONObject();
        for (VmOption option : this.vmOptions.values()) {
            if (option.isPair()) {
                vmOptions.put(option.getId(), option.toJSONObject());
            } else {
                vmOptions.put(option.getId(), option.toJSONObject());
            }
        }
        json.put("options", vmOptions);

        return json;
    }

    public LinkedHashMap<String, String> getInformation() {
        LinkedHashMap<String, String> information = new LinkedHashMap<>();
        information.put("status", status);
        information.put("version", version);
        information.put("type", type);
        information.put("release-time", releaseTime);
        information.put("release-type", releaseType);
        information.put("area", area);
        information.put("java-requires", String.valueOf(javaRequires));
        information.put("java-satisfy", String.valueOf(javaSatisfy));
        information.put("name", name);
        information.put("id", id);
        information.put("path", formatPath());
        information.put("url", url);
        information.put("last-launch", lastLaunch);
        information.put("formatted-by-id", String.valueOf(idFormatted));
        information.put("lock-status", lockStatus);
        information.put("task-id", taskId);
        try {
            information.put("last-task-status", taskManager.getStatus(UUID.fromString(taskId)).toString());
        } catch (Exception e) {

        }
        information.put("task-feedback", taskFeedback);

        JSONObject vmOptions = new JSONObject();
        for (VmOption option : this.vmOptions.values()) {
            if (option.isPair()) {
                vmOptions.put(option.getId(), option.toJSONObject());
            } else {
                vmOptions.put(option.getId(), option.toJSONObject());
            }
        }
        information.put("options", String.valueOf(vmOptions));
        try {
            information.put("progress", taskManager.getProgress(UUID.fromString(taskId)));
        } catch (Exception e) {

        }
        return information;
    }

    public boolean isIdFormatted() {
        return idFormatted;
    }

    public void setIdFormatted(boolean idFormatted) {
        this.idFormatted = idFormatted;
    }

    public boolean isJavaSatisfy() {
        return javaSatisfy;
    }

    public void setJavaSatisfy(boolean javaSatisfy) {
        this.javaSatisfy = javaSatisfy;
    }

    public String formatPath() {
        String result = String.format("%s/versions/%s", area, id);
        if (idFormatted & new File(result).isDirectory()) {
            return result;
        } else {
            return String.format("%s/versions/%s", area, name);
        }
    }

    public String formatManifest() {
        String result = String.format("%s/versions/%s/%s.json", area, id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s.json", area, name, name);
        }
    }

    public String formatClientPath() {
        String result = String.format("%s/versions/%s/%s_client.jar", area, id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s_client.jar", area, name, name);
        }
    }

    public String formatServerPath() {
        String result = String.format("%s/versions/%s/%s_server.jar", area, id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s_server.jar", area, name, name);
        }
    }

    public String formatAbsoluteManifest() {
        String result = String.format("%s/versions/%s/%s.json", new File(area).getAbsolutePath(), id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s.json", new File(area).getAbsolutePath(), name, name);
        }
    }

    public String formatAbsoluteClientPath() {
        String result = String.format("%s/versions/%s/%s_client.jar", new File(area).getAbsolutePath(), id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s_client.jar", new File(area).getAbsolutePath(), name, name);
        }
    }

    public String formatAbsoluteServerPath() {
        String result = String.format("%s/versions/%s/%s_server.jar", new File(area).getAbsolutePath(), id, id);
        if (idFormatted & new File(result).isFile()) {
            return result;
        } else {
            return String.format("%s/versions/%s/%s_server.jar", new File(area).getAbsolutePath(), name, name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return name;
    }

    public boolean checkArea(String area) {
        return this.area.equals(area);
    }
}
