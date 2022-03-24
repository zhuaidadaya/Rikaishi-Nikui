package com.github.zhuaidadaya.rikaishinikui.ui.style;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.level.LogLevel;
import com.github.zhuaidadaya.rikaishinikui.handler.text.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;

public class RikaishiNikuiColorStyle {
    private RikaishiNikuiColor background = RikaishiNikuiColor.empty();
    private RikaishiNikuiColor foreground = RikaishiNikuiColor.empty();

    private RikaishiNikuiColor caret = RikaishiNikuiColor.empty();

    public RikaishiNikuiColorStyle() {

    }

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

    public static MultipleText formatBlackLog(String log, LogLevel level) {
        MultipleText text = new MultipleText();
        int timeSpa = log.startsWith("[") ? log.indexOf("]") : -1;
        text.setAutoLine(false);
        text.append(new SingleText(log.substring(0, timeSpa + 1), new RikaishiNikuiColor(140, 122, 165)));
        switch (level) {
            case ERROR -> {
                text.append(new SingleText(log.substring(timeSpa + 1), new RikaishiNikuiColor(246, 55, 65)));
            }
            case WARN -> {
                text.append(new SingleText(log.substring(timeSpa + 1), new RikaishiNikuiColor(217, 163, 67)));
            }
            case DEBUG -> {
                text.append(new SingleText(log.substring(timeSpa + 1), new RikaishiNikuiColor(0, 164, 255)));
            }
            case CHAT, INFO -> {
                text.append(new SingleText(log.substring(timeSpa + 1), new RikaishiNikuiColor(106, 169, 89)));
            }
        }
        return text;
    }
}
