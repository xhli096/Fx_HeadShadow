package stu.lxh.fx_headshadow.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by LXH on 2018/12/16.
 */
public class PropertiesUtil {
    private static String configFilePath = null;

    /**
     * 获得文件路径
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName){
        try {
            configFilePath = PropertiesUtil.class.getClassLoader().getResource("config/" + fileName).getPath();
            configFilePath = java.net.URLDecoder.decode(configFilePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return configFilePath;
    }

    /**
     * 根据key获得值
     *
     * @param key
     * @return
     */
    public static String getValue(String key, String fileName) {
        String value = null;
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(getFilePath(fileName));
            properties.load(new InputStreamReader(fis, "UTF-8"));
            if (fis != null) fis.close();
            if (properties.containsKey(key)) {
                value = properties.getProperty(key).trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得所有的key
     *
     * @param fileName
     * @return
     */
    public static List getKeyList(String fileName) {
        List<String> list = new ArrayList<String>();
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(getFilePath(fileName));
            properties.load(fis);
            if (fis != null) {
                fis.close();
            }
            Enumeration<Object> enumerations = properties.keys();
            while(enumerations.hasMoreElements()) {
                Object o = enumerations.nextElement();
                list.add((String) o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}