package com.github.zhuaidadaya.rikaishinikui.network.downloader;

import com.github.zhuaidadaya.utils.file.FileUtil;
import com.github.zhuaidadaya.utils.file.NetworkFileUtil;
import com.github.zhuaidadaya.utils.file.checker.FileCheckUtil;
import com.github.zhuaidadaya.utils.resource.Resources;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {
    private int threads = 1;
    private int lastThreads = 0;
    private int files = 0;
    private int lastFiles = 0;
    private int downloadingFiles = 0;
    private boolean running = true;

    public StringBuilder downloadWithStringBuilder(String url) {
        return NetworkFileUtil.downloadToStringBuilder(url);
    }

    public void downloadFiles(Set<NetworkFileInformation> files) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        this.files = files.size();
        this.lastFiles = files.size();

        for(NetworkFileInformation information : files) {
            if(! running) {
                break;
            }
            downloadingFiles++;
            if(! FileCheckUtil.sha1(information.getPath()).equals(information.getSha1())) {
                while(downloadingFiles > 128) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {

                    }
                }
                Thread thread = new Thread(() -> {
                    try {
                        int tryCount = 0;
                        while(true) {
                            tryCount++;
                            try {
                                String url = information.getUrl();
                                String path = information.getPath();
                                if(information.getSize() > 1024 * 1024 * 3)
                                    downloadWithThreadPool(url, path, - 1);
                                else
                                    downloadWithBUf(url, path);
                                doneFile();
                                break;
                            } catch (Exception e) {

                            }

                            if(tryCount == 5) {
                                failFile();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        failFile();
                    }
                });
                threadPool.execute(thread);
            } else {
                doneFile();
            }
        }

        threadPool.shutdown();
        while(lastFiles != 0 & running) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {

            }
        }
    }

    public void downloadWithBUf(String url, String filePath) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = NetworkFileUtil.getHttp(url);
            connection.setRequestProperty("Charset", "UTF-8");
        } catch (Exception e) {

        }

        BufferedInputStream br;
        if(connection != null) {
            br = new BufferedInputStream(connection.getInputStream());
        } else {
            br = new BufferedInputStream(new FileInputStream(url));
        }

        File file = new File(filePath);

        Resources.createParent(filePath);
        if(! file.exists())
            file.createNewFile();

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buf = new byte[8192];
        int length;
        while((length = br.read(buf)) >= 0) {
            if(running) {
                out.write(buf, 0, length);
            } else {
                break;
            }
        }
        br.close();
        out.close();

        if(! running) {
            file.delete();
        }
    }

    public void downloadWithThreadPool(String url, String filePath, int threads) throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        long length = getContentLength(url);

        if(threads == - 1) {
            threads = (int) (length / 1024 / 1024 / 2.5);
        }

        threads = Math.max(1, threads);

        this.threads = threads;
        this.lastThreads = threads;

        for(int i = 0; i < threads; i++) {
            if(! running) {
                fail();
                break;
            }
            long start = i * length / threads;
            long end = (i + 1) * length / threads - 1;
            if(i == threads - 1) {
                end = length;
            }
            DownloadWithRange download = new DownloadWithRange(url, filePath, start, end, this);
            threadPool.execute(download);
        }
        threadPool.shutdown();

        while(lastThreads != 0) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {

            }
        }
    }

    public void cancel() {
        running = false;
    }

    public void done() {
        synchronized(this) {
            lastThreads--;
        }
    }

    public void fail() {
        synchronized(this) {
            lastThreads--;
        }
    }

    public void doneFile() {
        synchronized(this) {
            lastFiles--;
            downloadingFiles--;
        }
    }

    public void failFile() {
        synchronized(this) {
            lastFiles--;
            downloadingFiles--;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public long getContentLength(String urlLocation) throws IOException {
        URL url = null;
        if(urlLocation != null) {
            url = new URL(urlLocation);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");

        return conn.getContentLength();
    }

    public static final class DownloadWithRange implements Runnable {
        private final String url;
        private final String filePath;
        private final long start;
        private final long end;
        private final FileDownloader parent;

        public DownloadWithRange(String url, String filePath, long start, long end, FileDownloader parent) {
            this.url = url;
            this.filePath = filePath;
            this.start = start;
            this.end = end;
            this.parent = parent;
        }

        public void run() {
            int tryCount = 0;
            while(true) {
                tryCount++;
                try {
                    HttpURLConnection conn = NetworkFileUtil.getHttp(url);
                    conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setReadTimeout(20000);

                    Resources.createParent(filePath);

                    File file = new File(filePath);
                    RandomAccessFile out = new RandomAccessFile(file, "rw");
                    out.seek(start);

                    BufferedInputStream br = new BufferedInputStream(conn.getInputStream());
                    byte[] buf = new byte[8192];
                    int length;
                    while((length = br.read(buf)) >= 0) {
                        if(parent.isRunning()) {
                            out.write(buf, 0, length);
                        } else {
                            break;
                        }
                    }
                    br.close();
                    out.close();

                    if(parent.isRunning()) {
                        parent.done();
                    } else {
                        file.delete();
                        parent.fail();
                    }
                    break;
                } catch (Exception e) {

                }

                if(tryCount == 5) {
                    parent.fail();
                }
            }
        }
    }
}
