package com.bw30.openplatform.service;

import java.util.List;

import com.bw30.open.common.model.OpenPlatformApp;

public interface IOpenPlayFormAppService {

	List<OpenPlatformApp> getAppByUserId(Integer id);

	void addUserApp(OpenPlatformApp userapp);

	OpenPlatformApp getLastCreatApp(Integer id);
	
	OpenPlatformApp findById(Integer id);

	void updateApp(OpenPlatformApp userapp);

}
