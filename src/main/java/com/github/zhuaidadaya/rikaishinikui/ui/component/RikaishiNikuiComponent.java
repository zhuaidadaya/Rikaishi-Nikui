package com.github.zhuaidadaya.rikaishinikui.ui.component;

import com.github.zhuaidadaya.rikaishinikui.ui.color.RikaishiNikuiColor;
import com.github.zhuaidadaya.rikaishinikui.ui.style.RikaishiNikuiColorStyle;
import org.json.JSONObject;

public interface RikaishiNikuiComponent {
    JSONObject toJSONObject();

    void apply(JSONObject json);

    RikaishiNikuiComponent setBackground(RikaishiNikuiColor background);
    RikaishiNikuiComponent setForeground(RikaishiNikuiColor foreground);

    default RikaishiNikuiComponent setColor(RikaishiNikuiColor background, RikaishiNikuiColor foreground) {
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    default void setColor() {
        RikaishiNikuiColorStyle style = RikaishiNikuiColorStyle.getBlackStyle();
        setColor(style.getBackground(), style.getForeground());
    }
}
