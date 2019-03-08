package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import stu.lxh.fx_headshadow.util.RSAUtils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by LXH on 2019/3/6.
 */
public class ActivationViewController {
    @FXML
    private Hyperlink getLicenseHyperlink;

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

    @FXML
    public void initialize() {
        init();
        dealAction();
    }

    private void dealAction() {
        getLicenseHyperlink.setOnMouseClicked(event -> {
            // 生成license，填入默认位置

        });
    }

    /**
     * 使用公钥加密，私钥解密
     */
    private void generateLicense() {
        // 获取本机的cpu硬盘等型号
        String cupSerial = getCpuSerial();
        String localMac = getLocalMac();
        String osName = getOsName();

        String userSerial = cupSerial + "_" + localMac + "_" + osName + "_";
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
