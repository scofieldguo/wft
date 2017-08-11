package com.bw30.open.common.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftChannel;

public interface WftChannelMapper extends GenericMapper<WftChannel>{
	/**
	 * 更新开卡数
	 */
	public void updateCnum(@Param("num") Integer num, @Param("id") String id);
	
}
