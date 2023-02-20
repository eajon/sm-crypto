package cn.csfz;

import javafx.util.Pair;

public class Test {
    public static void main(String[] args) throws Exception {
        String text = "我是测试SM2加密类的内容";
        Pair<String, String> bankPair = CryptoUtil.getKeys();
        Pair<String, String> ptPair = CryptoUtil.getKeys();

        System.out.println(bankPair.getKey());
        System.out.println(bankPair.getValue());
        System.out.println(ptPair.getKey());
        System.out.println(ptPair.getValue());
        // 使用己方私钥加签
        String signStr = CryptoUtil.sign(text,bankPair.getKey());
        System.out.println(signStr);
        // 使用对方公钥加密
        String encryptStr =CryptoUtil.encrypt(text,ptPair.getValue());
        System.out.println(encryptStr);
        // 使用对方私钥解密
        String decryptStr= CryptoUtil.decrypt(encryptStr,ptPair.getKey());
        System.out.println(decryptStr);
        // 使用己方公钥验签
        boolean verify = CryptoUtil.verify(decryptStr,signStr,bankPair.getValue());
        System.out.println(verify);
    }



}
