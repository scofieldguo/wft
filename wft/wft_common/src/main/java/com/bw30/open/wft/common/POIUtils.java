package com.bw30.open.wft.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class POIUtils {
	private static final int SIZE_OF_SHEET = 50000;//excel单个sheet大小
	/**
	 * 创建excel，50000行/sheet
	 * @param dataList
	 * @param titleNameList
	 * @param nameMap
	 * @return
	 */
	public static HSSFWorkbook exportExcelWithSheets(List<Map<String, Object>> dataList,
			List<String> titleNameList, Map<String, String> nameMap) {
		HSSFWorkbook workbook = null;
		try {
			// 创建工作簿实例
			workbook = new HSSFWorkbook();
			// 获取样式
			HSSFCellStyle cellStyle = createTitleStyle(workbook);
			if (dataList != null && dataList.size() > 0) {
				int size = dataList.size();
				int num = size/SIZE_OF_SHEET;
				num = size%SIZE_OF_SHEET > 0 ? num + 1 : num;//sheet数
				for(int i = 1; i <= num; i++){
					HSSFSheet sheet = workbook.createSheet("sheet" + i);//创建sheet
					setSheetColumnWidth(sheet, titleNameList.size());// 设置列宽
					// 创建第一行标题
					HSSFRow titleRow = sheet.createRow(0);
					for (int j = 0; j < titleNameList.size(); j++) {
						createCell(titleRow, j, cellStyle,
								HSSFCell.CELL_TYPE_STRING, titleNameList.get(j));
					}
					
					List<Map<String, Object>> list = null;
					if(i < num){
						list = dataList.subList((i-1)*SIZE_OF_SHEET, i*SIZE_OF_SHEET);
					}else{
						list = dataList.subList((i-1)*SIZE_OF_SHEET, size);
					}
					for (int k = 0, s = list.size(); k < s; k++) {
						Map<String, Object> map = (Map<String, Object>) list.get(k);
						HSSFRow row = sheet.createRow(k + 1);
						for (int m = 0; m < titleNameList.size(); m++) {
							if (map.get(nameMap.get(titleNameList.get(m))) != null) {
								createCell(row, m, cellStyle,
										HSSFCell.CELL_TYPE_STRING, map.get(nameMap
												.get(titleNameList.get(m))));
							}
						}
					}
					
				}
			} else {
				HSSFSheet sheet = workbook.createSheet("sheet1");
				setSheetColumnWidth(sheet, titleNameList.size());
				createCell(sheet.createRow(0), 0, cellStyle,
						HSSFCell.CELL_TYPE_STRING, "无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	
	public static HSSFWorkbook exportExcel(List<Map<String, Object>> dataList,
			List<String> titleNameList, Map<String, String> nameMap) {
		HSSFWorkbook workbook = null;
		try {
			// 创建工作簿实例
			workbook = new HSSFWorkbook();
			// 创建工作表实例
			HSSFSheet sheet = workbook.createSheet("sheet1");
			// 设置列宽
			setSheetColumnWidth(sheet, titleNameList.size());
			// 获取样式
			HSSFCellStyle cellStyle = createTitleStyle(workbook);
			if (dataList != null && dataList.size() > 0) {
				// 创建第一行标题
				HSSFRow titleRow = sheet.createRow(0);// 建立新行
				for (int i = 0; i < titleNameList.size(); i++) {
					createCell(titleRow, i, cellStyle,
							HSSFCell.CELL_TYPE_STRING, titleNameList.get(i));
				}
				// 给excel填充数据
				for (int i = 0; i < dataList.size(); i++) {
					Map<String, Object> map = (Map<String, Object>) dataList
							.get(i);
					HSSFRow row = sheet.createRow(i + 1);
					for (int j = 0; j < titleNameList.size(); j++) {
						if (map.get(nameMap.get(titleNameList.get(j))) != null) {
							createCell(row, j, cellStyle,
									HSSFCell.CELL_TYPE_STRING, map.get(nameMap
											.get(titleNameList.get(j))));
						}
					}
				}
			} else {
				createCell(sheet.createRow(0), 0, cellStyle,
						HSSFCell.CELL_TYPE_STRING, "无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	@SuppressWarnings("deprecation")
	private static void setSheetColumnWidth(HSSFSheet sheet, int len) {
		// 根据你数据里面的记录有多少列，就设置多少列
		for (int i = 0; i < len; i++) {
			sheet.setColumnWidth((short) i, (short) 3000);
		}
	}

	// 设置excel的title样式
	private static HSSFCellStyle createTitleStyle(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setFontHeight((short) 200);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,##0.00"));// 货币型
		return cellStyle;
	}

	// 创建Excel单元格
	@SuppressWarnings("deprecation")
	private static void createCell(HSSFRow row, int column,
			HSSFCellStyle style, int cellType, Object value) {
		HSSFCell cell = row.createCell((short) column);
		if (style != null) {
			cell.setCellStyle(style);
		}
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_STRING:
			cell.setCellValue(new HSSFRichTextString(value.toString()));
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.parseDouble(value.toString()));
			break;
		default:
			break;
		}
	}

	public static List<Map<String, Object>> importExcel(File file) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			POIFSFileSystem poiFileSystem = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem);
			if (workbook.getNumberOfSheets() > 0) {
				HSSFSheet sheet = workbook.getSheetAt(0);
				Map<Integer, String> keyMap = new HashMap<Integer, String>();
				System.out.println("差"+(sheet.getLastRowNum()-sheet.getFirstRowNum()+1));
			//	sheet.getRow(2).getCell(1).getStringCellValue();
				Iterator<Row> rowIter = sheet.rowIterator();
				if (rowIter.hasNext()) {
					// 获取头
					Row headRow = rowIter.next();
					Iterator<Cell> cellIter = headRow.cellIterator();
					int columnCount = 0;
					while (cellIter.hasNext()) {
						Cell cell = cellIter.next();
						 System.out.println("列名@@"+cell.getStringCellValue());// 列名
						keyMap.put(columnCount++, cell.getStringCellValue());
					}
					// 获取内容体
					while (rowIter.hasNext()) {
						Map<String, Object> tmap = new HashMap<String, Object>();
						Row contentRow = rowIter.next();
						for (int index = 0; index < columnCount; index++) {
							Cell contentCell = contentRow
									.getCell((short) index);
							Object cellValue = getCellValue(contentCell);
							// System.out.println("key=>" + keyMap.get(index)
							// + ",value=>" + cellValue);
							tmap.put(keyMap.get(index), cellValue);
						}
						resultList.add(tmap);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}

	private static Object getCellValue(Cell contentCell) {
		Object cellValue = "";
		if (contentCell != null) {
			try {
				if (contentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					cellValue = contentCell.getRichStringCellValue()
							.getString();
				} else if (contentCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					cellValue = contentCell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date when = sdf.parse("2000-01-01");
					if (!((Date) cellValue).after(when)) {
						String cellValueStr = contentCell.getNumericCellValue()
								+ "";
						String str = cellValueStr.substring(0, cellValueStr
								.indexOf("."));
						if (new BigDecimal(str).compareTo(new BigDecimal(
								cellValueStr)) == 0) {
							cellValue = str;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return cellValue;
	}

	public static void main(String[] args) {
		String s = "D:\\resource\\201207\\1342679848716.xls";
		List<Map<String, Object>> list = importExcel(new File(
		/* "d:/user.xls" */s));
		System.out.println(list + "=" + list.size());
	}
}
