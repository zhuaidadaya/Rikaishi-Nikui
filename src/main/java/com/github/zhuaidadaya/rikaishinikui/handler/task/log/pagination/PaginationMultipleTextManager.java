package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.text.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

import java.util.Collection;

public class PaginationMultipleTextManager extends PaginationTextManager<Collection<MultipleText>, MultipleText> {
    public PaginationMultipleTextManager(PaginationCachedText<Collection<MultipleText>, ?> cachedText, RikaishiNikuiLogComponent component) {
        super(cachedText, component);
    }

    public void submit(MultipleText submit) {
        component.appendText(submit, false);
        if (!submit.isAutoLine()) {
            component.appendText(new SingleText("\n"), false);
        }
    }

    public void cover() {
        Collection<MultipleText> logs = get();
        component.setText(new SingleText(""));
        for (MultipleText text : logs) {
            MultipleText text1 = new MultipleText(text.toJSONObject());
            component.appendText(text1, false);
            if (!text.isAutoLine()) {
                component.appendText(new SingleText("\n"), false);
            }
        }
        update();
    }
}
