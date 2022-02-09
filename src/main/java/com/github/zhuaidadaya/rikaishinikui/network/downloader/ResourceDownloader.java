package com.github.zhuaidadaya.rikaishinikui.network.downloader;

import com.github.zhuaidadaya.utils.file.NetworkFileUtil;
import com.github.zhuaidadaya.utils.file.checker.FileCheckUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ResourceDownloader {
    private Set<FileDownloader> activeDownloads = new HashSet<>();
    private boolean running = true;

    public void downloadFile(NetworkFileInformation file, int threads) throws Exception {
        File target = new File(file.getPath());
        if(file.getSha1().equals("") || ! file.getSha1().equals(FileCheckUtil.sha1(target))) {
            target.delete();
            FileDownloader downloader = new FileDownloader();
            activeDownloads.add(downloader);
            try {
                if(threads == 0) {
                    downloader.downloadWithBUf(file.getUrl(), file.getPath());
                } else {
                    downloader.downloadWithThreadPool(file.getUrl(), file.getPath(), threads);
                }
            } catch (Exception e) {

            }
            activeDownloads.remove(downloader);
        }
    }

    public void downloadFile(String url, String filePath) throws Exception {
        downloadFile(new NetworkFileInformation(url, filePath), - 1);
    }

    public void downloadFile(String url, String filePath, String sha1) throws Exception {
        if(FileCheckUtil.sha1(new File(filePath)).equals(sha1)) {
            return;
        }
        downloadFile(url, filePath);
    }

    public void downloadFile(String url, String filePath, int threads, String sha1) throws Exception {
        downloadFile(new NetworkFileInformation(url, filePath, sha1), threads);
    }

    public StringBuilder downloadFileToStringBuilder(String url) {
        return new FileDownloader().downloadWithStringBuilder(url);
    }

    public void downloadFiles(Set<NetworkFileInformation> files) throws IOException {
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
