package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.vanilla;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class VanillaMinecraftClassifierParser {
    private final JSONObject classifiers;
    private final String os;
    private final JSONObject natives;
    private JSONArray rules;
    private Object2ObjectRBTreeMap<String, Boolean> rulesAllow = new Object2ObjectRBTreeMap<>();

    public VanillaMinecraftClassifierParser(JSONObject json, String os) {
        this.classifiers = json.getJSONObject("downloads").getJSONObject("classifiers");
        this.natives = json.getJSONObject("natives");
        this.os = os;

        try {
            this.rules = json.getJSONArray("rules");
        } catch (Exception ex) {

        }
    }

    public VanillaMinecraftArtifactParser getNative() {
        try {
            if (rules != null) {
                initRulesAllow();
                natives.getString(os.equals("macos") ? "osx" : os);
                if (rulesAllow.get(os)) {
                    return new VanillaMinecraftArtifactParser(classifiers.getJSONObject("natives-" + os));
                }
            } else {
                natives.getString(os.equals("macos") ? "osx" : os);
                return new VanillaMinecraftArtifactParser(classifiers.getJSONObject("natives-" + os));
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
        for (Object o : rules) {
            JSONObject rule = new JSONObject(o.toString());
            try {
                String action = rule.getString("action");
                String os = rule.getJSONObject("os").getString("name");

                if (os.equals("osx")) {
                    rulesAllow.put("macos", action.equals("allow"));
                    if (rulesAllow.get("osx")) {
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

    public String getName() {
        return classifiers.getString("name");
    }
}
