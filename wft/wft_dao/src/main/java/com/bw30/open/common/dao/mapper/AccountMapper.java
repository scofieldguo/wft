package com.bw30.open.common.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.OpenPlatformAccount;

public interface AccountMapper extends GenericMapper<OpenPlatformAccount>{

	OpenPlatformAccount getAccountByUserId(@Param("id")Integer id);

}
