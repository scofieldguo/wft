package com.bw30.openplatform.service.impl;

import java.util.List;

import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftOperator;
import com.bw30.openplatform.service.IWftOperatorService;

public class WftOperatorServiceImpl implements IWftOperatorService{

	private WftOperatorMapper wftOperatorMapper;
	public List<WftOperator> findOperator() {
		return wftOperatorMapper.findAll();
	}
	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}
	public WftOperator findById(Integer id) {
		// TODO Auto-generated method stub
		return wftOperatorMapper.findById(id);
	}

}
