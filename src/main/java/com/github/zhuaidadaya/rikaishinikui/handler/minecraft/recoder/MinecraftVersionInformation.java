package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder;

import org.json.JSONObject;

import java.util.UUID;

public class MinecraftVersionInformation {
    private String name = "unknown";
    private String id = UUID.randomUUID().toString();
    private String status = "unknown";
    private String area = "~";
    private String path = "/";
    private String type = "unknown";
    private String version = "unknown";
    private String url = "unknown";

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

    public void apply(JSONObject json) {
        this.name = json.getString("name");
        this.id = json.getString("id");
        this.status = json.getString("status");
        this.area = json.getString("area");
        this.path = json.getString("path");
        this.type = json.getString("type");
        this.version = json.getString("version");
        this.url = json.getString("url");
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

        return json;
    }

    public String formatPath() {
        return String.format("%s/versions/%s", area, id);
    }

    public String formatManifest() {
        return String.format("%s/versions/%s/%s.json", area, id, id);
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
