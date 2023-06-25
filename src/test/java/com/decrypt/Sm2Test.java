package com.decrypt;

import com.encrypt.biz.AesEncryptOptions;
import com.encrypt.biz.IEncryptOptions;
import com.encrypt.biz.Sm2EncryptOptions;
import com.encrypt.utils.Pair;
import org.junit.Test;

public class Sm2Test {

    /**
     * 测试sm2 生成密钥对加解密
     */
    @Test
    public void testSm2() {
        Pair<String, String> pair = Sm2EncryptOptions.generateKey();
        String publicKey = pair.getKey();
        String privateKey = pair.getValue();
        System.out.println("公钥: " + publicKey);
        System.out.println("私钥: " + privateKey);
        System.out.println("公钥长度: " + publicKey.length());
        System.out.println("私钥长度: " + privateKey.length());
        IEncryptOptions encryptOptions = new Sm2EncryptOptions(publicKey, privateKey);
        String helloWorld = "hello world";
        String encrypt = encryptOptions.encrypt(helloWorld);
        System.out.println("加密数据: "+ encrypt);
        String decrypt = encryptOptions.decrypt(encrypt);
        System.out.println("解密数据: "+ decrypt);
    }

    @Test
    public void testAesGenerator() {
        Pair<String, String> pair = AesEncryptOptions.generateKey(24);
        System.out.println(pair.getKey());
        System.out.println(pair.getValue());
    }


}
