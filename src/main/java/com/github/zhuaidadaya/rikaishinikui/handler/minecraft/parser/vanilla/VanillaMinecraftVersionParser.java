package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.MinecraftType;
import org.json.JSONObject;

public class VanillaMinecraftVersionParser {
    private final JSONObject version;
    private final String id;
    private final MinecraftType type;
    private final String url;
    private final String time;
    private final String releaseTime;

    public VanillaMinecraftVersionParser(JSONObject json) {
        version = json;
        this.id = json.getString("id");
        this.type = MinecraftType.parse(json.getString("type"));
        this.url = json.getString("url");
        this.time = json.getString("time");
        this.releaseTime = json.getString("releaseTime");
    }

    public JSONObject getVersion() {
        return version;
    }

    public String getType() {
        return type.getType();
    }

    public String getUrl() {
        return url;
    }

    public String getTime() {
        return time;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getId() {
        return id;
    }
}
