package com.encrypt.enums;

import com.encrypt.utils.Base64Utils;
import com.encrypt.utils.HexUtils;

import java.util.function.Function;

public enum ReturnTypeEnum {

    BASE64, HEX;

    public static Function<byte[], String> getEncryptFunc(String typeEnum) {
        if (ReturnTypeEnum.BASE64.name().equals(typeEnum)) {
            return Base64Utils::encodeToString;
        }
        if (ReturnTypeEnum.HEX.name().equals(typeEnum)) {
            return HexUtils::encodeToString;
        }
        return null;
    }

    public static Function<String, byte[]> getDecryptFunc(String typeEnum) {
        if (ReturnTypeEnum.BASE64.name().equals(typeEnum)) {
            return Base64Utils::decodeToString;
        }
        if (ReturnTypeEnum.HEX.name().equals(typeEnum)) {
            return HexUtils::decodeToString;
        }
        return null;
    }

}
