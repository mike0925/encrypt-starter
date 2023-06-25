package com.encrypt.biz;

/**
 * 暂未实现
 */
public class RsaEncryptOptions implements IEncryptOptions {

    private final String privateKey;
    private final String publicKey;

    public RsaEncryptOptions(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public String encrypt(String data) {
        return null;
    }

    @Override
    public String decrypt(String data) {
        return null;
    }
}
