package io.lumine.mythic.lib.util;

import java.util.function.BiConsumer;

public interface TriConsumer<T,U,V> {

    void accept(T t, U u,V v);


}
