package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftHotspot;

public interface IHotspotService {

	/**
	 * 热点插入
	 * @param wftHotspot
	 * @throws Exception
	 */
	public void insertHotspot(WftHotspot wftHotspot) throws Exception;
	
	/**
	 * 通过mac获取热点信息
	 * @param mac
	 * @return
	 * @throws Exception
	 */
	public List<WftHotspot> getHotspotByMac(String mac) throws Exception;
	
	/**
	 * 更新热点信息
	 * @param wftHotspot
	 * @throws Exception
	 */
	public void updateHotspot(WftHotspot wftHotspot) throws Exception;
}
