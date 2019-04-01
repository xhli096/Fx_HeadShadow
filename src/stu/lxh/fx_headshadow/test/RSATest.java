package stu.lxh.fx_headshadow.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import stu.lxh.fx_headshadow.dao.ActivationUserMapper;
import stu.lxh.fx_headshadow.entity.ActivationUser;
import stu.lxh.fx_headshadow.util.RSAUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 根据配置文件生成SqlSessionFactory
     * @return
     * @throws IOException
     */
    private static SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "config/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        return sqlSessionFactory;
    }

    /**
     * 获得sqlSession
     * @return
     * @throws IOException
     */
    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        return sqlSession;
    }

    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        int dpi = toolkit.getScreenResolution();

        System.out.println(width + " " + height + " " + dpi);
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
