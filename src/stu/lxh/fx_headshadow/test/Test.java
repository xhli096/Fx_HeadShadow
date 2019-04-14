package stu.lxh.fx_headshadow.test;

import stu.lxh.fx_headshadow.util.VariableSubstitutionUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws/*
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("patientCardNumber", "20190403001");
        paramsMap.put("patientName", "李星皞");
        paramsMap.put("date", dateFormat.format(new Date()));
        paramsMap.put("result", "这是你的测量结果，哈哈哈。");

        String inputUrl = "D:\\毕业设计\\测量结果.docx";
        CustomXWPFDocument customXWPFDocument = WorderToNewWordUtils.generateWord(inputUrl, paramsMap, null);
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\毕业设计\\结果.docx");

        customXWPFDocument.write(fileOutputStream);
        fileOutputStream.close();
*/
            IOException {

//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        FileChooser chooser =  new FileChooser();
//        String cwd = System.getProperty("user.dir");
//        System.out.println(cwd);

/*
        File file1 = new File("resources/config/configtest.properties");
        System.out.println(file1.exists());
*/

    }

    @org.junit.Test
    public void fun() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("project_path", System.getProperty("user.dir"));

        System.out.println(VariableSubstitutionUtil.getTranslate(VariableSubstitutionUtil.HEAD_SHADOW, map));
    }
}
