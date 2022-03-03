package com.github.zhuaidadaya.rikaishinikui.language;

import org.json.JSONObject;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class MultipleText extends Text {
    private boolean autoLine = true;
    private Collection<SingleText> texts = new LinkedHashSet<>();

    public MultipleText() {

    }

    public MultipleText(SingleText... texts) {
        this.texts.addAll(List.of(texts));
    }

    public MultipleText(JSONObject json) {
        texts = new LinkedHashSet<>();
        int count = 0;

        try {
            autoLine = json.getBoolean("auto-line");
        } catch (Exception e) {

        }

        for (int i = 0; i < json.keySet().size() + 1; i++) {
            if (json.has(Integer.toString(i))) {
                count++;
                append(new SingleText(json.getJSONObject(Integer.toString(i))));
            }
        }

        if (count < 1) {
            throw new IllegalStateException("unable to adding 0 inner texts");
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        int i = 0;
        for (SingleText text : texts) {
            json.put(String.valueOf(i++), text.toJSONObject());
        }
        return json;
    }

    public void apply(JSONObject json) {
        texts = new LinkedHashSet<>();
        for (String s : json.keySet()) {
            texts.add(new SingleText(json.getJSONObject(s)));
        }
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for (Text s : texts) {
            text.append(s).append("\n");
        }
        return text.toString();
    }

    public void format(Object... args) {
        boolean in = false;
        Collection<SingleText> texts = get();
        Iterator<SingleText> iterator = texts.iterator();
        SingleText text = null;
        for (Object o : args) {
            if (!in) {
                text = iterator.next();
                in = true;
            }
            try {
                text.format(o);
            } catch (Exception e) {
                text = iterator.next();
                text.format(o);
            }
        }
    }

    public void append(SingleText text) {
        if (text.contains("\n") || !autoLine) {
            texts.add(text);
        } else {
            texts.add(text.append("\n"));
        }
    }

    public SingleText get(int index) {
        return (SingleText) texts.toArray()[index];
    }

    public Collection<SingleText> get() {
        return texts;
    }

    public void applyToDoc(Document doc, boolean clear, Color defaultForeground) throws BadLocationException {
        for (SingleText singleText : get())
            singleText.applyToDoc(doc, clear, defaultForeground);
    }
}
