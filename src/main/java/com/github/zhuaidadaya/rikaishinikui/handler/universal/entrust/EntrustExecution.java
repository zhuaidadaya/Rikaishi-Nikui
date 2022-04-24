package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntrustExecution {
    private static final Object o = new Object();

    public static <T> void notNull(T target, Consumer<T> action) {
        if (target != null) {
            action.accept(target);
        }
    }

    public static <T> void nullRequires(T target, Consumer<T> action) {
        if (target == null) {
            action.accept(null);
        }
    }

    public static <T> void executeNull(T target, Consumer<T> asNotNull, Consumer<T> asNull) {
        if (target == null) {
            asNull.accept((T) o);
        } else {
            asNotNull.accept(target);
        }
    }

    public static <T> void before(T target, Consumer<T> first, Consumer<T> before) {
        first.accept(target);
        before.accept(target);
    }

    public static <T> void equalsValue(Supplier<T> target, Supplier<T> tester, Consumer<T> equalsAction, Consumer<T> elseAction) {
        if (tester.get().equals(target.get())) {
            equalsAction.accept(target.get());
        } else {
            elseAction.accept(target.get());
        }
    }

    public static <T> void assertValue(Supplier<T> target, Supplier<T> tester, Consumer<T> equalsAction, Consumer<T> elseAction) {
        if (tester.get() == target.get()) {
            equalsAction.accept(target.get());
        } else {
            elseAction.accept(target.get());
        }
    }

    public static <T> void noArg(Consumer<T> action) {
        action.accept(null);
    }

    public static <T> void action(T target, Consumer<T> action) {
        action.accept(target);
    }

    @SafeVarargs
    public static <T> void order(T target, Consumer<T>... actions) {
        for (Consumer<T> action : actions) {
            action.accept(target);
        }
    }

    public static <T> void trying(Consumer<T> action) {
        try {
            action.accept((T) o);
        } catch (Exception e) {

        }
    }

    public static <T> void trying(Consumer<T> action, Consumer<T> actionWhenException) {
        try {
            action.accept((T) o);
        } catch (Exception e) {
            actionWhenException.accept((T) o);
        }
    }

    public static <T> void trying(T target, Consumer<T> action) {
        try {
            action.accept(target);
        } catch (Exception e) {

        }
    }

    public static <T> void trying(T target, Consumer<T> action, Consumer<T> actionWhenException) {
        try {
            action.accept(target);
        } catch (Exception e) {
            actionWhenException.accept(target);
        }
    }
}

