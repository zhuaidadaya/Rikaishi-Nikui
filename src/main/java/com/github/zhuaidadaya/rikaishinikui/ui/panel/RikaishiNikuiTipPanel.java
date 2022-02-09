package com.github.zhuaidadaya.rikaishinikui.ui.panel;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.RikaishiNikuiComponent;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormat;

public class RikaishiNikuiTipPanel extends JTextPane implements RikaishiNikuiComponent {
    private final Document doc = new DefaultStyledDocument();
    private String text;
    private boolean formatted = true;

    public RikaishiNikuiTipPanel() {

    }

    public RikaishiNikuiTipPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiTipPanel(int width, int height) {
        setSize(width, height);
    }

    public RikaishiNikuiTipPanel(int width, int height, String name) {
        setSize(width, height);
        setName(name);
    }

    public void setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
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
        json.put("text", text);
        json.put("formatted", formatted);

        return json;
    }

    public void apply(JSONObject json) {
        setEditable(true);
        setXY(json.getInt("x"), json.getInt("y"));
        setSize(json.getInt("width"), json.getInt("height"));
        setBackground(new RikaishiNikuiColor(json.getJSONObject("background-color")));
        setForeground(new RikaishiNikuiColor(json.getJSONObject("foreground-color")));
        this.formatted = json.getBoolean("formatted");
        this.text = json.getString("text");
        updateText();
        repaint();
    }

    public void setFormatted(boolean formatted) {
        this.formatted = formatted;
    }

    public void setText(Text text) {
        this.text = text.getText();
        setDoc(text);
    }

    public void setText(String text) {
        this.text = text;
        setDoc(text, getForeground());
    }

    public void setText(String text, Color color) {
        this.text = text;
        setDoc(text, color);
    }

    private void setDoc(String text, Color color) {
        setDoc(new Text(text, color));
    }

    private void setDoc(Text text) {
        try {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet asset;
            try {
                asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, text.getAwtColor());
            } catch (Exception e) {
                asset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, getForeground());
            }
            doc.remove(0, doc.getLength());
            doc.insertString(doc.getLength(), text.getText(), asset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateText() {
        if(formatted) {
            setDoc(textFormat.format(this.text));
        }
        setDocument(doc);
    }
}
