package com.github.zhuaidadaya.rikaishinikui.ui.color;

import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiComponent;
import com.github.zhuaidadaya.utils.integer.IntegerUtil;
import org.json.JSONObject;

import java.awt.*;

public class RikaishiNikuiColor extends Color implements RikaishiNikuiComponent {
    private int alpha = 255;
    private boolean empty = false;
    private int r;
    private int g;
    private int b;

    public RikaishiNikuiColor() {
        super(0, 0, 0);
        empty = true;
        r = -1;
        g = -1;
        b = -1;
        alpha = -1;
    }

    public RikaishiNikuiColor(int r, int g, int b) {
        super(r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RikaishiNikuiColor(int r, int g, int b, int alpha) {
        super(r, g, b, alpha);
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public RikaishiNikuiColor(JSONObject color) {
        super(color.getInt("r"), color.getInt("g"), color.getInt("b"), IntegerUtil.getIntFromJSON(color, "alpha", 255));
        apply(color);
    }

    public static RikaishiNikuiColor parse(Color color) {
        return new RikaishiNikuiColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("r", r).put("g", g).put("b", b).put("alpha", alpha);
    }

    public void apply(JSONObject json) {
        this.r = json.getInt("r");
        this.g = json.getInt("g");
        this.b = json.getInt("b");
        this.alpha = IntegerUtil.getIntFromJSON(json, "alpha", 255);
    }

    public boolean isEmpty() {
        return empty;
    }

    public Color getAwtColor() {
        if (empty) {
            return null;
        } else {
            return new Color(r, g, b, alpha);
        }
    }

    public static RikaishiNikuiColor empty() {
        return new RikaishiNikuiColor();
    }
}
