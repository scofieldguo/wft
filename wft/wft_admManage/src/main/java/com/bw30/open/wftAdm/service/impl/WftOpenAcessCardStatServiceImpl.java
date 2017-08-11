package com.bw30.open.wftAdm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw30.open.common.dao.mapper.WftAcessCardMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenAcessCardStat;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wftAdm.service.operate.IWftOpenAcessCardStatService;

public class WftOpenAcessCardStatServiceImpl implements IWftOpenAcessCardStatService{

	
	private WftAcessCardMapper wftAcessCardMapper;
	private WftChannelMapper wftChannelMapper;
	@Override
	public List<WftOpenAcessCardStat> findAcessCardStat(String start,
		 Pager pager, String channel) {
	    Map<String,Object> paramMap=new HashMap<String,Object>();
	    paramMap.put("start", start);
	    paramMap.put("channel", channel);
	    pager.setRecCount(wftAcessCardMapper.countByParam(paramMap));
		return wftAcessCardMapper.pageFindByParam(paramMap,pager);
	}
	@Override
	public List<WftChannel> getAllChannel() {
		return wftChannelMapper.findAll();
	}
	public void setWftAcessCardMapper(WftAcessCardMapper wftAcessCardMapper) {
		this.wftAcessCardMapper = wftAcessCardMapper;
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}


}
