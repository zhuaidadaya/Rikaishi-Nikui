package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.ArtifactParser;
import org.json.JSONObject;

import java.io.File;

public class VanillaMinecraftArtifactParser extends ArtifactParser {
    private final JSONObject artifact;
    private String area = "";

    public VanillaMinecraftArtifactParser(JSONObject json) {
        this.artifact = json;
    }

    public String getUrl() {
        return artifact.getString("url");
    }

    public String getPath() {
        return artifact.getString("path");
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAbsolutePath() {
        return String.format("%s/libraries/%s", new File(area).getAbsolutePath(),  artifact.getString("path")).replace("\\", "/");
    }

    public String getName() {
        return getUrl();
    }

    public String getSha1() {
        return artifact.getString("sha1");
    }

    public int getSize() {
        return artifact.getInt("size");
    }
}
