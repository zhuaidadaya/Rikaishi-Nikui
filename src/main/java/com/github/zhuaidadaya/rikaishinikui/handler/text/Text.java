package com.github.zhuaidadaya.rikaishinikui.handler.text;

import org.json.JSONObject;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public abstract class Text {
    public abstract String getText();
    public abstract JSONObject toJSONObject();
    public abstract void apply(JSONObject json);

    public String toString() {
        return toJSONObject().toString();
    }

    public boolean equals(Object o) {
        try {
            return ((Text)o).toJSONObject().toString().equals(toJSONObject().toString());
        } catch (Exception e) {
            return false;
        }
    }

    public Color getAwtColor() {
        return null;
    }

    public abstract void applyToDoc(Document doc, boolean clear, Color defaultForeground) throws BadLocationException;
}
