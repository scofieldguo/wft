package com.bw30.open.wftAdm.controller.card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.dao.mapper.parameter.Parameter;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.controller.JsonModel;
import com.bw30.open.wftAdm.service.BaseDataService;
import com.bw30.open.wftAdm.service.card.CardExportService;
import com.bw30.open.wftAdm.service.card.CardService;

/**
 * 卡池管理
 * 
 * @author Jack
 *
 *         2014年11月25日
 */
@Controller
public class CardActiveController extends BaseController {
	private static final Logger LOG = Logger
			.getLogger(CardActiveController.class);

	@Resource
	private BaseDataService baseDataService;
	@Resource
	private CardService cardService;
	@Resource
	private CardExportService cardExportService;

	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void setCardExportService(CardExportService cardExportService) {
		this.cardExportService = cardExportService;
	}

	/**
	 * 卡池查询页面
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("card_index.do")
	public ModelAndView index(ModelMap viewMap, HttpServletRequest req) {
		List<WftProvince> provinceList = this.baseDataService.getAllProvince();
		List<WftOperator> operatorList = this.baseDataService.getAllOperator();
		List<WftChannel> channelList = this.baseDataService.getAllChannel();

		viewMap.addAttribute("provinceList", provinceList);
		viewMap.addAttribute("operatorList", operatorList);
		viewMap.addAttribute("channelList", channelList);

		return new ModelAndView("card/index", viewMap);
	}

	/**
	 * 返回JSON串
	 * 
	 * @param page
	 * @param rows
	 * @param channel
	 * @param no
	 * @param opid
	 * @param cstatus
	 * @param begVetime
	 * @param endVetime
	 * @return
	 */
	@RequestMapping("card_list.do")
	@ResponseBody
	public JsonModel getCardList(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) int rows,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "no", required = false) String no,
			@RequestParam(value = "opid", required = false) Integer opid,
			@RequestParam(value = "cstatus", required = false) Integer cstatus,
			@RequestParam(value = "begVetime", required = false) String begVetime,
			@RequestParam(value = "endVetime", required = false) String endVetime) {
		pager.setPageIndex(page);
		pager.setPageSize(rows);

		Parameter parameter = new Parameter();
		parameter.add("channel", channel);
		parameter.add("no", no);
		parameter.add("opid", opid);
		parameter.add("cstatus", cstatus);
		parameter.add("begVetime", begVetime);
		parameter.add("endVetime", endVetime);
		List<WftCardActive> cardList = null;
		if (null != channel) {
			cardList = this.cardService.getCardActiveList(
					parameter.getParamMap(), pager);
		}
		JsonModel jsonModel = this.constructJsonModel(cardList, pager);
		return jsonModel;
	}

	/**
	 * 
	 * @param oper
	 * @param id
	 * @param viewMap
	 * @param response
	 */
	@RequestMapping("card_modify.do")
	public void modify(
			@RequestParam(value = "oper", required = true) String oper,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "channel", required = false) String channel,
			ModelMap viewMap, HttpServletResponse response) {
		if (oper.equals("open")) {
			if (!this.cardService.enableCardFromActive(channel, id)) {
				LOG.error("enable card active error:channel" + channel
						+ ", id=" + id);
			}
		} else if (oper.equals("stop")) {
			if (!this.cardService.stopCardFromActive(channel, id)) {
				LOG.error("stop card active error:channel=" + channel + ", id="
						+ id);
			}
		}

	}

	/**
	 * 卡池查询
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@Deprecated
	@RequestMapping("cardActiveList.do")
	public ModelAndView list(ModelMap viewMap, HttpServletRequest req) {
		Integer pageId = this.getIntegerParam("pageId", req, 1);
		pager.setPageIndex(pageId);

		String channel = this.getStringParam("channel", req, null);
		String no = this.getStringParam("no", req, null);
		Integer opid = this.getIntegerParam("opid", req, null);
		Integer ctype = this.getIntegerParam("ctype", req, null);
		Integer prvid = this.getIntegerParam("prvid", req, null);
		Integer cstatus = this.getIntegerParam("cstatus", req, null);
		String begVetime = this.getStringParam("begVetime", req, null);
		String endVetime = this.getStringParam("endVetime", req, null);
		String begIntime = this.getStringParam("begIntime", req, null);
		String endIntime = this.getStringParam("endIntime", req, null);
		Integer begTvalue = this.getIntegerParam("begTvalue", req, null);
		Integer endTvalue = this.getIntegerParam("endTvalue", req, null);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channel", channel);
		params.put("no", no);
		params.put("opid", opid);
		params.put("ctype", ctype);
		params.put("cstatus", cstatus);
		params.put("prvid", prvid);
		params.put("begVetime", begVetime);
		params.put("endVetime", endVetime);
		params.put("begIntime", begIntime);
		params.put("endIntime", endIntime);
		params.put("begTvalue", begTvalue);
		params.put("endTvalue", endTvalue);

		List<WftCardActive> cardList = null;
		if (null != channel) {
			cardList = this.cardService.getCardActiveList(params, pager);
		}

		List<WftProvince> provinceList = this.baseDataService.getAllProvince();
		List<WftOperator> operatorList = this.baseDataService.getAllOperator();
		List<WftChannel> channelList = this.baseDataService.getAllChannel();
		Map<Integer, String> provinceMap = this.baseDataService
				.getAllProvinceMap();
		Map<Integer, String> operatorMap = this.baseDataService
				.getAllOperatorMap();
		Map<String, String> channelMap = this.baseDataService
				.getAllChannelMap();

		viewMap.addAttribute("provinceList", provinceList);
		viewMap.addAttribute("operatorList", operatorList);
		viewMap.addAttribute("channelList", channelList);
		viewMap.addAttribute("provinceMap", provinceMap);
		viewMap.addAttribute("operatorMap", operatorMap);
		viewMap.addAttribute("channelMap", channelMap);
		viewMap.addAttribute("cardList", cardList);
		viewMap.addAttribute("pager", pager);
		viewMap.addAllAttributes(params);
		viewMap.addAttribute("queryStr", req.getQueryString());

		return new ModelAndView("card/cardActiveList", viewMap);
	}

	/**
	 * 启用卡<br/>
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@Deprecated
	@RequestMapping("cardActiveOpen.do")
	public ModelAndView open(HttpServletRequest req,
			HttpServletResponse response) {
		String channel = this.getStringParam("channel", req, null);
		Integer cid = this.getIntegerParam("cid", req, null);
		if (!this.cardService.enableCardFromActive(channel, cid)) {
			LOG.error("enable card active error:channel" + channel + ", id="
					+ cid);
		}
		return new ModelAndView("forward:cardActiveList.do");
	}

	/**
	 * 停用卡<br/>
	 * 只能停用"可用"的卡
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@Deprecated
	@RequestMapping("cardActiveStop.do")
	public ModelAndView stop(HttpServletRequest req,
			HttpServletResponse response) {
		String channel = this.getStringParam("channel", req, null);
		Integer cid = this.getIntegerParam("cid", req, null);
		if (!this.cardService.stopCardFromActive(channel, cid)) {
			LOG.error("stop card active error:channel=" + channel + ", id="
					+ cid);
		}
		return new ModelAndView("forward:cardActiveList.do");
	}

	/**
	 * 删除卡<br/>
	 * 只能删除"停用"的卡
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@Deprecated
	@RequestMapping("cardActiveDelete.do")
	public ModelAndView delete(ModelMap viewMap, HttpServletRequest req) {
		String channel = this.getStringParam("channel", req, null);
		Integer cid = this.getIntegerParam("cid", req, null);
		if (!this.cardService.deleteCardFromActive(channel, cid)) {
			LOG.error("delete card active error:channel=" + channel + ", id="
					+ cid);
		}
		return new ModelAndView("forward:cardActiveList.do");
	}

	/**
	 * 导出数据
	 * 
	 * @param page
	 * @param rows
	 * @param channel
	 * @param no
	 * @param opid
	 * @param cstatus
	 * @param begVetime
	 * @param endVetime
	 * @param all
	 * @param response
	 */
	@RequestMapping("card_export.do")
	public void exportExcel(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) int rows,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "no", required = false) String no,
			@RequestParam(value = "opid", required = false) Integer opid,
			@RequestParam(value = "cstatus", required = false) Integer cstatus,
			@RequestParam(value = "begVetime", required = false) String begVetime,
			@RequestParam(value = "endVetime", required = false) String endVetime,
			@RequestParam(value = "all", required = false) Integer all,
			HttpServletResponse response) {
		pager.setPageIndex(page);
		pager.setPageSize(rows);
		Parameter parameter = new Parameter();
		parameter.add("channel", channel);
		parameter.add("no", no);
		parameter.add("opid", opid);
		parameter.add("cstatus", cstatus);
		parameter.add("begVetime", begVetime);
		parameter.add("endVetime", endVetime);
		List<WftCardActive> cardList = null;
		if (null != channel) {
			if (0 == all) {// 导出本页
				cardList = this.cardService.getCardActiveList(
						parameter.getParamMap(), pager);
			} else {// 导出全部
				cardList = this.cardService.getCardActiveList(
						parameter.getParamMap(), null);
			}
		}
		this.cardExportService.exportCardActive(channel, cardList, "card.xls",
				response);
	}

	/**
	 * 导出卡池
	 * 
	 * @param viewMap
	 * @param req
	 * @param resp
	 * @return
	 */
	@Deprecated
	@RequestMapping("cardActiveExport.do")
	public ModelAndView export(ModelMap viewMap, HttpServletRequest req,
			HttpServletResponse resp) {
		Integer pageId = this.getIntegerParam("pageId", req, 1);
		pager.setPageIndex(pageId);

		Integer all = this.getIntegerParam("all", req, 0);

		String channel = this.getStringParam("channel", req, null);
		String no = this.getStringParam("no", req, null);
		Integer opid = this.getIntegerParam("opid", req, null);
		Integer ctype = this.getIntegerParam("ctype", req, null);
		Integer prvid = this.getIntegerParam("prvid", req, null);
		Integer cstatus = this.getIntegerParam("cstatus", req, null);
		String begVetime = this.getStringParam("begVetime", req, null);
		String endVetime = this.getStringParam("endVetime", req, null);
		String begIntime = this.getStringParam("begIntime", req, null);
		String endIntime = this.getStringParam("endIntime", req, null);
		Integer begTvalue = this.getIntegerParam("begTvalue", req, null);
		Integer endTvalue = this.getIntegerParam("endTvalue", req, null);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channel", channel);
		params.put("no", no);
		params.put("opid", opid);
		params.put("ctype", ctype);
		params.put("cstatus", cstatus);
		params.put("prvid", prvid);
		params.put("begVetime", begVetime);
		params.put("endVetime", endVetime);
		params.put("begIntime", begIntime);
		params.put("endIntime", endIntime);
		params.put("begTvalue", begTvalue);
		params.put("endTvalue", endTvalue);

		List<WftCardActive> cardList = null;
		if (null != channel) {
			if (0 == all) {// 导出本页
				cardList = this.cardService.getCardActiveList(params, pager);
			} else {// 导出全部
				cardList = this.cardService.getCardActiveList(params, null);
			}
		}
		this.cardExportService.exportCardActive(channel, cardList, "card.xls",
				resp);

		return new ModelAndView("forward:cardActiveList.do");
	}

	/**
	 * 问题卡查询
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("cardInvalidList.do")
	public ModelAndView cardInvalidList(ModelMap viewMap, HttpServletRequest req) {
		String channel = this.getStringParam("channel", req, null);
		Integer opid = this.getIntegerParam("opid", req, null);
		String begStoptime = this.getStringParam("begStoptime", req, null);
		String endStoptime = this.getStringParam("endStoptime", req, null);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channel", channel);
		params.put("opid", opid);
		params.put("cstatus", WftCardActive.CSTATUS_STOP);
		params.put("begStoptime", begStoptime);
		params.put("endStoptime", endStoptime);

		List<WftCardActive> cardList = null;
		if (null != channel) {
			cardList = this.cardService.getCardActiveList(params, null);
			if (null != cardList && 0 < cardList.size()) {
				for (WftCardActive ca : cardList) {
					ca.setPwd(PwdEncrypt.decryptCardPwd(ca.getPwd()));
				}
			}
		}

		List<WftOperator> operatorList = this.baseDataService.getAllOperator();
		List<WftChannel> channelList = this.baseDataService.getAllChannel();
		Map<Integer, String> operatorMap = this.baseDataService
				.getAllOperatorMap();
		Map<String, String> channelMap = this.baseDataService
				.getAllChannelMap();

		viewMap.addAttribute("operatorList", operatorList);
		viewMap.addAttribute("channelList", channelList);
		viewMap.addAttribute("operatorMap", operatorMap);
		viewMap.addAttribute("channelMap", channelMap);
		viewMap.addAttribute("cardList", cardList);
		viewMap.addAllAttributes(params);

		return new ModelAndView("card/cardInvalidList", viewMap);
	}

	/**
	 * 导出问题卡
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("cardInvalidExport.do")
	public void cardInvalidExport(HttpServletRequest req,
			HttpServletResponse resp) {
		String channel = this.getStringParam("channel", req, null);
		Integer opid = this.getIntegerParam("opid", req, null);
		String begStoptime = this.getStringParam("begStoptime", req, null);
		String endStoptime = this.getStringParam("endStoptime", req, null);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channel", channel);
		params.put("opid", opid);
		params.put("cstatus", WftCardActive.CSTATUS_STOP);
		params.put("begStoptime", begStoptime);
		params.put("endStoptime", endStoptime);

		List<WftCardActive> cardList = null;
		if (null != channel) {
			cardList = this.cardService.getCardActiveList(params, null);
		}
		try {
			this.cardExportService.exportInvalidCard(channel, cardList,
					"card.xls", resp);
		} catch (Exception e) {
			LOG.error("export invalid card error", e);
		}

	}

	/**
	 * 启用问题卡
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping("cardInvalidOpen.do")
	public void cardInvalidOpen(HttpServletRequest req, HttpServletResponse resp) {
		String channel = this.getStringParam("channel", req, null);
		Integer id = this.getIntegerParam("id", req, null);
		if (!this.cardService.enableCardFromActive(channel, id)) {
			LOG.error("enable card active error:channel" + channel + ", id="
					+ id);
			this.returnJson("{\"flag\":0}", resp);
		} else {
			this.returnJson("{\"flag\":1}", resp);
		}

	}

	@RequestMapping("remvoeCTCCCardFromCache.do")
	public void removeCard(HttpServletRequest req, HttpServletResponse resp) {
		String channel = this.getStringParam("channel", req, null);
		List<Integer> list = cardService.removeCacheCard(channel);
		LOG.info("remove card cache size=" + list.size());
		this.returnText("remove card cache size=" + list.size(), resp);
	}
}
