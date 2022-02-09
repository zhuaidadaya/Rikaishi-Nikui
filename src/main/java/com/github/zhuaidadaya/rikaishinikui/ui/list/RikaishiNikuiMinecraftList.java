package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import java.util.Collection;
import java.util.Vector;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.config;

public class RikaishiNikuiMinecraftList extends JList<MinecraftVersionInformation> implements RikaishiNikuiComponent {
    private int width = 0;
    private int height = 0;
    private boolean followParent = true;
    private Collection<?> data;

    public RikaishiNikuiMinecraftList() {

    }

    public RikaishiNikuiMinecraftList(String name) {
        setName(name);
    }


    public RikaishiNikuiMinecraftList(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiMinecraftList setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiMinecraftList setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiMinecraftList setSelectionBackground(RikaishiNikuiColor color) {
        setSelectionBackground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiMinecraftList setSelectionForeground(RikaishiNikuiColor color) {
        setSelectionForeground(color.getAwtColor());
        return this;
    }

    public RikaishiNikuiMinecraftList setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public RikaishiNikuiMinecraftList setSelectionColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
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

    @Override
    public void apply(JSONObject json) {
        setColor(new RikaishiNikuiColor(json.getJSONObject("background-color")), new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        setSelectionColor(new RikaishiNikuiColor(json.getJSONObject("selection-background-color")), new RikaishiNikuiColor(json.getJSONObject("selection-foreground-color")));
        setXY(json.getInt("x"), json.getInt("y"));
    }

    public void setListData(Collection<MinecraftVersionInformation> data) {
        this.data = data;
        String area = config.getConfigString("area");
        int selected = getSelectedIndex();
        Vector<? extends MinecraftVersionInformation> vector = new Vector<>(data);
        for(MinecraftVersionInformation minecraft : data) {
            if(!minecraft.getArea().equals("~")) {
                if(! minecraft.checkArea(area)) {
                    vector.remove(minecraft);
                }
            }
        }
        this.setListData(vector);
        setSelectedIndex(selected);
    }
}
