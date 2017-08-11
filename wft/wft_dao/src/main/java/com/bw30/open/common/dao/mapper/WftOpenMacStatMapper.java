package com.bw30.open.common.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftOpenMacStat;

public interface WftOpenMacStatMapper extends  GenericMapper<WftOpenMacStat>{

	void creatTable(@Param("code")String code);

	void insertMac(@Param("wftOpenMacStat")WftOpenMacStat wftQQMac,@Param("code") String code);

	int findSuccByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("code") String code);

	int findFailByParam(@Param("paramMap")Map<String, Object> paramMap, @Param("code")String code);

	int findFailOTByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("code") String code);

}
