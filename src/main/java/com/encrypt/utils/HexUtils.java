package com.encrypt.utils;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public class HexUtils {

    public static String encodeToString(String data) {
        byte[] encodeBytes = Hex.encode(data.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes, StandardCharsets.UTF_8);
    }

    public static String encodeToString(byte[] data) {
        byte[] encodeBytes = Hex.encode(data);
        return new String(encodeBytes, StandardCharsets.UTF_8);
    }

    public static byte[] decodeToString(String data) {
        return Hex.decode(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decodeToString(byte[] data) {
        return Hex.decode(data);
    }


}
