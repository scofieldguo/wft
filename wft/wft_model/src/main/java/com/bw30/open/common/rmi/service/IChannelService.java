package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftChannel;

public interface IChannelService {
	public void insert(WftChannel channel);
	
	public WftChannel findById(String id);
	
	public List<WftChannel> findAll();
	
	public void update(WftChannel channel);
	
	public void delete(String id);
	
	public void updateCnum(Integer num, String id);
}
