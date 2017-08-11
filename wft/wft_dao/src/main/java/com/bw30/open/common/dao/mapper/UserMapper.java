package com.bw30.open.common.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.wft.common.Pager;

public interface UserMapper extends GenericMapper<OpenPlatformUser> {
	public OpenPlatformUser findUser(@Param("email") String mail,@Param("pwd") String pwd,@Param("code")String code);
	public void updateByMap(@Param("id")Integer id,@Param("paramMap")Map<String, Object> paramMap);
	public void updateByMail(@Param("mail")String mail,@Param("paramMap")Map<String, Object> paramMap);
	public List<OpenPlatformUser> findByPage(@Param("pager")Pager pager);
}
