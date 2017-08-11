package com.bw30.open.wftAdm.controller.card;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.BaseDataService;
import com.bw30.open.wftAdm.service.card.CardService;

/**
 * controller: 卡库管理 
 *
 * @version 2014-4-22 下午08:22:48
 *
 */
@Controller
public class CardStoreController extends BaseController{
	private static final Logger LOG = Logger.getLogger(CardStoreController.class);
	
	/**
	 * 添加的新卡信息：<uid, 卡信息>
	 */
	private static final Map<Integer, List<WftCardStore>> UPLOAD_CARD_FOR_ADD = new HashMap<Integer, List<WftCardStore>>();
	
	@Resource
	private BaseDataService baseDataService;
	@Resource
	private CardService cardService;
	@Resource
	private WftChannelMapper wftChannelMapper;
	
	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}
	
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	/**
	 * 卡库查询
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("cardList.do")
	public ModelAndView list(ModelMap viewMap, HttpServletRequest req) {
		Integer pageId = this.getIntegerParam("pageId", req, 1);
		pager.setPageIndex(pageId);
		
		Integer opid = this.getIntegerParam("opid", req, null);
		Integer prvid = this.getIntegerParam("prvid", req, null);
		Integer ctype = this.getIntegerParam("ctype", req, null);
		
		String begVetime = this.getStringParam("begVetime", req, null);
		String endVetime = this.getStringParam("endVetime", req, null);
		String begIntime = this.getStringParam("begIntime", req, null);
		String endIntime = this.getStringParam("endIntime", req, null);
		
		String channel = this.getStringParam("channel", req, null);
		Integer autorecharge = this.getIntegerParam("autorecharge", req, null);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("opid", opid);
		params.put("prvid", prvid);
		params.put("ctype", ctype);
		params.put("begVetime", begVetime);
		params.put("endVetime", endVetime);
		params.put("begIntime", begIntime);
		params.put("endIntime", endIntime);
		
		//出库渠道
		if(null != channel && "0".equals(channel)){//未出库
			params.put("inplace", WftCardStore.CARD_IN_STORE);
		}else{
			params.put("channel", channel);
		}
		//是否自动充值
		
		List<WftCardStore> cardList = this.cardService.getCardList(params, pager);
		
		List<WftProvince> provinceList = this.baseDataService.getAllProvince();
		List<WftOperator> operatorList = this.baseDataService.getAllOperator();
		List<WftChannel> channelList = this.baseDataService.getAllChannel();
		Map<Integer, String> provinceMap = this.baseDataService.getAllProvinceMap();
		Map<Integer, String> operatorMap = this.baseDataService.getAllOperatorMap();
		Map<String, String> channelMap = this.baseDataService.getAllChannelMap();
		
		viewMap.addAttribute("provinceList", provinceList);
		viewMap.addAttribute("operatorList", operatorList);
		viewMap.addAttribute("channelList", channelList);
		viewMap.addAttribute("provinceMap", provinceMap);
		viewMap.addAttribute("operatorMap", operatorMap);
		viewMap.addAttribute("channelMap", channelMap);
		viewMap.addAttribute("cardList", cardList);
		viewMap.addAttribute("pager", pager);
		
		params.put("channel", channel);
		params.put("autorecharge", autorecharge);
		viewMap.addAllAttributes(params);
		
		return new ModelAndView("card/cardList", viewMap);
	}

	
	/**
	 * 验证并添加新卡
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("cardToAdd.do")
	public ModelAndView toAdd(ModelMap viewMap, HttpServletRequest req){
		return new ModelAndView("card/cardAdd", viewMap);
	}
	
	@RequestMapping("cardCheckForAdd.do")
	public ModelAndView checkForAdd(ModelMap viewMap, HttpServletRequest req){
//		HttpSession session=req.getSession();
//		WftUserinfo user = (WftUserinfo)session.getAttribute("user");
		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		MultipartFile xlsFile = mreq.getFile("cardFile");//获取xls文件
		List<CardAddModel> camList = new ArrayList<CardAddModel>();
		try{
			List<WftCardStore> csList = this.cardService.uploadAndCheckForAdd(xlsFile, camList);
			viewMap.addAttribute("camList", camList);
			if(0 < camList.size() && camList.size() > csList.size()){//验证不通过
				viewMap.addAttribute("pass", "fail");
			}else{
//				UPLOAD_CARD.put(user.getId(), csList);
				UPLOAD_CARD_FOR_ADD.put(1, csList);
			}
			
			List<WftChannel> chList = this.wftChannelMapper.findAll();
			viewMap.addAttribute("channelList", chList);
			return new ModelAndView("card/cardAdd", viewMap);
		}catch(IOException ioe){
			LOG.error("upload and check card for add error", ioe);
			return this.errorView("卡池管理->添加新卡", "文件读取错误");
		}
	}
	
	@RequestMapping("cardAdd.do")
	public ModelAndView add(ModelMap viewMap, HttpServletRequest req){
		Integer opt = this.getIntegerParam("opt", req, 1);
		Date prtime = this.getDateParam("prtime", req, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		String channel = this.getStringParam("channel", req, null);
		
		List<WftCardStore> csList = UPLOAD_CARD_FOR_ADD.get(1);
		
		if(1 == opt){//不调入现有卡池，设置预启用时间
			if(null != prtime){
				this.cardService.addCardsToStore(csList, false, channel, prtime);
			}else{
				return this.errorView("卡池管理->添加新卡", "没有设置预启用时间");
			}
		}else{//调入卡池
			if(null != channel){
				this.cardService.addCardsToStore(csList, true, channel, null);
			}else{
				return this.errorView("卡池管理->添加新卡", "没有选择合作方");
			}
		}
		
		//清除map中已成功添加的卡
		UPLOAD_CARD_FOR_ADD.remove(1);
		
		return new ModelAndView("redirect:cardList.do", viewMap);
	}
	
	/**
	 * 批量出库
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("cardToActive.do")
	public ModelAndView addToActive(ModelMap viewMap, HttpServletRequest req) {
		String channel = this.getStringParam("outchannel", req, null);
		String[] cids = this.getParams("cid", req);
		try{
			this.cardService.addCardsToActive(channel, cids);
		}catch(Exception e){
			LOG.error("add card to active error", e);
		}
		return new ModelAndView("forward:cardList.do", viewMap);
	}
	
	
}
