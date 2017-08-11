package com.bw30.open.common.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftTenMinLoginStat;

public interface WftTenMinLoginStatMapper extends GenericMapper<WftTenMinLoginStat> {
	
	/**
	 * 获取时间段内每小时汇总数据
	 * @param paramMap
	 * @return
	 */
	public List<WftTenMinLoginStat> findEveryHourConnStat(@Param("paramMap") Map<String, Object> paramMap);
	
	
	/**
	 * 获取时间段内每天每小时数据
	 * @param paramMap
	 * @return
	 */
	public List<WftTenMinLoginStat> findEveryHourConnStatByDay(@Param("paramMap") Map<String, Object> paramMap);

}
