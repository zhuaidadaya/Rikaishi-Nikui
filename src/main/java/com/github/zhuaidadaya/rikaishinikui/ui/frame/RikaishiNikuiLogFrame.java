package com.github.zhuaidadaya.rikaishinikui.ui.frame;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationTextManager;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.handler.text.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiScrollPanel;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiTextPanel;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class RikaishiNikuiLogFrame extends JFrame implements RikaishiNikuiComponent, RikaishiNikuiLogComponent {
    private final RikaishiNikuiTextPanel textPane = new RikaishiNikuiTextPanel();
    private final RikaishiNikuiScrollPanel scrollPane = new RikaishiNikuiScrollPanel(textPane);
    private PaginationTextManager<?, Text> manager;

    public RikaishiNikuiLogFrame() {
        init();
    }

    public RikaishiNikuiLogFrame(JSONObject json) {
        apply(json);
        init();
    }

    public RikaishiNikuiLogFrame(String name) {
        setName(name);
        init();
    }

    public RikaishiNikuiLogFrame(int width, int height) {
        setSize(width, height);
        init();
    }

    public RikaishiNikuiLogFrame(int width, int height, String name) {
        setSize(width, height);
        setName(name);
        init();
    }

    public RikaishiNikuiLogFrame(int width, int height, String name, String text) {
        setSize(width, height);
        setName(name);
        setText(text);
        init();
    }

    public void setText(String text) {
        textPane.setText(text);
    }

    public void appendText(String text, Color color) {
        appendText(text, false, color);
    }

    public void appendText(String text, boolean clear, Color color) {
        appendText(new SingleText(text, color), clear);
    }

    public void appendText(Text text, boolean clear) {
        textPane.appendText(text, clear);
    }

    @Override
    public Document getDoc() {
        return textPane.getDoc();
    }

    @Override
    public String getText() {
        return textPane.getText();
    }

    public void setText(Text text) {
        textPane.setText(text);
    }

    public void init() {
        setTitle(getName());

        textPane.setEditable(false);

        setLayout(null);
        setResizable(false);
        scrollPane.getVerticalScrollBar().setValue(0);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.setViewportView(textPane);
        scrollPane.setBorder(null);
        scrollPane.setBounds(0, 0, getWidth(), getHeight() - 25);
        add(scrollPane);
    }

    public void setCaretPosition(int position) {
        textPane.setCaretPosition(position);
    }

    public void setCaretPositionToLast() {
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    public void setCaretPositionToFirst() {
        scrollPane.getVerticalScrollBar().setValue(0);
    }

    public void apply(JSONObject json) {
        setHeight(json.getInt("height"));
        setWidth(json.getInt("width"));
    }

    public void setHeight(int height) {
        setSize(getWidth(), height);
    }

    public void setWidth(int width) {
        setSize(width, getHeight());
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        json.put("height", getHeight());
        json.put("width", getWidth());
        json.put("background-color", RikaishiNikuiColor.parse(textPane.getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(textPane.getForeground()).toJSONObject());

        return json;
    }

    public RikaishiNikuiLogFrame setBackground(RikaishiNikuiColor color) {
        textPane.setBackground(color);
        return this;
    }

    public RikaishiNikuiLogFrame setForeground(RikaishiNikuiColor color) {
        textPane.setForeground(color);
        return this;
    }

    public RikaishiNikuiLogFrame setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public void setDoc(Document doc) {
        textPane.setDoc(doc);
    }

    public void setSuperDoc(Document doc) {
        textPane.setSuperDoc(doc);
    }

    public PaginationTextManager<?, Text> getTextManager() {
        return manager;
    }

    public void setTextManager(PaginationTextManager<?, Text> manager) {
        this.manager = manager;
    }
}
