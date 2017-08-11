package com.bw30.open.common.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.wft.common.Pager;

public interface WftOpenStatMapper extends GenericMapper<WftOpenStat>{

	List<WftOpenStat> findPagerByParam(@Param("paramMap")Map<String, Object> paramMap,@Param("pager")Pager pager);

	List<WftOpenStat> findWithParam(@Param("paramMap")Map<String, Object> paramMap);

	int countByChannel(@Param("start")String start,@Param("end") String end,@Param("channel") String channel);

	List<WftOpenStat> findPagerByChannel(@Param("start")String start,@Param("end") String end,@Param("pager") Pager pager,
			@Param("channel")String channel);

	List<WftOpenStat> findByChannel(@Param("start")String start,@Param("end") String end,@Param("channel") String channel);

	public  WftOpenStat findByCondition(@Param("dairy")String dairy,@Param("channel")String channel);

}
