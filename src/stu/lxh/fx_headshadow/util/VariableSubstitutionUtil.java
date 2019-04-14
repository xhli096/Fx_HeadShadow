package stu.lxh.fx_headshadow.util;

import org.omg.PortableInterceptor.INACTIVE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EnumName VariableSubstitutionUtil
 * author:xinghaol_1
 * Date:2019/4/4 18:41
 * Email:xinghaol_1@tujia.com
 */
public enum VariableSubstitutionUtil {
    HEAD_SHADOW("headShadow");

    private String title;
    private static Properties properties;

    private VariableSubstitutionUtil(String title) {
        this.title = title;
    }

    private static final String PROPERTIES_FILE = "config/configtest.properties";
    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}}");

    static {
        try {
            properties = new Properties();
            InputStream inputStream = VariableSubstitutionUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTitle() {
        return title;
    }

    public static String get(VariableSubstitutionUtil variableSubstitutionUtil) {
        String value = properties.getProperty(variableSubstitutionUtil.getTitle());
        return  value == null ? null : loop(value);
    }

    private static String loop(String key) {
        // 定义matcher匹配其中的路径变量
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer stringBuffer = new StringBuffer();
        boolean flag = false;

        System.out.println(matcher.find());
        while (matcher.find()) {
            String matcherKey = matcher.group();
            String matcherValue = properties.getProperty(matcherKey);
            System.out.println("matcherKey:" + matcherKey + " matcherValue:"  + matcherValue);
            if(matcherValue != null) {
                // TODO ?对不对不晓得
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(matcherValue));
                flag = true;
            }
        }

        matcher.appendTail(stringBuffer);

        // flag为false时，说明已经匹配不到路径变量，则不需要递归查找
        return flag ? loop(stringBuffer.toString()) : key;
    }

    public static String getTranslate(VariableSubstitutionUtil variableSubstitutionUtil, Map<String, String> params) {
        String path = get(variableSubstitutionUtil);
        if(path == null || path.equals("")) {
            return "";
        }

        if(params != null && !params.keySet().isEmpty()) {
            // 需要替换的字段均放入map，包括需要替换的日期
            for(String key : params.keySet()) {
                path = path.replace("${" + key + "}", params.get(key));
            }
        }

        return path;
    }
}
