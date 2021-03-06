package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

import java.util.Collection;

public class PaginationSingleTextManager extends PaginationTextManager<Collection<SingleText>, SingleText> {
    public PaginationSingleTextManager(PaginationCachedText<Collection<SingleText>, ?> cachedText, RikaishiNikuiLogComponent component) {
        super(cachedText, component);
    }

    public void submit(SingleText submit) {
        SingleText text1 = new SingleText(submit);
        component.appendText(text1.append("\n"), false);
        update();
    }

    public void cover() {
        Collection<SingleText> logs = get();
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
