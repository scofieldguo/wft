package com.bw30.open.wftAdm.controller.operator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.service.operate.IWftOpenStatProSuccessService;


@Controller
public class WftOpenStatProSuccessController extends BaseController {

	@Resource
	private IWftOpenStatProSuccessService wftOpenStatProSuccessService;
	@RequestMapping("province.do")
	public ModelAndView proSuccessStatOpen(HttpServletRequest req,ModelMap view){
		String start=this.getStringParam("start", req, null);
		String end=this.getStringParam("end", req, null);
		String ssid=this.getStringParam("ssid", req, "ChinaNet");
		if(start==null || end ==null){
			start=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
			end=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd");
		}
		List<WftOperator> operatorList = wftOpenStatProSuccessService.findAllOperator();
		List<WftTotalStat> wftTotalStatList = wftOpenStatProSuccessService.findByProvice(ssid, start, end);//默认查询前一天  电信数据
		List<WftProvince> provinceList = wftOpenStatProSuccessService.findAllWftProvince();
		Map<Integer, String> prvidToPrvname = new HashMap<Integer, String>();
		for(WftProvince wftProvince : provinceList ){
			prvidToPrvname.put(wftProvince.getId(), wftProvince.getName());
		}
		for(int i = 0; i < wftTotalStatList.size(); i++ ){
			String prvName = null;
			prvName = prvidToPrvname.get(wftTotalStatList.get(i).getPrvid());
			wftTotalStatList.get(i).setPrvname(prvName);
		}
		view.put("ssid", ssid);
		view.put("start", start);
		view.put("end", end);
		view.put("operatorList", operatorList);
		view.put("wftTotalStatList", wftTotalStatList);
		return new ModelAndView("stat/proSuccess",view);
	}
	
	public void setWftOpenStatProSuccessService(
			IWftOpenStatProSuccessService wftOpenStatProSuccessService) {
		this.wftOpenStatProSuccessService = wftOpenStatProSuccessService;
	}

}
