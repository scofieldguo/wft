package com.bw30.open.common.dao.mapper.parameter;

import java.util.HashMap;
import java.util.Map;
/**
 * 参数构造
 * @author raymond
 *
 */
public class Parameter {

	private Map<String, Object> paramMap;

	private boolean NullFlag = true; // 空值判定,针对字符串空值判定，默认为真

	public Parameter() {
		this.paramMap = new HashMap<String, Object>();
	}

	public Parameter(boolean nullFlag) {
		this.NullFlag = nullFlag;
		this.paramMap = new HashMap<String, Object>();
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void add(String key, Object value) {
		if (checkValue(value)) {
			paramMap.put(key, value);
		}
	}

	/**
	 * 监测Value参数
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkValue(Object value) {
		if (value == null) {
			return false;
		}
		if ((value instanceof String) && NullFlag) {
			if (value.toString().length() < 1) {
				return false;
			}
		}
		return true;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

}
