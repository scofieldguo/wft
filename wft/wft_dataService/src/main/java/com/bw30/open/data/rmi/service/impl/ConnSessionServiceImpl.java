package com.bw30.open.data.rmi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftConnSessionMapper;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.rmi.service.IConnSessionService;

public class ConnSessionServiceImpl implements IConnSessionService{
	@Resource
	private WftConnSessionMapper wftConnSessionMapper;
	
	public void setWftConnSessionMapper(WftConnSessionMapper wftConnSessionMapper) {
		this.wftConnSessionMapper = wftConnSessionMapper;
	}

	public void insert(String channel, WftConnSession connSession) {
		connSession.setChannel(channel);
		this.wftConnSessionMapper.insert(connSession);
	}

	public WftConnSession findById(String channel, String csid) {
		return this.wftConnSessionMapper.findById(channel, csid);
	}

	public void update(String channel, WftConnSession connSession) {
		connSession.setChannel(channel);
		this.wftConnSessionMapper.update(connSession);
	}

	public void delete(String channel, String csid) {
		this.wftConnSessionMapper.delete(channel, csid);
	}

	public List<WftConnSession> findByCid(String channel, Integer cid) {
		return this.wftConnSessionMapper.findByCid(cid, null, channel);
	}
	
	public WftConnSession findBillSessionByCid(String channel, Integer cid, Integer status) {
		List<WftConnSession> csList = this.wftConnSessionMapper.findByCid(cid,
				status, channel);
		if (null != csList && 0 < csList.size()) {
			return csList.get(0);
		}
		return null;
	}

}
