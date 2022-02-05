package com.github.zhuaidadaya.utils.integer;

import org.json.JSONObject;

public class IntegerUtil {
    public static int getNotNullInt(Object o, int defaultInt) {
        try {
            return Integer.parseInt(o.toString());
        } catch(Exception ex) {
            return defaultInt;
        }
    }

    public static int getIntFromJSON(JSONObject json,String key, int defaultInt) {
        try {
            return Integer.parseInt(json.get(key).toString());
        } catch(Exception ex) {
            return defaultInt;
        }
    }
}
