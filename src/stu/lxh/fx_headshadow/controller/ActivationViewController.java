package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by LXH on 2019/3/6.
 */
public class ActivationViewController {
    @FXML
    private Hyperlink getLicenseHyperlink;

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

    private void generateLicense() {
        // 获取本机的cpu硬盘等型号
    }

    private String getLocalMac() throws UnknownHostException, SocketException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();

        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < mac.length; i++) {
            if(i != 0) {
                sb.append("-");
            }

            int tmp = mac[i] & 0xff;
            String str = Integer.toHexString(tmp);
            if(str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }

        return sb.toString().toUpperCase();
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
