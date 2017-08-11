package com.bw30.open.common.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftMacStat;

public interface WftMacStatMapper extends  GenericMapper<WftMacStat>{

	void creatTable(@Param("channel")String channel);

	void insertMac(@Param("wftMacStat")WftMacStat wftMacStat,@Param("channel") String channel);

	int findSuccByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("channel") String channel);

	int findFailByParam(@Param("paramMap")Map<String, Object> paramMap, @Param("channel")String channel);

	int findFailOTByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("channel") String channel);

}
