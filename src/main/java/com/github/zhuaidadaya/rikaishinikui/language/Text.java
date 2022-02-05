package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import java.awt.*;

public class Text {
    private String text = "";
    private RikaishiNikuiColor color = RikaishiNikuiColor.parse(Color.BLACK);

    public Text() {

    }

    public Text(String text) {
        this.text = text;
    }

    public Text(String text, RikaishiNikuiColor color) {
        this.text = text;
        this.color = color;
    }

    public Text(String text, Color color) {
        this.text = text;
        this.color = RikaishiNikuiColor.parse(color);
    }

    public Text(JSONObject json) {
        text = json.getString("text");

        try {
            color = new RikaishiNikuiColor(json.getJSONObject("color"));
        } catch (Exception e) {

        }
    }

    public void format(Object o) {
        text = text.formatted(o);
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

    public Text replace(String target,String replacement) {
        text = text.replace(target,replacement);
        return this;
    }
}
