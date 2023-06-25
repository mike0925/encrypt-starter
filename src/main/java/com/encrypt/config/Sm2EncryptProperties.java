package com.encrypt.config;

import lombok.Data;

@Data
public class Sm2EncryptProperties {

    private String privateKey;

    private String publicKey;
}
