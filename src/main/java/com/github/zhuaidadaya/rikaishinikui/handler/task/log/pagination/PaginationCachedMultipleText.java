package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.language.MultipleText;
import com.github.zhuaidadaya.rikaishinikui.language.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class PaginationCachedMultipleText extends PaginationCachedText<Collection<MultipleText>, MultipleText> {
    private Collection<MultipleText> texts = new LinkedHashSet<>();
    private int length = 0;

    public PaginationCachedMultipleText(UUID id) {
        super(id);
    }

    public PaginationCachedMultipleText(UUID id, int pageSize) {
        super(id, pageSize);
    }

    public PaginationCachedMultipleText(UUID id, int pageSize, String base) {
        super(id, pageSize, base);
    }

    public void spawnTextManager(RikaishiNikuiLogComponent component) {
        setTextManager(new PaginationMultipleTextManager(this,component));
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void append(MultipleText text) {
        if (this.length > pageSize) {
            cache();
        }
        length += text.getLength();
        this.texts.add(text);
    }

        public void cache() {
        String cache = (base == null ? "" : "/") + "logs/cached/" + cachedId.toString() + "/cached-" + (pages.size() + 1) + ".log";
        FileUtil.write(new File(cache), texts);
        pages.put(pages.size() + 1, cache);
        texts = new LinkedHashSet<>();
    }

    public Collection<MultipleText> read(int index) {
        Collection<MultipleText> texts = new LinkedHashSet<>();
        try {
            if (index != -1) {
                texts = textFormatter.formatMultipleTextsFromFile(new File(pages.get(Math.min(pages.size(), index))));
            } else {
                return read();
            }
        } catch (Exception e) {

        }
        return texts;
    }

    public Collection<MultipleText> read() {
        return texts;
    }

    public StringBuilder readStringBuilder(int index) {
        Collection<SingleText> texts = new LinkedHashSet<>();
        try {
            texts = textFormatter.formatSingTextsFromFile(new File(pages.get(Math.min(pages.size(), index))));
        } catch (Exception e) {

        }
        StringBuilder builder = new StringBuilder();
        for (SingleText text : texts) {
            builder.append(text.getText()).append("\n");
        }
        return builder;
    }

    public StringBuilder readStringBuilder() {
        StringBuilder builder = new StringBuilder();
        for (MultipleText text : texts) {
            builder.append(text.getText()).append("\n");
        }
        return builder;
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
        texts = new LinkedHashSet<>();
    }
}