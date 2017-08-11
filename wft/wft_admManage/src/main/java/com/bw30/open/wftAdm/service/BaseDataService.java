package com.bw30.open.wftAdm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.dao.mapper.WftProvinceMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;

public class BaseDataService {
	@Resource
	private WftProvinceMapper wftProvinceMapper;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;

	// 省份
	private static List<WftProvince> PROVINCE_LIST = null;
	private static Map<Integer, String> PROVINCE_MAP = null;

	// 运营商
	private static List<WftOperator> OPERATOR_LIST = null;
	private static Map<Integer, String> OPERATOR_MAP = null;

	public void setWftProvinceMapper(WftProvinceMapper wftProvinceMapper) {
		this.wftProvinceMapper = wftProvinceMapper;
	}

	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	/**
	 * 根据名称获取省份
	 * 
	 * @param name
	 * @return
	 */
	public WftProvince getProvinceByName(String name) {
		List<WftProvince> prvList = this.wftProvinceMapper.findByName(name);
		if (null != prvList && 0 < prvList.size()) {
			return prvList.get(0);
		}
		return null;
	}

	/**
	 * 获取省份列表，id升序
	 * 
	 * @return
	 */
	public List<WftProvince> getAllProvince() {
		if (null == PROVINCE_LIST) {
			PROVINCE_LIST = this.wftProvinceMapper.findAll();
		}
		return PROVINCE_LIST;
	}

	/**
	 * 获取省份map<id, name>
	 * 
	 * @return
	 */
	public Map<Integer, String> getAllProvinceMap() {
		if (null == PROVINCE_MAP || PROVINCE_MAP.isEmpty()) {
			PROVINCE_MAP = new HashMap<Integer, String>();
			List<WftProvince> prvList = this.getAllProvince();
			if (null != prvList) {
				for (WftProvince prv : prvList) {
					PROVINCE_MAP.put(prv.getId(), prv.getName());
				}
			}
		}
		return PROVINCE_MAP;
	}

	/**
	 * 根据名称获取运营商
	 * 
	 * @param name
	 * @return
	 */
	public WftOperator getOperatorByName(String name) {
		List<WftOperator> opList = this.wftOperatorMapper.findByName(name);
		if (null != opList && 0 < opList.size()) {
			return opList.get(0);
		}
		return null;
	}

	/**
	 * 获取运营商列表，id升序
	 * 
	 * @return
	 */
	public List<WftOperator> getAllOperator() {
		if (null == OPERATOR_LIST) {
			OPERATOR_LIST = this.wftOperatorMapper.findAll();
		}
		return OPERATOR_LIST;
	}

	/**
	 * 获取运营商map<id, name>
	 * 
	 * @return
	 */
	public Map<Integer, String> getAllOperatorMap() {
		if (null == OPERATOR_MAP || OPERATOR_MAP.isEmpty()) {
			OPERATOR_MAP = new HashMap<Integer, String>();
			List<WftOperator> opList = this.getAllOperator();
			if (null != opList) {
				for (WftOperator op : opList) {
					OPERATOR_MAP.put(op.getId(), op.getName());
				}
			}
		}
		return OPERATOR_MAP;
	}

	/**
	 * 获取渠道列表，id升序
	 * 
	 * @return
	 */
	public List<WftChannel> getAllChannel() {
		return this.wftChannelMapper.findAll();
	}

	/**
	 * 获取运营商map<id, name>
	 * 
	 * @return
	 */
	public Map<String, String> getAllChannelMap() {
		Map<String, String> channelMap = new HashMap<String, String>();
		List<WftChannel> channelList = this.getAllChannel();
		if (null != channelList) {
			for (WftChannel channel : channelList) {
				channelMap.put(channel.getCode(), channel.getName());
			}
		}
		return channelMap;
	}
	
}
