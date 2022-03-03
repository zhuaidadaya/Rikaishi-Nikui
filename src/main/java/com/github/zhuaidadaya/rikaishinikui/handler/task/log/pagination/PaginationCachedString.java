package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.UUID;

public class PaginationCachedString extends PaginationCachedLog<String, String> {
    public StringBuilder log = new StringBuilder();

    public PaginationCachedString(UUID id) {
        super(id);
    }

    public PaginationCachedString(UUID id, int pageSize) {
        super(id, pageSize);
    }

    public PaginationCachedString(UUID id, int pageSize, String base) {
        super(id, pageSize, base);
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void append(String log) {
        if(this.log.length() > pageSize) {
            cache();
        }
        this.log.append(log).append("\n");
    }

    public void cache() {
        String cache = base + "/logs/cached/" + cachedId.toString() + "/cached-" + (pages.size() + 1) + ".log";
        FileUtil.write(new File(cache), log);
        pages.put(pages.size() + 1, cache);
        log = new StringBuilder();
    }

    public String read(int index) {
        try {
            return FileUtil.readAsStringBuilder(new BufferedReader(new FileReader(pages.get(Math.min(pages.size(), index))))).toString();
        } catch (Exception e) {

        }
        return "";
    }

    public StringBuilder readAsStringBuilder(int index) {
        try {
            return FileUtil.readAsStringBuilder(new BufferedReader(new FileReader(pages.get(Math.min(pages.size(), index)))));
        } catch (Exception e) {

        }
        return new StringBuilder("");
    }

    public String read() {
        return log.toString();
    }

    public StringBuilder readAsStringBuilder() {
        return log;
    }

    public int getLastPage() {
        return pages.lastIntKey() + 1;
    }

    public int getCurrentPage() {
        return pages.lastIntKey();
    }

    public void clear() {
        String cache = "logs/cached/" + cachedId.toString() + "/";
        try {
            FileUtil.deleteFiles(cache);
        } catch (Exception e) {

        }
        pages = new Int2ObjectRBTreeMap<>();
        log = new StringBuilder();
    }
}
