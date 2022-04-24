package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntrustParser {
    public static <T> T getNotNull(T target, @NotNull T defaultValue) {
        if (target == null) {
            return defaultValue;
        }
        return target;
    }

    public static <T> T nullRequires(T target, Supplier<T> action) {
        return action.get();
    }

    public static <T> T build(Supplier<T> obj) {
        return obj.get();
    }

    public static <T> T operation(T target, Consumer<T> action) {
        action.accept(target);
        return target;
    }

    public static <T> T create(Supplier<T> action) {
        return action.get();
    }

    public static <T> T tryCreate(Supplier<T> action, T defaultValue) {
        try {
            return action.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> T trying(Supplier<T> action) {
        try {
            return action.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T trying(Supplier<T> action, Supplier<T> actionWhenException) {
        try {
            return action.get();
        } catch (Exception e) {
            return actionWhenException.get();
        }
    }

    public static <T> T operation(Supplier<T> target) {
        return target.get();
    }

    public static <T> T tryInstance(Class<T> clazz, Supplier<T> target, T defaultValue) {
        try {
            return target.get();
        } catch (Throwable e) {
            return defaultValue;
        }
    }
}
