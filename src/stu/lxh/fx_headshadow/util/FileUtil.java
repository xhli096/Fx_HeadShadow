package stu.lxh.fx_headshadow.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by LXH on 2019/3/6.
 */
public class FileUtil {
    /**
     * 获得类的基路径，打成jar包也可以正确获得路径
     * @return
     */
    public static String getBasePath() {
        String filePath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if(filePath.endsWith(".jar")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        File file = new File(filePath);
        filePath = file.getAbsolutePath();

        return filePath;
    }
}
