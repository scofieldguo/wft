package com.bw30.open.data.rmi.service.impl;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.UserMapper;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.rmi.service.IPlatformUserService;

public class PlatformUserServiceImpl implements IPlatformUserService{
	@Resource
	private UserMapper userMapper;
	
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public OpenPlatformUser findById(Integer id) {
		return this.userMapper.findById(id);
	}

}
