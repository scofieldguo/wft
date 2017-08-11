package com.bw30.openplatform.service.impl;

import java.util.List;

import com.bw30.open.common.dao.mapper.AppMapper;
import com.bw30.open.common.model.OpenPlatformApp;
import com.bw30.openplatform.service.IOpenPlayFormAppService;

public class OpenPlayFormAppServiceImpl implements IOpenPlayFormAppService{

	private AppMapper appMapper;

	public List<OpenPlatformApp> getAppByUserId(Integer id) {
		return appMapper.getAppByUserId(id);
	}
	
	public OpenPlatformApp getLastCreatApp(Integer id) {
		return appMapper.getLastCreatApp(id);
	}
	
	public void updateApp(OpenPlatformApp userapp) {
		appMapper.update(userapp);
	}

	public void addUserApp(OpenPlatformApp userapp) {
		appMapper.insert(userapp);
		
	}
	public void setAppMapper(AppMapper appMapper) {
		this.appMapper = appMapper;
	}

	public OpenPlatformApp findById(Integer id) {
		// TODO Auto-generated method stub
		return appMapper.findById(id);
	}

}
