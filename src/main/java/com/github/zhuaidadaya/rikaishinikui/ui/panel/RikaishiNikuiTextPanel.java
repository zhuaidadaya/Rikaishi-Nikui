package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;

public class RikaishiNikuiTextPanel extends JTextPane implements RikaishiNikuiComponent {
    public RikaishiNikuiTextPanel() {

    }

    public RikaishiNikuiTextPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiTextPanel(int width, int height) {
        setSize(width, height);
    }

    public RikaishiNikuiTextPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public void setXY(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
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

    public void apply(JSONObject json) {
        setEditable(false);
        setXY(json.getInt("x"), json.getInt("y"));
        setSize(json.getInt("width"), json.getInt("height"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        updateUI();
    }
}
