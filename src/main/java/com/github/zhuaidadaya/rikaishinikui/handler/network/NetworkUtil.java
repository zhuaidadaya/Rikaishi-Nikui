package com.github.zhuaidadaya.rikaishinikui.handler.network;

import com.github.zhuaidadaya.rikaishinikui.handler.resource.Resources;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
    public static void main(String[] args) {
        try {
            System.out.println(
                    NetworkUtil.downloadToStringBuilder(
                            "http://yysk.yitzu.cn.qingf.top/api/x/fanyi.php?msg=%E4%BD%A0%E5%A5%BD"
                    )
            );
        } catch (Exception e) {

        }
    }

    public static HttpURLConnection getHttp(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        return connection;
    }

    public static StringBuilder downloadToStringBuilder(String url) {
        return EntrustParser.trying(() -> {
            StringBuilder builder = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                connection = getHttp(url);
                connection.setRequestProperty("Charset", "UTF-8");
            } catch (Exception e) {

            }

            BufferedReader br;
            if (connection != null) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new FileReader(url));
            }
            String cache;
            while ((cache = br.readLine()) != null) {
                builder.append(cache).append("\n");
            }
            br.close();

            return builder;
        }, StringBuilder::new);
    }

    public static void downloadToFile(String url, String filePath) throws Exception {
        HttpURLConnection connection = null;
        try {
            connection = getHttp(url);
            connection.setRequestProperty("Charset", "UTF-8");
        } catch (Exception e) {

        }

        BufferedInputStream br;
        if (connection != null) {
            br = new BufferedInputStream(connection.getInputStream());
        } else {
            br = new BufferedInputStream(new FileInputStream(url));
        }

        Resources.createParent(filePath);

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] buf = new byte[8192];
        int length;
        while ((length = br.read(buf)) >= 0) {
            out.write(buf, 0, length);
        }
        br.close();
        out.close();
    }
}
