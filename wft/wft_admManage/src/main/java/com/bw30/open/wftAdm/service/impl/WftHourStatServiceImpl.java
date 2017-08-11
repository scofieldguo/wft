package com.bw30.open.wftAdm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftTenMinLoginStatMapper;
import com.bw30.open.common.model.stat.WftTenMinLoginStat;

public class WftHourStatServiceImpl {
	
	@Resource
	private WftTenMinLoginStatMapper wftTenMinLoginStatMapper;

	public List<WftTenMinLoginStat> findEveryHourConnStat(String startDate,String endDate,Integer opid,String channel){
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("opid", opid);
		paramMap.put("channel", channel);
		List<WftTenMinLoginStat> list = wftTenMinLoginStatMapper.findEveryHourConnStat(paramMap);
		return list;
	}
	
	public List<WftTenMinLoginStat> findEveryHourConnStatByDay(String startDate,String endDate,Integer opid,String channel,Integer hour){
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("opid", opid);
		paramMap.put("channel", channel);
		paramMap.put("hour", hour);
		List<WftTenMinLoginStat> list = wftTenMinLoginStatMapper.findEveryHourConnStatByDay(paramMap);
		return list;
	}

	public void setWftTenMinLoginStatMapper(
			WftTenMinLoginStatMapper wftTenMinLoginStatMapper) {
		this.wftTenMinLoginStatMapper = wftTenMinLoginStatMapper;
	}
	
}
