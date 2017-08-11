package com.bw30.open.data.rmi.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftProvinceMapper;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.common.rmi.service.IProvinceService;

public class ProvinceServiceImpl implements IProvinceService {

	@Resource
	private WftProvinceMapper wftProvinceMapper;
	
	public WftProvince findById(Integer id) throws Exception {
		WftProvince wftProvince = wftProvinceMapper.findById(id);
		return wftProvince;
	}
	
	public List<WftProvince> findByParam(Map<String, Object> paramMap) throws Exception{
		List<WftProvince> list = wftProvinceMapper.findByParam(paramMap);
		return list;
	}
	
	public List<WftProvince> findByName(String name) throws Exception{
		List<WftProvince> list = wftProvinceMapper.findByName(name);
		return list;
	}
	
	public List<WftProvince> findByNameRegx(String name) throws Exception{
		List<WftProvince> list = wftProvinceMapper.findByNameRegx(name);
		return list;
	}

	public void setWftProvinceMapper(WftProvinceMapper wftProvinceMapper) {
		this.wftProvinceMapper = wftProvinceMapper;
	}
	
}
