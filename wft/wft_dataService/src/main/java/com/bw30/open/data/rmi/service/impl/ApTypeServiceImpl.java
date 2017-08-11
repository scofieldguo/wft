package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftApTypeMapper;
import com.bw30.open.common.model.WftApType;
import com.bw30.open.common.rmi.service.IApTypeService;

public class ApTypeServiceImpl implements IApTypeService{
	@Resource
	private WftApTypeMapper wftApTypeMapper;
	public void setWftApTypeMapper(WftApTypeMapper wftApTypeMapper) {
		this.wftApTypeMapper = wftApTypeMapper;
	}

	public WftApType findById(String ssid) {
		return this.wftApTypeMapper.findById(ssid);
	}

	@Override
	public List<WftApType> findByOpid(Integer opid) {
		return this.wftApTypeMapper.findByOpid(opid);
	}
	
}
