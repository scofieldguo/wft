package com.bw30.open.common.rmi.service;

import com.bw30.open.common.model.OpenPlatformUser;

public interface IPlatformUserService {
	/**
	 * 通过渠道ID 获取 OpenPlatformUser
	 * @param id
	 * @return
	 */
	public OpenPlatformUser findById(Integer id);
}
