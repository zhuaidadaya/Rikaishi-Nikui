package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.Parser;
import org.json.JSONObject;

import java.io.File;

public class FabricMinecraftArtifactParser extends Parser {
    private final JSONObject artifact;
    private final String name;
    private String area = "";
    private String gameVersion = "";
    private String loaderVersion = "";

    public FabricMinecraftArtifactParser(JSONObject json, String gameVersion, String loaderVersion) {
        this.artifact = json;
        this.gameVersion = gameVersion;
        this.loaderVersion = loaderVersion;
        this.name = json.getString("name");
    }

    public String getUrl() {
        return "https://maven.fabricmc.net/" + getPath();
    }

    public String getBaseUrl() {
        return artifact.getString("url");
    }

    public String getPath() {
        String path;
        String[] parts = this.name.split(":", 3);
        path = parts[0].replace(".", "/") + "/" + parts[1] + "/" + parts[2] + "/" + parts[1] + "-" + parts[2] + ".jar";
        return path;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAbsolutePath() {
        return String.format("%s/libraries/%s", new File(area).getAbsolutePath(), getPath());
    }

    public String getName() {
        String profileName = String.format("%s-%s-%s", "fabric-loader", loaderVersion, gameVersion);
        return String.format("%s/libraries/%s", area, profileName);
    }
}
