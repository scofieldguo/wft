package com.bw30.open.data.rmi.service.impl;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.rmi.service.ICardStoreService;

public class CardStoreServiceImpl implements ICardStoreService {
	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	
	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void update(WftCardStore card) {
		this.wftCardStoreMapper.update(card);
	}

}
