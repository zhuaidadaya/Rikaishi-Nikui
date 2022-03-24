package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.text.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

import java.util.Collection;

public class PaginationMultipleTextManager extends PaginationTextManager<Collection<MultipleText>> {
    public PaginationMultipleTextManager(PaginationCachedText<Collection<MultipleText>, ?> cachedText, RikaishiNikuiLogComponent component) {
        super(cachedText, component);
    }

    public void submit() {
        Collection<MultipleText> logs = get();
        int caret = 0;
        component.setText(new SingleText(""));
        for (MultipleText text : logs) {
            MultipleText text1 = new MultipleText(text.toJSONObject());
            component.appendText(text1, false);
            if (!text.isAutoLine()) {
                component.appendText(new SingleText("\n"), false);
                caret += 1;
            }
            caret += text1.getLength();
        }
        component.updateText();
        component.setCaretPosition(caret);
    }
}
