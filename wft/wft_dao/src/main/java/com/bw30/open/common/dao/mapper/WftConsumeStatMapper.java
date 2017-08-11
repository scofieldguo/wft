package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;

public interface WftConsumeStatMapper extends GenericMapper<WftConsumeStat> {
	public List<WftConsumeStat> findByCondition(@Param("dairy") String dairy,
			@Param("channel") String channel, @Param("opid") Integer opid);

	public List<Integer> getUsedTime(@Param("firstDayOfMonth")String firstDayOfMonth, @Param("today")String today,
			@Param("channel")String channelcode,@Param("opid") int opIdCtcc);
	
	public void deleteByDairy(@Param("dairy") String dairy);
	

	List<WftConsumeStat> getIntervalStat(@Param("startday")String startday,@Param("endday")String endday,
			@Param("channelcode")String channelcode,@Param("opIdCtcc") int opIdCtcc);

}

