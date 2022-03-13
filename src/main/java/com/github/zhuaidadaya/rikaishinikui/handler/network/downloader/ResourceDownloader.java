package com.github.zhuaidadaya.rikaishinikui.handler.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.file.checker.FileCheckUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ResourceDownloader {
    private final Object2ObjectRBTreeMap<String, FileDownloader> progresses = new Object2ObjectRBTreeMap<>();
    private ObjectArrayList<FileDownloader> activeDownloads = new ObjectArrayList<>();
    private boolean running = true;

    public void downloadFile(NetworkFileInformation file, int threads) {
        downloadFile(file, threads, Thread.currentThread().getName());
    }

    public void downloadFile(NetworkFileInformation file, int threads, String threadName) {
        File target = new File(file.getPath());
        if (file.getSha1().equals("") || !file.getSha1().equals(FileCheckUtil.sha1(target))) {
            target.delete();
            FileDownloader downloader = new FileDownloader();
            progresses.put(threadName, downloader);
            activeDownloads.add(downloader);
            try {
                downloader.downloadWithThreadPool(file.getUrl(), file.getPath(), threads);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progresses.remove(threadName);
            activeDownloads.remove(downloader);
        }
    }

    public void downloadFile(String url, String filePath) {
        downloadFile(new NetworkFileInformation(url, filePath), -1);
    }

    public void downloadFile(String url, String filePath, String sha1) throws Exception {
        downloadFile(url, filePath, sha1, Thread.currentThread().getName());
    }

    public void downloadFile(String url, String filePath, String sha1, String threadName) throws Exception {
        if (FileCheckUtil.sha1(new File(filePath)).equals(sha1)) {
            return;
        }
        downloadFile(url, filePath);
    }

    public void downloadFile(String url, String filePath, int threads, String sha1) {
        downloadFile(new NetworkFileInformation(url, filePath, sha1), threads);
    }

    public StringBuilder downloadFileToStringBuilder(String url) {
        return new FileDownloader().downloadWithStringBuilder(url);
    }

    public void downloadFiles(List<NetworkFileInformation> files) {
        downloadFiles(files, Thread.currentThread().getName());
    }

    public void downloadFiles(Set<NetworkFileInformation> files) {
        downloadFiles(files, Thread.currentThread().getName());
    }

    public void downloadFiles(List<NetworkFileInformation> files, String threadName) {
        FileDownloader downloader = new FileDownloader();
        progresses.put(threadName, downloader);
        activeDownloads.add(downloader);
        try {
            downloader.downloadFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progresses.remove(threadName);
        activeDownloads.remove(downloader);
    }

    public void downloadFiles(Set<NetworkFileInformation> files, String threadName) {
        FileDownloader downloader = new FileDownloader();
        progresses.put(threadName, downloader);
        activeDownloads.add(downloader);
        try {
            downloader.downloadFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progresses.remove(threadName);
        activeDownloads.remove(downloader);
    }

    public LinkedHashMap<String, String> getProgress() {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (String s : progresses.keySet()) {
            result.put(s, progresses.get(s).getProgress());
        }
        return result;
    }

    public void stop() {
        running = false;
        for (FileDownloader downloader : activeDownloads) {
            downloader.cancel();
        }

        activeDownloads = new ObjectArrayList<>();
    }
}
