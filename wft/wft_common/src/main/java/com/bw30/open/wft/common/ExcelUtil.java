package com.bw30.open.wft.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	/**
	 * 输出Excel
	 * 
	 * @author 44RTV2X
	 * 
	 */
	public static void PrintExcel(HSSFWorkbook workbook, String excelName,
			HttpServletResponse response) {
		response.setContentType("application/vnd.ms-excel");// 设置生成的文件类型
		// 设置文件头编码方式和文件名
		try {
			response.setHeader("Content-Disposition", "filename="
					+ new String(excelName.getBytes("utf-8"), "iso8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printExcel(HSSFWorkbook workbook, String excelName,
			HttpServletResponse response) {
		response.setContentType("application/vnd.ms-excel");// 设置生成的文件类型
		// 设置文件头编码方式和文件名
		try {
			response.setHeader("Content-Disposition", "filename="
					+ new String(excelName.getBytes("gbk"), "iso8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static HSSFWorkbook exportToExcel(String[] titleNameArr,
			List<Object[]> dataList) {
		Map<String, String> nameMap = new HashMap<String, String>();
		int length = titleNameArr.length;
		for (int i = 0; i < length; i++) {
			nameMap.put(titleNameArr[i], titleNameArr[i]);
		}
		List<Map<String, Object>> realDataList = new ArrayList<Map<String, Object>>();
		for (Object[] dataArr : dataList) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for (int i = 0; i < length; i++) {
				dataMap.put(titleNameArr[i], dataArr[i]);
			}
			realDataList.add(dataMap);
		}
		List<String> titleNameList = Arrays.asList(titleNameArr);
		return POIUtils.exportExcel(realDataList, titleNameList, nameMap);
	}
	
	/**
	 * 读取excel内容
	 * 2003
	 * */
	public static List<Map<String,Object>> readXls(InputStream in){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		try{
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(in);
			for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if(hssfSheet == null)continue;
				HSSFRow firstRow = hssfSheet.getRow(0);//第一行，标题
				int minCol = firstRow.getFirstCellNum();
				int maxCol = firstRow.getLastCellNum();
				for(int rowNum=1;rowNum<=hssfSheet.getLastRowNum();rowNum++){
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					Map<String,Object> titleMap = new HashMap<String, Object>();
					for(int col=minCol;col<maxCol;col++){
						HSSFCell cell = hssfRow.getCell(col);
						if(cell==null) continue;
						titleMap.put(firstRow.getCell(col).toString(), cell.toString());
					}
					result.add(titleMap);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 读取excel内容
	 * 2007
	 * */
	public static List<Map<String,Object>> readXlsx(InputStream in){
		System.out.println("===================================<>");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		try{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
//			for(int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++){
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);//只取第一个sheet
				XSSFRow firstRow = xssfSheet.getRow(0);//第一行，标题
				int minCol = firstRow.getFirstCellNum();
				int maxCol = firstRow.getLastCellNum();
				for(int rowNum = 1;rowNum<=xssfSheet.getLastRowNum();rowNum++){
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					Map<String,Object> titleMap = new HashMap<String, Object>();
					for(int col=minCol;col<maxCol;col++){
						XSSFCell cell = xssfRow.getCell(col);
						if(cell==null||"".equals(cell.toString().trim()))continue;
						String cellString = getStringVal(cell);
						titleMap.put(firstRow.getCell(col).toString(), cellString);
					}
					result.add(titleMap);
				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	private static String getStringVal(XSSFCell cell){
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue()?"true":"false";
		case Cell.CELL_TYPE_ERROR:
			return ErrorEval.getText(cell.getErrorCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().toString();
		default:
			return null;
		}
	}
}
