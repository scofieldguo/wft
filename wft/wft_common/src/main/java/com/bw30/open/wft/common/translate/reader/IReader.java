package com.bw30.open.wft.common.translate.reader;

import java.util.List;

public interface IReader<T> {
	
	/**
	 * 读取Excel结果
	 * @param args
	 * @return
	 */
	public List<T> getModels(Object... objects);

}
