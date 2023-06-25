## 加解密starter 目前支持DES AES  SM2,  RSA未实现
## 学习springboot starter 小demo

```xml
        <dependency>
            <groupId>com.encrypt</groupId>
            <artifactId>encrypt-starter</artifactId>
            <version>1.0</version>
        </dependency>

```

```yaml
### 加密配置
encrypt: 
  enable: true    ### 开启加密
  type: DES       ### DES方式加解密
  return-type: HEX    ### 加密后的字节数组 以16进制编码返回字符串
  exclude-url: "/test/decrypt,test/decrypt1"    ### 开放路径
  des:           ### des方式 配置key iv(还有aes  sm2)等等
    key: X5mU4sYC
    iv: j1B4cGrA
```

