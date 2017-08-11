package com.bw30.open.wft.common.translate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bw30.open.wft.common.translate.inter.FieldMeta;

public class ExcelWriter {

	private String filePath;

	private XSSFWorkbook workbook;

	private XSSFSheet sheet;

	private List<? extends Object> datas;

	/**
	 * default construct
	 * 
	 * @param filePath
	 */
	public ExcelWriter(String filePath, List<? extends Object> datas) {
		this.filePath = filePath;
		this.datas = datas;
		workbook = new XSSFWorkbook();
	}

	public void write() {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filePath);
			this.setTitle().setData(); // 设置标题和数据
			workbook.write(fileOut);
			fileOut.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param map
	 */
	private ExcelWriter setTitle() {
		Map<Integer, String> map = getTitleMap();
		sheet = workbook.createSheet();
		XSSFRow title = sheet.createRow(0);// 首位置增加标题
		for (Integer lineNumber : map.keySet()) {
			XSSFCell cell = title.createCell(lineNumber);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(map.get(lineNumber));
		}
		return this;
	}

	/**
	 * 设置数据
	 */
	private ExcelWriter setData() {
		for (int i = 1; i <= datas.size(); i++) {
			Object object = datas.get(i - 1);
			setRowData(i, object);
		}
		return this;
	}

	/**
	 * 添加对象
	 * 
	 * @param row
	 * @param object
	 */
	private void setRowData(int number, Object object) {
		XSSFRow row = sheet.createRow(number);
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			XSSFCell cell = row.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			try {
				if (field != null && field.get(object) != null){
					cell.setCellValue(field.get(object).toString());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e2){
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 获取对象属性
	 * 
	 * @return
	 */
	private Map<Integer, String> getTitleMap() {
		Object object = datas.get(0);// 取出首个对象
		Field[] fields = object.getClass().getDeclaredFields();
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			FieldMeta meta = field.getAnnotation(FieldMeta.class);
			map.put(i, meta.name());
		}
		return map;
	}

	
	public List<? extends Object> getDatas() {
		return datas;
	}

	public void setDatas(List<? extends Object> datas) {
		this.datas = datas;
	}

}
