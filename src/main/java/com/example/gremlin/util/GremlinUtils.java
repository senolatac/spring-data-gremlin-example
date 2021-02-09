package com.example.gremlin.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * @author sa
 * @date 9.02.2021
 * @time 09:57
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GremlinUtils
{
    public static <T> T createInstance(@NonNull Class<T> type) {
        final T instance;

        try {
            instance = type.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("can not access type constructor", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("failed to create instance of given type", e);
        }

        return instance;
    }
}
