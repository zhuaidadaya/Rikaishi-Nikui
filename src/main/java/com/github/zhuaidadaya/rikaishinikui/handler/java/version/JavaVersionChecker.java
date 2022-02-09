package com.github.zhuaidadaya.rikaishinikui.handler.java.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JavaVersionChecker {
    public JavaVersion check(String path) {
        try {
            String command = "\"" + path + "\" -version";
            StringBuilder builder = new StringBuilder();
            String cache;
            Process java = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(java.getErrorStream()));
            while((cache = br.readLine()) != null) {
                builder.append(cache).append("\n");
            }

            String verId = builder.substring(builder.indexOf("\"") + 1, builder.lastIndexOf("\""));
            return new JavaVersion(verId, builder.toString().contains("64-Bit"));
        } catch (Exception e) {
            return JavaVersion.unknownJava();
        }
    }
}
