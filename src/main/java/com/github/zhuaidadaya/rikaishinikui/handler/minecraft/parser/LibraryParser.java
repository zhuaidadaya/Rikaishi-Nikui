package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

public abstract class LibraryParser extends Parser {
    public abstract ArtifactParser getLibrary();

    public abstract String getUrl();

    public abstract String getPath();

    public abstract void setArea(String area);

    public abstract String getAbsolutePath();

    public abstract String getName();
}
