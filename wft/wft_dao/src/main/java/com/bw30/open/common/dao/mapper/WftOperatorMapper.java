package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftOperator;

public interface WftOperatorMapper extends GenericMapper<WftOperator>{
	public List<WftOperator> findBySsid(@Param("ssid") String ssid);
	
	public List<WftOperator> findByName(@Param("name") String name);
}