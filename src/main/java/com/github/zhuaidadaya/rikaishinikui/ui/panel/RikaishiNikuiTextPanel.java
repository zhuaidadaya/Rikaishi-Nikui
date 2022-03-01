package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.language.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiTextComponent;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class RikaishiNikuiTextPanel extends JTextPane implements RikaishiNikuiComponent, RikaishiNikuiTextComponent {
    private String text;
    private Document doc = new DefaultStyledDocument();

    public RikaishiNikuiTextPanel() {
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiTextPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiTextPanel(int width, int height) {
        setSize(width, height);
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiTextPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public void setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
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

        return json;
    }

    public void apply(JSONObject json) {
        setEditable(false);
        setXY(json.getInt("x"), json.getInt("y"));
        setSize(json.getInt("width"), json.getInt("height"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        repaint();
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

    public void appendText(Text text, boolean clear) {
        try {
            if (text instanceof SingleText) {
                if (text.getText().equals("")) {
                    super.setText("");
                    return;
                }
                StyleContext sc = StyleContext.getDefaultStyleContext();
                AttributeSet asset;
                try {
                    asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, text.getAwtColor());
                } catch (Exception e) {
                    asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, getForeground());
                }
                if (clear)
                    doc.remove(0, doc.getLength());
                doc.insertString(doc.getLength(), text.getText(), asset);
            } else {
                if (text instanceof MultipleText) {
                    for (SingleText singleText : ((MultipleText) text).get())
                        appendText(singleText, clear);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateText() {
        setDocument(doc);
    }
}
