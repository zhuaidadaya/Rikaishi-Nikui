package com.github.zhuaidadaya.utils.times;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Times {
    public static String getTime(TimeType timeType) {
        return getTime(timeType,"");
    }

    public static String getTime(TimeType timeType, String customFormat) {
        switch(timeType) {
            case ALL -> {
                return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
            }
            case AS_SECOND -> {
                return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            }
            case AS_MINUTE -> {
                return new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
            }
            case AS_CLOCK -> {
                return new SimpleDateFormat("yyyy-MM-dd_HH").format(new Date());
            }
            case AS_DAY -> {
                return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
            case AS_MONTH -> {
                return new SimpleDateFormat("yyyy-MM").format(new Date());
            }
            case AS_YEAR -> {
                return new SimpleDateFormat("yyyy").format(new Date());
            } case LOG -> {
                return new SimpleDateFormat("[HH:mm:ss] ").format(new Date());
            }
            case LONG_LOG -> {
                return new SimpleDateFormat("[yyyy-MM-dd+HH:mm:ss:SSS] ").format(new Date());
            }
            case CUSTOM -> {
                return new SimpleDateFormat(customFormat).format(new Date());
            }
            default -> {
                return "";
            }
        }
    }
}
