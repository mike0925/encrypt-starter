package com.encrypt.biz;

import com.encrypt.utils.Pair;
import com.encrypt.utils.RandomStringGenerator;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * key 建议8位
 */
public class DesEncryptOptions implements IEncryptOptions {

    private final String key;

    private final String iv;

    public DesEncryptOptions(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    @Override
    public String encrypt(String data) {
        IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "DES");
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
            byte[] encryptData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return getEncryptFunc().apply(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String decrypt(String data) {
        byte[] encryptDataBytes = getDecryptFunc().apply(data);
        IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "DES");
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
            return new String(cipher.doFinal(encryptDataBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成key以及iv
     * @param digit
     * @return Pair<key, iv>
     */
    public static Pair<String, String> generateKey() {
        String key = RandomStringGenerator.generator(8);
        String iv = RandomStringGenerator.generator(8);
        return Pair.of(key, iv);
    }
}
