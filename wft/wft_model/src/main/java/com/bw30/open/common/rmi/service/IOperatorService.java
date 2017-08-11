package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftOperator;

public interface IOperatorService {

	/**
	 * 通过ssid获取运营商信息
	 * @param ssid
	 * @return
	 * @throws Exception
	 */
	public List<WftOperator> findBySsid(String ssid) throws Exception;
}
