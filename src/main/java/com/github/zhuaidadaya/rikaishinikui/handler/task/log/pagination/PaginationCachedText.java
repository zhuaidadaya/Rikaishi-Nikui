package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.util.UUID;

public abstract class PaginationCachedText<T, L> {
    public UUID cachedId;
    public Int2ObjectRBTreeMap<String> pages = new Int2ObjectRBTreeMap<>();
    public int pageSize = 1024 * 256;
    public String base;
    private PaginationTextManager<?> textManager;

    public PaginationCachedText() {
        init(UUID.randomUUID(), pageSize, null);
    }

    public PaginationCachedText(UUID id) {
        init(id, pageSize, null);
    }

    public PaginationCachedText(UUID id, int pageSize) {
        init(id, pageSize, null);
    }

    public PaginationCachedText(UUID id, int pageSize, String base) {
        init(id, pageSize, base);
    }

    public void init(UUID id, int pageSize, String base) {
        this.cachedId = id;
        this.pageSize = pageSize > 0 ? pageSize : this.pageSize;
        this.base = base;
    }

    public abstract void spawnTextManager(RikaishiNikuiLogComponent component);

    public void setComponent(RikaishiNikuiLogComponent component) {
        if (textManager == null) {
            spawnTextManager(component);
        }
        textManager.setComponent(component);
    }

    public PaginationTextManager<?> getTextManager() {
        return textManager;
    }

    public void setTextManager(PaginationTextManager<?> textManager) {
        this.textManager = textManager;
    }

    public abstract void clear();

    public abstract T read();

    public abstract T read(int index);

    public abstract void append(L log);

    public abstract void cache();
}
