package com.github.zhuaidadaya.utils.file.checker;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.UUID;

public class FileCheckUtil {
    public static String sha1(String url) {
        return sha1(new File(url));
    }

    public static String sha1(File file) {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            MappedByteBuffer mappedByteBuffer;
            long bufferSize = 1024 * 256;
            long fileLength = file.length();
            long lastBuffer = fileLength % bufferSize;
            long bufferCount = fileLength / bufferSize;
            for(int b = 0; b < bufferCount; b++) {
                mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, b * bufferSize, bufferSize);
                messageDigest.update(mappedByteBuffer);
            }
            if(lastBuffer != 0) {
                mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, bufferCount * bufferSize, lastBuffer);
                messageDigest.update(mappedByteBuffer);
            }
            byte[] digest = messageDigest.digest();
            String hexString;
            for(byte b : digest) {
                hexString = Integer.toHexString(b & 0xFF);
                if(hexString.length() < 2) {
                    builder.append(0);
                }
                builder.append(hexString);
            }
            return builder.toString();
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }
}
