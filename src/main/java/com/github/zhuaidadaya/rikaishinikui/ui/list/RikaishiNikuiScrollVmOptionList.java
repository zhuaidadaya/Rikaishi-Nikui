package com.github.zhuaidadaya.rikaishinikui.ui.list;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.recoder.MinecraftVersionInformation;
import com.github.zhuaidadaya.rikaishinikui.handler.option.vm.VmOption;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Collection;
import java.util.Vector;

public class RikaishiNikuiScrollVmOptionList extends JScrollPane implements RikaishiNikuiComponent {
    private final RikaishiNikuiVmOptionList list;
    private boolean disableBorder = false;

    public RikaishiNikuiScrollVmOptionList(RikaishiNikuiVmOptionList list) {
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

    public void setListData(Collection<VmOption> data) {
        list.setListData(data);
    }

    public void setListData(Vector<? extends VmOption> data) {
        list.setListData(data);
    }

    public void setListData(VmOption[] data) {
        list.setListData(data);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public VmOption getSelectedValue() {
        return list.getSelectedValue();
    }

    public void setSelectValue(VmOption option) {
        list.setSelectedValue(option,true);
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
}
