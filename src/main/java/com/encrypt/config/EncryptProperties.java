package com.encrypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = EncryptProperties.ENCRYPT_PREFIX)
public class EncryptProperties {

    /**
     * properties prefix
     */
    static final String ENCRYPT_PREFIX = "encrypt";

    /**
     * 是否开启
     */
    public static boolean enable;

    /**
     * 加密方式
     */
    public static String type;

    /**
     * 返回数据格式
     */
    public static String returnType;

    /**
     * 排除路径
     */
    public static String excludeUrl;

    /**
     * des加解密
     */
    @NestedConfigurationProperty
    private DesEncryptProperties des;

    @NestedConfigurationProperty
    private AESEncryptProperties aes;

    @NestedConfigurationProperty
    private Sm2EncryptProperties sm2;

    public void setEnable(boolean enable) {
        EncryptProperties.enable = enable;
    }

    public void setType(String type) {
        EncryptProperties.type = type;
    }

    public void setReturnType(String returnType) {
        EncryptProperties.returnType = returnType;
    }

    public void setExcludeUrl(String excludeUrl) {
        EncryptProperties.excludeUrl = excludeUrl;
    }
}
