package com.bw30.openplatform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bw30.open.common.dao.mapper.WftConsumeStatMapper;
import com.bw30.open.common.dao.mapper.WftOpenPlatFormStatMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftConsumeStat;
import com.bw30.open.common.model.stat.WftOpenPlatFormStat;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.open.wft.common.Pager;
import com.bw30.openplatform.service.IStatService;

public class StatServiceImpl implements IStatService {

	@Resource
	private WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private WftConsumeStatMapper wftConsumeStatMapper;
	public List<WftOpenPlatFormStat> getWftTotalStats(String startDate,String endDate,String channel,Integer opId,Pager pager){
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("channel", channel);
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		Integer count = wftOpenPlatFormStatMapper.countByParamGroupByOp(paramMap);
		if(count==null || count==0){
			return null;
		}
		pager.setRecCount(count);
		List<WftOpenPlatFormStat> list = wftOpenPlatFormStatMapper.pageFindByParamGroupByOp(paramMap, pager);
		return list;
	}


	public void exportExcel(String startDate, String endDate, String channel,
			Integer opId,HttpServletResponse res) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate",startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("channel", channel);
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		List<WftOpenPlatFormStat> list= wftOpenPlatFormStatMapper.findByParamGroupByOp(paramMap);
		HSSFWorkbook workbook = export(list);
		ExcelUtil.PrintExcel(workbook, "数据",
				res);
		
		
	}
	
	private Map<Integer, String> getOperatorMap(){
		Map<Integer, String> opMap =  new HashMap<Integer, String>();
		
		List<WftOperator> list = wftOperatorMapper.findAll();
		for(WftOperator wftOperator :list){
			opMap.put(wftOperator.getId(), wftOperator.getName());
		}
		return opMap;
	}
	private HSSFWorkbook export(List<WftOpenPlatFormStat> list) {
		Map<Integer, String> opMap = getOperatorMap();
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		setTitle(titleName, nameMap);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		for (WftTotalStat t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dairy",t.getDairyStr());
			map.put("op", opMap.get(t.getOpid()));
			map.put("utvaluetrue",t.getUtvaluetrue());
			map.put("daylv", t.getDaylv()+"%");
			map.put("avghour", t.getAvghour());
			map.put("conncnt",t.getConnsuc()+t.getConnfail());
			map.put("connsuc", t.getConnsuc());
			map.put("connlv", t.getConnlv()+"%");
			map.put("percnt", t.getPerfail()+t.getPersuc());
			map.put("persuc", t.getPersuc());
			map.put("perlv", t.getPerlv()+"%");
			realdataList.add(map);
		}
		return POIUtils.exportExcel(realdataList, titleName, nameMap);
	}
	private void setTitle(List<String> titleName, Map<String, String> nameMap) {
		// title璁剧疆
		titleName.add("日期");
		titleName.add("运营商");
		titleName.add("消耗时长");
		titleName.add("日消耗增长率");
		titleName.add("人均消耗时长");
		titleName.add("连接次数");
		titleName.add("连接成功次数");
		titleName.add("次数成功率");
		titleName.add("连接人数");
		titleName.add("成功连接人数");
		titleName.add("人数链接成功率");
		
		nameMap.put("日期", "dairy");
		nameMap.put("运营商", "op");
		nameMap.put("消耗时长", "utvaluetrue");
		nameMap.put("日消耗增长率", "daylv");
		nameMap.put("人均消耗时长", "avghour");
		nameMap.put("连接次数", "conncnt");
		nameMap.put("连接成功次数","connsuc");
		nameMap.put("次数成功率", "connlv");
		nameMap.put("连接人数", "percnt");
		nameMap.put("成功连接人数", "persuc");
		nameMap.put("人数链接成功率", "perlv");
	}
	
	
	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}

	public List<WftOpenPlatFormStat> getWftMacStats(String startDate, String endDate,
			String channel, Integer opId, Pager pager) {
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("channel", channel);
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		Integer count = wftOpenPlatFormStatMapper.countMacByParamGroupByOp(paramMap);
		if(count==null || count==0){
			return null;
		}
		pager.setRecCount(count);
		List<WftOpenPlatFormStat> list = wftOpenPlatFormStatMapper.pageFindMacByParamGroupByOp(paramMap, pager);
		return list;
	}
	
	public void exportMacExcel(String startDate, String endDate,
			String channel, Integer opId, HttpServletResponse res) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate",startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("channel", channel);
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		List<WftOpenPlatFormStat> list= wftOpenPlatFormStatMapper.findMacByParamGroupByOp(paramMap);
		HSSFWorkbook workbook = exportMac(list);
		ExcelUtil.PrintExcel(workbook, "数据",
				res);
		// TODO Auto-generated method stub
		
	}

	
	private HSSFWorkbook exportMac(List<WftOpenPlatFormStat> list) {
		Map<Integer, String> opMap = getOperatorMap();
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		setMacTitle(titleName, nameMap);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		for (WftTotalStat t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dairy",t.getDairyStr());
			map.put("op", opMap.get(t.getOpid()));
			map.put("macsuc",t.getMacsuc());
			map.put("macfail", t.getMacfail());
			map.put("maclv", t.getMaclv()+"%");
			map.put("overnfail", t.getOvernfail());
			realdataList.add(map);
		}
		return POIUtils.exportExcel(realdataList, titleName, nameMap);
	}
	private void setMacTitle(List<String> titleName, Map<String, String> nameMap) {
		titleName.add("日期");
		titleName.add("运营商");
		titleName.add("连接成功热点数");
		titleName.add("连接失败热点数");
		titleName.add("热点连接成功率");
		titleName.add("连接3次以上不成功热点数");
		
		nameMap.put("日期", "dairy");
		nameMap.put("运营商", "op");
		nameMap.put("连接成功热点数", "macsuc");
		nameMap.put("连接失败热点数", "macfail");
		nameMap.put("热点连接成功率", "maclv");
		nameMap.put("连接3次以上不成功热点数", "overnfail");
	}

	public WftTotalStat getLastDayStat(String startDate, String endDate,
			String channelcode, int opIdCtcc) {
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("channel", channelcode);
		paramMap.put("opid", opIdCtcc);
		return wftOpenPlatFormStatMapper.getLastDayStat(paramMap);
	}
	public List<WftConsumeStat> getIntervalStat(String startday, String endday,
			String channelcode, int opIdCtcc) {
		// TODO Auto-generated method stub
		return wftConsumeStatMapper.getIntervalStat(startday,endday,channelcode,opIdCtcc);
	}
	
	public List<WftOpenPlatFormStat> getIntervalConnStat(String startday,
			String endday, String channelcode, int opIdCtcc) {
		// TODO Auto-generated method stub
		return wftOpenPlatFormStatMapper.getIntervalStat(startday,endday,channelcode,opIdCtcc);
	}
	
	public List<Integer> getUsedTime(String firstDayOfMonth, String today,
			String channelcode, int opIdCtcc) {
		return wftConsumeStatMapper.getUsedTime(firstDayOfMonth,today,channelcode,opIdCtcc);
	}

	public void setWftConsumeStatMapper(WftConsumeStatMapper wftConsumeStatMapper) {
		this.wftConsumeStatMapper = wftConsumeStatMapper;
	}


	public void setWftOpenPlatFormStatMapper(
			WftOpenPlatFormStatMapper wftOpenPlatFormStatMapper) {
		this.wftOpenPlatFormStatMapper = wftOpenPlatFormStatMapper;
	}





	
	
}
