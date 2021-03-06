package com.github.zhuaidadaya.rikaishinikui.handler.language;

public enum Language {
    CHINESE(0, "Chinese"), ENGLISH(1, "English"), CHINESE_TW(2, "Chinese_tw"), AUTO(3, "Auto");

    private final int value;
    private final String name;

    Language(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Language of(String name) {
        Language language = null;
        switch(name.toLowerCase()) {
            case "chinese" -> language = CHINESE;
            case "english" -> language = ENGLISH;
            case "chinese_tw" -> language = CHINESE_TW;
            case "auto" -> language = AUTO;
        }
        return language;
    }

    public static String getName(Language language) {
        String name = "";
        switch(language) {
            case CHINESE -> name = "Chinese";
            case ENGLISH -> name = "English";
            case CHINESE_TW -> name = "Chinese_tw";
            case AUTO -> name = "Auto";
        }
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
