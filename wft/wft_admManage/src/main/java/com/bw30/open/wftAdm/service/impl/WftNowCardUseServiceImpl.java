package com.bw30.open.wftAdm.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftCardUseTempModel;
import com.bw30.open.wftAdm.service.operate.IWftNowCardUseService;

public class WftNowCardUseServiceImpl implements IWftNowCardUseService{

	private WftCardActiveMapper wftCardActiveMapper;
	private WftChannelMapper wftChannelMapper;
	private  WftOperatorMapper wftOperatorMapper;
	@Override
	public List<WftCardUseTempModel> getNowCardUse() {
		List<WftCardUseTempModel> list = new ArrayList<WftCardUseTempModel>();
		List<WftOperator> oplist = wftOperatorMapper.findAll();
		List<WftChannel> chanelList= wftChannelMapper.findAll();
		for (WftChannel wftChannel : chanelList) {
			for (WftOperator wftOprator : oplist) {
				Integer avaliba=wftCardActiveMapper.countCardUse(WftCardActive.CSTATUS_AVAILABLE,wftChannel.getCode(),wftOprator.getSsid());
				Integer useing=wftCardActiveMapper.countCardUse(WftCardActive.CSTATUS_USING,wftChannel.getCode(),wftOprator.getSsid());
				Integer overtime=wftCardActiveMapper.countCardOverTime(wftChannel.getCode(),wftOprator.getSsid());
				WftCardUseTempModel model =new WftCardUseTempModel();
				model.setAvailable(avaliba);
				model.setChannelName(wftChannel.getName());
				model.setUseing(useing);
				model.setSsid(wftOprator.getSsid());
				model.setOvertime(overtime);
				list.add(model);
			}
		}
		return list;
	}
	@Override
	public List<WftCardUseTempModel> getNowCardUseBySsid(String ssid) {
		List<WftCardUseTempModel> list = new ArrayList<WftCardUseTempModel>();
		List<WftOperator> oplist = wftOperatorMapper.findBySsid(ssid);
		List<WftChannel> chanelList= wftChannelMapper.findAll();
		for (WftChannel wftChannel : chanelList) {
			for (WftOperator wftOprator : oplist) {
				Integer avaliba=wftCardActiveMapper.countCardUse(WftCardActive.CSTATUS_AVAILABLE,wftChannel.getCode(),wftOprator.getSsid());
				Integer useing=wftCardActiveMapper.countCardUse(WftCardActive.CSTATUS_USING,wftChannel.getCode(),wftOprator.getSsid());
				Integer overtime=wftCardActiveMapper.countCardOverTime(wftChannel.getCode(),wftOprator.getSsid());
				WftCardUseTempModel model =new WftCardUseTempModel();
				model.setAvailable(avaliba);
				model.setChannelName(wftChannel.getName());
				model.setUseing(useing);
				model.setSsid(wftOprator.getSsid());
				model.setOvertime(overtime);
				list.add(model);
			}
		}
		return list;
	}
	
	
	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

}
