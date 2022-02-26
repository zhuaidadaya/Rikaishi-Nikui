package com.github.zhuaidadaya.rikaishinikui.abstractly;

import java.util.Collection;

public interface Multiple<T> {
     T get(int index);
     Collection<T> get();
}
