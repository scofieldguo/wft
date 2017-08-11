package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.rmi.service.ICardActiveService;

public class CardActiveServiceImpl implements ICardActiveService{
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	
	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}
	
	public void insert(String channel, WftCardActive card){
		card.setChannel(channel);
		this.wftCardActiveMapper.insert(card);
	}
	
	public WftCardActive findById(String channel, Integer id) {
		return this.wftCardActiveMapper.findById(channel, id);
	}

	public void update(String channel, WftCardActive card) {
		card.setChannel(channel);
		this.wftCardActiveMapper.update(card);
	}

	public List<WftCardActive> findCardByUid(String channel, Integer opid,
			String uid) {
		return this.wftCardActiveMapper.findCardByUid(channel,opid, uid);
	}

}
