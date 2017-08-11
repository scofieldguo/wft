package com.bw30.open.wftAdm.controller.operator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.stat.WftOpenTopCardUse;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.open.wftAdm.controller.BaseController;
import com.bw30.open.wftAdm.model.ofc.Graph;
import com.bw30.open.wftAdm.service.IWftOpenChannelService;
import com.bw30.open.wftAdm.service.operate.IWftOpenAcessCardStatService;
import com.bw30.open.wftAdm.service.operate.IWftOpenTopCardUseService;

@Controller
public class WftOpenTopCardUseController extends BaseController{
	
	String[] colorList = { "FF0000", "F8CE2A", "09F709", "00FFFF", "0000FF",
			"FF00FF", "68228B", "C4723C", "FFAEB9", "000000" };	
	@Resource
	private IWftOpenTopCardUseService wftOpenTopCardUseService;
	@Resource
	private IWftOpenAcessCardStatService wftOpenAcessCardStatService;
	@Resource
	private IWftOpenChannelService wftOpenChannelService;
	
	@RequestMapping("cardTopIndex.do")
	public ModelAndView accessIndex(HttpServletRequest request,ModelMap view){
		List<WftChannel> channelList=wftOpenAcessCardStatService.getAllChannel();
		String	start=DateUtil.formateDate(new Date(),"yyyy-MM-dd HH");
		view.put("channelList", channelList);
		view.put("start", start);
		System.out.println(start);
		return new ModelAndView("stat/cardtopuse",view);
	}
	@RequestMapping("cardTopUse.do")
	public ModelAndView statOpen(HttpServletRequest req,ModelMap view){
		Integer pageId=this.getIntegerParam("pageId", req, 1);
		String start=this.getStringParam("start", req, null);
		String channel=this.getStringParam("channel", req, null);
		String hour=this.getStringParam("hour", req, null);
		List<WftChannel> channelList=wftOpenAcessCardStatService.getAllChannel();
		System.out.println("channel"+channel);
		if(start==null){
			start=DateUtil.formateDate(DateUtil.getNextNumDay(new Date(), -1),"yyyy-MM-dd HH");
		}
		hour=start.substring(11,13);
		String dariy=start.substring(0,10);
		pager.setPageIndex(pageId);
		List<WftOpenTopCardUse> list=wftOpenTopCardUseService.find(dariy,channel,pager,hour);
		view.put("list", list);
		view.put("pager", pager);
		view.put("start",start );
		view.put("channel",channel );
		view.put("hour", hour);
		view.put("channelList", channelList);
		return new ModelAndView("stat/cardtopuse",view);
	}

	@RequestMapping("usepeakUI.do")
	public ModelAndView usepeakUI(HttpServletRequest request,HttpServletResponse response){
		List<WftChannel> channelList=wftOpenAcessCardStatService.getAllChannel();
		String daytime=this.getStringParam("daytime", request, null);
		String channel=this.getStringParam("channel", request, "10001");
		if (daytime==null){
			daytime=DateUtil.formateDate(new Date(),"yyyy-MM-dd ");
			
		}
		Random rand=new Random();
		ModelAndView mv = new ModelAndView("stat/usepeak");
		mv.addObject("daytime",daytime);
		mv.addObject("random", rand.nextInt());
		mv.addObject("channelList",channelList);
		mv.addObject("channel", channel);
		return mv;
	}
	
