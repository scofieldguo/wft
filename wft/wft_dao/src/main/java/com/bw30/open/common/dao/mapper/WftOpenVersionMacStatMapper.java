package com.bw30.open.common.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.versionstat.WftOpenVersionMacStat;

public interface WftOpenVersionMacStatMapper extends GenericMapper<WftOpenVersionMacStat>{

	void creatTable(@Param("code")String channel);

	void insertMac(@Param("wftOpenVersionMacStat")WftOpenVersionMacStat wftMac,@Param("code") String channel);

	int statMac(@Param("dairy")String dayYMD,@Param("code") String channel,@Param("version") String version,@Param("flag") int i,@Param("op") String operator);

}
