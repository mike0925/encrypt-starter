package com.encrypt.biz;

import com.encrypt.utils.Pair;
import com.encrypt.utils.RandomStringGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AES 密钥长度可以是 128 位，192 位和 256 位
 * 因此密钥长度(key)建议设置为16,24,32
 * iv为16
 */
public class AesEncryptOptions implements IEncryptOptions{

    private final String key;

    private final String iv;

    public AesEncryptOptions(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public String encrypt(String data) {
        IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
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
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
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
    public static Pair<String, String> generateKey(int digit) {
        if (!Stream.of(16,24,32).collect(Collectors.toList()).contains(digit)) {
            throw new IllegalArgumentException("仅支持生成16,24,32位3种随机字符");
        }
        String key = RandomStringGenerator.generator(digit);
        String iv = RandomStringGenerator.generator(16);
        return Pair.of(key, iv);
    }

}
