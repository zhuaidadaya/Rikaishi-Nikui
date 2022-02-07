package com.github.zhuaidadaya.rikaishinikui.handler.minecraft;

public enum MinecraftType {
    RELEASE("release"), SNAPSHOT("snapshot"), OLD_BETA("old_beta"), OLD_ALPHA("old_alpha"), UNKNOWN("unknown");

    private final String type;

    MinecraftType(String type) {
        this.type = type;
    }

    public static MinecraftType parse(String type) {
        return switch(type) {
            case "release" -> RELEASE;
            case "snapshot" -> SNAPSHOT;
            case "old_beta" -> OLD_BETA;
            case "old_alpha" -> OLD_ALPHA;
            default -> UNKNOWN;
        };
    }

    public String getType() {
        return type;
    }
}
