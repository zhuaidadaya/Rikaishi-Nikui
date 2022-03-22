package com.github.zhuaidadaya.rikaishinikui.handler.file.checker;

import com.github.zhuaidadaya.rikaishinikui.handler.identifier.RandomIdentifier;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileCheckUtil {
    public static String sha1(String url) {
        return sha1(new File(url));
    }

    public static String sha1(File file) {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            MappedByteBuffer mappedByteBuffer;
            long bufferSize = 1024 * 128;
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
            String hexString;
            for(byte b : messageDigest.digest()) {
                hexString = Integer.toHexString(b & 255);
                if(hexString.length() < 2) {
                    builder.append(0);
                }
                builder.append(hexString);
            }
            return builder.toString();
        } catch (Exception e) {
            return RandomIdentifier.randomIdentifier();
        }
    }
}
