package com.encrypt.config;

import com.encrypt.biz.AesEncryptOptions;
import com.encrypt.biz.DesEncryptOptions;
import com.encrypt.biz.Sm2EncryptOptions;
import com.encrypt.filter.ParamsDecryptFilter;
import com.encrypt.utils.SpringContextUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "encrypt", name = "enable", havingValue = "true")
@EnableConfigurationProperties(EncryptProperties.class)
public class EncryptAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "encrypt", name = "type", havingValue = "DES")
    public DesEncryptOptions desEncryptOptions(EncryptProperties encryptProperties) {
        DesEncryptProperties des = encryptProperties.getDes();
        if (des == null) {
            throw new IllegalArgumentException("DES加密方式未配置key以及iv");
        }
        return new DesEncryptOptions(des.getKey(), des.getIv());
    }

    @Bean
    @ConditionalOnProperty(prefix = "encrypt", name = "type", havingValue = "AES")
    public AesEncryptOptions aesEncryptOptions(EncryptProperties encryptProperties) {
        AESEncryptProperties aes = encryptProperties.getAes();
        if (aes == null) {
            throw new IllegalArgumentException("AES加密方式未配置key以及iv");
        }
        return new AesEncryptOptions(aes.getKey(), aes.getIv());
    }

    @Bean
    @ConditionalOnProperty(prefix = "encrypt", name = "type", havingValue = "SM2")
    public Sm2EncryptOptions sm2EncryptOptions(EncryptProperties encryptProperties) {
        Sm2EncryptProperties sm2 = encryptProperties.getSm2();
        if (sm2 == null) {
            throw new IllegalArgumentException("SM2加密方式未配置publicKey以及privateKey");
        }
        return new Sm2EncryptOptions(sm2.getPublicKey(), sm2.getPrivateKey());
    }

    @Bean
    public ParamsDecryptFilter paramsDecryptFilter() {
        return new ParamsDecryptFilter();
    }

    @Bean
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }
}
