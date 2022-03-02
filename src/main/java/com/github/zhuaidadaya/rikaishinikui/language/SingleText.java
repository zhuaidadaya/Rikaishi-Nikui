package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.text.*;
import java.awt.*;

public class SingleText extends Text {
    private String text = "";
    private RikaishiNikuiColor color = RikaishiNikuiColor.empty();

    public SingleText() {

    }

    public SingleText(String text) {
        this.text = text;
    }

    public SingleText(String text, RikaishiNikuiColor color) {
        this.text = text;
        this.color = color;
    }

    public SingleText(String text, Color color) {
        this.text = text;
        this.color = RikaishiNikuiColor.parse(color);
    }

    public SingleText(JSONObject json) {
        text = json.getString("text");

        try {
            color = new RikaishiNikuiColor(json.getJSONObject("color"));
        } catch (Exception e) {

        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("text", text);
        json.put("color", color.toJSONObject());
        return json;
    }

    public void apply(JSONObject json) {
        this.text = json.getString("text");
        this.color = new RikaishiNikuiColor(json.getJSONObject("color"));
    }

    public void format(Object o) {
        if (text.contains("%s")) {
            text = text.replaceFirst("%s", o.toString());
        } else {
            throw new IllegalArgumentException("miss flag \"%s\"");
        }
    }

    public boolean contains(String target) {
        return text.contains(target);
    }

    public SingleText append(String text) {
        this.text += text;
        return this;
    }

    public String getText() {
        return text;
    }

    public Text setText(String text) {
        this.text = text;
        return this;
    }

    public RikaishiNikuiColor getColor() {
        return color;
    }

    public Text setColor(Color color) {
        this.color = RikaishiNikuiColor.parse(color);
        return this;
    }

    public Text setColor(RikaishiNikuiColor color) {
        this.color = color;
        return this;
    }

    public Color getAwtColor() {
        return color.getAwtColor();
    }

    public Text replace(String target, String replacement) {
        text = text.replace(target, replacement);
        return this;
    }

    public void applyToDoc(Document doc, boolean clear, Color defaultForeground) throws BadLocationException {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet asset;
        try {
            asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, getAwtColor());
        } catch (Exception e) {
            asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, defaultForeground);
        }
        if (clear)
            doc.remove(0, doc.getLength());
        doc.insertString(doc.getLength(), getText(), asset);
    }
}
