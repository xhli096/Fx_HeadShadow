package stu.lxh.fx_headshadow.util;

import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SwingUtil {
	public static void initComboBoxList(ComboBox<String> cmb, int start, int end) {
		cmb.getItems().clear();
		for(int i = start; i <= end; i++) {
			cmb.getItems().add(String.valueOf(i));
		}
		cmb.getSelectionModel().select(0);
	}
	
	public static void setDateComboBox(ComboBox<String> cmb, int year, int month) {
		Calendar today = Calendar.getInstance();
		today.set(year, month-1, 1);
		int maxDate = today.getActualMaximum(Calendar.DAY_OF_MONTH);
		initComboBoxList(cmb, 1, maxDate);
	}
	
	public static void checkboxListSelect(JCheckBox[] checkboxArray, boolean selected) {
		for (JCheckBox checkbox : checkboxArray) {
			checkbox.setSelected(selected);
		}
	}
	
	public static void checkboxListSelect(JCheckBox[] checkboxArray) {
		for (JCheckBox checkbox : checkboxArray) {
			checkbox.setSelected(!checkbox.isSelected());
		}
	}
	
	public static String getCurrentDate(String pattern) {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		return sdf.format(today.getTime());
	}
	
}
