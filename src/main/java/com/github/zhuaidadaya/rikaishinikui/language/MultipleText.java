package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.abstractly.Multiple;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class MultipleText implements Text, Multiple<SingleText> {
    private Collection<SingleText> texts = new LinkedHashSet<>();

    public MultipleText() {

    }

    public MultipleText(SingleText... texts) {
        this.texts.addAll(List.of(texts));
    }

    public MultipleText(JSONObject json) {
        texts = new LinkedHashSet<>();
        for(String s : json.keySet()) {
            texts.add(new SingleText(json.getJSONObject(s)));
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        int i = 0;
        for(SingleText text : texts) {
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

    @Override
    public SingleText get(int index) {
        return (SingleText) texts.toArray()[index];
    }

    @Override
    public Collection<SingleText> get() {
        return texts;
    }
}
