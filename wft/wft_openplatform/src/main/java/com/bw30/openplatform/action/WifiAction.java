package com.bw30.openplatform.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.dao.mapper.WftSaleCardOperationMapper;
import com.bw30.open.common.dao.mapper.WftSaleCardRecord10011Mapper;
import com.bw30.open.common.model.OpenPlatformAccount;
import com.bw30.open.common.model.OpenPlatformUser;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.sale.WftSaleCardOperation;
import com.bw30.open.common.model.sale.WftSaleCardRecord10011;
import com.bw30.open.wft.common.DateUtil;
import com.bw30.open.wft.common.ExcelUtil;
import com.bw30.open.wft.common.POIUtils;
import com.bw30.openplatform.service.WiFiCardService;
import com.bw30.openplatform.util.ServletUtil;

public class  WifiAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger logger=Logger.getLogger(WifiAction.class);
	
	private String number;
	private String time;
//	private IOpenPlayFormAccountService openPlayFormAccountService;
	private OpenPlatformUser user;
	private WiFiCardService wifiCardService;
	private WftSaleCardRecord10011Mapper wftSaleCardRecord10011Mapper;
	private WftSaleCardOperationMapper wftSaleCardOperationMapper;
	private WftChannelMapper wftChannelMapper;
 	private String order;
	private File source;
	private String cno;
	private String excelname;
	private Integer flag;
	private String startDate;
	private String endDate;
	private Integer orderFlag=0;
	private Integer chargeCount;
	private Integer type;
	private Integer status;
	private List<WftSaleCardRecord10011> list;
	private List<WftSaleCardOperation> opList;
	private static final String file_path="/opt/tomcat/tomcat/excel/";
	private static HashMap<String,String> ctypeMap=new HashMap<String,String>();
	private static Map<String,Object> orderMap=new HashMap<String, Object>();;
	private PrintWriter out;
	static{
		ctypeMap.put("0.25", "18");
		ctypeMap.put("1", "20");
		ctypeMap.put("2", "21");
		ctypeMap.put("5", "22");
		ctypeMap.put("10","23");
		ctypeMap.put("20", "24");
		ctypeMap.put("50", "25");
		
	}
	
	static{
		doHandle();
	}
	
	public static void doHandle(){
		Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("  ");
				orderMap.clear();
			}
		},getFirstDayOfMonth(),24*60*60*1000);
		
	}
    public static Date getFirstDayOfMonth(){
    	Calendar c = Calendar.getInstance();
    	c.setTime(new Date());
//    	c.set(Calendar.DAY_OF_MONTH,1);
    	c.set(Calendar.HOUR_OF_DAY,23);
    	c.set(Calendar.MINUTE, 59);
    	return c.getTime();
    	
    }
	public String open(){
		common();
		return "cardIndex";
	}

	public void batchOpenCard(){
		String json="";
		try {
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		logger.info(user.getId()+" "+DateUtil.formateDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"openCard number="+number+" time="+time);
		String date=DateUtil.formateDate(new Date(), "yyyyMMddHHmmss");
		final String order=user.getId()+date;
		out=getResponse().getWriter();
		Thread thread=new Thread(new Runnable() {
			public void run() {
				WftChannel channel=wftChannelMapper.findById(user.getId());
				int succ=0,fail=0;
				orderMap.put(order+"success", succ);
				orderMap.put(order+"fail", fail);
				String ctype=ctypeMap.get(time);
				for(int i=0;i<Integer.parseInt(number);i++){
					logger.info(user.getId()+"start open card orderid="+order);
					WftSaleCardRecord10011 cardRecord = wifiCardService.openCard(ctype, order, user.getId().toString(),channel.getOpenkey());
					logger.info(user.getId()+"open card orderid="+order+" and cardRecord="+cardRecord);
					if(cardRecord!=null){
						addOperationRecord(cardRecord.getCno(),WftSaleCardOperation.OP_OPEN,cardRecord.getDuration(),WftSaleCardOperation.STATUS_ON,user.getId(),order);
						addUseAccountTime(user.getId(),cardRecord.getDuration()); //更新账户
						orderMap.put(order+"success", succ+=1);
						logger.info(user.getId()+"start open card success orderid="+order);
					}else{
						addOperationRecord("w000000",WftSaleCardOperation.OP_OPEN,0,WftSaleCardOperation.STATUS_OFF,user.getId(),order);
						orderMap.put(order+"fail", fail+=1);
					}
				}
				logger.info(String.format("%s opercard orderid=%s succ=%s and fail=%s", user.getId(),order,succ,fail));
			}
		});
		if(judgeBalance(user,number,time)){
			logger.info(user.getId()+"balance is enough and start open card");
			json="{\"flag\":1,\"order\":\""+order+"\"}";
			thread.start();
		}else{
			json="{\"flag\":0}";
			
		}
		out.print(json);
		} catch (Exception e) {
			json="{\"flag\":0}";
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();	
		}
	}
	
	
	private boolean judgeBalance(OpenPlatformUser user,String number,String time){
		OpenPlatformAccount useraccount=openPlayFormAccountService.getAccountByUserId(user.getId());
		BigDecimal bd_balace=new BigDecimal(useraccount.getBuytime()).subtract(new BigDecimal(useraccount.getUsetime()));
		BigDecimal totalBuy=new BigDecimal(number).multiply(new BigDecimal(time)).multiply(new BigDecimal(3600));
		logger.info(user.getId()+"batch open card totalbuy="+totalBuy+" and balacne="+bd_balace);
		if(bd_balace.subtract(totalBuy).longValue()>0){
			return true;
		}
		return false;
	}

	
	/**
	 * 进度条展示取成功失败数
	 * */
	public void getOrderCount(){
		int succ=(Integer)orderMap.get(order+"success");
		int fail=(Integer)orderMap.get(order+"fail");
		try {
			out=getResponse().getWriter();
			out.print("{\"succ\":"+succ+",\"fail\":"+fail+"}");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 导出excel
	 * */
	public void exportOpenCard() throws Exception{
		try {
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		List<WftSaleCardRecord10011> list=wftSaleCardRecord10011Mapper.findRecordByOrderId(user.getId(),order);
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		String titilName="时长卡时长,账号,密码,有效期";
		String nameStr="hour,account,pwd,date";
		List<String> titleName = getTitleName(titilName);
		Map<String, String> nameMap =getNameMap(titilName,nameStr);
		for (WftSaleCardRecord10011 t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hour", t.getDuration()/3600);
			map.put("account", t.getCno());
			map.put("pwd", t.getPwd());
			map.put("date",DateUtil.formateDate(t.getEnd_time(), "yyyy-MM-dd"));
			realdataList.add(map);
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(realdataList, titleName, nameMap);
		ExcelUtil.PrintExcel(workbook, "数据",
				getResponse());
		
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	private Map<String, String> getNameMap(String titilName, String nameStr) {
		Map<String, String> nameMap=new HashMap<String, String>();
		String[] nameArray = titilName.trim().split(",");
		String[] nameStrArray = nameStr.trim().split(",");
		for (int i = 0; i < nameArray.length; i++) {
			nameMap.put(nameArray[i], nameStrArray[i]);
		}
		return nameMap;
	}

	private List<String> getTitleName(String name) {
		List<String> titleList=new ArrayList<String>();
		String nameArray[]=name.split(",");
		for (int i = 0; i < nameArray.length; i++) {
			titleList.add(nameArray[i]);
		}
		return titleList;
	}

	
	
	public void upload(){
		// 上传文件并返回保存路径
		//String file_path="D:\\chargeCard\\";
		String fileName = System.currentTimeMillis()+ "-" + new Random().nextInt(1000000) +".xlsx";  
        File targetFile = new File(file_path);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {
        	InputStream in =new FileInputStream(source);
        	File  newFile=new File(file_path,fileName);
        	OutputStream os = new FileOutputStream(newFile);
    		byte[] buffer = new byte[1024];
    		int len = -1;
    		while ((len = in.read(buffer)) != -1) {
    			os.write(buffer, 0, len);
    		}
    		in.close();
    		os.close();
    		PrintWriter out;
    		out=getResponse().getWriter();
    		out.print(fileName);
        } catch (Exception e) {  
            e.printStackTrace();
        }
	}
	
	public String chargeIndex(){
		common();
		return "chargeIndex";
	}
	
	public void chargeCard(){
		String str="";
		try {
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		out=getResponse().getWriter();
		WftChannel channel=wftChannelMapper.findById(user.getId());
		int succ=0,fail=0;
		Map<String,Integer> mapParam=new HashMap<String, Integer>();
		mapParam.put("succ", 0);
		mapParam.put("fail", 0);
		if(judgeBalance(user,String.valueOf(chargeCount),time)){
			if(flag==1){
				while(chargeCount>0){
					logger.info("chargeCard start cno="+cno+" chargeCount="+chargeCount);
					doHandle(cno,user,channel,mapParam);
					chargeCount--;
				}
				str="{\"flag\":1,\"succ\":"+mapParam.get("succ")+",\"fail\":"+mapParam.get("fail")+",\"order\":\""+order+"\"}";
				logger.info(str);
			}else{
				String filename=file_path+excelname;
				logger.info("wifiAction chargeCard filename="+filename);
				File orFile=new File(filename);
				InputStream in=new FileInputStream(orFile);
				System.out.println(in);
				List<Map<String,Object>> data = ExcelUtil.readXlsx(in);
				for(Map<String,Object> map:data){
					String cno=(String)map.get("卡号");
					System.out.println(cno);
//					doHandle(cno,user,channel,mapParam);
				}
				str="{\"flag\":1,\"succ\":"+mapParam.get("succ")+",\"fail\":"+mapParam.get("fail")+",\"order\":\""+order+"\"}";
			}
		
		}else{
			str="{\"flag\":0,\"error\":\"余额不足\"}";
		}
		out.print(str);
		out.close();
		} catch (Exception e) {
			out.print(str);
			e.printStackTrace();
		}
		
	}
	
	private void doHandle(String cno,OpenPlatformUser user,WftChannel channel,Map<String, Integer> map){
		WftSaleCardRecord10011 oldRecord=wftSaleCardRecord10011Mapper.findByCno(cno,user.getId());
		String codeop=ctypeMap.get(time);
		WftSaleCardRecord10011 record = wifiCardService.recharge(codeop, cno, user.getId().toString(),channel.getOpenkey());
		if(record!=null){
			addOperationRecord(cno,WftSaleCardOperation.OP_CHARGE,record.getDuration(),WftSaleCardOperation.STATUS_ON,user.getId(),order);
			addUseAccountTime(user.getId(),new BigDecimal(time).multiply(new BigDecimal(3600)).intValue()); //更新账户
			map.put("succ",(Integer) map.get("succ")+1);
		}else{
			addOperationRecord(cno,WftSaleCardOperation.OP_CHARGE,oldRecord.getDuration(),WftSaleCardOperation.STATUS_OFF,user.getId(),order);
			map.put("fail",(Integer) map.get("fail")+1);
		}
	}
	
	
	private void addUseAccountTime(Integer userid,Integer time){
		OpenPlatformAccount useraccount=openPlayFormAccountService.getAccountByUserId(userid);
		BigDecimal bd= new BigDecimal(useraccount.getUsetime()).add(new BigDecimal(time));
		useraccount.setUsetime(bd.toString());
		openPlayFormAccountService.updateAccount(useraccount);
		
	}
	
	
	public void exportChargeCard() throws Exception{
		try {
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("order_id",order);
		List<WftSaleCardOperation> list=wftSaleCardOperationMapper.findByParam(paramMap,user.getId());
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		String titilName="账号,状态";
		String nameStr="account,date";
		List<String> titleName = getTitleName(titilName);
		Map<String, String> nameMap =getNameMap(titilName,nameStr);
		for (WftSaleCardOperation t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", t.getCno());
			map.put("date",t.getStatus()==WftSaleCardOperation.STATUS_ON?"充值成功":"充值失败");
			realdataList.add(map);
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(realdataList, titleName, nameMap);
		ExcelUtil.PrintExcel(workbook, "数据",
				getResponse());
		
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	private void addOperationRecord(String cno,Integer type,Integer duration,Integer status,Integer user,String order_id){
		try {
		
		WftSaleCardOperation operation=new WftSaleCardOperation();
		operation.setCno(cno);
		operation.setDuration(duration);
		operation.setOptime(new Date());
		operation.setType(type);
		operation.setStatus(status);
		operation.setChannel_id(user);
		operation.setOrder_id(order_id);
		wftSaleCardOperationMapper.insert(operation);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String record(){
		common();
		try{
			user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
			Map<String,Object> paramMap=new HashMap<String, Object>();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("cno",cno);
			paramMap.put("time", time);
			paramMap.put("orderFlag", orderFlag);
			pager.setRecCount(wftSaleCardRecord10011Mapper.countByParam(paramMap,user.getId()));
			list=wftSaleCardRecord10011Mapper.pageFindByParam(paramMap,user.getId(), pager);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "record";
	}
	
	public String operation(){
		common();
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("type", type);
		paramMap.put("status", status);
		paramMap.put("cno", cno);
		paramMap.put("duration", time);
		pager.setRecCount(wftSaleCardOperationMapper.countByParam(paramMap,user.getId()));
		opList=wftSaleCardOperationMapper.pageFindByParam(user.getId(),paramMap, pager);
		return "operation";
	}
	
	public void recordExport(){
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		HashMap<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("cno",cno);
		paramMap.put("time", time);
		List<WftSaleCardRecord10011> list=wftSaleCardRecord10011Mapper.findByParam(paramMap, user.getId());
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		String titilName="账号,密码,总时长,日期,有效期,";
		String nameStr="account,pwd,time,date,limit";
		List<String> titleName = getTitleName(titilName);
		Map<String, String> nameMap =getNameMap(titilName,nameStr);
		for (WftSaleCardRecord10011 t : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", t.getCno());
			map.put("pwd", t.getPwd());
			map.put("time", t.getDuration());
			map.put("date", DateUtil.formateDate(t.getStart_time(), "yyyy-MM-dd"));
			map.put("limit",DateUtil.formateDate(t.getEnd_time(), "yyyy-MM-dd"));
			realdataList.add(map);
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(realdataList, titleName, nameMap);
		ExcelUtil.PrintExcel(workbook, "数据",
				getResponse());
		
	}
	
	public void operationExport(){
		user=(OpenPlatformUser)ServletUtil.getSessionObject("userData");
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("type", type);
		paramMap.put("status", status);
		paramMap.put("cno", cno);
		paramMap.put("duration", time);
		List<WftSaleCardOperation> opList=wftSaleCardOperationMapper.findByParam(paramMap, user.getId());
		List<Map<String, Object>> realdataList = new ArrayList<Map<String, Object>>();
		String titilName="账号,总时长,日期,操作,状态";
		String nameStr="cno,time,date,type,status";
		List<String> titleName = getTitleName(titilName);
		Map<String, String> nameMap =getNameMap(titilName,nameStr);
		for (WftSaleCardOperation t : opList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cno", t.getCno());
			map.put("time", t.getDuration());
			map.put("date",DateUtil.formateDate(t.getOptime(), "yyyy-MM-dd"));
			map.put("type", t.getType()==WftSaleCardOperation.OP_CHARGE?"充值":"开卡");
			map.put("status",t.getStatus()==WftSaleCardOperation.STATUS_ON?"成功":"失败");
			realdataList.add(map);
		}
		HSSFWorkbook workbook = POIUtils.exportExcel(realdataList, titleName, nameMap);
		ExcelUtil.PrintExcel(workbook, "数据",
				getResponse());
		
	}
	
	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}



	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


	public void setWftSaleCardRecord10011Mapper(
			WftSaleCardRecord10011Mapper wftSaleCardRecord10011Mapper) {
		this.wftSaleCardRecord10011Mapper = wftSaleCardRecord10011Mapper;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getExcelname() {
		return excelname;
	}

	public void setExcelname(String excelname) {
		this.excelname = excelname;
	}


	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public List<WftSaleCardRecord10011> getList() {
		return list;
	}

	public void setList(List<WftSaleCardRecord10011> list) {
		this.list = list;
	}

	public void setWftSaleCardOperationMapper(
			WftSaleCardOperationMapper wftSaleCardOperationMapper) {
		this.wftSaleCardOperationMapper = wftSaleCardOperationMapper;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<WftSaleCardOperation> getOpList() {
		return opList;
	}

	public void setOpList(List<WftSaleCardOperation> opList) {
		this.opList = opList;
	}

	public void setWifiCardService(WiFiCardService wifiCardService) {
		this.wifiCardService = wifiCardService;
	}

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}
	
	


public Integer getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(Integer chargeCount) {
		this.chargeCount = chargeCount;
	}

public static void main(String[] args) {
//	System.out.println("\\s*[,]+\\s*");
//	System.out.println((byte) -1);
//	System.out.println(Integer.MAX_VALUE);
	System.out.println(getFirstDayOfMonth());
}


	public static byte[] charToByte(char c) {
	    byte[] b = new byte[2];
	    b[0] = (byte) ((c & 0xFF00) >> 8);
	    b[1] = (byte) (c & 0xFF);
	    return b;
	}
	
	public static char byteToChar(byte[] b) {
	    char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
	    return c;
	}
	
}
