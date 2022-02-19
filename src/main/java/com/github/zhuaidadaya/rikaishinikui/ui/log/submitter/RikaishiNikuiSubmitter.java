package com.github.zhuaidadaya.rikaishinikui.ui.log.submitter;

import com.github.zhuaidadaya.rikaishinikui.language.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiTextComponent;

public class RikaishiNikuiSubmitter {
    private final RikaishiNikuiTextComponent component;

    public RikaishiNikuiSubmitter(RikaishiNikuiTextComponent component) {
        this.component = component;
    }

    public void submit(String log) {
        component.setText(new Text(log));
        component.updateText();
        component.setCaretPosition(log.length());
    }
}
