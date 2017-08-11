package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftConnSession;

public interface IConnSessionService {
	public void insert(String channel, WftConnSession connSession);
	
	public WftConnSession findById(String channel,String csid);

	public void update(String channel, WftConnSession connSession);

	public void delete(String channel, String csid);

	public List<WftConnSession> findByCid(String channel, Integer cid);
	
	public WftConnSession findBillSessionByCid(String channel, Integer cid, Integer status);
}
