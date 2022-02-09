package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Collection;
import java.util.Vector;

public class RikaishiNikuiScrollObjectList extends JScrollPane implements RikaishiNikuiComponent {
    private final RikaishiNikuiObjectList list;
    private boolean disableBorder = false;

    public RikaishiNikuiScrollObjectList(RikaishiNikuiObjectList list) {
        setViewportView(list);
        setSize(list.getSize());
        this.list = list;
    }

    public void setXY(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
    }

    public void setListXY(int x, int y) {
        list.setBounds(x, y, list.getWidth(), list.getHeight());
    }

    public void setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
    }

    public void setListColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        list.setBackground(background);
        list.setForeground(foreground);
    }

    public void setSelectionColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        list.setSelectionBackground(background);
        list.setSelectionForeground(foreground);
    }

    public void setListData(Collection<?> data) {
        list.setListData(data);
    }

    public void setListData(Vector<? extends String> data) {
        list.setListData(data);
    }

    public void setListData(String[] data) {
        list.setListData(data);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public String getSelectedValue() {
        return list.getSelectedValue();
    }

    public void disableBorder() {
        disableBorder = true;
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public void enableBorder() {
        disableBorder = false;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("x", getX());
        json.put("y", getY());
        json.put("width", getWidth());
        json.put("height", getHeight());
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());
        json.put("disable-border", disableBorder);

        json.put("list", list.toJSONObject());
        return json;
    }

    public void apply(JSONObject json) {
        setSize(json.getInt("width"), json.getInt("height"));
        setXY(json.getInt("x"), json.getInt("y"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        this.disableBorder = json.getBoolean("disable-border");
        if(disableBorder)
            disableBorder();
        else
            enableBorder();
        list.apply(json.getJSONObject("list"));
    }
}
