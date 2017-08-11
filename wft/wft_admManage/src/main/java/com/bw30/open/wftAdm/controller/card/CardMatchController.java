package com.bw30.open.wftAdm.controller.card;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.bw30.open.common.model.BillCompare;
import com.bw30.open.wft.common.translate.model.ExcelModel;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.controller.JsonModel;
import com.bw30.open.wftAdm.service.card.CardMatchService;
import com.bw30.open.wftAdm.service.card.thread.CardThread;
/**
 * 佛山移动账单比对
 *
 * @author raymond
 *
 */
@Controller
public class CardMatchController extends BaseController {

	@Resource
	private CardMatchService cardMatchService;

	private final Logger logger = Logger.getLogger(CardMatchService.class);

	/**
	 * 对应首页
	 * 
	 * @param viewMap
	 * @param req
	 * @return
	 */
	@RequestMapping("card_match_index.do")
	public ModelAndView index(ModelMap viewMap, HttpServletRequest req) {
		// cardMatchService.compareBill();
		return new ModelAndView("card/match_index", viewMap);
	}

	@RequestMapping("card_match_count.do")
	@ResponseBody
	public int count() {
		return CardThread.getCount().get();
	}
	
	@RequestMapping("card_match_delete.do")
	@ResponseBody
	public int delete(){
		cardMatchService.delete();
		return 0;
	}
	
	@RequestMapping("card_match_compare.do")
	@ResponseBody
	public int compare(ModelMap viewMap, HttpServletRequest req) {
		int count = cardMatchService.compareBill();
		return count;
	}
	
	@RequestMapping("card_match_total.do")
	@ResponseBody
	public JSONObject total() {
		List<BillCompare> comparedList = cardMatchService.getComparedList(true);
		long comparedTime = cardMatchService.getTotalTime(comparedList);
		List<BillCompare> list = cardMatchService.getComparedList(false);
		long totalTime = cardMatchService.getTotalTime(list);
		CardThread.setCount(new AtomicInteger(0));//清空
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("compared", comparedList.size());
		jsonObject.put("total", list.size());
		jsonObject.put("comparedTime", comparedTime);
		jsonObject.put("totalTime", totalTime);
		return jsonObject;
	}

	/**
	 * 获取数据
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("card_match_list.do")
	@ResponseBody
	public JsonModel list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) int rows,
			@RequestParam(value = "date", required = false) String date) {
		pager.setPageIndex(page);
		pager.setPageSize(rows);
		List<BillCompare> list = cardMatchService.getBillCompares(pager);
		JsonModel jsonModel = this.constructJsonModel(list, pager);
		return jsonModel;
	}

	/**
	 * 上传账单
	 * 
	 * @param viewMap
	 * @param request
	 * @return
	 */
	@RequestMapping("card_match_upload.do")
	public ModelAndView upload(
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "date", required = false) String date,
			ModelMap viewMap, HttpServletRequest request) {
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
		MultipartFile xlsFile = mreq.getFile("uploadFile");// 获取xls文件
		logger.info("文件上传成功》》》》》》》》》》》》》》》》》》》" + xlsFile.getName());
		List<ExcelModel> models = cardMatchService.getDatas(xlsFile, id, date);
		if (models != null && models.size() > 0) {
			logger.info("上传账单处理成功，条数=" + models.size());
			List<BillCompare> list = cardMatchService
					.toBillCompares(models, id);
			cardMatchService.addBill(list);
		}
		return new ModelAndView("card/match_index", viewMap);
	}

	public CardMatchService getCardMatchService() {
		return cardMatchService;
	}

	public void setCardMatchService(CardMatchService cardMatchService) {
		this.cardMatchService = cardMatchService;
	}

}
