package com.encrypt.utils;

import java.lang.reflect.Array;

public class ArrayUtils {

    public static <T> T[] append(T[] t, T... elements) {
        if (t == null || t.length == 0) {
            return elements;
        }
        return insert(t, t.length, elements);
    }

    public static <T> T[] insert(T[] t, int index, T... newElements) {
        if (newElements == null || newElements.length == 0) {
            return t;
        }
        if (t == null || t.length == 0) {
            return newElements;
        }
        final int len = t.length;
        if (index < 0) {
            index = (index % len) + len;
        }
        final T[] result = newArray(t.getClass().getComponentType(), Math.max(len, index) + newElements.length);
        System.arraycopy(t, 0, result, 0, Math.min(len, index));
        System.arraycopy(newElements, 0, result, index, newElements.length);
        if (index < len) {
            System.arraycopy(t, index, result, index + newElements.length, len - index);
        }
        return result;
    }

    public static <T> T[] newArray(Class<?> clazz, int len) {
        return (T[]) Array.newInstance(clazz, len);
    }
}
