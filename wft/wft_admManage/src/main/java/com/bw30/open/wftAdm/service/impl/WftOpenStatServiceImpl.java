package com.bw30.open.wftAdm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftOpenStatMapper;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.common.model.stat.WftOpenStat;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wft.mongo.MongoDBService;
import com.bw30.open.wftAdm.service.operate.IWftOpenStatService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class WftOpenStatServiceImpl implements IWftOpenStatService{
    private WftOpenStatMapper wftOpenStatMapper;
    private WftChannelMapper wftChannelMapper;
    private MongoDBService mongoDBService;
	public List<WftOpenStat> findOpenStat(String start, String end, Pager pager) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("start",start );
		paramMap.put("end", end);
		pager.setRecCount(wftOpenStatMapper.countByParam(paramMap));
		return wftOpenStatMapper.findPagerByParam(paramMap,pager);
	}
	public void setWftOpenStatMapper(WftOpenStatMapper wftOpenStatMapper) {
		this.wftOpenStatMapper = wftOpenStatMapper;
	}
	public void exportExcel(String start, String end, HttpServletResponse res) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("start",start);
		paramMap.put("end",end);
		List<WftOpenStat> list= wftOpenStatMapper.findWithParam(paramMap);
		HSSFWorkbook workbook = export(list);
		ExcelUtil.PrintExcel(workbook, "数据",
				res);
		
	}
	private HSSFWorkbook export(List<WftOpenStat> list) {
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		setTitle(titleName, nameMap);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		for (WftOpenStat t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dairy",t.getDairy());
			map.put("channel",t.getChannel());
			map.put("cnvalue",t.getCnutvaluestr());
			map.put("cnvalueop",t.getCnutvalueopstr());
			map.put("cnop", t.getWftConsumeStat().getTvalue());
			map.put("cmvalue",t.getCmutvalue());
			map.put("cmvalueop",t.getCmutvalueop());
			map.put("jarcnt", t.getSdkcnt());
			map.put("jarper", t.getSdkper());
			map.put("cncnsucc",t.getCncnsucccnt());
			map.put("cncnfail", t.getCncnfailcnt());
			map.put("cnrate", t.getCncnrate()+"%");
			map.put("cncnsuper", t.getCncnsuccper());
			map.put("cncnfaper", t.getCncnfailper());
			map.put("cnperrate", t.getCncnperrate());
			map.put("cmcnsucc", t.getCmcnsucccnt());
			map.put("cmcnfail", t.getCmcnfailcnt());
			map.put("cmrate", t.getCmcnrate()+"%");
			map.put("cmcnsuper", t.getCmcnsuccper());
			map.put("cmcnfaper", t.getCmcnfailper());
			map.put("cmperrate", t.getCmcnperrate()+"%");
			map.put("cmsumac", t.getCmsuccmac());
			map.put("cmfamac", t.getCmfailmac());
			map.put("cmmacrate", t.getCmmacrate()+"%");
			map.put("cm3",t.getCmfaovthree());
			map.put("cnsumac",t.getCnsuccmac());
			map.put("cnfamac", t.getCnfailmac());
			map.put("cnmacrate", t.getCnmacrate()+"%");
			map.put("cn3",t.getCnfaovthree());
			map.put("cmfacard", t.getCmaccesscardfail());
			map.put("cnfacard", t.getCnaccesscardsucc());
			realdataList.add(map);
		}
		return POIUtils.exportExcel(realdataList, titleName, nameMap);
	}
	private void setTitle(List<String> titleName, Map<String, String> nameMap) {
		// title璁剧疆
		titleName.add("日期");
		titleName.add("渠道");
		titleName.add("电信消耗时长-平台");
		titleName.add("电信消耗时长-运营商");
		titleName.add("电信运营商平台统计消耗时长");
		titleName.add("移动消耗时长-平台");
		titleName.add("移动消耗时长-运营商");
		titleName.add("打开jar包次数");
		titleName.add("打开jar包人数");
		titleName.add("电信连接成功次数");
		titleName.add("电信连接失败次数");
		titleName.add("电信次数连接成功率");
		titleName.add("电信连接成功人数");
		titleName.add("电信连接失败人数");
		titleName.add("电信人数连接成功率");
		titleName.add("移动连接成功次数");
		titleName.add("移动连接失败次数");
		titleName.add("移动连接次数成功率");
		titleName.add("移动连接成功人数");
		titleName.add("移动连接失败人数");
		titleName.add("移动连接人数成功率");
		titleName.add("移动连接成功热点数");
		titleName.add("移动连接失败热点数");
		titleName.add("移动连接热点陈功率");
		titleName.add("移动连接3次以上不成功热点数");
		titleName.add("电信连接成功热点数");
		titleName.add("电信连接失败热点数");
		titleName.add("电信热点连接成功率");
		titleName.add("电信连接3次以上不成功热点数");
		titleName.add("移动取卡失败次数");
		titleName.add("电信取卡失败次数");
		
		
		nameMap.put("日期", "dairy");
		nameMap.put("渠道", "channel");
		nameMap.put("电信消耗时长-平台", "cnvalue");
		nameMap.put("电信消耗时长-运营商", "cnvalueop");
		nameMap.put("电信运营商平台统计消耗时长", "cnop");
		nameMap.put("移动消耗时长-平台", "cmvalue");
		nameMap.put("移动消耗时长-运营商","cmvalueop");
		nameMap.put("打开jar包次数", "jarcnt");
		nameMap.put("打开jar包人数", "jarper");
		nameMap.put("电信连接成功次数", "cncnsucc");
		nameMap.put("电信连接失败次数", "cncnfail");
		nameMap.put("电信次数连接成功率", "cnrate");
		nameMap.put("电信连接成功人数", "cncnsuper");
		nameMap.put("电信连接失败人数", "cncnfaper");
		nameMap.put("电信人数连接成功率", "cnperrate");
		nameMap.put("移动连接成功次数", "cmcnsucc");
		nameMap.put("移动连接失败次数", "cmcnfail");
		nameMap.put("移动连接次数成功率", "cmrate");
		nameMap.put("移动连接成功人数", "cmcnsuper");
		nameMap.put("移动连接失败人数", "cmcnfaper");
		nameMap.put("移动连接人数成功率", "cmperrate");
		nameMap.put("移动连接成功热点数", "cmsumac");
		nameMap.put("移动连接失败热点数", "cmfamac");
		nameMap.put("移动连接热点陈功率", "cmmacrate");
		nameMap.put("移动连接3次以上不成功热点数", "cm3");
		nameMap.put("电信连接成功热点数", "cnsumac");
		nameMap.put("电信连接失败热点数", "cnfamac");
		nameMap.put("电信热点连接成功率", "cnmacrate");
		nameMap.put("电信连接3次以上不成功热点数", "cn3");
		nameMap.put("移动取卡失败次数", "cmfacard");
		nameMap.put("电信取卡失败次数", "cnfacard");
	}
	@Override
	public List<WftOpenStat> findOpenStatByChannel(String start, String end,
			Pager pager, String channel) {
		pager.setRecCount(wftOpenStatMapper.countByChannel(start,end,channel));
		return wftOpenStatMapper.findPagerByChannel(start,end,pager,channel);
	}
	
	@Override
	public void exportChannelExcel(String start, String end,
			HttpServletResponse res, String channel) {
		List<WftOpenStat> list= wftOpenStatMapper.findByChannel(start,end,channel);
		HSSFWorkbook workbook = export(list);
		ExcelUtil.PrintExcel(workbook, "数据",
				res);
		
	}
	@Override
	public List<WftChannel> findChannel() {
		return wftChannelMapper.findAll();
	}
	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	@Override
	public void exportUnoteExcel(Date start, Date end, Integer opid, String channel,String note,HttpServletResponse res) {
		BasicDBObject condition=new BasicDBObject();//条件 
		condition.append("etime", new BasicDBObject("$gte",start).append("$lt", end));
		condition.append("channel", channel);
		if(null == note || "".equals(note.trim())){
			condition.append("note", new BasicDBObject("$exists",true));
		}else{
			condition.append("note", note);
		}
		if(0!=opid){
			condition.append("opid", opid);
		}
		BasicDBObject key=new BasicDBObject();//条件 
		key.put("cno", 1);
		key.put("opid", 1);
		key.put("ssid", 1);
		key.put("stime", 1);
		key.put("etime", 1);
		key.put("note", 1);
		DBCursor cursor=mongoDBService.getCollection(MongoDBService.CONNDATAKEY+channel).find(condition, key);
		List<String> titleName = new ArrayList<String>();
		Map<String, String> nameMap = new HashMap<String, String>();
		titleName.add("卡号");
		titleName.add("运营商");
		titleName.add("ssid");
		titleName.add("上线时间");
		titleName.add("下线时间");
		titleName.add("错误码");
		nameMap.put("卡号", "cno");
		nameMap.put("运营商", "opid");
		nameMap.put("ssid", "ssid");
		nameMap.put("上线时间", "stime");
		nameMap.put("下线时间", "etime");
		nameMap.put("错误码", "note");
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		while(cursor.hasNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			DBObject conn=cursor.next();
			map.put("cno", conn.get("cno"));
			map.put("opid", conn.get("opid"));
			map.put("ssid", conn.get("ssid"));
			map.put("stime", DateUtil.formateDate((Date)conn.get("stime"), "yyyy-MM-dd HH:mm:ss"));
			map.put("etime",  DateUtil.formateDate((Date)conn.get("etime"), "yyyy-MM-dd HH:mm:ss"));
			map.put("note",conn.get("note"));
			realdataList.add(map);
		}
		HSSFWorkbook workbook =  POIUtils.exportExcelWithSheets(realdataList, titleName, nameMap);//POIUtils.exportExcel(realdataList, titleName, nameMap)
		ExcelUtil.PrintExcel(workbook, "note_data.xls",
				res);
	}
	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}

}
