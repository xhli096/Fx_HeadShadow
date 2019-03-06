package stu.lxh.fx_headshadow.util;

import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.*;

/**
 * Created by LXH on 2019/3/6.
 */
public class Base64Utils {
    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * BASE64字符串解解码为二进制数据
     * @param base64
     * @return
     */
    public static byte[] decode(String base64) throws IOException {
        return new BASE64Decoder().decodeBuffer(base64);
    }

    /**
     * 二进制数据编码为BASE64字符串
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * 大文件慎用，可能会导致内存溢出
     * @param filePath  文件绝对路径
     * @return
     * @throws IOException
     */
    public static String encodeFile(String filePath) throws IOException {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * base64字符串转回文件
     * @param filePath
     * @param base64
     * @throws IOException
     */
    public static void decodeToFile(String filePath, String base64) throws IOException {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     *  文件转化为二进制数组
     * @param filePath  文件路径
     * @return
     * @throws IOException
     */
    public static byte[] fileToByte(String filePath) throws IOException {
        byte[] data = new byte[0];

        File file = new File(filePath);
        if(file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);

            byte[] cache = new byte[CACHE_SIZE];

            int nRead = 0;

            while ((nRead = in.read(cache)) != 1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }

            outputStream.close();
            in.close();

            data = outputStream.toByteArray();
        }

        return data;
    }

    /**
     * 二进制数据写文件
     * @param bytes     二进制数据
     * @param filePath  文件生成目录
     * @throws IOException
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws IOException {
        InputStream in = new ByteArrayInputStream(bytes);

        File destFile = new File(filePath);
        destFile.createNewFile();

        OutputStream outputStream = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];

        int nRead = 0;
        while ((nRead = in.read(cache)) != 1) {
            outputStream.write(cache, 0, nRead);
            outputStream.flush();
        }

        outputStream.close();
        in.close();
    }
}