	@RequestMapping("usepeak.do")
	public void usepeak(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String daytime=this.getStringParam("daytime", request, null);
		String channel=this.getStringParam("channel", request, "10001");
		System.out.println("daytime"+daytime);
		Graph g = new Graph();
		List<String> labelList = new ArrayList<String>();
		for (int i=0;i<=23;i++) {
			if(i<=9){
				labelList.add("0"+String.valueOf(i));
				labelList.add("0"+i+":"+30);	
			}else{
			labelList.add(String.valueOf(i));
			labelList.add(i+":"+30);
		}
			}
		g.set_x_labels(labelList);
		Integer max_y_label=0;
		int max=0;
	    	for(int j=0;j<6;j++){
	    		if(j==0){
	    			g.line_hollow("2", "4", colorList[j], "CMCC在线计费", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getChargeTopValue(label,daytime,"CMCC",channel);
	    				if(o==null){
	    					o=0;
	    				}	
	    				if (o>max)
	    					max=o;
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    		}
	    		else if(j==1){
	    			g.line_hollow("2", "4", colorList[j], "CMCC在线计费+不计费", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getTopValue(label,daytime,"CMCC",channel,1);
	    					if(o==null){
	    						o=0;
	    					}
	    				if (o>max){
	    					max=o;
	    					}
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    		}
	    		else if(j==2){
	    			g.line_hollow("2", "4", colorList[j], "ChinaNet在线计费", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getChargeTopValue(label,daytime,"ChinaNet",channel);
	    					if(o==null){
	    						o=0;
	    					}
	    				if (o>max){
	    					max=o;
	    					}
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    		}
	    		else if(j==3){
	    			g.line_hollow("2", "4", colorList[j], "ChinaNet在线计费+不计费", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getTopValue(label,daytime,"ChinaNet",channel,1);
	    					if(o==null){
	    						o=0;
	    					}
	    				if (o>max){
	    					max=o;
	    					}
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    		}else if(j==4){
	    			g.line_hollow("2", "4", colorList[j], "ChinaNet在线计费+不计费+取卡失败", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getTopValue(label,daytime,"ChinaNet",channel,2);
	    					if(o==null){
	    						o=0;
	    					}
	    				if (o>max){
	    					max=o;
	    					}
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    			
	    		}else if(j==5){
	    			g.line_hollow("2", "4", colorList[j], "CMCC在线计费+不计费+取卡失败", "10");
	    			List<String> datalist=new ArrayList<String>();
	    			try {
	    				for (String label:labelList){
	    					Integer o=wftOpenTopCardUseService.getTopValue(label,daytime,"CMCC",channel,2);
	    					if(o==null){
	    						o=0;
	    					}
	    				if (o>max){
	    					max=o;
	    					}
	    				datalist.add(o.toString());
	    				if (max_y_label<o)
	    					max_y_label=o;
	    				}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			} 
	    			g.set_data(datalist);
	    			g.set_y_max(max);
	    		}
	    	}
//		response.setCharacterEncoding("utf-8");
		response.setContentType("charset=utf-8");
		response.getWriter().write(g.render());
	}
	
	@RequestMapping("jqchart.do")
	public ModelAndView jq(HttpServletRequest request,ModelMap view){
		List<WftChannel> channelList=wftOpenChannelService.findAll();
		view.put("channelList", channelList);
//		String channelId=this.getStringParam("channelId", request, "10001");
		return new ModelAndView("stat/test",view);
	}
	@RequestMapping("interval.do")
	public void interval(HttpServletRequest request,HttpServletResponse response){
		String channelId=this.getStringParam("channelId", request, "10001");
		String dairy=DateUtil.formateDate(new Date(), "yyyy-MM-dd");
		Integer limit=this.getIntegerParam("limit", request, 20);
		List<WftOpenTopCardUse> list=wftOpenTopCardUseService.getlast20(dairy,channelId,limit);
		Collections.reverse(list);
		response.setContentType("charset=utf-8");
		System.out.println(com.alibaba.fastjson.JSON.toJSONString(list));
		try {
			response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(list));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("exportChinaNet.do")
	public void exportChinaNet(HttpServletRequest request,HttpServletResponse response){
		String daytime=this.getStringParam("daytime", request, null);
		String channel=this.getStringParam("channel", request, "10001");
		Integer flag=this.getIntegerParam("flag", request, 1);
		String op="";
		if(flag==1){
			op="ChinaNet";
		}else{
			op="CMCC";
		}
		List<String> labelList = new ArrayList<String>();
		for (int i=0;i<=23;i++) {
			if(i<=9){
				labelList.add("0"+String.valueOf(i));
				labelList.add("0"+i+":"+30);	
			}else{
			labelList.add(String.valueOf(i));
			labelList.add(i+":"+30);
		}
			}
		List<Tem> list=new ArrayList<WftOpenTopCardUseController.Tem>();
		for (String lable:labelList) {
			Tem tem=new Tem();
			Integer o=0;
			o=wftOpenTopCardUseService.getTopValue(lable,daytime,op,channel,2);
			tem.setLable(lable);
			if(o==null){
				o=0;
			}
			tem.setNumber(o);
			list.add(tem);
		}

		HSSFWorkbook workbook = export(list);
		ExcelUtil.PrintExcel(workbook, "数据",
				response);
	}
	
	private HSSFWorkbook export(List<Tem> list) {
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		setTitle(titleName, nameMap);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		for (Tem t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dairy",t.getLable());
			map.put("channel",t.getNumber());
			realdataList.add(map);
		}
		return POIUtils.exportExcel(realdataList, titleName, nameMap);
	}
	private void setTitle(List<String> titleName, Map<String, String> nameMap) {
		// title璁剧疆
		titleName.add("时间");
		titleName.add("峰值");
		nameMap.put("时间", "dairy");
		nameMap.put("峰值", "channel");
	}
	public void setWftOpenTopCardUseService(
			IWftOpenTopCardUseService wftOpenTopCardUseService) {
		this.wftOpenTopCardUseService = wftOpenTopCardUseService;
	}
	public void setWftOpenAcessCardStatService(
			IWftOpenAcessCardStatService wftOpenAcessCardStatService) {
		this.wftOpenAcessCardStatService = wftOpenAcessCardStatService;
	}
	public void setWftOpenChannelService(
			IWftOpenChannelService wftOpenChannelService) {
		this.wftOpenChannelService = wftOpenChannelService;
	}
	class Tem{
		private String lable;
		private int number;
		
		public String getLable() {
			return lable;
		}
		public void setLable(String lable) {
			this.lable = lable;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
		
	}
}
