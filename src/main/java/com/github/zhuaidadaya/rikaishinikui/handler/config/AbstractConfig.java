package com.github.zhuaidadaya.rikaishinikui.handler.config;

import org.json.JSONArray;
import org.json.JSONObject;

public interface AbstractConfig {
    Object getValue();
    String getString() ;

    int getInt();
    long getLong() ;
    boolean getBoolean() ;

    JSONObject getJSONObject() ;
    JSONArray getJSONArray();
}
