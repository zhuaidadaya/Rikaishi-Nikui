package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;

public abstract class PaginationTextManager<T> {
    private int page = -1;
    private final PaginationCachedText<T, ?> cachedText;
    protected RikaishiNikuiLogComponent component;

    public PaginationTextManager(PaginationCachedText<T, ?> cachedText, RikaishiNikuiLogComponent component) {
        this.cachedText = cachedText;
        this.component = component;
    }

    public PaginationCachedText<T, ?> getCachedText() {
        return cachedText;
    }

    public RikaishiNikuiLogComponent getComponent() {
        return component;
    }

    public void setComponent(RikaishiNikuiLogComponent component) {
        this.component = component;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public T get() {
        return get(page);
    }

    public T get(int page) {
        return cachedText.read(page);
    }

    public abstract void submit();
}
