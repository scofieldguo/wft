package com.bw30.open.wftAdm.service.impl;

import com.bw30.open.common.dao.mapper.WftAdminUserMapper;
import com.bw30.open.common.model.login.WftAdminUser;
import com.bw30.open.wftAdm.service.IUserService;

public class UserServiceImpl implements IUserService{

	private WftAdminUserMapper wftAdminUserMapper;
	@Override
	public boolean isUser(WftAdminUser userInfo) {
		WftAdminUser user=new WftAdminUser();
		if(userInfo.getName()!=null){
		 user=wftAdminUserMapper.findUserByName(userInfo.getName());
		}
		if(user!=null){
			return true;
		}
		return false;
		
	}

	@Override
	public WftAdminUser getUserinfo(String loginName, String password) {
		return wftAdminUserMapper.getUserInfo(loginName,password);
	}

	public void setWftAdminUserMapper(WftAdminUserMapper wftAdminUserMapper) {
		this.wftAdminUserMapper = wftAdminUserMapper;
	}

}
