package com.bw30.open.wft.common.translate.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bw30.open.wft.common.translate.processor.model.XRow;

/**
 * Excel处理器，兼容Excel-2003与Excel-2007
 * 
 * @author raymond
 * @version 1.0
 */
public abstract class ExcelProcessor implements ExcelRowProcessor {
	private ExcelRowProcessor processor;

	public ExcelProcessor(String fileName) throws Exception {
		if (fileName == null || fileName.equals("")) {
			throw new Exception("构造Excel导入器失败，未指定文件全名。");
		}
		File file = new File(fileName);
		if (!file.exists()) {
			throw new Exception("构造Excel导入器失败，指定的文件不存在：" + fileName);
		}
		if (fileName.endsWith("xls")) {
			processor = new MyExcel2003RowProcessor(fileName);
		} else {
			processor = new MyExcel2007RowProcessor(fileName);
		}
	}
	
	public ExcelProcessor(File file) throws Exception{
		String fileName = file.getName();
		InputStream is = new FileInputStream(file);
		if (fileName.endsWith("xls")){
			processor = new MyExcel2003RowProcessor(is);
		}else{
			processor = new MyExcel2007RowProcessor(is);
		}
	}
	
	
	/**
	 * 默认处理2007版本
	 * @param is
	 * @throws Exception
	 */
	public ExcelProcessor(InputStream is) throws Exception {
		processor = new MyExcel2007RowProcessor(is);
	}

	public void processByRow() throws Exception {
		processor.processByRow();
	}

	public void processByRow(int sheetIndex) throws Exception {
		processor.processByRow(sheetIndex);
	}

	public void stop() throws IOException {
		processor.stop();
	}

	public abstract void processRow(XRow row);

	private class MyExcel2003RowProcessor extends Excel2003RowProcessor {
		public MyExcel2003RowProcessor(String filename) throws Exception {
			super(filename);
		}

		public MyExcel2003RowProcessor(InputStream is) throws Exception {
			super(is);
		}
		
		@Override
		public void processRow(XRow row) {
			ExcelProcessor.this.processRow(row);
		}
	}

	private class MyExcel2007RowProcessor extends Excel2007RowProcessor {
		public MyExcel2007RowProcessor(String filename) throws Exception {
			super(filename);
		}

		public MyExcel2007RowProcessor(InputStream is) throws Exception {
			super(is);
		}

		@Override
		public void processRow(XRow row) {
			ExcelProcessor.this.processRow(row);
		}
	}
}
