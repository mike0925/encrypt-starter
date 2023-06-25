package com.encrypt.utils;

import java.util.Random;

public class RandomStringGenerator {

    public static String generator(int digit) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            int index = random.nextInt(base.length());
            builder.append(base.charAt(index));
        }
        return builder.toString();
    }
}
