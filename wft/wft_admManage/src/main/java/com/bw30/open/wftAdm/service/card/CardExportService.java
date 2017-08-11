package com.bw30.open.wftAdm.service.card;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wftAdm.service.BaseDataService;

public class CardExportService {
	@Resource
	private BaseDataService baseDataService;
	
	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final Map<Integer, String> OPERATOR = new HashMap<Integer, String>();
	private static final Map<Integer, String> CARD_TYPE = new HashMap<Integer, String>();
	private static final Map<Integer, String> CARD_USTATUS = new HashMap<Integer, String>();
	private static final Map<Integer, String> CARD_CSTATUS = new HashMap<Integer, String>();
	static{
		OPERATOR.put(0, "未知");
		OPERATOR.put(1, "中国移动");
		OPERATOR.put(2, "中国电信");
		OPERATOR.put(3, "中国联通");
		
		CARD_TYPE.put(0, "电子卡");
		CARD_TYPE.put(1, "包月卡");
		CARD_TYPE.put(2, "不可跨月时长卡");
		CARD_TYPE.put(3, "可跨月时长卡");
		CARD_TYPE.put(4, "电子卡");
		CARD_TYPE.put(5, "账期卡");
		
		CARD_USTATUS.put(0, "正常");
		CARD_USTATUS.put(1, "异常");
		
		CARD_CSTATUS.put(0, "未启用");
		CARD_CSTATUS.put(1, "启用");
		CARD_CSTATUS.put(2, "可用");
		CARD_CSTATUS.put(3, "使用中");
		CARD_CSTATUS.put(4, "停用");
	}
	
	private static List<String> TITLE_CARD_ACTIVE = new ArrayList<String>();
	private static Map<String, String> NAME_CARD_ACTIVE = new HashMap<String, String>();
	static{
		TITLE_CARD_ACTIVE.add("卡id");
		TITLE_CARD_ACTIVE.add("卡号");
		TITLE_CARD_ACTIVE.add("渠道");
		TITLE_CARD_ACTIVE.add("运营商");
		TITLE_CARD_ACTIVE.add("省份");
		TITLE_CARD_ACTIVE.add("漫游");
		TITLE_CARD_ACTIVE.add("类型");
		TITLE_CARD_ACTIVE.add("分配次数");
		TITLE_CARD_ACTIVE.add("截止日期");
		TITLE_CARD_ACTIVE.add("金额");
		TITLE_CARD_ACTIVE.add("时长");
		TITLE_CARD_ACTIVE.add("流量");
		TITLE_CARD_ACTIVE.add("剩余时长");
		TITLE_CARD_ACTIVE.add("剩余流量");
		TITLE_CARD_ACTIVE.add("预启用时间");
		TITLE_CARD_ACTIVE.add("成功率");
		TITLE_CARD_ACTIVE.add("最后使用时间");
		TITLE_CARD_ACTIVE.add("使用状态");
		TITLE_CARD_ACTIVE.add("卡片状态");

		NAME_CARD_ACTIVE.put("卡id", "cid");
		NAME_CARD_ACTIVE.put("卡号", "cno");
		NAME_CARD_ACTIVE.put("渠道", "channel");
		NAME_CARD_ACTIVE.put("运营商", "op");
		NAME_CARD_ACTIVE.put("省份", "prv");
		NAME_CARD_ACTIVE.put("漫游", "rflag");
		NAME_CARD_ACTIVE.put("类型", "ctype");
		NAME_CARD_ACTIVE.put("分配次数", "ucount");
		NAME_CARD_ACTIVE.put("截止日期", "vetime");
		NAME_CARD_ACTIVE.put("金额", "mvalue");
		NAME_CARD_ACTIVE.put("时长", "bvalue");
		NAME_CARD_ACTIVE.put("流量", "bflow");
		NAME_CARD_ACTIVE.put("剩余时长", "tvalue");
		NAME_CARD_ACTIVE.put("剩余流量", "tflow");
		NAME_CARD_ACTIVE.put("预启用时间", "intime");
		NAME_CARD_ACTIVE.put("成功率", "usp");
		NAME_CARD_ACTIVE.put("最后使用时间", "utime");
		NAME_CARD_ACTIVE.put("使用状态", "ustatus");
		NAME_CARD_ACTIVE.put("卡片状态", "cstatus");
	}
	
