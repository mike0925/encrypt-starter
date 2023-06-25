package com.encrypt.biz;

import com.encrypt.utils.HexUtils;
import com.encrypt.utils.Pair;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * 注: 公私密钥均采用16进制
 * 排列方式采用C1C3C2
 */
public class Sm2EncryptOptions implements IEncryptOptions{

    private final String publicKey;

    private final String privateKey;

    private static final SM2Engine.Mode MODE = SM2Engine.Mode.C1C3C2;

    public Sm2EncryptOptions(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public String encrypt(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] publicKeyBytes = HexUtils.decodeToString(publicKey);
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
            ECPoint pubPoint = sm2ECParameters.getCurve().decodePoint(publicKeyBytes);
            ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pubPoint, domainParameters);
            SM2Engine sm2Engine = new SM2Engine(MODE);
            sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));
            return getEncryptFunc().apply(sm2Engine.processBlock(bytes, 0, bytes.length));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败");
        }
    }

    @Override
    public String decrypt(String data) {
        if (!data.startsWith("04")) {
            data = "04".concat(data);
        }
        //解密字节数据
        byte[] dataBytes = getDecryptFunc().apply(data);
        try {
            BigInteger privateKeyD = new BigInteger(privateKey, 16);
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
            ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
            SM2Engine sm2Engine = new SM2Engine(MODE);
            sm2Engine.init(false, privateKeyParameters);
            byte[] decryptData = sm2Engine.processBlock(dataBytes, 0, dataBytes.length);
            return new String(decryptData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败");
        }
    }

    /**
     * 对外api
     * @return Pair key = publicKey value = privateKey
     */
    public static Pair<String, String> generateKey() {
        KeyPair keyPair = initKey();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        BCECPublicKey _publicKey = (BCECPublicKey) publicKey;
        BCECPrivateKey _privateKey = (BCECPrivateKey) privateKey;
        return Pair.of(HexUtils.encodeToString(_publicKey.getQ().getEncoded(false)), HexUtils.encodeToString(_privateKey.getD().toByteArray()));
    }

    private static KeyPair initKey() {
        try {
            ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            kpg.initialize(sm2Spec);
            return kpg.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("密钥对生成失败");
        }
    }

}
