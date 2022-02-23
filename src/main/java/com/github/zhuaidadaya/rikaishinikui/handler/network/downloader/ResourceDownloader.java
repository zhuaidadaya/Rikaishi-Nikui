package com.github.zhuaidadaya.rikaishinikui.handler.network.downloader;

import com.github.zhuaidadaya.rikaishinikui.handler.file.checker.FileCheckUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ResourceDownloader {
    private Set<FileDownloader> activeDownloads = new HashSet<>();
    private boolean running = true;

    public void downloadFile(NetworkFileInformation file, int threads) {
        File target = new File(file.getPath());
        if(file.getSha1().equals("") || ! file.getSha1().equals(FileCheckUtil.sha1(target))) {
            target.delete();
            FileDownloader downloader = new FileDownloader();
            activeDownloads.add(downloader);
            try {
                downloader.downloadWithThreadPool(file.getUrl(), file.getPath(), threads);
            } catch (Exception e) {

            }
            activeDownloads.remove(downloader);
        }
    }

    public void downloadFile(String url, String filePath) {
        downloadFile(new NetworkFileInformation(url, filePath), - 1);
    }

    public void downloadFile(String url, String filePath, String sha1) throws Exception {
        if(FileCheckUtil.sha1(new File(filePath)).equals(sha1)) {
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

    public void downloadFiles(Set<NetworkFileInformation> files) {
        FileDownloader downloader = new FileDownloader();
        activeDownloads.add(downloader);
        try {
            downloader.downloadFiles(files);
        } catch (Exception e) {

        }
        activeDownloads.remove(downloader);
    }

    public void stop() {
        running = false;
        for(FileDownloader downloader : activeDownloads) {
            downloader.cancel();
        }

        activeDownloads = new HashSet<>();
    }
}
