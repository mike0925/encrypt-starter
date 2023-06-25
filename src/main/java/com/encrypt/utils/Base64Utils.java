package com.encrypt.utils;

import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Utils {

    public static String encodeToString(String data) {
        byte[] encodeBytes = Base64.encode(data.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes, StandardCharsets.UTF_8);
    }

    public static String encodeToString(byte[] data) {
        byte[] encodeBytes = Base64.encode(data);
        return new String(encodeBytes, StandardCharsets.UTF_8);
    }

    public static byte[] decodeToString(String data) {
        return Base64.decode(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decodeToString(byte[] data) {
        return Base64.decode(data);
    }
}
