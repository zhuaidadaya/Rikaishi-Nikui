package com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationTextManager;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

public class RikaishiNikuiSubmitter {
    private final RikaishiNikuiLogComponent component;
    private final PaginationTextManager<?> manager;

    public RikaishiNikuiSubmitter(PaginationTextManager<?> manager) {
        this.component = manager.getComponent();
        this.manager = manager;
    }

    public RikaishiNikuiLogComponent getComponent() {
        return component;
    }

    public PaginationTextManager<?> getManager() {
        return manager;
    }

    public void submit() {
        manager.submit();
    }
}
