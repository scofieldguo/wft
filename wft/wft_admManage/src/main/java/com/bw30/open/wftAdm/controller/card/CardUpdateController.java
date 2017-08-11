package com.bw30.open.wftAdm.controller.card;

import java.util.ArrayList;
import java.util.List;

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
import com.bw30.open.wftAdm.service.BaseDataService;
import com.bw30.open.wftAdm.service.card.CardDeleteService;
import com.bw30.open.wftAdm.service.card.CardUpdateService;

@Controller
public class CardUpdateController {
	private static final Logger LOG = Logger.getLogger(CardUpdateController.class);
	
	@Resource
	private BaseDataService baseDataService;
	@Resource
	private CardUpdateService cardUpdateService;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private CardDeleteService cardDeleteService;
	
	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setCardUpdateService(CardUpdateService cardUpdateService) {
		this.cardUpdateService = cardUpdateService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setCardDeleteService(CardDeleteService cardDeleteService) {
		this.cardDeleteService = cardDeleteService;
	}

	/**
	 * 停卡
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("toStop.do")
	public ModelAndView toStop(ModelMap viewMap, HttpServletRequest req){
		return new ModelAndView("card/cardStop", viewMap);
	}
	
	/**
	 * 停卡
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("stop.do")
	public ModelAndView stop(ModelMap viewMap, HttpServletRequest req){
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		MultipartFile txtFile = mreq.getFile("cardFile");
		List<CardUpdateModel> modelList = new ArrayList<CardUpdateModel>();
		try{
			if(null == txtFile){
				viewMap.addAttribute("pass", "fail");
				return new ModelAndView("card/cardStop", viewMap);
			}
			
			if(this.cardUpdateService.stopCard(txtFile, modelList)){//成功
				viewMap.addAttribute("pass", "ok");
				viewMap.addAttribute("modelList", modelList);
				return new ModelAndView("card/cardStop", viewMap);
			}
		}catch(Exception e){
			LOG.error("stop card error", e);
		}
		
		viewMap.addAttribute("pass", "fail");
		viewMap.addAttribute("modelList", modelList);
		return new ModelAndView("card/cardStop", viewMap);
	}
	
	/**
	 * 删除卡
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("toDeleteCard")
	public ModelAndView toDeleteCard(ModelMap viewMap, HttpServletRequest req){
		return new ModelAndView("card/deleteCard",viewMap);
	}
	
	@RequestMapping("deleteCard.do")
	public ModelAndView deleteCard(ModelMap viewMap, HttpServletRequest req){
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		MultipartFile txtFile = mreq.getFile("cardFile");
		List<String> modelList = new ArrayList<String>();
		try{
			if(null == txtFile){
				viewMap.addAttribute("pass", 1);
				return new ModelAndView("card/deleteCard", viewMap);
			}
			
			cardDeleteService.deleteCard(txtFile, modelList);
		}catch(Exception e){
			LOG.error("delete card error", e);
		}
		
		viewMap.addAttribute("pass", 2);
		viewMap.addAttribute("modelList", modelList);
		return new ModelAndView("card/deleteCard", viewMap);
	}
	
	/**
	 * 修改卡密码
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("toUpdatePwd.do")
	public ModelAndView toUpdatePwd(ModelMap viewMap, HttpServletRequest req){
		return new ModelAndView("card/updatePwd", viewMap);
	}
	
	/**
	 * 修改卡密码
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("updatePwd.do")
	public ModelAndView updatePwd(ModelMap viewMap, HttpServletRequest req){
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		MultipartFile txtFile = mreq.getFile("cardFile");
		List<CardUpdateModel> modelList = new ArrayList<CardUpdateModel>();
		try{
			if(null == txtFile){
				viewMap.addAttribute("pass", "fail");
				return new ModelAndView("card/updatePwd", viewMap);
			}
			
			if(this.cardUpdateService.updatePwd(txtFile, modelList)){//成功
				viewMap.addAttribute("pass", "ok");
				viewMap.addAttribute("modelList", modelList);
				return new ModelAndView("card/updatePwd", viewMap);
			}
		}catch(Exception e){
			LOG.error("stop card error", e);
		}
		
		viewMap.addAttribute("pass", "fail");
		viewMap.addAttribute("modelList", modelList);
		return new ModelAndView("card/updatePwd", viewMap);
	}
	
}
