package com.github.zhuaidadaya.rikaishinikui.ui.component;

import org.json.JSONObject;

public interface RikaishiNikuiComponent {
    JSONObject toJSONObject();
    void apply(JSONObject json);
}
