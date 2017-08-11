package com.bw30.open.wftAdm.service;

import com.bw30.open.common.model.login.WftAdminUser;

public interface IUserService {

	boolean isUser(WftAdminUser userInfo);

	WftAdminUser getUserinfo(String loginName, String password);

}
