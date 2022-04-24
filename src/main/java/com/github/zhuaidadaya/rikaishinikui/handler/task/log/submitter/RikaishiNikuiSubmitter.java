package com.github.zhuaidadaya.rikaishinikui.handler.task.log.submitter;

import com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination.PaginationTextManager;
import com.github.zhuaidadaya.rikaishinikui.handler.text.Text;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

public class RikaishiNikuiSubmitter {
    private final RikaishiNikuiLogComponent component;
    private final PaginationTextManager<? ,Text> manager;

    public RikaishiNikuiSubmitter(PaginationTextManager<?,Text> manager) {
        this.component = manager.getComponent();
        this.manager = manager;
    }

    public RikaishiNikuiLogComponent getComponent() {
        return component;
    }

    public PaginationTextManager<?,Text> getManager() {
        return manager;
    }

    public void cover() {
        manager.cover();
    }

    public void submit(Text submit) {
        manager.submit(submit);
    }
}
