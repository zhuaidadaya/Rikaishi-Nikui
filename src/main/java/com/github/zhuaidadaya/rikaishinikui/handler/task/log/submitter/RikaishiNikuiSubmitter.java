package com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter;

import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiTextComponent;

import java.util.Collection;

public class RikaishiNikuiSubmitter {
    private final RikaishiNikuiTextComponent component;

    public RikaishiNikuiSubmitter(RikaishiNikuiTextComponent component) {
        this.component = component;
    }

    public void submit(String log) {
        submit(new SingleText(log));
    }

    public void submit(SingleText log) {
        component.setText(log);
        component.updateText();
        component.setCaretPosition(log.getText().length());
    }

    public void submit(Collection<SingleText> logs) {
        int caret = 0;
        component.setText(new SingleText(""));
        for (SingleText text : logs) {
            SingleText text1 = new SingleText(text);
            component.appendText(text1.append("\n"), false);
            caret += text1.getText().length();
        }
        component.updateText();
        component.setCaretPosition(caret);
    }
}
