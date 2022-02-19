package com.github.zhuaidadaya.utils.string.checker;

public class StringCheckUtil {
    public static String getNotNull(String getReturn, String defaultReturn) {
        if(getReturn != null) {
            return getReturn;
        } else {
            return defaultReturn;
        }
    }
}
