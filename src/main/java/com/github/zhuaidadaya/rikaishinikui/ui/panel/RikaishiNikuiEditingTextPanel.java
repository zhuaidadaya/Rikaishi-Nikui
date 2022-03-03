package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiTextComponent;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.awt.*;

public class RikaishiNikuiEditingTextPanel extends JTextPane implements RikaishiNikuiComponent, RikaishiNikuiTextComponent {
    private Document doc = new DefaultStyledDocument();
    private final int lineLimit = - 1;
    private String text;

    public RikaishiNikuiEditingTextPanel() {
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiEditingTextPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiEditingTextPanel(int width, int height) {
        setSize(width, height);
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiEditingTextPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public RikaishiNikuiEditingTextPanel setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    public void setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground, RikaishiNikuiColor caretColor) {
        setBackground(background);
        setForeground(foreground);
        setCaretColor(caretColor);
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
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());
        json.put("caret-color", RikaishiNikuiColor.parse(getCaretColor()).toJSONObject());

        return json;
    }

    public void apply(JSONObject json) {
        getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "none");
        setEditable(true);
        setXY(json.getInt("x"), json.getInt("y"));
        setSize(json.getInt("width"), json.getInt("height"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        setCaretColor(new RikaishiNikuiColor(json.getJSONObject("caret-color")));
        repaint();
    }

    @Override
    public RikaishiNikuiEditingTextPanel setBackground(RikaishiNikuiColor color) {
        setBackground(color.getAwtColor());
        return this;
    }

    @Override
    public RikaishiNikuiEditingTextPanel setForeground(RikaishiNikuiColor color) {
        setForeground(color.getAwtColor());
        return this;
    }

    public void setText(Text text) {
        this.text = text.getText();
        appendText(text, true);
    }

    public void setText(String text) {
        this.text = text;
        appendText(text, true, getForeground());
    }

    public void setText(String text, Color color) {
        this.text = text;
        appendText(text, true, color);
    }

    public void appendText(String text) {
        appendText(text, false, getForeground());
    }

    public void appendText(Text text) {
        appendText(text, false);
    }

    public void appendText(String text, Color color) {
        appendText(text, false, color);
    }

    public void appendText(String text, boolean clear, Color color) {
        appendText(new SingleText(text, color), clear);
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public void setSuperDoc(Document doc) {
        super.setDocument(doc);
    }
}
