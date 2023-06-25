package com.encrypt.config;

import lombok.Data;

@Data
public class DesEncryptProperties {

    /**
     * 长度建议为8
     */
    private String key;

    private String iv;
}
