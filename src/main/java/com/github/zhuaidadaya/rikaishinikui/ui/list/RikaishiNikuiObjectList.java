package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import java.util.Collection;
import java.util.Vector;

public class RikaishiNikuiObjectList extends JList<String> implements RikaishiNikuiComponent {
    private final int width = 0;
    private final int height = 0;
    private boolean followParent = true;
    private Collection<?> data;

    public RikaishiNikuiObjectList() {

    }

    public RikaishiNikuiObjectList(String name) {
        setName(name);
    }


    public RikaishiNikuiObjectList(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiObjectList setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiObjectList setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiObjectList setSelectionBackground(RikaishiNikuiColor color) {
        setSelectionBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiObjectList setSelectionForeground(RikaishiNikuiColor color) {
        setSelectionForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiObjectList setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public RikaishiNikuiObjectList setSelectionColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
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
        if(getWidth() != width || getHeight() != height) {
            setBounds(getX(), getY(), width, height);
        }
    }

    @Override
    public void apply(JSONObject json) {
        //        setSize(json.getInt("width"), json.getInt("height"));
        setColor(new RikaishiNikuiColor(json.getJSONObject("background-color")), new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        setSelectionColor(new RikaishiNikuiColor(json.getJSONObject("selection-background-color")), new RikaishiNikuiColor(json.getJSONObject("selection-foreground-color")));
        setXY(json.getInt("x"), json.getInt("y"));
    }

    public void setListData(Collection<?> data) {
        this.data = data;
        int selected = getSelectedIndex();
        Vector<String> vector = new Vector<>();
        for(Object s : data) {
            vector.add(s.toString());
        }
        this.setListData(vector);
        setSelectedIndex(selected);
    }
}
