package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.util.UUID;

public abstract class PaginationCachedLog<T, L> {
    public final UUID cachedId;
    public Int2ObjectRBTreeMap<String> pages = new Int2ObjectRBTreeMap<>();
    public int pageSize = 1024 * 256;
    public String base;

    public PaginationCachedLog(UUID id) {
        cachedId = id;
    }

    public PaginationCachedLog(UUID id, int pageSize) {
        this.cachedId = id;
        this.pageSize = pageSize > 0 ? pageSize : this.pageSize;
    }

    public PaginationCachedLog(UUID id, int pageSize, String base) {
        this.cachedId = id;
        this.pageSize = pageSize > 0 ? pageSize : this.pageSize;
        this.base = base;
    }

    public abstract void clear();

    public abstract T read();

    public abstract T read(int index);

    public abstract void append(L log);

    public abstract void cache();
}
