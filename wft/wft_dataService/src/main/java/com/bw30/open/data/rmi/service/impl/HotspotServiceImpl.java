package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftHotspotMapper;
import com.bw30.open.common.model.WftHotspot;
import com.bw30.open.common.rmi.service.IHotspotService;

public class HotspotServiceImpl implements IHotspotService  {
	@Resource
	private WftHotspotMapper wftHotspotMapper;
	
	public void insertHotspot(WftHotspot wftHotspot) throws Exception{
		wftHotspotMapper.insert(wftHotspot);
	}

	public List<WftHotspot> getHotspotByMac(String mac) throws Exception{
		List<WftHotspot> list = wftHotspotMapper.findByMac(mac);
		return list;
	}
	
	public void updateHotspot(WftHotspot wftHotspot) throws Exception{
		wftHotspotMapper.update(wftHotspot);
	}

	public void setWftHotspotMapper(WftHotspotMapper wftHotspotMapper) {
		this.wftHotspotMapper = wftHotspotMapper;
	}
	
}
