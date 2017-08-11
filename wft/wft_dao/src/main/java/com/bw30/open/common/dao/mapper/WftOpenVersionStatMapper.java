package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.versionstat.WftOpenVersionStat;

public interface WftOpenVersionStatMapper extends GenericMapper<WftOpenVersionStat>{

	List<WftOpenVersionStat> findVersionStat(@Param("dairy")String dairy,@Param("version") String version,@Param("channel") String channel);

}
