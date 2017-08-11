package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.rmi.service.IOperatorService;

public class OperatorServiceImpl implements IOperatorService {

	@Resource
	private WftOperatorMapper wftOperatorMapper;
	
	public List<WftOperator> findBySsid(String ssid) throws Exception{
		List<WftOperator> list = wftOperatorMapper.findBySsid(ssid);
		return list;
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}
	
}
