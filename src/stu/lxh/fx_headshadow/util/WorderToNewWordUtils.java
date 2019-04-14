package stu.lxh.fx_headshadow.util;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorderToNewWordUtils {
    public static CustomXWPFDocument generateWord(String inputUrl, Map<String, Object> paramsMap, Map<String, Object> mapList) {
        CustomXWPFDocument customXWPFDocument = null;

        try {
            customXWPFDocument = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));

            // 解析替换表格内容
            WorderToNewWordUtils.changeTable(customXWPFDocument, paramsMap, mapList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customXWPFDocument;
    }

    private static void changeTable(CustomXWPFDocument customXWPFDocument, Map<String, Object> textMap, Map<String, Object> mapList) {
        List<XWPFTable> tables = customXWPFDocument.getTables();
        for(int i = 0; i < tables.size(); i++) {
            XWPFTable xwpfTable = tables.get(i);
            if(checkText(xwpfTable.getText())) {
                List<XWPFTableRow> rows = xwpfTable.getRows();
                eachTable(rows, textMap);
            }
        }
    }

    private static void  eachTable(List<XWPFTableRow> rows, Map<String, Object> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for(XWPFTableCell cell : cells) {
                if(checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for(XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for(XWPFRun run : runs) {
                            Object object = changeValue(run.toString(), textMap);
                            if(object instanceof String) {
                                run.setText((String) object, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Object changeValue(String value, Map<String, Object> textMap) {
        Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
        Object val = "";
        for(Map.Entry<String, Object> textSet : textSets) {
            String key = textSet.getKey();
            if(value.indexOf(key) != -1) {
                val = textSet.getValue();
            }
        }
        return val;
    }

    private static boolean checkText(String text) {
        boolean check = false;
        if(text.indexOf("$") != -1) {
            check = true;
        }

        return check;
    }
}
