package com.bw30.open.common.rmi.service;

import java.util.List;
import java.util.Map;

import com.bw30.open.common.model.WftProvince;

public interface IProvinceService {
	
	/**
	 * 通过Id获取省份信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WftProvince findById(Integer id) throws Exception;
	
	/**
	 * 通过Map参数获取省份
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<WftProvince> findByParam(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 通过中文名获取省份
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<WftProvince> findByName(String name) throws Exception;
	
	/**
	 * 匹配中文名开头相同的省份
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<WftProvince> findByNameRegx(String name) throws Exception;

}
