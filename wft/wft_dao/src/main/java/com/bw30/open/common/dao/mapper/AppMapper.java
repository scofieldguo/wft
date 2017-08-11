package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.OpenPlatformApp;

public interface AppMapper extends GenericMapper<OpenPlatformApp>{

	List<OpenPlatformApp> getAppByUserId(@Param("uid")Integer uid);

	OpenPlatformApp getLastCreatApp(@Param("uid")Integer id);

}
