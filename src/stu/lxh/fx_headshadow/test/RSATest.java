package stu.lxh.fx_headshadow.test;

import stu.lxh.fx_headshadow.controller.ActivationViewController;
import stu.lxh.fx_headshadow.util.RSAUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by LXH on 2019/3/7.
 */
public class RSATest {
    private static String publicKey;
    private static String privateKey;

    static {
        try {
            Map<String, Object> keyMap = RSAUtils.genKeyPair();
            publicKey = RSAUtils.getPublicKey(keyMap);
            privateKey = RSAUtils.getPrivateKey(keyMap);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }

    private static void test() throws Exception {
        System.out.println("公钥加密-私钥解密");

        String source = "一行没有意义的文字，看完了等于没看";
        System.out.println("加密前的文字：" + source);
        byte[] data = source.getBytes();
        byte[] encodeData = RSAUtils.encryptByPublicKey(data, publicKey);

        System.out.println("加密后的问题：" + new String(encodeData));

        byte[] decodeData = RSAUtils.decryptByPrivateKey(encodeData, privateKey);
        String target = new String(decodeData);
        System.out.println("解密后的文字：" + target);
        System.out.println("------------------------------------------------");
    }

    private static void testSign() throws Exception {
        System.out.println("私钥加密-公钥解密");

        String source = "这是一行测试RSA数字签名的无意义文字";
        System.out.println("加密前的文字：" + source);
        byte[] data = source.getBytes();
        byte[] encodeData = RSAUtils.encryptByPrivateKey(data, privateKey);
        System.out.println("加密后的文字：" + new String(encodeData));

        byte[] decodeData = RSAUtils.decryptByPublicKey(encodeData, publicKey);
        String target = new String(decodeData);

        System.out.println("解密后的文字：" + new String(target));

        System.out.println("私钥签名-公钥验证签名");

        String sign = RSAUtils.sign(encodeData, privateKey);
        System.out.println("私钥签名：" + sign);
        boolean status = RSAUtils.verify(encodeData, publicKey, sign);
        System.out.println("验证结果：" + status);
    }
}
