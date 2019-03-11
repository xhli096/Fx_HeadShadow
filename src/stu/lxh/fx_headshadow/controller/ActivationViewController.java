package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import stu.lxh.fx_headshadow.dao.ActivationUserMapper;
import stu.lxh.fx_headshadow.demo.Main;
import stu.lxh.fx_headshadow.entity.ActivationUser;
import stu.lxh.fx_headshadow.util.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by LXH on 2019/3/6.
 */
public class ActivationViewController {
    @FXML
    private Hyperlink getLicenseHyperlink;

    private static String publicKey;
    private static String privateKey;

    @FXML
    private TextField licenseTextField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    /**
     * 表示尚未激活
     */
    private static final int NOT_ACTIVATION = 0;
    /**
     * 表示已经激活
     */
    private static final int ACTIVATION = 1;

    static {
        try {
            Map<String, Object> keyMap = RSAUtils.genKeyPair();
            publicKey = RSAUtils.getPublicKey(keyMap);
            privateKey = RSAUtils.getPrivateKey(keyMap);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        init();
        dealAction();
    }

    /**
     * 根据配置文件生成SqlSessionFactory
     * @return
     * @throws IOException
     */
    private SqlSessionFactory getSqlSessionFactory() throws IOException {
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
    private SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        return sqlSession;
    }

    private void dealAction() {
        /**
         * 点击获取序列号
         */
        getLicenseHyperlink.setOnMouseClicked(event -> {
            // 生成license，填入默认位置
            generateLicense();
        });

        /**
         * 点击确定按钮后，需要验证是否符合规定，做数据库的修改
         */
        confirmButton.setOnMouseClicked(event -> {
            String licence = licenseTextField.getText();
            System.out.println("licence: \n" + licence);
            boolean goon = false;
            if(licence == null || licence.length() == 0) {
                // 异常，处理,弹出窗口提示使用者
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error Information");
                alert.setContentText("请您输入正确的序列号，若无序列号，请联系管理员获取。");
                alert.showAndWait();
            } else {
                goon = true;
            }

            if(goon) {
                // 根据此licence查找、判定是否已经有计算机已经使用过这个licence
                SqlSession sqlSession = null;
                ActivationUser activationUser;
                try {
                    sqlSession = getSqlSession();
                    ActivationUserMapper activationUserMapper = sqlSession.getMapper(ActivationUserMapper.class);
                    activationUser = activationUserMapper.getSerialByLicense(licence);
                    sqlSession.commit();

                    // 说明数据库中有以该序列号的计算机，且没有被激活，更新进行激活
                    if(activationUser != null && activationUser.getSerial() != null && activationUser.getActivation() == NOT_ACTIVATION) {
                        if(activationUser.getSerial().startsWith(getDatabaseSerialKey())) {
                            activationUserMapper.updateActivationLicense(ACTIVATION, activationUser.getSerial());
                            Main.setSave(true);
                        }
                    } else if(activationUser != null && activationUser.getSerial() != null && activationUser.getActivation() == ACTIVATION) {
                        // 提示用户此序列号已经被使用，无法再次被使用
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error Information");
                        alert.setContentText("此序列号已被使用，请联系管理员获取新的序列号");
                    } else if(activationUser == null) {
                        // 进行解码验证，若信息正确则直接插入并进行激活
                        byte[] decode = Base64Utils.decode(licence);
                        decode = RSAUtils.decryptByPrivateKey(decode, privateKey);
                        String decodingString = new String(decode);
                        // 说明此序列号是由该计算机的硬件信息生成的，然后填入进行激活，否则是盗取他人信息，非法
                        if(decodingString.startsWith(getDatabaseSerialKey())) {
                            ActivationUser user  = new ActivationUser();
                            user.setLicense(licence);
                            user.setActivation(ACTIVATION);
                            user.setSerial(getDatabaseSerialKey());
                            activationUserMapper.insertAndActivationLicense(user);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Error Information");
                            alert.setContentText("请您输入正确的序列号，若无序列号，请联系管理员获取。");
                            alert.showAndWait();
                        }
                    }

                    sqlSession.commit();
                    StageManager.getStage("activationStage").close();
                    StageManager.removeStage("activationStage");
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error Information");
                    alert.setContentText("请您输入正确的序列号，若无序列号，请联系管理员获取。");
                    alert.showAndWait();
                } finally {
                    sqlSession.close();
                }
            }
       });

        /**
         * 点击取消按钮
         */
        cancelButton.setOnMouseClicked(event -> {
            StageManager.getStage("activationStage").close();
            StageManager.removeStage("activationStage");
        });
    }

    private String getDatabaseSerialKey() {
        String cupSerial = ComputerUtil.getCpuSerial();
        String localMac = ComputerUtil.getLocalMac();
        String osName = ComputerUtil.getOsName();

        return cupSerial + "_" + localMac + "_" + osName;
    }

    /**
     * 使用公钥加密，私钥解密
     * 将CPU型号、Mac地址和系统名称组成字符串作为数据库的表主键
     */
    private String generateLicense() {
        // 获取本机的cpu硬盘等型号
        int serialUserYear = Integer.parseInt(PropertiesUtil.getValue("serialUserYear", "config.properties"));
        long endTime = increaseYears(serialUserYear);
        String databaseSerialKey = getDatabaseSerialKey();
        String userSerial = databaseSerialKey + "_" + System.currentTimeMillis() + "_" + endTime;

        byte[] userSerialBytes = new byte[0];
        try {
            userSerialBytes = RSAUtils.encryptByPublicKey(userSerial.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encode = Base64Utils.encode(userSerialBytes);

        licenseTextField.setText(encode);
        System.out.println(encode);
        encode = licenseTextField.getText();
        ActivationUser activationUser = new ActivationUser(databaseSerialKey, encode);

        // 将生成的license保存到数据库中
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
            ActivationUserMapper activationUserMapper = sqlSession.getMapper(ActivationUserMapper.class);
            activationUserMapper.insertActivationLicense(activationUser);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        return encode;
    }

    private long increaseYears(int serialUserYear) {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();

        calendar.setTime(date);
        calendar.add(Calendar.YEAR, serialUserYear);
        return calendar.getTime().getTime();
    }

    private void init() {

    }
}
