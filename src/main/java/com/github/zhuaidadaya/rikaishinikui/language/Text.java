package com.github.zhuaidadaya.rikaishinikui.language;

import java.awt.*;

public interface Text {
    String getText();
    default Color getAwtColor() {
        return null;
    }
}
