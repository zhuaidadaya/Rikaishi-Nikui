package com.github.zhuaidadaya.rikaishinikui.handler.file;

import com.github.zhuaidadaya.utils.resource.Resources;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    public static void deleteFiles(String path) {
        File file = new File(path);
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteFiles(f.getAbsolutePath());
            }
        }
        file.delete();
    }

    public static IllegalFileName legally(String name) {
        return new IllegalFileName(name);
    }

    public static void unzip(String zip, String path) throws RuntimeException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zip);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = path + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    File targetFile = new File(path + "/" + entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    FastBufferedInputStream is = new FastBufferedInputStream(zipFile.getInputStream(entry));
                    FastBufferedOutputStream fos = new FastBufferedOutputStream(new FileOutputStream(targetFile));
                    int len;
                    byte[] buf = new byte[1024 * 1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to unzip: " + zip, e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String read(BufferedReader reader) {
        String cache;
        StringBuilder builder = new StringBuilder();
        try {
            while ((cache = reader.readLine()) != null) builder.append(cache).append("\n");
        } catch (Exception e) {
            return "";
        }
        return builder.toString();
    }

    public static StringBuilder readAsStringBuilder(BufferedReader reader) {
        String cache;
        StringBuilder builder = new StringBuilder();
        try {
            while ((cache = reader.readLine()) != null) builder.append(cache).append("\n");
        } catch (Exception e) {

        }
        return builder;
    }

    public static void write(File file, StringBuilder information) {
        write(file, information.toString());
    }

    public static void write(File file, String information) {
        try {
            Resources.createParent(file.getPath());
            FastBufferedOutputStream writer = new FastBufferedOutputStream(new FileOutputStream(file));
            writer.write(information.getBytes());
            writer.close();
        } catch (Exception e) {

        }
    }

    public static void write(File file, Collection<?> information) {
        try {
            Resources.createParent(file.getPath());
            FastBufferedOutputStream writer = new FastBufferedOutputStream(new FileOutputStream(file, true));

            for (Object o : information) {
                writer.write((o.toString() + "\n").getBytes());
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openInBrowse(String url) throws IOException {
        URI uri = URI.create(url);
        Desktop dp = Desktop.getDesktop();
        if (dp.isSupported(Desktop.Action.BROWSE)) {
            dp.browse(uri);
        }
    }

    public static void openInExplorer(String s) throws IOException {
        Runtime.getRuntime().exec("explorer.exe \"" + s.replace("/", "\\") + "\"");
    }

    public static void openInNautilus(String s) throws IOException {
        Runtime.getRuntime().exec("nautilus " + s);
    }
}
