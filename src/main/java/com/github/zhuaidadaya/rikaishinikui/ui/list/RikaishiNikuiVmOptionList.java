package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import org.json.JSONObject;

import javax.swing.*;
import java.util.Collection;
import java.util.Vector;

public class RikaishiNikuiVmOptionList extends JList<VmOption> implements RikaishiNikuiComponent {
    private int width = 0;
    private int height = 0;
    private boolean followParent = true;
    private Collection<?> data;

    public RikaishiNikuiVmOptionList() {

    }

    public RikaishiNikuiVmOptionList(String name) {
        setName(name);
    }


    public RikaishiNikuiVmOptionList(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiVmOptionList setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiVmOptionList setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiVmOptionList setSelectionBackground(RikaishiNikuiColor color) {
        setSelectionBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiVmOptionList setSelectionForeground(RikaishiNikuiColor color) {
        setSelectionForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiVmOptionList setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public RikaishiNikuiVmOptionList setSelectionColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setSelectionBackground(background);
        setSelectionForeground(foreground);
        return this;
    }

    public void setFollowParent(boolean follow) {
        this.followParent = follow;
    }

    public void setXY(int x, int y) {
        if(! followParent) {
            if(getX() != x || getY() != y) {
                setBounds(x, y, getWidth(), getHeight());
            }
        }
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        json.put("x", getX());
        json.put("y", getY());
        json.put("height", height);
        json.put("width", width);
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());
        json.put("selection-background-color", RikaishiNikuiColor.parse(getSelectionBackground()).toJSONObject());
        json.put("selection-foreground-color", RikaishiNikuiColor.parse(getSelectionForeground()).toJSONObject());

        return json;
    }

    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        if(getWidth() != width || getHeight() != height) {
            setBounds(getX(), getY(), width, height);
        }
    }

    public int listSize() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void apply(JSONObject json) {
        setColor(new RikaishiNikuiColor(json.getJSONObject("background-color")), new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        setSelectionColor(new RikaishiNikuiColor(json.getJSONObject("selection-background-color")), new RikaishiNikuiColor(json.getJSONObject("selection-foreground-color")));
        setXY(json.getInt("x"), json.getInt("y"));
    }

    public void setListData(Collection<VmOption> data) {
        this.data = data;
        int selected = getSelectedIndex();
        Vector<? extends VmOption> vector = new Vector<>(data);
        this.setListData(vector);
        setSelectedIndex(selected);
    }
}
