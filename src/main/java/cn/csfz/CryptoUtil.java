package cn.csfz;

import cn.csfz.crypto.ECKeyUtil;
import cn.csfz.crypto.SecureUtil;
import cn.csfz.crypto.asymmetric.KeyType;
import cn.csfz.crypto.asymmetric.SM2;
import javafx.util.Pair;
import org.bouncycastle.util.encoders.Base64;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptoUtil {

    public static String sign(String data,String privateKeyStr)
    {
        SM2 sm2 =new SM2();
        sm2.setPrivateKeyParams(ECKeyUtil.decodePrivateKeyParams(SecureUtil.decode(privateKeyStr)));
        return new String(Base64.encode(sm2.sign(data.getBytes())));
    }

    public static boolean verify(String decrypt, String sign ,String publicKeyStr)
    {
        SM2 sm2 =new SM2();
        sm2.setPublicKeyParams(ECKeyUtil.decodePublicKeyParams(SecureUtil.decode(publicKeyStr)));
        return sm2.verify(decrypt.getBytes(),Base64.decode(sign.getBytes()));
    }

    public static String encrypt(String data,String publicKeyStr)
    {
        SM2 sm2 =new SM2();
        sm2.setPublicKeyParams(ECKeyUtil.decodePublicKeyParams(SecureUtil.decode(publicKeyStr)));
        return new String(Base64.encode(sm2.encrypt(data.getBytes(), KeyType.PublicKey)));
    }

    public static String decrypt(String encrypt,String privateKeyStr)
    {
        SM2 sm2 =new SM2();
        sm2.setPrivateKeyParams(ECKeyUtil.decodePrivateKeyParams(SecureUtil.decode(privateKeyStr)));
        byte[] decrypt = sm2.decrypt(Base64.decode(encrypt), KeyType.PrivateKey);
        return new String(decrypt);
    }


    public static Pair<String, String> getKeys( ) throws Exception {
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        // 生成私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // 生成公钥
        PublicKey publicKey = keyPair.getPublic();
        // 对公私钥进行base64编码
        String privateKeyString = new String(Base64.encode(privateKey.getEncoded()));
        String publicKeyString = new String(Base64.encode(publicKey.getEncoded()));
        return new Pair<String, String>(privateKeyString, publicKeyString);
    }
}
