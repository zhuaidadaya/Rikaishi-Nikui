package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.utils.resource.Resources;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.logger;

public class LanguageResource {
    private final Map<Language, LanguageLibrary> languages = new HashMap<>();

    public void set(Language name, String resource) {
        try {
            JSONObject json = new JSONObject(FileUtil.read(new BufferedReader(new InputStreamReader(Resources.getResource(resource, getClass()), StandardCharsets.UTF_8))));
            languages.put(name, new LanguageLibrary(json.getJSONObject("format")));
        } catch (Exception e) {
            logger.warn("failed to load language: " + resource);
        }
    }

    public void set(String resource) {
        try {
            JSONObject json = new JSONObject(FileUtil.read(new BufferedReader(new InputStreamReader(Resources.getResource(resource, getClass()), StandardCharsets.UTF_8))));
            languages.put(Language.of(json.getString("language")), new LanguageLibrary(json.getJSONObject("format")));
        } catch (Exception e) {
            logger.warn("failed to load language: " + resource);
        }
    }

    public LanguageLibrary get(Language name) {
        return languages.get(name);
    }

    public Collection<Language> getNames() {
        return languages.keySet();
    }
}

