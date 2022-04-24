package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

public class PaginationStringTextManager extends PaginationTextManager<String, String> {
    public PaginationStringTextManager(PaginationCachedText<String, ?> cachedText, RikaishiNikuiLogComponent component) {
        super(cachedText, component);
    }

    public void submit(String submit) {
        component.appendText(new SingleText(submit), false);
        update();
    }

    public void cover() {
        component.setText(new SingleText(get()));
        update();
    }
}
