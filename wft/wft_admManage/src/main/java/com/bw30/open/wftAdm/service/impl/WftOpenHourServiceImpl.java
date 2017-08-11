package com.bw30.open.wftAdm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.WftOpenHourSuccessStatMapper;
import com.bw30.open.common.model.stat.WftOpenHourSuccessStat;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wftAdm.service.operate.IWftOpenHourService;

public class WftOpenHourServiceImpl implements IWftOpenHourService{
	
	private WftOpenHourSuccessStatMapper wftOpenHourSuccessStatMapper;

	@Override
	public List<WftOpenHourSuccessStat> findByParam(String dairy, Pager pager,String channel) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("dairy", dairy);
		paramMap.put("channel",channel);
		pager.setRecCount(wftOpenHourSuccessStatMapper.countByParam(paramMap));
		return wftOpenHourSuccessStatMapper.pageFindByParam(paramMap, pager);
	}

	public void setWftOpenHourSuccessStatMapper(
			WftOpenHourSuccessStatMapper wftOpenHourSuccessStatMapper) {
		this.wftOpenHourSuccessStatMapper = wftOpenHourSuccessStatMapper;
	}

}
