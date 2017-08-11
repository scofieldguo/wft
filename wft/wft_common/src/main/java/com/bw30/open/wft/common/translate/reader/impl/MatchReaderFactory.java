package com.bw30.open.wft.common.translate.reader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bw30.open.wft.common.translate.model.ExcelModel;
import com.bw30.open.wft.common.translate.processor.model.XRow;
import com.bw30.open.wft.common.translate.reader.AbsExcelReader;
import com.bw30.open.wft.common.translate.reader.IReader;

public class MatchReaderFactory {

	private final Integer SHEET_NUM = 1;

	private IReader<ExcelModel> reader;

	private final Logger logger = Logger.getLogger(MatchReaderFactory.class);

	/**
	 * 读取文件
	 * 
	 * @param fileType
	 * @param is
	 */
	public MatchReaderFactory(Integer fileType, InputStream is) {
		try {
			reader = (fileType == 1) ? new FsReader(is) : new BwReader(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<ExcelModel> getResult() {
		return reader.getModels(SHEET_NUM);
	}

	public class BwReader extends AbsExcelReader<ExcelModel> {

		public BwReader(InputStream is) throws Exception {
			super(is);
		}

		@Override
		public void processHandle(Object... objects) {
			// TODO Auto-generated method stub
			Integer sheetId = (Integer) objects[0];
			try {
				this.processByRow(sheetId);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void processRow(XRow row) {
			// TODO Auto-generated method stub
			ExcelModel model = new ExcelModel();
			for (int i = 0; i < row.getCellsSize(); i++) {
				String value = row.getCell(i).getValue();
				switch (i) {
				case 5:
					model.setMobileNum(value);
					break;
				case 1:
					model.setOnline_time(value);
					break;
				case 2:
					model.setOffline_time(value);
					break;
				default:
					break;
				}
			}
			models.add(model);
		}

	}

	public class FsReader extends AbsExcelReader<ExcelModel> {

		private List<ExcelModel> sourceModels = new ArrayList<ExcelModel>();

		private long preTime;

		private ExcelModel preModel;

		public FsReader(InputStream is) throws Exception {
			super(is);
		}

		@Override
		public void processHandle(Object... objects) {
			// TODO Auto-generated method stub
			Integer sheetId = (Integer) objects[0];
			try {
				this.processByRow(sheetId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sourceModels == null || sourceModels.size() < 1) {
				logger.info("Get SourceModels list is empty or null");
				return;
			}
			logger.info("Get SourceModels list is successful, size is="
					+ sourceModels.size());
			for (int i = 1; i < sourceModels.size(); i++) {
				ExcelModel model = sourceModels.get(i);
				if (!mergeData(model)) {
					if (preModel != null) { // 首对象不添加
						models.add(preModel); // 每次添加前一个元素
					}
					preModel = model; // 指针指向当前元素
					preTime = stringToTime(model.getOffline_time());
				}
			}
			models.add(preModel);
		}

		@Override
		public void processRow(XRow row) {
			// TODO Auto-generated method stub
			ExcelModel model = new ExcelModel();
			for (int i = 0; i < row.getCellsSize(); i++) {
				String value = row.getCell(i).getValue();
				switch (i) {
				case 0:
					model.setMobileNum(value);
					break;
				case 1:
					model.setProvince(value);
					break;
				case 4:
					model.setDate(value);
					break;
				case 5:
					model.setOnline_time(value);
					break;
				case 6:
					model.setOffline_time(value);
					break;
				default:
					break;
				}
			}
			sourceModels.add(model);
		}

		/**
		 * 合并对象
		 * 
		 * @param model
		 * @return
		 */
		private boolean mergeData(ExcelModel model) {
			if (preTime == 0L) {
				return false;
			}
			long startTime = stringToTime(model.getOnline_time());
			if ((preTime == startTime)
					&& (preModel.getMobileNum().equals(model.getMobileNum()))) {
				preModel.setOffline_time(model.getOffline_time());
				preModel.setFlag("Yes");
				preTime = stringToTime(model.getOffline_time()); // 时间向后偏移
				return true;
			}
			return false;
		}

		private Long stringToTime(String time) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(time);
				return date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public static void main(String[] args) {
		String fileName = "/Users/raymond/Downloads/北纬账单0601.xlsx";
		try {
			MatchReaderFactory factory = new MatchReaderFactory(0, new FileInputStream(new File(fileName)));
			List<ExcelModel> list = factory.getResult();
			System.out.println(list.size());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
