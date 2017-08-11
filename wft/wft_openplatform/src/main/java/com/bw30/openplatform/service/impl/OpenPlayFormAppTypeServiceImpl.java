package com.bw30.openplatform.service.impl;

import java.util.List;

import com.bw30.open.common.dao.mapper.AppTypeMapper;
import com.bw30.open.common.model.OpenPlatformAppType;
import com.bw30.openplatform.service.IOpenPlayFormAppTypeService;

public class OpenPlayFormAppTypeServiceImpl implements IOpenPlayFormAppTypeService{
	private AppTypeMapper appTypeMapper;
	public List<OpenPlatformAppType> findAppType(Integer statusOn) {
		return appTypeMapper.findByStatus(statusOn);
	}
	public void setAppTypeMapper(AppTypeMapper appTypeMapper) {
		this.appTypeMapper = appTypeMapper;
	}

}
