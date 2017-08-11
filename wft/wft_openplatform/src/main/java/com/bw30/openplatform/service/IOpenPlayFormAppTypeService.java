package com.bw30.openplatform.service;

import java.util.List;

import com.bw30.open.common.model.OpenPlatformAppType;


public interface IOpenPlayFormAppTypeService {

	List<OpenPlatformAppType> findAppType(Integer statusOn);

}
