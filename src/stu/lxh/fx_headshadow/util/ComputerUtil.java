package stu.lxh.fx_headshadow.util;

import java.io.*;

/**
 * Created by LXH on 2019/3/11.
 */
public class ComputerUtil {
    /**
     * 获得系统的名称
     * @return
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * 获取当前计算机的物理地址
     * @return
     */
    public static String getLocalMac() {
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
    public static String getCpuSerial() {
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
}
