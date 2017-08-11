package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftHotspot;

public interface WftHotspotMapper extends GenericMapper<WftHotspot>{
	/**
	 * 查询热点
	 * 
	 * @param mac
	 * @return
	 */
	public List<WftHotspot> findByMac(@Param("mac") String mac);
	
}
