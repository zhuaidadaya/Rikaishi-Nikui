package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class MinecraftLibraryParser {
    private JSONObject library;
    private MinecraftDownloadParser downloadParser;
    private Object2ObjectRBTreeMap<String, Boolean> rulesAllow = new Object2ObjectRBTreeMap<>();
    private JSONArray rules;
    private String os;

    public MinecraftLibraryParser(JSONObject json,String os) {
        this.library = json;
        this.downloadParser = new MinecraftDownloadParser(json.getJSONObject("downloads"));
        try {
            this.rules = json.getJSONArray("rules");
            initRulesAllow();
        } catch(Exception ex) {

        }
        this.os = os;
    }

    public MinecraftArtifactParser getLibrary() {
        try {
            initRulesAllow();
            if(rulesAllow.get(os)) {
                return downloadParser.getArtifactParser();
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void initRulesAllow() {
        rulesAllow = new Object2ObjectRBTreeMap<>();
        rulesAllow.put("windows", true);
        rulesAllow.put("linux", true);
        rulesAllow.put("macos", true);
        if(rules != null) {
            for(Object o : rules) {
                JSONObject rule = new JSONObject(o.toString());
                try {
                    String action = rule.getString("action");
                    String os = rule.getJSONObject("os").getString("name");

                    if(os.equals("osx")) {
                        rulesAllow.put("macos", action.equals("allow"));
                        if(rulesAllow.get("macos")) {
                            rulesAllow.put("windows", false);
                            rulesAllow.put("linux", false);
                        }
                    } else {
                        rulesAllow.put(os, true);
                    }
                } catch (Exception ex) {
                    rulesAllow.put("windows", true);
                    rulesAllow.put("linux", true);
                    rulesAllow.put("macos", true);
                }
            }
        }
    }

    public String getUrl() {
        return downloadParser.getUrl();
    }

    public String getPath() {
        return downloadParser.getPath();
    }

    public void setArea(String area) {
        downloadParser.setArea(area);
    }

    public String getAbsolutePath() {
        return downloadParser.getAbsolutePath();
    }

    public String getSha1() {
        return downloadParser.getSha1();
    }

    public String getName() {
        return library.getString("name");
    }
}
