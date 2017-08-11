package com.bw30.open.wftAdm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.dao.mapper.WftProvinceMapper;
import com.bw30.open.common.dao.mapper.WftTotalStatMapper;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wftAdm.service.operate.IWftOpenStatProSuccessService;

public class WftOpenStatProSuccessServiceImpl implements IWftOpenStatProSuccessService{
	private WftOperatorMapper wftOperatorMapper;
	private WftTotalStatMapper wftTotalStatMapper;
	private WftProvinceMapper wftProvinceMapper;
	
	@Override
	public List<WftOperator> findAllOperator() {
		return wftOperatorMapper.findAll();
	}
	
	@Override
	public List<WftProvince> findAllWftProvince() {
		return wftProvinceMapper.findAll();
	}
	
	@Override
	public List<WftTotalStat> findByProvice(String ssid,
			String start, String end) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ssid", ssid);
		paramMap.put("start", start);
		paramMap.put("end", end);
		return wftTotalStatMapper.findByParamGroupByPrv(paramMap);
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public void setWftTotalStatMapper(WftTotalStatMapper wftTotalStatMapper) {
		this.wftTotalStatMapper = wftTotalStatMapper;
	}

	public void setWftProvinceMapper(WftProvinceMapper wftProvinceMapper) {
		this.wftProvinceMapper = wftProvinceMapper;
	}

}
