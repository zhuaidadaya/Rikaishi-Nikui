package com.github.zhuaidadaya.rikaishinikui.handler.times;

public class TimeUtil {
    public static long currentMillions() {
        return System.currentTimeMillis();
    }

    public static long processedTime(long start) {
        return currentMillions() - start;
    }
}
