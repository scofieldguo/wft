package com.bw30.open.wft.common.translate.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.bw30.open.wft.common.translate.processor.ExcelProcessor;

public abstract class AbsExcelReader<T> extends ExcelProcessor implements IReader<T> {
	
	public AbsExcelReader(InputStream is) throws Exception {
		super(is);
	}

	public AbsExcelReader(String fileName) throws Exception {
		super(fileName);
	}

	protected List<T> models = new ArrayList<T>();
	
	public List<T> getModels(Object... objects){
		this.processHandle(objects);
		return models;
	}
	
	/**
	 * 自定义操作
	 * @param objects
	 */
	public abstract void processHandle(Object... objects);
	
}
