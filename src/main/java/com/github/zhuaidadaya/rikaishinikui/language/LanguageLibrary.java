package com.github.zhuaidadaya.rikaishinikui.language;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.json.JSONObject;

import java.util.Map;

public class LanguageLibrary {
    Map<String, String> formats = new Object2ObjectRBTreeMap<>();

    public LanguageLibrary(JSONObject format) {
        for (String s : format.keySet()) {
            formats.put(s, format.get(s).toString());
        }
    }

    public String get(String key) {
        String result = formats.get(key);
        return result == null ? key : result;
    }

    public JSONObject getJSONObject(String key) {
        return new JSONObject(formats.get(key));
    }
}
