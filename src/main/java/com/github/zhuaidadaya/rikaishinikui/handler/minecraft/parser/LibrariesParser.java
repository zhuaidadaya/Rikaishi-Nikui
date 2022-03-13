package com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser;

import com.github.zhuaidadaya.rikaishinikui.handler.network.downloader.NetworkFileInformation;

import java.util.Map;
import java.util.Set;

public abstract class LibrariesParser extends Parser {
    public abstract Map<String, ? extends LibraryParser> getLibraries();

    public abstract Set<NetworkFileInformation> getLibrariesDownloads();
}
