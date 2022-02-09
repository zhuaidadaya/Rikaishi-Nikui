package com.github.zhuaidadaya.rikaishinikui.handler.java.version;

public class JavaVersion {
    private String id;
    private boolean is64Bit;

    public JavaVersion(String id,boolean is64Bit) {
        this.id = id;
        this.is64Bit = is64Bit;
    }

    public static JavaVersion unknownJava() {
        return new JavaVersion("unknown", true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs64Bit() {
        return is64Bit;
    }

    public void setIs64Bit(boolean is64Bit) {
        this.is64Bit = is64Bit;
    }
}
