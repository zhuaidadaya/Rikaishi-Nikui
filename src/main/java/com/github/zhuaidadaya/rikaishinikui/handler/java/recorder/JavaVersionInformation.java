package com.github.zhuaidadaya.rikaishinikui.handler.java.recorder;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.UUID;

public class JavaVersionInformation {
    private boolean isUnknown = false;
    private boolean available = true;
    private String name = "unknown";
    private String javaId;
    private String id = UUID.randomUUID().toString();
    private boolean is64Bit;
    private String type = "unknown";
    private String status = "unknown";
    private String path = "unknown";
    private int version = 17;
    private boolean used = false;

    public JavaVersionInformation(JSONObject json) {
        apply(json);
    }

    public JavaVersionInformation(String path, String javaId, boolean is64Bit) {
        this.path = path;
        this.javaId = javaId;
        this.is64Bit = is64Bit;
    }

    public JavaVersionInformation(String path, String javaId, boolean is64Bit, String name) {
        this.path = path;
        this.javaId = javaId;
        this.is64Bit = is64Bit;
        this.name = name;
    }

    public JavaVersionInformation(String path, String javaId, boolean is64Bit, String name, String type) {
        this.path = path;
        this.javaId = javaId;
        this.is64Bit = is64Bit;
        this.name = name;
        this.type = type;
    }

    public static JavaVersionInformation unknownJava() {
        JavaVersionInformation information = new JavaVersionInformation("unknown", "unknown", true);
        information.setUnknown(true);
        information.setAvailable(false);
        return information;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path.equals("path.java") ? "java" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public void setUnknown(boolean unknown) {
        isUnknown = unknown;
    }

    public String getJavaId() {
        return javaId;
    }

    public void setJavaId(String javaId) {
        this.javaId = javaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs64Bit() {
        return is64Bit;
    }

    public void setIs64Bit(boolean is64Bit) {
        this.is64Bit = is64Bit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void apply(JSONObject json) {
        this.path = json.getString("path");
        this.is64Bit = json.getBoolean("64-bit");
        this.javaId = json.getString("java-id");
        this.id = json.getString("id");
        this.type = json.getString("type");
        this.name = json.getString("name");
        this.status = json.getString("status");
        this.available = json.getBoolean("available");
        this.used = json.getBoolean("used");
        this.version = json.getInt("version");;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("java-id", javaId);
        json.put("64-bit", is64Bit);
        json.put("type", type);
        json.put("name", name);
        json.put("id", id);
        json.put("status", status);
        json.put("path", path);
        json.put("available", available);
        json.put("used", used);
        json.put("version", version);
        return json;
    }

    public LinkedHashMap<String, String> getInformation() {
        LinkedHashMap<String,String> information = new LinkedHashMap<>();
        information.put("used", String.valueOf(used));
        information.put("status", status);
        information.put("java-id", javaId);
        information.put("version", String.valueOf(version));
        information.put("available", String.valueOf(available));
        information.put("64-bit", String.valueOf(is64Bit));
        information.put("type", type);
        information.put("name", name);
        information.put("id", id);
        information.put("path", path);
        return information;
    }

    public String toString() {
        return name;
    }
}
