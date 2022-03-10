package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import org.json.JSONObject;

public class VanillaMinecraftServiceJarParser {
    private final JSONObject download;
    private final String area;
    private final String name;
    private final VanillaMinecraftArtifactParser clientArtifactParser;
    private VanillaMinecraftArtifactParser serverArtifactParser;

    public VanillaMinecraftServiceJarParser(JSONObject json, String area, String name) {
        download = json;
        clientArtifactParser = new VanillaMinecraftArtifactParser(json.getJSONObject("client"));
        try {
            serverArtifactParser = new VanillaMinecraftArtifactParser(json.getJSONObject("server"));
        } catch (Exception e) {
            serverArtifactParser = null;
        }
        this.area = area;
        this.name = name;
    }

    public String getClientUrl() {
        return clientArtifactParser.getUrl();
    }

    public String getClientPath() {
        return String.format("%s/versions/%s/%s_client.jar", area, name, name);
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
        return String.format("%s/versions/%s/%s_server.jar", area, name, name);
    }

    public String getServerSha1() {
        return serverArtifactParser.getSha1();
    }

    public int getServerSize() {
        return serverArtifactParser.getSize();
    }
}
