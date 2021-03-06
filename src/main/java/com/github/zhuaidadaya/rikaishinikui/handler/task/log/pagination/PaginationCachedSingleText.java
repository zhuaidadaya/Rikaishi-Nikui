package com.github.zhuaidadaya.rikaishinikui.handler.task.log.pagination;

import com.github.zhuaidadaya.rikaishinikui.handler.file.FileUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.text.SingleText;
import com.github.zhuaidadaya.rikaishinikui.ui.component.RikaishiNikuiLogComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

import static com.github.zhuaidadaya.rikaishinikui.storage.Variables.textFormatter;

public class PaginationCachedSingleText extends PaginationCachedText<Collection<SingleText>, SingleText> {
    private ObjectArrayList<SingleText> texts = new ObjectArrayList<>();
    private int length = 0;

    public PaginationCachedSingleText(UUID id) {
        super(id);
    }

    public PaginationCachedSingleText(UUID id, int pageSize) {
        super(id, pageSize);
    }

    public PaginationCachedSingleText(UUID id, int pageSize, String base) {
        super(id, pageSize, base);
    }

    public void spawnTextManager(RikaishiNikuiLogComponent component) {
        setTextManager(new PaginationSingleTextManager(this,component));
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void append(SingleText text) {
        if (this.length > pageSize) {
            cache();
        }
        length += text.length();
        this.texts.add(text);
    }

    public void cache() {
        String cache = (base == null ? "" : "/") + "logs/cached/" + cachedId.toString() + "/cached-" + (pages.size() + 1) + ".log";
        FileUtil.write(new File(cache), texts);
        pages.put(pages.size() + 1, cache);
        texts = new ObjectArrayList<>();
    }

    public ObjectArrayList<SingleText> read(int index) {
        ObjectArrayList<SingleText> texts = new ObjectArrayList<>();
        try {
            if (index != -1) {
                texts = textFormatter.formatSingTextsFromFile(new File(pages.get(Math.min(pages.size(), index))));
            } else {
                return read();
            }
        } catch (Exception e) {

        }
        return texts;
    }

    public ObjectArrayList<SingleText> read() {
        return texts;
    }

    public StringBuilder readStringBuilder(int index) {
        ObjectArrayList<SingleText> texts = new ObjectArrayList<>();
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
        for (SingleText text : texts) {
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
        texts = new ObjectArrayList<>();
    }
}
