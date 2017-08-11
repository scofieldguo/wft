package com.bw30.open.wftAdm.service.impl;

import java.util.List;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.wftAdm.service.IWftOpenChannelService;

public class WftOpenChannelServiceImpl implements IWftOpenChannelService{

	private WftChannelMapper wftChannelMapper;
	@Override
	public List<WftChannel> findAll() {
		return wftChannelMapper.findAll();
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

}
