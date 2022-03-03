package com.github.zhuaidadaya.rikaishinikui.ui.frame;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class RikaishiNikuiFrame extends JFrame implements RikaishiNikuiComponent {
    private String text = "";
    private boolean formatted = false;

    public RikaishiNikuiFrame() {
    }

    public RikaishiNikuiFrame(JSONObject json) {
        apply(json);
    }

    public RikaishiNikuiFrame(String name) {
        setName(name);
    }

    public RikaishiNikuiFrame(String name, String text) {
        setName(name);
        setText(text);
    }

    public RikaishiNikuiFrame(int width, int height) {
        setSize(width, height);
    }

    public RikaishiNikuiFrame(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiFrame(int width, int height, String name, boolean formatted) {
        setSize(width, height);
        setName(name);
        this.formatted = formatted;
    }

    public RikaishiNikuiFrame(int width, int height, String name, boolean formatted, String text) {
        setSize(width, height);
        setName(name);
        setText(text);
        this.formatted = formatted;
    }

    public void setBackground(RikaishiNikuiColor color) {
        this.getContentPane().setBackground(color.getAwtColor());
    }

    public void apply(JSONObject json) {
        setHeight(json.getInt("height"));
        setWidth(json.getInt("width"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        formatText();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(Text text) {
        setText(text.getText());
    }

    public void setTitle(Text text) {
        setTitle(text.getText());
    }

    public void setHeight(int height) {
        setSize(getWidth(), height);
    }

    public void setWidth(int width) {
        setSize(width, getHeight());
    }

    public void formatText() {
        if(formatted) {
            setTitle(textFormatter.format(text));
        } else {
            setTitle(text);
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        json.put("height", getHeight());
        json.put("width", getWidth());
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());

        return json;
    }
}
