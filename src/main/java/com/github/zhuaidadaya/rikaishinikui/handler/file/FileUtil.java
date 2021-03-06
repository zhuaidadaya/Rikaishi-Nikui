package com.github.zhuaidadaya.rikaishinikui.handler.file;

import com.github.zhuaidadaya.rikaishinikui.handler.resource.Resources;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
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
                    Resources.createFile(targetFile.getAbsolutePath());
                    FastBufferedInputStream is = new FastBufferedInputStream(zipFile.getInputStream(entry));
                    FastBufferedOutputStream fos = new FastBufferedOutputStream(new FileOutputStream(targetFile));
                    int len;
                    byte[] buf = new byte[1024 * 1024];
                    while ((len = is.read(buf)) != - 1) {
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

    public static String strictRead(BufferedReader reader) {
        String cache;
        StringBuilder builder = new StringBuilder();
        try {
            while ((cache = reader.readLine()) != null)
                builder.append(cache).append("\n");
        } catch (Exception e) {
            return "";
        }
        return builder.delete(builder.length() - 1, builder.length()).toString();
    }

    public static String readZip(String zip, String inZipFile) throws RuntimeException {
        try {
            FastBufferedInputStream is = new FastBufferedInputStream(new ZipFile(zip).getInputStream(new ZipEntry(inZipFile)));
            StringBuilder builder = new StringBuilder();
            int length;
            byte[] buf = new byte[1024];
            while ((length = is.read(buf)) != - 1) {
                String s = new String(buf, 0, length, StandardCharsets.UTF_8);
                builder.append(s);
            }
            is.close();
            return builder.toString();
        } catch (Exception e) {

        }
        return "";
    }

    public static String read(BufferedReader reader) {
        String cache;
        StringBuilder builder = new StringBuilder();
        try {
            while ((cache = reader.readLine()) != null)
                builder.append(cache).append("\n");
        } catch (Exception e) {
            return "";
        }
        return builder.toString();
    }

    public static StringBuilder readAsStringBuilder(BufferedReader reader) {
        String cache;
        StringBuilder builder = new StringBuilder();
        try {
            while ((cache = reader.readLine()) != null)
                builder.append(cache).append("\n");
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
        Runtime.getRuntime().exec(new String[]{"explorer.exe", "\"" + s.replace("/", "\\") + "\""});
    }

    public static void openInNautilus(String s) throws IOException {
        Runtime.getRuntime().exec(new String[]{"nautilus", s});
    }

    public static String absPath(String path) {
        return new File(path).getAbsolutePath();
    }

    public static void clone(String from, String to) {
        for (File f : Objects.requireNonNull(new File(from).listFiles())) {
            try {
                if (f.isDirectory()) {
                    if (Objects.requireNonNull(f.listFiles()).length > 0) {
                        clone(from + "/" + f.getName(), to + "/" + f.getName());
                    }
                } else if (f.isFile()) {
                    copy(f.getAbsolutePath(), to + "/" + f.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void copy(String from, String to) throws Exception {
        Resources.createFile(to);
        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(from));
        FastBufferedOutputStream output = new FastBufferedOutputStream(new FileOutputStream(to));
        byte[] buff = new byte[8192];
        int length;
        while ((length = input.read(buff, 0, buff.length)) != - 1) {
            output.write(buff, 0, length);
        }
        input.close();
        output.close();
    }
}
