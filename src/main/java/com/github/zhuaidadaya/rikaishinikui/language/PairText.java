package com.github.zhuaidadaya.rikaishinikui.language;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import org.json.JSONObject;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class PairText extends Text {
    private String text = "";
    private String head = "";
    private String end = "";
    private String split = ": ";
    private RikaishiNikuiColor headColor = RikaishiNikuiColor.empty();
    private RikaishiNikuiColor splitColor = RikaishiNikuiColor.empty();
    private RikaishiNikuiColor endColor = RikaishiNikuiColor.empty();

    public PairText() {

    }

    public PairText(SingleText head, SingleText end) {
        this.head = head.getText();
        this.end = end.getText();
        this.headColor = head.getColor();
        this.endColor = end.getColor();
    }

    public PairText(String head, String end) {
        this.head = head;
        this.end = end;
    }

    public PairText(String head, String end, String split) {
        this.head = head;
        this.split = split;
        this.end = end;
    }

    public PairText(String head, String end, RikaishiNikuiColor color) {
        this.head = head;
        this.end = end;
        this.headColor = color;
        this.splitColor = color;
        this.endColor = color;
    }

    public PairText(String head, String end, String split, RikaishiNikuiColor color) {
        this.head = head;
        this.split = split;
        this.end = end;
        this.headColor = color;
        this.splitColor = color;
        this.endColor = color;
    }

    public PairText(String head, String end, RikaishiNikuiColor headColor, RikaishiNikuiColor endColor) {
        this.head = head;
        this.end = end;
        this.headColor = headColor;
        this.splitColor = headColor;
        this.endColor = endColor;
    }

    public PairText(String head, String end, String split, RikaishiNikuiColor headColor, RikaishiNikuiColor endColor) {
        this.head = head;
        this.split = split;
        this.end = end;
        this.headColor = headColor;
        this.splitColor = headColor;
        this.endColor = endColor;
    }

    public PairText(String head, String end, String split, RikaishiNikuiColor headColor, RikaishiNikuiColor endColor, RikaishiNikuiColor splitColor) {
        this.head = head;
        this.split = split;
        this.end = end;
        this.headColor = headColor;
        this.splitColor = splitColor;
        this.endColor = endColor;
    }

    public PairText(Text head, Text end, Text split) {
        this.head = head.getText();
        if (head instanceof SingleText)
            this.headColor = ((SingleText) head).getColor();
        this.split = split.getText();
        if (split instanceof SingleText)
            this.splitColor = ((SingleText) split).getColor();
        this.end = end.getText();
        if (end instanceof SingleText)
            this.endColor = ((SingleText) end).getColor();
    }

    public PairText(JSONObject json) {
        apply(json);
    }

    public RikaishiNikuiColor getEndColor() {
        return endColor;
    }

    public void setEndColor(RikaishiNikuiColor endColor) {
        this.endColor = endColor;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("head", head);
        json.put("end", end);
        json.put("split", split);
        json.put("head-color", headColor.toJSONObject());
        json.put("split-color", splitColor.toJSONObject());
        json.put("end-color", endColor.toJSONObject());
        return json;
    }

    public void apply(JSONObject json) {
        head = json.getString("head");
        end = json.getString("end");
        split = json.getString("split");
        headColor = new RikaishiNikuiColor(json.getJSONObject("head-color"));
        splitColor = new RikaishiNikuiColor(json.getJSONObject("split-color"));
        endColor = new RikaishiNikuiColor(json.getJSONObject("end-color"));
        text = head + split + end;
    }

    public void format(Object o) {
        if (text.contains("%s")) {
            text = text.replaceFirst("%s", o.toString());
        } else {
            throw new IllegalArgumentException("miss flag \"%s\"");
        }
    }

    public boolean contains(String target) {
        return text.contains(target);
    }

    public String getText() {
        text = head + split + end;
        return text;
    }

    public Text setText(String text) {
        this.text = text;
        return this;
    }

    public RikaishiNikuiColor getHeadColor() {
        return headColor;
    }

    public void setHeadColor(RikaishiNikuiColor headColor) {
        this.headColor = headColor;
    }

    public Text setColor(Color color) {
        this.headColor = RikaishiNikuiColor.parse(color);
        return this;
    }

    public Text setColor(RikaishiNikuiColor color) {
        this.headColor = color;
        return this;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public RikaishiNikuiColor getSplitColor() {
        return splitColor;
    }

    public void setSplitColor(RikaishiNikuiColor splitColor) {
        this.splitColor = splitColor;
    }

    public Color getAwtColor() {
        return headColor.getAwtColor();
    }

    public Text replace(String target, String replacement) {
        text = text.replace(target, replacement);
        return this;
    }

    public void applyToDoc(Document doc, boolean clear, Color defaultForeground) throws BadLocationException {
        new SingleText(getHead(), getHeadColor()).applyToDoc(doc, clear, defaultForeground);
        new SingleText(getSplit(), getSplitColor()).applyToDoc(doc, clear, defaultForeground);
        new SingleText(getEnd(), getEndColor()).applyToDoc(doc, clear, defaultForeground);
    }
}
