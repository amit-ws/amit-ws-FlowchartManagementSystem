package com.conceptile.util;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class GenericUtil {
    public static void ensureNotNull(Object object, String message) {
        Optional.ofNullable(object)
                .orElseThrow(() -> new IllegalArgumentException(message));
    }

    public static <T> void ensureListNotEmpty(Collection<T> list, String message) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <K, V> void ensureMapNotEmpty(Map<K, V> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

}