package com.github.zhuaidadaya.rikaishinikui.ui;

import org.json.JSONObject;

import java.awt.*;

public interface RikaishiNikuiComponent {
    JSONObject toJSONObject();
    void apply(JSONObject json);
}
