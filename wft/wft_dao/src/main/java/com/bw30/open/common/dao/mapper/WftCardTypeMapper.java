package com.bw30.open.common.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bw30.open.common.dao.mapper.common.GenericMapper;
import com.bw30.open.common.model.WftCardType;

public interface WftCardTypeMapper extends GenericMapper<WftCardType>{
	/**
	 * 查询卡品
	 * 
	 * @param opid 运营商id
	 * @param code 开放平台卡品
	 * @return
	 */
	public List<WftCardType> findCardType(@Param("opid") Integer opid, @Param("code") String code);
	
	WftCardType findCardCodeop(@Param("codeop") String codeop);
}
