package stu.lxh.fx_headshadow.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 公钥/私钥/签名工具包
 * Created by LXH on 2019/3/6.
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
public class RSAUtils {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    /**
     * RSA最大加密铭文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 127;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 生成密钥对（公钥和私钥）
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;

    }

    /**
     * 用私钥对信息生成数字签名
     * @param data              已加密的数据
     * @param privateKey        私钥（BASE64编码）
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initSign(privateK);
        signature.update(data);

        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data          已加密数据
     * @param publicKey     公钥（BASE64编码）
     * @param sign          数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKe = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initVerify(publicKe);
        signature.update(data);

        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 私钥解密
     * @param encryptedData     已加密的数据
     * @param privateKey        私钥（BASE64解码）
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        Key privateKe = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKe);

        int inputLen = encryptedData.length;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int offset = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段解密
        while (inputLen - offset > 0) {
            if(inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offset, inputLen - offset);
            }

            outputStream.write(cache, 0, cache.length);
            i++;

            offset = i * MAX_DECRYPT_BLOCK;
        }

        byte[] decryptedData = outputStream.toByteArray();
        outputStream.close();

        return decryptedData;
    }

    /**
     * 公钥解密
     * @param encryptedData     已加密数据
     * @param publicKey         公钥（BASE64编码）
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        Key publicKe = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKe);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int offSet = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if(inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            outputStream.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }

        byte[] decryptedData = outputStream.toByteArray();
        outputStream.close();

        return decryptedData;
    }

    /**
     * 公钥加密
     * @param data          源数据
     * @param publicKey     公钥（base64编码）
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKe = keyFactory.generatePublic(x509EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKe);

        int inputLen = data.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int offset = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLen - offset);
            }

            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = outputStream.toByteArray();
        outputStream.close();

        return encryptedData;
    }

    /**
     * 私钥加密
     * @param data          源数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKe = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKe);

        int inputLen = data.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLen - offset);
            }
            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = outputStream.toByteArray();
        outputStream.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     * @param keyMap        密钥对
     * @return
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 获取公钥
     * @param keyMap        密钥对
     * @return
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return Base64Utils.encode(key.getEncoded());
    }
}
