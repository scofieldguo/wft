package com.bw30.open.wft.common.translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel2007-2011
 * 
 * @author raymond
 *
 */
public class ExcelReader {

	public ExcelReader(InputStream is) {
		try {
			workbook = new XSSFWorkbook(is);
			System.out.println("读取文件完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ExcelReader(String filePath) {
		try {
			InputStream is = new FileInputStream(new File(filePath));
			System.out.println("文件生成流成功");
			workbook = new XSSFWorkbook(is);
			System.out.println("读取文件完成");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 构造工作区 by index
	 * 
	 * @param index
	 * @return
	 */
	public XSSFSheet getSheet(int index) {
		System.out.println("getSheet");
		return workbook.getSheetAt(index);
	}

	/**
	 * 构造工作区 by name
	 * 
	 * @param name
	 * @return
	 */
	public XSSFSheet getSheet(String name) {
		return workbook.getSheet(name);
	}

	/**
	 * 获取行数据列表 by index
	 * 
	 * @param index
	 * @param beginIndex
	 * @return
	 */
	public List<XSSFRow> getRows(int index, int beginIndex) {
		XSSFSheet sheet = getSheet(index);
		System.out.println("getSheet is success");
		List<XSSFRow> rows = createRows(sheet, beginIndex);
		return rows;
	}

	/**
	 * 获取行数据列表 by name
	 * 
	 * @param name
	 * @param beginIndex
	 * @return
	 */
	public List<XSSFRow> getRows(String name, int beginIndex) {
		XSSFSheet sheet = getSheet(name);
		List<XSSFRow> rows = createRows(sheet, beginIndex);
		return rows;
	}

	/**
	 * 获取单元格Cell数据
	 * 
	 * @param row
	 * @return
	 */
	public Map<Integer, XSSFCell> getCell(XSSFRow row) {
		Map<Integer, XSSFCell> map = new HashMap<Integer, XSSFCell>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			XSSFCell cell = row.getCell(i);
			map.put(i, cell);
		}
		return map;
	}

	/**
	 * 获取单元格Cell 字符串
	 * 
	 * @param row
	 * @return
	 */
	public Map<Integer, String> getCellValue(XSSFRow row) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			XSSFCell cell = row.getCell(i);
			String value = cellToString(cell);
			map.put(i, value);
		}
		return map;
	}

	/**
	 * 转换Cell数据 to String
	 * 
	 * @param cell
	 * @return
	 */
	private String cellToString(XSSFCell cell) {
		String value = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)){
				Date date = cell.getDateCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				value = sdf.format(date);
			}else{
				value += (long) cell.getNumericCellValue();
			}
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * 构造行数据列表
	 * 
	 * @param sheet
	 * @param beginIndex
	 * @return
	 */
	private List<XSSFRow> createRows(XSSFSheet sheet, int beginIndex) {
		List<XSSFRow> rows = new ArrayList<XSSFRow>();
		
		for (int i = beginIndex; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			rows.add(row);
		}
		System.out.println("rows create success");
		return rows;
	}

	private XSSFWorkbook workbook;

}
