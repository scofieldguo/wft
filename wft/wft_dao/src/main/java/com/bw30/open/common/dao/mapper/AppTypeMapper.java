package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.OpenPlatformAppType;

public interface AppTypeMapper extends GenericMapper<OpenPlatformAppType>{

	List<OpenPlatformAppType> findByStatus(@Param("status")Integer statusOn);

}
