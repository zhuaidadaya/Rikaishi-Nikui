package com.github.zhuaidadaya.rikaishinikui.ui.button;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.launcher;
import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormat;

public class RikaishiNikuiButton extends JButton implements RikaishiNikuiComponent {
    private int height = 0;
    private int width = 0;
    private String text = "";
    private boolean formatted = false;
    private boolean borderPainted = true;

    public RikaishiNikuiButton() {
        setName(UUID.randomUUID().toString());
    }

    public RikaishiNikuiButton(JSONObject json) {
        apply(json);
    }

    public RikaishiNikuiButton(int width) {
        setSize(width, getHeight());
        setName(UUID.randomUUID().toString());
        this.width = width;
    }

    public RikaishiNikuiButton(int width, int height) {
        setSize(width, height);
        setName(UUID.randomUUID().toString());
        this.height = height;
        this.width = width;
    }

    public RikaishiNikuiButton(int width, int height, String name) {
        setSize(width, height);
        setName(name);
        this.height = height;
        this.width = width;
    }

    public RikaishiNikuiButton(int width, int height, String name, String text, boolean formatted) {
        setSize(width, height);
        setName(name);
        this.height = height;
        this.width = width;
        this.text = text;
        this.formatted = formatted;
    }

    public void apply(JSONObject json) {
        try {
            setSize(json.getInt("width"), json.getInt("height"));
            setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
            setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
            setName(json.getString("name"));
            this.text = json.getString("text");
            this.formatted = json.getBoolean("formatted");
            this.borderPainted = json.getBoolean("border-painted");
            if(borderPainted)
                enableBorderPainted();
            else
                disableBorderPainted();
        } catch (Exception e) {
            launcher.parseError(e, "rending button", false);
        }
    }

    public void formatText() {
        if(formatted) {
            setText(textFormat.format(text));
        } else {
            setText(text);
        }
    }

    public void setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
    }


    public void setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
    }

    public void setText(Text text) {
        setText(text.getText());
    }

    public void setSize(int width, int height) {
        setBounds(getX(), getY(), width, height);
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public RikaishiNikuiButton disableBorderPainted() {
        setBorderPainted(false);
        setFocusPainted(false);
        borderPainted = false;
        return this;
    }

    public RikaishiNikuiButton enableBorderPainted() {
        setBorderPainted(true);
        setFocusPainted(true);
        borderPainted = true;
        return this;
    }

    public RikaishiNikuiButton setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("height", height);
        json.put("width", width);
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());
        json.put("text", text);
        json.put("formatted", formatted);
        json.put("border-painted", borderPainted);
        json.put("name", getName());

        return json;
    }
}
