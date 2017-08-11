package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftApType;

public interface WftApTypeMapper extends GenericMapper<WftApType> {

	List<WftApType> findByOpid(@Param("opid") Integer opid);
	
}
