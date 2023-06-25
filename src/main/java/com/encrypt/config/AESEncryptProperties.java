package com.encrypt.config;

import lombok.Data;

@Data
public class AESEncryptProperties {

    private String key;
    private String iv;
}
