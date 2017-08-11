package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftProvince;

public interface WftProvinceMapper extends GenericMapper<WftProvince>{
	
	public List<WftProvince> findByName(@Param("name") String name);
	
	/**
	 * 模糊匹配，已name开头
	 * 
	 * @param name
	 * @return
	 */
	public List<WftProvince> findByNameRegx(@Param("name") String name);
	
}
