package com.github.zhuaidadaya.rikaishinikui.network;

import com.github.zhuaidadaya.rikaishinikui.handler.minecraft.parser.*;
import com.github.zhuaidadaya.utils.file.checker.FileCheckUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ResourceDownloader {
    public void downloadFile(String url, String filePath, int threads) throws Exception {
        new File(filePath).delete();
        new FileDownloader().downloadWithThreadPool(url, filePath, threads);
    }

    public void downloadFile(String url, String filePath) throws Exception {
        downloadFile(url, filePath, - 1);
    }

    public void downloadFile(String url, String filePath, String sha1) throws Exception {
        if(FileCheckUtil.sha1(new File(filePath)).equals(sha1)) {
            return;
        }
        downloadFile(url, filePath);
    }

    public void downloadFile(String url, String filePath, int threads, String sha1) throws Exception {
        if(FileCheckUtil.sha1(new File(filePath)).equals(sha1)) {
            return;
        }
        downloadFile(url, filePath, threads);
    }

    public StringBuilder downloadFileToStringBuilder(String url) {
        return new FileDownloader().downloadWithStringBuilder(url);
    }

    public void downloadFiles(Set<NetworkFileInformation> files) throws IOException {
        new FileDownloader().downloadFiles(files);
    }
}
