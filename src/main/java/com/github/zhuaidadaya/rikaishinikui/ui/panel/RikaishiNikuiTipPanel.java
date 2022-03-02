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

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormat;

public class RikaishiNikuiTipPanel extends JTextPane implements RikaishiNikuiComponent, RikaishiNikuiTextComponent {
    private Document doc = new DefaultStyledDocument();
    private String text;
    private boolean formatted = true;

    public RikaishiNikuiTipPanel() {
        setName("RikaishiNikuiComponent#" + this);
    }

    public RikaishiNikuiTipPanel(String name) {
        setName(name);
    }

    public RikaishiNikuiTipPanel(int width, int height) {
        setSize(width, height);
        setName("RikaishiNikuiComponent#" + this);
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
        setEditable(false);
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
        appendText(text, true);
    }

    public void setText(String text) {
        this.text = text;
        appendText(new SingleText(text, getForeground()), true);
    }

    public void updateText() {
        if (formatted) {
            setText(textFormat.format(this.text));
        }
        setSuperDoc(doc);
        this.doc = new DefaultStyledDocument();
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
