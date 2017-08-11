package com.bw30.open.wft.common.translate.reader.impl;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.bw30.open.wft.common.translate.model.CmccCardModel;
import com.bw30.open.wft.common.translate.processor.model.XRow;
import com.bw30.open.wft.common.translate.reader.AbsExcelReader;
import com.bw30.open.wft.common.translate.reader.AbsTxtReader;
import com.bw30.open.wft.common.translate.reader.IReader;

public class WlanReaderFactory {

	private IReader<CmccCardModel> reader;

	private final Logger logger = Logger.getLogger(WlanReaderFactory.class);

	public WlanReaderFactory(Integer fileType, InputStream is) {
		try {
			reader = (fileType == 1) ? new TxtReader(is) : new ExcelReader(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CmccCardModel> getResult() {
		return reader.getModels();
	}

	public class TxtReader extends AbsTxtReader<CmccCardModel> {

		public TxtReader(InputStream is) throws Exception {
			super(is);
		}

		@Override
		public void processHandle(String string, Object... objects) {
			// TODO Auto-generated method stub
			String[] strings = string.split(",");
			if (strings == null || strings.length < 1) {
				logger.error("Split string is null");
				return;
			}
			CmccCardModel model = new CmccCardModel();
			for (int i = 0; i < strings.length; i++) {
				String value = strings[i];
				logger.info("Translate tmpLine:num=" + i + ",get value="
						+ value);
				switch (i) {
				case 0:
					model.setMobileNo(value);
					break;
				case 1:
					model.setPassword(value);
					break;
				case 2:
					model.setBvalue(value);
					break;
				default:
					break;
				}
			}
			models.add(model);
		}
	}

	public class ExcelReader extends AbsExcelReader<CmccCardModel> {

		private final Integer SHEET_NUM = 1;

		public ExcelReader(InputStream is) throws Exception {
			super(is);
		}

		@Override
		public void processHandle(Object... objects) {
			// TODO Auto-generated method stub
			try {
				this.processByRow(SHEET_NUM);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void processRow(XRow row) {
			// TODO Auto-generated method stub
			CmccCardModel model = new CmccCardModel();
			for (int i = 0; i < row.getCellsSize(); i++) {
				String value = row.getCell(i).getValue();
				switch (i) {
				case 0:
					model.setMobileNo(value);
					break;
				case 1:
					model.setPassword(value);
					break;
				case 2:
					model.setBvalue(value);
					break;
				default:
					break;
				}
			}
			if (row.getRowIndex() > 0) {
				models.add(model);
			}
		}

	}

}
