package com.github.zhuaidadaya.rikaishinikui.ui.component;

import com.github.zhuaidadaya.rikaishinikui.language.Text;

public interface RikaishiNikuiTextComponent {
    String getText();
    void setText(Text text);
    void updateText();
    void setCaretPosition(int position);
}
