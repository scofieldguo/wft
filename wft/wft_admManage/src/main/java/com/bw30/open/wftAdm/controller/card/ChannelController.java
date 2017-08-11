package com.bw30.open.wftAdm.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.WftCardTypeMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardType;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.controller.BaseController;

/**
 * 渠道配置管理
 * 
 * @author Jack
 * 
 *         2014年11月25日
 */
@Controller
public class ChannelController extends BaseController {

	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftCardTypeMapper wftCardTypeMapper;

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftCardTypeMapper(WftCardTypeMapper wftCardTypeMapper) {
		this.wftCardTypeMapper = wftCardTypeMapper;
	}

	@RequestMapping("channelConfig.do")
	public ModelAndView list(ModelMap viewMap, HttpServletRequest req) {
		List<WftChannel> channelList = this.wftChannelMapper.findAll();
		List<WftCardType> ctList = this.wftCardTypeMapper.findCardType(
				WftOperator.OP_ID_CTCC, null);
		if(null != ctList && 0 < ctList.size()){
			Map<String, String> ctMap = new HashMap<String, String>();
			for(WftCardType ct : ctList){
				ctMap.put(ct.getCodeop(), ct.getStandby());
			}
			viewMap.addAttribute("ctMap", ctMap);
		}
		viewMap.addAttribute("channelList", channelList);
		
		return new ModelAndView("card/channelList", viewMap);
	}

	@RequestMapping("toConfig.do")
	public ModelAndView toConfig(ModelMap viewMap, HttpServletRequest req) {
		String id = req.getParameter("id");
		WftChannel channel = this.wftChannelMapper.findById(id);
		List<WftCardType> ctList = this.wftCardTypeMapper.findCardType(
				WftOperator.OP_ID_CTCC, null);
		viewMap.addAttribute("channel", channel);
		viewMap.addAttribute("ctList", ctList);
		return new ModelAndView("card/channelConfig", viewMap);
	}

	@RequestMapping("config.do")
	public ModelAndView config(ModelMap viewMap, HttpServletRequest req) {
		String id = req.getParameter("id");
		Integer ctccnum = this.getIntegerParam("ctccnum", req, null);
		String ctypeforopenctcc = this.getStringParam("ctypeforopenctcc", req,
				null);
		Integer ctccbalance = this.getIntegerParam("ctccbalance", req, null);
		String ctypeforrechargectcc = this.getStringParam(
				"ctypeforrechargectcc", req, null);

		WftChannel channel = this.wftChannelMapper.findById(id);
		if (null != channel) {
			channel = new WftChannel();
			channel.setCode(id);
			channel.setCtccnum(ctccnum);
			channel.setCtypeforopenctcc(ctypeforopenctcc);
			channel.setCtccbalance(ctccbalance);
			channel.setCtypeforrechargectcc(ctypeforrechargectcc);
			this.wftChannelMapper.update(channel);
		}
		return new ModelAndView("redirect:channelConfig.do");
	}

}
