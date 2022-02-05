package com.github.zhuaidadaya.rikaishinikui.ui.frame;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiScrollPanel;
import com.github.zhuaidadaya.rikaishinikui.ui.panel.RikaishiNikuiTextPanel;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class RikaishiNikuiTextFrame extends JFrame implements RikaishiNikuiComponent {
    private final RikaishiNikuiTextPanel textPane = new RikaishiNikuiTextPanel();
    private final RikaishiNikuiScrollPanel scrollPane = new RikaishiNikuiScrollPanel(textPane);
    private String text;

    public RikaishiNikuiTextFrame() {
        init();
    }

    public RikaishiNikuiTextFrame(JSONObject json) {
        apply(json);
        init();
    }

    public RikaishiNikuiTextFrame(String name) {
        setName(name);
        init();
    }

    public RikaishiNikuiTextFrame(int width, int height) {
        setSize(width, height);
        init();
    }

    public RikaishiNikuiTextFrame(int width, int height, String name) {
        setSize(width, height);
        setName(name);
        init();
    }

    public RikaishiNikuiTextFrame(int width, int height, String name, String text) {
        setSize(width, height);
        setName(name);
        setText(text);
        init();
    }

    public void setText(String text) {
        this.text = text;
        textPane.setText(text);
    }

    public void setText(String text, Color color) {
        this.text = text;
        appendText(text, true, new Color(155, 144, 255));
    }

    public void appendText(String text, Color color) {
        appendText(text, false, color);

    }

    public void appendText(String text, boolean clear, Color color) {
        appendText(new Text(text, color), clear);
    }

    public void appendText(Text text, boolean clear) {
        try {
            Document doc = textPane.getDocument();
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, text.getAwtColor());
            if(clear)
                textPane.setText("");
            doc.insertString(doc.getLength(), text.getText(), asset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        setTitle(getName());

        textPane.setEditable(false);

        scrollPane.getVerticalScrollBar().setValue(0);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.setViewportView(textPane);
        scrollPane.setBorder(null);
        scrollPane.setBounds(0, 0, getWidth(), getHeight());
        add(scrollPane);
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
        json.put("background-color", RikaishiNikuiColor.parse(getBackground()).toJSONObject());
        json.put("foreground-color", RikaishiNikuiColor.parse(getForeground()).toJSONObject());

        return json;
    }
}
