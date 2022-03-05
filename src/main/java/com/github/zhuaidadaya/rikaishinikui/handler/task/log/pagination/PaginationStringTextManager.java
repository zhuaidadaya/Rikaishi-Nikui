package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

public class PaginationStringTextManager extends PaginationTextManager<String> {
    public PaginationStringTextManager(PaginationCachedText<String, ?> cachedText, RikaishiNikuiLogComponent component) {
        super(cachedText, component);
    }

    public void submit() {
        SingleText log = new SingleText(get());
        component.setText(log);
        component.updateText();
        component.setCaretPosition(log.length());
    }
}
