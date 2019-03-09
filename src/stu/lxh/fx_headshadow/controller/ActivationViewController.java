package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import stu.lxh.fx_headshadow.dao.ActivationUserMapper;
import stu.lxh.fx_headshadow.entity.ActivationUser;
import stu.lxh.fx_headshadow.util.Base64Utils;
import stu.lxh.fx_headshadow.util.PropertiesUtil;
import stu.lxh.fx_headshadow.util.RSAUtils;

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
         * 点击确定按钮后，需要验证是否符合规定
         */
        confirmButton.setOnMouseClicked(event -> {
            String licence = licenseTextField.getText();
            if(licence == null || licence.length() == 0) {
                // 异常，处理
            }

            // 根据此licence查找、判定是否已经有计算机已经使用过这个licence
        });
    }

    /**
     * 使用公钥加密，私钥解密
     * 将CPU型号、Mac地址和系统名称组成字符串作为数据库的表主键
     */
    private String generateLicense() {
        // 获取本机的cpu硬盘等型号
        String cupSerial = getCpuSerial();
        String localMac = getLocalMac();
        String osName = getOsName();

        int serialUserYear = Integer.parseInt(PropertiesUtil.getValue("serialUserYear", "config.properties"));
        long endTime = increaseYears(serialUserYear);
        String databaseSerialKey = cupSerial + "_" + localMac + "_" + osName;
        String userSerial = databaseSerialKey + "_" + System.currentTimeMillis() + "_" + endTime;

        byte[] userSerialBytes = new byte[0];
        try {
            userSerialBytes = RSAUtils.encryptByPublicKey(userSerial.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encode = Base64Utils.encode(userSerialBytes);
        ActivationUser activationUser = new ActivationUser(databaseSerialKey, encode);

        // 将生成的license保存到数据库中
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
            ActivationUserMapper activationUserMapper = sqlSession.getMapper(ActivationUserMapper.class);
            activationUserMapper.insertAvtivationLicense(activationUser);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
            // 如果是
        } finally {
            sqlSession.close();
        }
        licenseTextField.setText(encode);

        System.out.println(encode);

        return encode;
    }

    private long increaseYears(int serialUserYear) {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();

        calendar.setTime(date);
        calendar.add(Calendar.YEAR, serialUserYear);
        return calendar.getTime().getTime();
    }

    /**
     * 获得系统的名称
     * @return
     */
    private String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * 获取当前计算机的物理地址
     * @return
     */
    private String getLocalMac() {
        String mac = "";
        String line = "";

        String os = System.getProperty("os.name");

        if(os != null && os.startsWith("Windows")) {
            try {
                String command = "cmd.exe /c ipconfig /all";
                Process process = Runtime.getRuntime().exec(command);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

                while((line = bufferedReader.readLine()) != null) {
                    if(line.indexOf("物理地址") != -1) {
                        int index = line.indexOf(":") + 2;
                        mac = line.substring(index);
                        break;
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mac;
    }

    /**
     * 获取CPU号，多个CPU时，仅仅读取第一个
     * @return
     */
    private String getCpuSerial() {
        String result = "";

        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "On Error Resume Next \r\n\r\n" + "strComputer = \".\"  \r\n"
                    + "Set objWMIService = GetObject(\"winmgmts:\" _ \r\n"
                    + "    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\n"
                    + "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n "
                    + "For Each objItem in colItems\r\n " + "    Wscript.Echo objItem.ProcessorId  \r\n "
                    + "    exit for  ' do the first cpu only! \r\n" + "Next                    ";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }

        return result.trim();
    }

    private void init() {

    }
}
