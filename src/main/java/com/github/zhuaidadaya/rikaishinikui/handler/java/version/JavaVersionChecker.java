package com.github.zhuaidadaya.rikaishinikui.handler.java.version;

import com.github.zhuaidadaya.rikaishinikui.handler.java.recorder.JavaVersionInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class JavaVersionChecker {
    public JavaVersionInformation check(String path) {
        try {
            if(path.equals("path.java"))
                path = "java";

            File file1 = new File(path);
            File file2 = new File(path + "/java.exe");
            File file3 = new File(path + "/bin/java.exe");

            boolean filter1 = new File(path).isFile();
            boolean filter2 = new File(path + "/java.exe").isFile();
            boolean filter3 = new File(path + "/bin/java.exe").isFile();

            if(path.equals("java") || filter1 || filter2 || filter3) {
                if(!path.equals("java")) {
                    if(filter1) {
                        path = file1.getAbsolutePath();
                    } else if(filter2) {
                        path = file2.getAbsolutePath();
                    } else {
                        path = file3.getAbsolutePath();
                    }
                }
                String command = "\"" + path + "\" -version";
                StringBuilder builder = new StringBuilder();
                String cache;
                Process java = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(java.getErrorStream()));
                String version = "unknown";
                String type = "unknown";
                int line = 0;
                while((cache = br.readLine()) != null) {
                    line++;
                    builder.append(cache).append("\n");
                    if(line == 3) {
                        if(cache.contains("Java HotSpot(TM)")) {
                            type = "Java HotSpot";
                        } else {
                            type = "Open JDK";
                        }
                    }
                    if(line == 2) {
                        version = cache.substring(cache.lastIndexOf("(") + 1, cache.lastIndexOf(")"));
                    }
                }

                if(line < 3)
                    throw new Exception();

                String verId = builder.substring(builder.indexOf("\"") + 1, builder.lastIndexOf("\""));
                JavaVersionInformation information = new JavaVersionInformation(path, verId, builder.toString().contains("64-Bit"), version, type);
                if(path.equals("java")) {
                    information.setStatus("status.default");
                    information.setPath("path.java");
                } else {
                    information.setStatus("status.custom");
                    information.setPath(new File(path).getAbsolutePath());
                }
                return information;
            } else {
                return JavaVersionInformation.unknownJava();
            }
        } catch (Exception e) {
            return JavaVersionInformation.unknownJava();
        }
    }
}
