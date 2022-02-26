package com.github.zhuaidadaya.rikaishinikui.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LanguageResource {
    private final Map<Language, String> map = new HashMap<>();

    public void set(Language name, String resource) {
        map.put(name,resource);
    }

    public String get(Language name) {
        return map.get(name);
    }

    public Collection<Language> getNames() {
        return map.keySet();
    }
}

