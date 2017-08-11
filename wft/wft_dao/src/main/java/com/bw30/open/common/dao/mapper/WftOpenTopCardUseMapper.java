package com.bw30.open.common.dao.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftOpenTopCardUse;
import com.bw30.open.wft.common.Pager;

public interface WftOpenTopCardUseMapper extends GenericMapper<WftOpenTopCardUse>{

	List<WftOpenTopCardUse> find(@Param("start")String start,@Param("channel") String channel,@Param("pager") Pager pager,
			@Param("hour")String hour);

	int countByCondition(@Param("start")String start,@Param("channel") String channel,@Param("hour") String hour);

	Integer getChargeTopValue(@Param("paramMap")Map<String, Object> paramMap);

	Integer getTopValue(@Param("paramMap")Map<String, Object> paramMap);

	List<WftOpenTopCardUse> getlast20(@Param("paramMap")Map<String, Object> paramMap);

}
