package com.bw30.open.common.support;

import java.util.HashMap;
import java.util.Map;

public class DynamicSqlParameter {

	@Deprecated
	private Map<String, Object> paramMap;

	public static Map<String, Object> getParamMap(NameValuePair[] nameValuePairs) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (NameValuePair nameValuePair : nameValuePairs) {
			paramMap.put(nameValuePair.getName(), nameValuePair.getValue());
		}
		return paramMap;
	}

	@Deprecated
	public DynamicSqlParameter(String[] strs, Object[] objs) {
		try {
			paramMap = getMap(strs, objs);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	private Map<String, Object> getMap(String[] strs, Object[] objs)
			throws ParameterException {
		if (strs.length != objs.length)
			throw new ParameterException("字段和数值的长度不对应");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < objs.length; i++) {
			map.put(strs[i], objs[i]);
		}
		return map;
	}

	@Deprecated
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

}
