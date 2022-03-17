package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.fabric;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.LibraryParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class FabricMinecraftLibraryParser extends LibraryParser {
    private final JSONObject library;
    private final FabricMinecraftDownloadParser downloadParser;
    private Object2ObjectRBTreeMap<String, Boolean> rulesAllow = new Object2ObjectRBTreeMap<>();
    private JSONArray rules;
    private final String os;

    public FabricMinecraftLibraryParser(JSONObject json, String gameVersion,String loaderVersion,String os) {
        this.library = json;
        this.downloadParser = new FabricMinecraftDownloadParser(json,gameVersion,loaderVersion);
        this.os = os;
        try {
            this.rules = json.getJSONArray("rules");
        } catch (Exception ex) {

        }
    }

    public FabricMinecraftArtifactParser getLibrary() {
        try {
            initRulesAllow();
            if (rulesAllow.get(os)) {
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
        if (rules != null) {
            for (Object o : rules) {
                JSONObject rule = new JSONObject(o.toString());
                try {
                    String action = rule.getString("action");
                    String os = rule.getJSONObject("os").getString("name");

                    if (os.equals("osx")) {
                        rulesAllow.put("macos", action.equals("allow"));
                        if (rulesAllow.get("macos")) {
                            rulesAllow.put("windows", false);
                            rulesAllow.put("linux", false);
                            break;
                        }
                    } else {
                        rulesAllow.put(os,  action.equals("allow"));
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

    public String getName() {
        return library.getString("name");
    }
}
