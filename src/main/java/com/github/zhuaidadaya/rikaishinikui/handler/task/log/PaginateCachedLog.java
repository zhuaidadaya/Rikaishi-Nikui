package com.github.zhuaidadaya.rikaishinikui.handler.task.log;

import com.github.zhuaidadaya.utils.file.FileUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.UUID;

public class PaginateCachedLog {
    private final UUID cachedId;
    private Int2ObjectRBTreeMap<String> pages = new Int2ObjectRBTreeMap<>();
    private int pageSize = 1024 * 256;
    private StringBuilder log = new StringBuilder();

    public PaginateCachedLog(UUID id) {
        this.cachedId = id;
    }

    public PaginateCachedLog(UUID id, int pageSize) {
        this.cachedId = id;
        this.pageSize = pageSize;
    }

    public void append(String log) {
        if(this.log.length() > pageSize) {
            cache();
        }
        this.log.append(log).append("\n");
    }

    public void cache() {
        String cache = "logs/cached/" + cachedId.toString() + "/cached-" + (pages.size() + 1) + ".log";
        FileUtil.write(new File(cache), log);
        pages.put(pages.size() + 1, cache);
        log = new StringBuilder();
    }

    public StringBuilder read(int index) {
        try {
            log = FileUtil.readAsStringBuilder(new BufferedReader(new FileReader(pages.get(Math.min(pages.size(), index)))));
        } catch (Exception e) {

        }
        return log;
    }

    public StringBuilder read() {
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
