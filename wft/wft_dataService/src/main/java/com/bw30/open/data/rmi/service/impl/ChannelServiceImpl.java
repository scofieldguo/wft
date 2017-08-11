package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.rmi.service.IChannelService;

public class ChannelServiceImpl implements IChannelService{
	@Resource
	private WftChannelMapper wftChannelMapper;
	
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void insert(WftChannel channel) {
		this.wftChannelMapper.insert(channel);
	}

	public WftChannel findById(String id) {
		return this.wftChannelMapper.findById(id);
	}

	public List<WftChannel> findAll() {
		return this.wftChannelMapper.findAll();
	}

	public void update(WftChannel channel) {
		this.wftChannelMapper.update(channel);
	}

	public void delete(String id) {
		this.wftChannelMapper.delete(id);
	}

	public void updateCnum(Integer num, String id) {
		this.wftChannelMapper.updateCnum(num, id);
	}

}
