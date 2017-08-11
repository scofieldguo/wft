package com.bw30.open.wftAdm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bw30.open.common.dao.mapper.UserMapper;
import com.bw30.open.common.dao.mapper.WftOperatorMapper;
import com.bw30.open.common.dao.mapper.WftTotalStatMapper;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.stat.WftTotalStat;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.open.wft.common.Pager;

public class WftTotalStatServiceImpl {

	@Resource
	private WftTotalStatMapper wftTotalStatMapper;
	@Resource
	private WftOperatorMapper wftOperatorMapper;
	@Resource
	private UserMapper userMapper;
	
	
	public List<WftTotalStat> getWftTotalStats(String startDate,String endDate,String channel,Integer opId,Pager pager){
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		if(channel !=null && !"0".equals(channel)){
			paramMap.put("channel", channel);
		}
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		Integer count = wftTotalStatMapper.countByParamGroupByOp(paramMap);
		if(count==null || count==0){
			return null;
		}
		pager.setRecCount(count);
		List<WftTotalStat> list = wftTotalStatMapper.pageFindByParamGroupByOp(paramMap, pager);
		return list;
	}
	
	/**
	 * 手机端不分页
	 * */
	public List<WftTotalStat> getWftTotalStats(String startDate,String endDate,String channel,Integer opId){
		Map<String, Object> paramMap =  new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		if(channel !=null && !"0".equals(channel)){
			paramMap.put("channel", channel);
		}
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		List<WftTotalStat> list = wftTotalStatMapper.findByParamGroupByOp(paramMap);
		return list;
	}


	public void exportExcel(String startDate, String endDate, String channel,
			Integer opId,HttpServletResponse res) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate",startDate);
		paramMap.put("endDate", endDate);
		if(channel !=null && !"0".equals(channel)){
			paramMap.put("channel", channel);
		}
		if(opId !=null && opId != 0){
			paramMap.put("opid", opId);
		}
		List<WftTotalStat> list= wftTotalStatMapper.findByParamGroupByOp(paramMap);
		HSSFWorkbook workbook = export(list);
		ExcelUtil.PrintExcel(workbook, "data",
				res);
		
		
	}
	
	private Map<String, String> getUserMap(){
		List<OpenPlatformUser> list = userMapper.findAll();
		Map<String, String> map = new HashMap<String, String>();
		
		for (OpenPlatformUser openPlatformUser : list) {
			map.put(openPlatformUser.getChannelcode(), openPlatformUser.getName());
		}
		return map;
	}
	private Map<Integer, String> getOperatorMap(){
		Map<Integer, String> opMap =  new HashMap<Integer, String>();
		
		List<WftOperator> list = wftOperatorMapper.findAll();
		for(WftOperator wftOperator :list){
			opMap.put(wftOperator.getId(), wftOperator.getName());
		}
		return opMap;
	}
	private HSSFWorkbook export(List<WftTotalStat> list) {
		Map<Integer, String> opMap = getOperatorMap();
		Map<String, String> userMap = getUserMap();
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		setTitle(titleName, nameMap);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		for (WftTotalStat t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dairy",t.getDairyStr());
			map.put("channel", userMap.get(t.getChannel()));
			map.put("op",opMap.get(t.getOpid()));
			map.put("utvalue", t.getUtvalue());
			map.put("utvalueop", t.getUtvalueop());
			map.put("utvaluetrue",t.getUtvaluetrue());
			map.put("sdkcnt", t.getSdkcnt());
			map.put("sdkper", t.getSdkper());
			map.put("connsuc", t.getConnsuc());
			map.put("connfail", t.getConnfail());
			map.put("connlv", t.getConnlv()+"%");
			map.put("persuc", t.getPersuc());
			map.put("perfail", t.getPerfail());
			map.put("perlv", t.getPerlv()+"%");
			map.put("macsuc", t.getMacsuc());
			map.put("macfail", t.getMacfail());
			map.put("maclv", t.getMaclv()+"%");
			map.put("overnfail", t.getOvernfail());
			map.put("nocard", t.getNocard());
			realdataList.add(map);
		}
		return POIUtils.exportExcel(realdataList, titleName, nameMap);
	}
	private void setTitle(List<String> titleName, Map<String, String> nameMap) {
		// title璁剧疆
		titleName.add("日期");
		titleName.add("渠道");
		titleName.add("运营商");
		titleName.add("消耗时长-平台");
		titleName.add("消耗时长-对账");
		titleName.add("消耗时长-运营商");
		titleName.add("SDK打开次数");
		titleName.add("SDK打开人数");
		titleName.add("连接成功次数");
		titleName.add("连接失败次数");
		titleName.add("连接成功率");
		titleName.add("连接成功人数");
		titleName.add("连接失败人数");
		titleName.add("人数成功率");
		titleName.add("连接成功热点数");
		titleName.add("连接失败热点数");
		titleName.add("热点成功率");
		titleName.add("3次以上失败热点数");
		titleName.add("取卡失败数");
		
		nameMap.put("日期", "dairy");
		nameMap.put("渠道", "channel");
		nameMap.put("运营商", "op");
		nameMap.put("消耗时长-平台", "utvalue");
		nameMap.put("消耗时长-对账", "utvalueop");
		nameMap.put("消耗时长-运营商", "utvaluetrue");
		nameMap.put("SDK打开次数", "sdkcnt");
		nameMap.put("SDK打开人数", "sdkper");
		nameMap.put("连接成功次数", "connsuc");
		nameMap.put("连接失败次数", "connfail");
		nameMap.put("连接成功率", "connlv");
		nameMap.put("连接成功人数", "persuc");
		nameMap.put("连接失败人数", "perfail");
		nameMap.put("人数成功率", "perlv");
		nameMap.put("连接成功热点数", "macsuc");
		nameMap.put("连接失败热点数", "macfail");
		nameMap.put("热点成功率", "maclv");
		nameMap.put("3次以上失败热点数","overnfail");
		nameMap.put("取卡失败数", "nocard");
	}


	public void setWftTotalStatMapper(WftTotalStatMapper wftTotalStatMapper) {
		this.wftTotalStatMapper = wftTotalStatMapper;
	}


	public void setWftOperatorMapper(WftOperatorMapper wftOperatorMapper) {
		this.wftOperatorMapper = wftOperatorMapper;
	}


	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
}
