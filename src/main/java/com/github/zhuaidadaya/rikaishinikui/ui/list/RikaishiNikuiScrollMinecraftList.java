package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Collection;
import java.util.Vector;

public class RikaishiNikuiScrollMinecraftList extends JScrollPane implements RikaishiNikuiComponent {
    private final RikaishiNikuiMinecraftList list;
    private boolean disableBorder = false;

    public RikaishiNikuiScrollMinecraftList(RikaishiNikuiMinecraftList list) {
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

    public RikaishiNikuiScrollMinecraftList setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public void setListColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        list.setBackground(background);
        list.setForeground(foreground);
    }

    public void setSelectionColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        list.setSelectionBackground(background);
        list.setSelectionForeground(foreground);
    }

    public void setListData(Collection<MinecraftVersionInformation> data) {
        list.setListData(data);
    }

    public void setListData(Vector<? extends MinecraftVersionInformation> data) {
        list.setListData(data);
    }

    public void setListData(MinecraftVersionInformation[] data) {
        list.setListData(data);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public MinecraftVersionInformation getSelectedValue() {
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

        if(list.listSize() > 0 & list.getSelectedIndex() < 0) {
            list.setSelectedIndex(0);
        }

        list.apply(json.getJSONObject("list"));
    }

    @Override
    public RikaishiNikuiComponent setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    @Override
    public RikaishiNikuiComponent setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }
}
