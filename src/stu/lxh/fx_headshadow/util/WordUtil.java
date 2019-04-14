package stu.lxh.fx_headshadow.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordUtil {
    public static void generateWord(String cardNumber, String dateTime, String result) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("${cardNumber}", cardNumber);
        paramsMap.put("${dateTime}", dateTime);
        paramsMap.put("${result}", result);


    }

    private static void replaceText(XWPFDocument document, Map<String, Object> paramsMap) {
        Iterator<XWPFParagraph> iterator = document.getParagraphsIterator();
    }
}
