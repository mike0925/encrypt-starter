package com.encrypt.biz;

import com.encrypt.config.EncryptProperties;
import com.encrypt.enums.ReturnTypeEnum;
import com.encrypt.utils.Base64Utils;

import java.util.function.Function;

public interface IEncryptOptions {

    default Function<byte[], String> getEncryptFunc() {
        Function<byte[], String> encryptFunc = ReturnTypeEnum.getEncryptFunc(EncryptProperties.returnType);
        return encryptFunc == null ? Base64Utils::encodeToString : encryptFunc;
    }

    default Function<String, byte[]> getDecryptFunc() {
        Function<String, byte[]> decryptFunc = ReturnTypeEnum.getDecryptFunc(EncryptProperties.returnType);
        return decryptFunc == null ? Base64Utils::decodeToString : decryptFunc;
    }

    String encrypt(String data);

    String decrypt(String data);

}
