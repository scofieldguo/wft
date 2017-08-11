package com.bw30.open.common.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.login.WftAdminUser;

public interface WftAdminUserMapper extends GenericMapper<WftAdminUserMapper>{

	WftAdminUser findUserByName(@Param("name")String name);

	WftAdminUser getUserInfo(@Param("name")String loginName,@Param("password") String password);

}
