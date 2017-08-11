package com.bw30.open.wftAdm.controller.mobile;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.api.WftChannelAndOpData;
import com.bw30.open.common.model.api.WftHourData;
import com.bw30.open.common.model.api.WftTotalStatData;
import com.bw30.open.common.model.stat.WftCardUseTempModel;
import com.bw30.open.common.model.stat.WftTenMinLoginStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.cardpool.rmi.service.ICardPoolService;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.impl.WftHourStatServiceImpl;
import com.bw30.open.wftAdm.service.impl.WftTotalStatServiceImpl;
import com.bw30.open.wftAdm.service.operate.IWftNowCardUseService;
/**
 * @author xugx
 * 监控平台手机端接口
 * */
@RequestMapping("api")
@Controller
public class WftMobileAPIController extends BaseController {
	@Resource
	private WftHourStatServiceImpl wftHourStatService;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private IWftNowCardUseService wftNowCardUseService;
	@Resource
	private ICardPoolService cardPoolService;
	@Resource
	private WftTotalStatServiceImpl wftTotalStatServiceImpl;
	
	/**
	 * 获取运营商和渠道列表
	 * */
	@RequestMapping("channelAndOpAPI")
	public void channelAndOpAPI(HttpServletRequest request,HttpServletResponse response){
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		WftChannelAndOpData data = new WftChannelAndOpData();
		data.setChannelList(channelList);
		data.setOpList(opList);
		this.returnJson(data,response);
	}
	
	/**
	 * 日使用时间分布
	 * */
	@RequestMapping("hourDataAPI.do")
	public void hourDataAPI(HttpServletRequest request,HttpServletResponse response) {
		String channel = getStringParam("channel", request, null);
		Integer opid = getIntegerParam("opid", request, 0);
		String startDate = getStringParam("startDate", request, "");
		String endDate = getStringParam("endDate", request, "");
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		List<WftTenMinLoginStat> hourList = wftHourStatService.findEveryHourConnStat(startDate, endDate, opid, channel);
		WftHourData data = new WftHourData();
		data.setChannel(channel);
		data.setOpid(opid);
		data.setStartDate(startDate);
		data.setEndDate(endDate);
		data.setOpList(opList);
		data.setChannelList(channelList);
		data.setHourList(hourList);
		this.returnJson(data,response);
	}
	
	/**
	 * cmcc使用情况
	 * */
	@RequestMapping("cardUseAPI.do")
	public void cardUseAPI(HttpServletRequest req,HttpServletResponse response){
		List<WftCardUseTempModel> uselist=wftNowCardUseService.getNowCardUseBySsid("CMCC");
		this.returnJson(uselist,response);
	}
	
	/**
	 * 连接数据
	 * */
	@RequestMapping("totalStatAPI.do")
	public void findTotalStat(HttpServletRequest request,HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String channel = request.getParameter("channel");
		Integer opid = getIntegerParam("opid", request,0);
		List<WftTotalStat> totalStatList = wftTotalStatServiceImpl.getWftTotalStats(startDate, endDate, channel, opid);
		List<WftOperator> opList = wftOperatorMapper.findAll();
		List<WftChannel> channelList = wftChannelMapper.findAll();
		WftTotalStatData data = new WftTotalStatData();
		data.setChannel(channel);
		data.setOpid(opid);
		data.setStartDate(startDate);
		data.setEndDate(endDate);
		data.setOpList(opList);
		data.setChannelList(channelList);
		data.setTotalStatList(totalStatList);
		this.returnJson(data,response);
	}
	
	
	public void setWftHourStatService(WftHourStatServiceImpl wftHourStatService) {
		this.wftHourStatService = wftHourStatService;
	}
	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftNowCardUseService(IWftNowCardUseService wftNowCardUseService) {
		this.wftNowCardUseService = wftNowCardUseService;
	}

	public void setCardPoolService(ICardPoolService cardPoolService) {
		this.cardPoolService = cardPoolService;
	}
	
	
}
