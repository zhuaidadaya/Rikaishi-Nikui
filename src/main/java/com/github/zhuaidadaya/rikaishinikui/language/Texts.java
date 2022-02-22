package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Texts {
    private Collection<Text> texts = new LinkedHashSet<>();

    public Texts() {

    }

    public Texts(Text... texts) {
        this.texts.addAll(List.of(texts));
    }

    public Texts(JSONObject json) {
        texts = new LinkedHashSet<>();
        for(String s : json.keySet()) {
            texts.add(new Text(json.getJSONObject(s)));
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        int i = 0;
        for(Text text : texts) {
            json.put(String.valueOf(i++),text.toJSONObject());
        }
        return json;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for(Text s : texts) {
            text.append(s).append("\n");
        }
        return text.toString();
    }
}
