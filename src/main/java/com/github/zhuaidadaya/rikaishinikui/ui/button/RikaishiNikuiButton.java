package com.github.zhuaidadaya.rikaishinikui.ui.button;

import com.github.zhuaidadaya.rikaishinikui.handler.text.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class RikaishiNikuiButton extends JButton implements RikaishiNikuiComponent {
    private int height = 0;
    private int width = 0;
    private String text = "";
    private boolean formatted = false;
    private boolean borderPainted = true;
    private boolean active = false;
    private RikaishiNikuiColor activeBackground = RikaishiNikuiColor.parse(getBackground());
    private RikaishiNikuiColor activeForeground = RikaishiNikuiColor.parse(getForeground());
    private int id = 0;
    private boolean visible = true;
    private ActionListener listener;

    public RikaishiNikuiButton() {
        setName(UUID.randomUUID().toString());
    }

    public RikaishiNikuiButton(JSONObject json) {
        apply(json);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
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

    public RikaishiNikuiButton(int width, int height, String name, String text, boolean formatted, boolean active) {
        setSize(width, height);
        setName(name);
        this.height = height;
        this.width = width;
        this.text = text;
        this.formatted = formatted;
        this.active = active;

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RikaishiNikuiButton setActionListeners(ActionListener... listeners) {
        for (ActionListener listener : super.getActionListeners())
            super.removeActionListener(listener);
        for (ActionListener listener : listeners)
            super.addActionListener(listener);
        return this;
    }

    public void apply(JSONObject json) {
        setSize(json.getInt("width"), json.getInt("height"));

        setVisible(json.getBoolean("visible"));
        super.setVisible(visible);
        this.text = json.getString("text");
        this.formatted = json.getBoolean("formatted");
//        this.active = json.getBoolean("active");
        this.borderPainted = json.getBoolean("border-painted");

        if (active) {
            setBackground(new RikaishiNikuiColor(json.getJSONObject("active-background-color")));
            setForeground(new RikaishiNikuiColor(json.getJSONObject("active-foreground-color")));
        } else {
            setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
            setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        }

        setName(json.getString("name"));

        if (borderPainted)
            enableBorderPainted();
        else
            disableBorderPainted();
        formatText();
        setId(json.getInt("id"));
    }

    public void formatText() {
        if (formatted) {
            setText(textFormatter.format(text));
        } else {
            setText(text);
        }
    }

    public RikaishiNikuiButton setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }


    public RikaishiNikuiButton setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }

    public void setText(Text text) {
        setText(text.getText());
    }

    public int getId() {
        return id;
    }

    public RikaishiNikuiButton setId(int id) {
        this.id = id;
        return this;
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

    public RikaishiNikuiButton setActiveColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        activeBackground = background;
        activeForeground = foreground;
        return this;
    }

    public RikaishiNikuiButton setActiveColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground, boolean active) {
        activeBackground = background;
        activeForeground = foreground;
        setActive(active);
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("height", height);
        json.put("width", width);
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());
        json.put("active-background-color", activeBackground.toJSONObject());
        json.put("active-foreground-color", activeForeground.toJSONObject());
        json.put("text", text);
        json.put("formatted", formatted);
        json.put("border-painted", borderPainted);
        json.put("name", getName());
        json.put("id", id);
        json.put("visible", visible);
//        json.put("active", active);

        return json;
    }
}
