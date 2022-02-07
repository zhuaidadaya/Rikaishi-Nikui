package com.github.zhuaidadaya.utils.file.checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class FileCheckUtil {
    public static String sha1(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buf = new byte[1024 * 1024 * 10];
            int length;
            while((length = in.read(buf)) > 0) {
                digest.update(buf, 0, length);
            }
            StringBuilder sha1 = new StringBuilder(new BigInteger(1, digest.digest()).toString(16));
            length = 40 - sha1.length();
            if(length > 0) {
                for(int i = 0; i < length; i++) {
                    sha1.insert(0, "0");
                }
            }
            return sha1.toString();
        } catch (Exception e) {

        }
        return "";
    }

    public static String sha1(String url) {
        return sha1(new File(url));
    }
}
