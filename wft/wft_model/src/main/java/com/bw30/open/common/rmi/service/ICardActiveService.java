package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftCardActive;

public interface ICardActiveService {
	public void insert(String channel, WftCardActive card);
	
	public WftCardActive findById(String channel, Integer id);
	
	public void update(String channel, WftCardActive card);
	
	/**
	 * 获取用户分配的卡， 按分配时间倒序
	 * 
	 * @param channel
	 * @param opid
	 * @param uid
	 * @return
	 */
	public List<WftCardActive> findCardByUid(String channel,Integer opid, String uid);
	
}