	/**
	 * 导出卡池excel
	 * 
	 * @param cardActiveList
	 * @param fileName
	 */
	public void exportCardActive(String channel, List<WftCardActive> cardActiveList, String fileName, HttpServletResponse resp){
		Map<String, String> channelMap = this.baseDataService.getAllChannelMap();
		Map<Integer, String> prvMap = this.baseDataService.getAllProvinceMap();
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if(null != cardActiveList){
			for (WftCardActive ca : cardActiveList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cid", ca.getId());
				map.put("cno", ca.getNo());
				map.put("channel", channelMap.get(channel));
				map.put("op", OPERATOR.get(ca.getOpid()));
				map.put("prv", prvMap.get(ca.getPrvid()));
				map.put("rflag", "可以漫游");
				map.put("ctype", CARD_TYPE.get(ca.getCtype()));
				map.put("ucount", ca.getUcount());
				map.put("vetime", null == ca.getVetime() ? "" : SDF_YMD_HMS.format(ca.getVetime()));
				map.put("mvalue", 0);
				map.put("bvalue", ca.getBvalue());
				map.put("flow", 0);
				map.put("tvalue", ca.getTvalue());
				map.put("tflow", 0);
				map.put("intime", null == ca.getIntime() ? "" : SDF_YMD_HMS.format(ca.getIntime()));
				map.put("usp", 0);
				map.put("utime", null == ca.getUtime() ? "" : SDF_YMD_HMS.format(ca.getUtime()));
				map.put("cstatus", CARD_CSTATUS.get(ca.getCstatus()));
				map.put("ustatus", CARD_USTATUS.get(ca.getUstatus()));
				dataList.add(map);
			}
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(dataList, TITLE_CARD_ACTIVE, NAME_CARD_ACTIVE);
		ExcelUtil.PrintExcel(workbook, fileName, resp);
	}
	
	
	private static List<String> TITLE_CARD_INVALID = new ArrayList<String>();
	private static Map<String, String> NAME_CARD_INVALID = new HashMap<String, String>();
	static{
		TITLE_CARD_INVALID.add("卡号");
		TITLE_CARD_INVALID.add("渠道");
		TITLE_CARD_INVALID.add("运营商");
		TITLE_CARD_INVALID.add("密码");
		TITLE_CARD_INVALID.add("错误码");
		TITLE_CARD_INVALID.add("停卡时间");
		
		NAME_CARD_INVALID.put("卡号", "cno");
		NAME_CARD_INVALID.put("渠道", "channel");
		NAME_CARD_INVALID.put("运营商", "op");
		NAME_CARD_INVALID.put("密码", "pwd");
		NAME_CARD_INVALID.put("错误码", "stopcode");
		NAME_CARD_INVALID.put("停卡时间", "stoptime");
	}
	/**
	 * 导出问题卡excel
	 * 
	 * @param channel
	 * @param cardActiveList
	 * @param fileName
	 * @param resp
	 */
	public void exportInvalidCard(String channel, List<WftCardActive> cardActiveList, String fileName, HttpServletResponse resp){
		Map<String, String> channelMap = this.baseDataService.getAllChannelMap();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if(null != cardActiveList){
			for (WftCardActive ca : cardActiveList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cno", ca.getNo());
				map.put("channel", channelMap.get(channel));
				map.put("op", OPERATOR.get(ca.getOpid()));
				map.put("pwd", PwdEncrypt.decryptCardPwd(ca.getPwd()));
				map.put("stopcode", ca.getStopcode());
				map.put("stoptime", null == ca.getStoptime() ? "" : SDF_YMD_HMS.format(ca.getStoptime()));
				dataList.add(map);
			}
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(dataList, TITLE_CARD_INVALID, NAME_CARD_INVALID);
		ExcelUtil.PrintExcel(workbook, fileName, resp);
	}
	
}
