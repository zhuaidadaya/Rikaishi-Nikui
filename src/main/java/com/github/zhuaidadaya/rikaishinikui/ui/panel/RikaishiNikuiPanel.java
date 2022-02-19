package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import java.util.UUID;

public class RikaishiNikuiPanel extends JPanel implements RikaishiNikuiComponent {
    public RikaishiNikuiPanel() {
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiPanel(JSONObject json) {
        apply(json);
    }

    public RikaishiNikuiPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiPanel(int width, int height) {
        setSize(width, height);
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public void apply(JSONObject json) {
            setXY(json.getInt("x"), json.getInt("y"));
            setSize(json.getInt("width"), json.getInt("height"));
            setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
    }

    public void setHeight(int height) {
        setSize(getWidth(), height);
    }

    public void setWidth(int width) {
        setSize(width, getHeight());
    }

    public void setSize(int width, int height) {
        setBounds(getX(), getY(), width, height);
    }

    public void setXY(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
    }

    public void setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        json.put("x", getX());
        json.put("y", getY());
        json.put("height", getHeight());
        json.put("width", getWidth());
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());

        return json;
    }
}
