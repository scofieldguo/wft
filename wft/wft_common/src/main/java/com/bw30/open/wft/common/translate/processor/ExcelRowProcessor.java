package com.bw30.open.wft.common.translate.processor;

import java.io.IOException;

import com.bw30.open.wft.common.translate.processor.model.XRow;

/**
 * 接口，Excel行级处理器
 * 
 * @author raymond
 * @version 1.0
 */
public interface ExcelRowProcessor {
	
	public void processByRow() throws Exception;

	public void processByRow(int sheetIndex) throws Exception;

	public void processRow(XRow row);

	public void stop() throws IOException;
	
}
