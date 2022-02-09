package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public class MinecraftServiceJarParser {
    private JSONObject download;
    private String area;
    private String name;
    private MinecraftArtifactParser clientArtifactParser;
    private MinecraftArtifactParser serverArtifactParser;

    public MinecraftServiceJarParser(JSONObject json, String area,String name) {
        download = json;
        clientArtifactParser = new MinecraftArtifactParser(json.getJSONObject("client"));
        serverArtifactParser = new MinecraftArtifactParser(json.getJSONObject("server"));
        this.area = area;
        this.name = name;
    }

    public String getClientUrl() {
        return clientArtifactParser.getUrl();
    }

    public String getClientPath() {
        return String.format("%s/versions/%s/%s_client.jar",area,name,name);
    }

    public String getClientSha1() {
        return clientArtifactParser.getSha1();
    }

    public int getClientSize() {
        return clientArtifactParser.getSize();
    }

    public String getServerUrl() {
        return serverArtifactParser.getUrl();
    }

    public String getServerPath() {
        return String.format("%s/versions/%s/%s_server.jar",area,name,name);
    }

    public String getServerSha1() {
        return serverArtifactParser.getSha1();
    }

    public int getServerSize() {
        return serverArtifactParser.getSize();
    }
}
