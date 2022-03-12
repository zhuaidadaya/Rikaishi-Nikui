package com.github.zhuaidadaya.rikaishinikui.handler.minecraft;

public enum MinecraftLoaderType {
    VANILLA, FABRIC, FORGE;

    public static MinecraftLoaderType of(String str) {
        return switch (str.toLowerCase()) {
            case "fabric" -> FABRIC;
            case "forge" -> FORGE;
            default -> VANILLA;
        };
    }
}
