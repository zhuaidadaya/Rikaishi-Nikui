package com.github.zhuaidadaya.rikaishinikui.ui.style;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;

public class RikaishiNikuiColorStyle {
    private RikaishiNikuiColor background = RikaishiNikuiColor.empty();
    private RikaishiNikuiColor foreground = RikaishiNikuiColor.empty();

    private RikaishiNikuiColor caret = RikaishiNikuiColor.empty();

    public RikaishiNikuiColorStyle(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        this.background = background;
        this.foreground = foreground;
    }

    public static RikaishiNikuiColorStyle getBlackStyle() {
        RikaishiNikuiColorStyle style = new RikaishiNikuiColorStyle(new RikaishiNikuiColor(43, 43, 43), new RikaishiNikuiColor(214, 214, 214));
        return style;
    }

    public static RikaishiNikuiColorStyle getWhiteStyle() {
        RikaishiNikuiColorStyle style = new RikaishiNikuiColorStyle(new RikaishiNikuiColor(233, 233, 233), new RikaishiNikuiColor(26,  26, 26));
        return style;
    }

    public RikaishiNikuiColor getBackground() {
        return background;
    }

    public void setBackground(RikaishiNikuiColor background) {
        this.background = background;
    }

    public RikaishiNikuiColor getForeground() {
        return foreground;
    }

    public void setForeground(RikaishiNikuiColor foreground) {
        this.foreground = foreground;
    }

    public RikaishiNikuiColor getCaret() {
        return caret;
    }

    public void setCaret(RikaishiNikuiColor caret) {
        this.caret = caret;
    }
}
