package com.github.zhuaidadaya.rikaishinikui.handler.file;

import com.github.zhuaidadaya.utils.resource.Resources;

import java.io.*;
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
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(information);
            writer.close();
        } catch (Exception e) {

        }
    }

    public static void write(File file, Collection<?> information) {
        try {
            Resources.createParent(file.getPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));

            for (Object o : information) {
                writer.write(o.toString() + "\n");
            }

            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
