package com.github.zhuaidadaya.utils.file;

import com.github.zhuaidadaya.utils.resource.Resources;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkFileUtil {
    public static HttpURLConnection getHttp(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        return connection;
    }

    public static StringBuilder downloadToStringBuilder(String url) {
        try {
            StringBuilder builder = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                connection = getHttp(url);
                connection.setRequestProperty("Charset", "UTF-8");
            } catch (Exception e) {

            }

            BufferedReader br;
            if(connection != null) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new FileReader(url));
            }
            String cache;
            while((cache = br.readLine()) != null) {
                builder.append(cache).append("\n");
            }
            br.close();

            return builder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StringBuilder();
    }

    public static void downloadToFile(String url, String filePath) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = getHttp(url);
            connection.setRequestProperty("Charset", "UTF-8");
        } catch (Exception e) {

        }

        BufferedInputStream br;
        if(connection != null) {
            br = new BufferedInputStream(connection.getInputStream());
        } else {
            br = new BufferedInputStream(new FileInputStream(url));
        }

        Resources.createParent(filePath);

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] buf = new byte[8192];
        int length;
        while((length = br.read(buf)) >= 0) {
            out.write(buf, 0, length);
        }
        br.close();
        out.close();
    }
}