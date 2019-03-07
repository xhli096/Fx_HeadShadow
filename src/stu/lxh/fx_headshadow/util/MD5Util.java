package stu.lxh.fx_headshadow.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LXH on 2019/3/6.
 */
public class MD5Util {
    public static String encode(String plainText) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(plainText.getBytes());

            byte[] b = messageDigest.digest();
            int i;
            StringBuffer stringBuffer = new StringBuffer("");

            for(int offset = 0; offset < b.length; offset++) {
                i = b[offset];

                if(i < 0) {
                    i += 256;
                }
                if(i < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i));
            }

            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
