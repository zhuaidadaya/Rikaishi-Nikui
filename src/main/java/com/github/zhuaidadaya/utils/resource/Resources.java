package com.github.zhuaidadaya.utils.resource;

import java.io.*;
import java.net.URL;
import java.util.Iterator;


public class Resources extends Thread {
    public static InputStream getResource(String resource, Class<?> getC) {
        return getC.getResourceAsStream(resource);
    }

    public static URL getResourceByFile(String resource, Class<?> getC) {
        return getC.getResource(resource);
    }

    public static int getElementsCounter(Iterator<String> iterator) {
        int result = 0;
        while(iterator.hasNext()) {
            result += 1;
            iterator.next();
        }

        return result;
    }

    public static void createPath(String path) {
        if(! new File(path).isDirectory())
            new File(path).mkdirs();
    }

    public static void createParent(String path) {
        if(! new File(path).isDirectory())
            new File(new File(path).getParent()).mkdirs();
    }
}