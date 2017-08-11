package com.bw30.open.wftAdm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.WftOpenTopCardUseMapper;
import com.bw30.open.common.model.stat.WftOpenTopCardUse;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wftAdm.service.operate.IWftOpenTopCardUseService;

public class WftOpenTopCardUseServiceImpl implements IWftOpenTopCardUseService{

	
	private WftOpenTopCardUseMapper wftOpenTopCardUseMapper;
	@Override
	public List<WftOpenTopCardUse> find(String start, String channel,
			Pager pager, String hour) {
		pager.setRecCount(wftOpenTopCardUseMapper.countByCondition(start,channel,hour));
		return wftOpenTopCardUseMapper.find(start,channel,pager,hour);
	}
	public void setWftOpenTopCardUseMapper(
			WftOpenTopCardUseMapper wftOpenTopCardUseMapper) {
		this.wftOpenTopCardUseMapper = wftOpenTopCardUseMapper;
	}
	@Override
	public Integer getChargeTopValue(String label, String daytime, String ssid,String channel) {
		Map<String,Object> paramMap=new HashMap<String,Object>(); 
		paramMap.put("hour",label);
		paramMap.put("dairy", daytime);
		paramMap.put("flag",1 );
		paramMap.put("ssid", ssid);
		paramMap.put("channel", channel);
		if(label.contains(":")){
			String hour=label.split(":")[0];
			paramMap.put("hour", hour);
			paramMap.put("flag", 2);
		}
		return wftOpenTopCardUseMapper.getChargeTopValue(paramMap);
	}
	@Override
	public Integer getTopValue(String label, String daytime, String ssid,String channel,Integer whichData) {
		Map<String,Object> paramMap=new HashMap<String,Object>(); 
		paramMap.put("hour",label);
		paramMap.put("dairy", daytime);
		paramMap.put("flag",1 );
		paramMap.put("ssid", ssid);
		paramMap.put("channel", channel);
		paramMap.put("data", whichData);
		if(label.contains(":")){
			String hour=label.split(":")[0];
			paramMap.put("hour", hour);
			paramMap.put("flag", 2);
		}
		return wftOpenTopCardUseMapper.getTopValue(paramMap);
	}
	@Override
	public List<WftOpenTopCardUse> getlast20(String dairy, String channel,Integer limit) {
		Map<String,Object> paramMap=new HashMap<String,Object>(); 
		paramMap.put("dairy",dairy);
		paramMap.put("channel", channel);
		paramMap.put("limit", limit);
		return wftOpenTopCardUseMapper.getlast20(paramMap);
	}

}
