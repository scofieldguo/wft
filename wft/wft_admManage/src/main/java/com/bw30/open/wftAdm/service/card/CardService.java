package com.bw30.open.wftAdm.service.card;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.bw30.open.cardCache.proto.Resp;
import com.bw30.open.cardCache.proto.resp.RemoveCardResp;
import com.bw30.open.cardCache.rmi.service.ICardCacheRmiService;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftCardStoreMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftCardStore;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.common.model.WftProvince;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wft.common.WifiTongUtils;
import com.bw30.open.wftAdm.controller.card.CardAddModel;
import com.bw30.open.wftAdm.service.BaseDataService;

public class CardService {
	private static final Logger LOG = Logger.getLogger(CardService.class);

	private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");
	// private static final SimpleDateFormat SDF_YMDHM = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat SDF_YMDHMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Resource
	private WftCardStoreMapper wftCardStoreMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private BaseDataService baseDataService;
	@Resource
	private ICardCacheRmiService cardCacheRmiService;

	public void setWftCardStoreMapper(WftCardStoreMapper wftCardStoreMapper) {
		this.wftCardStoreMapper = wftCardStoreMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setCardCacheRmiService(ICardCacheRmiService cardCacheRmiService) {
		this.cardCacheRmiService = cardCacheRmiService;
	}

	/**
	 * 根据卡号获取卡库中的卡
	 * 
	 * @param no
	 * @return
	 */
	public WftCardStore getCardByNo(String no) {
		List<WftCardStore> cardList = this.wftCardStoreMapper.findByNo(no);
		if (null != cardList && 0 < cardList.size()) {
			return cardList.get(0);
		}
		return null;
	}
	
	/**
	 * 查询卡库
	 * 
	 * @param params
	 * @param pager
	 * @return
	 */
	public List<WftCardStore> getCardList(Map<String, Object> params,
			Pager pager) {
		List<WftCardStore> cardList = null;
		Object begVetime = params.get("begVetime");
		Object endVetime = params.get("endVetime");
		Object begIntime = params.get("begIntime");
		Object endIntime = params.get("endIntime");

		try {
			if (null != begVetime) {
				params.put("begVetime",
						SDF_YMD.parse(String.valueOf(begVetime)));
			}
			if (null != endVetime) {
				params.put("endVetime",
						SDF_YMD.parse(String.valueOf(endVetime)));
			}
			if (null != begIntime) {
				params.put("begIntime",
						SDF_YMD.parse(String.valueOf(begIntime)));
			}
			if (null != endIntime) {
				params.put("endIntime",
						SDF_YMD.parse(String.valueOf(endIntime)));
			}

		} catch (ParseException pe) {
			LOG.error("", pe);
		}

		if(null != pager){
			Integer count = this.wftCardStoreMapper.countByParam(params);
			count = null != count ? count : 0;
			pager.setRecCount(count);
			if (0 < count) {
				cardList = this.wftCardStoreMapper.pageFindByParam(params, pager);
			}
		}else{
			cardList = this.wftCardStoreMapper.pageFindByParam(params, null);
		}
		

		// FIXME 为了前端页面正常显示日期、百分数
		params.put("begVetime", begVetime);
		params.put("endVetime", endVetime);
		params.put("begIntime", begIntime);
		params.put("endIntime", endIntime);
		
		return cardList;
	}
	
	/**
	 * 卡批量出库
	 * @param cids
	 */
	public void addCardsToActive(String channel, String[] cids) {
		if (null != cids && 0 < cids.length) {
			List<WftCardActive> caList = new ArrayList<WftCardActive>();
			List<Integer> ids = new ArrayList<Integer>();
			for (String cid : cids) {
				if(null == this.wftCardActiveMapper.findById(channel, Integer.parseInt(cid))){
					WftCardStore cs = this.wftCardStoreMapper.findById(Integer
							.parseInt(cid));
					WftCardActive ca = new WftCardActive();
					ca.setChannel(channel);
					ca.setId(cs.getId());
					ca.setOpid(cs.getOpid());
					ca.setSsid(cs.getSsid());
					ca.setPrvid(cs.getPrvid());
					ca.setCtype(cs.getCtype());
					ca.setNo(cs.getNo());
					ca.setPwd(cs.getPwd());
					ca.setVbtime(cs.getVbtime());
					ca.setVetime(cs.getVetime());
					ca.setBvalue(cs.getBvalue());
					ca.setTvalue(cs.getTvalue());
					ca.setUstatus(cs.getUstatus());
					ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
					ca.setIntime(new Date());
					ca.setCache(WftCardActive.OUT_CACHE);
					caList.add(ca);
					
					ids.add(Integer.parseInt(cid));
				}
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("channel", channel);
			data.put("list", caList);
			this.wftCardActiveMapper.batchInsert(data);

			// 批量修改卡库中卡的状态
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cstatus", WftCardStore.CARD_CSTATUS_AVAILABLE);
			params.put("inplace", WftCardStore.CARD_IN_ACTIVE);
			params.put("intime", new Date());
			params.put("channel", channel);
			params.put("ids", ids);

			this.wftCardStoreMapper.batchUpdateStatus(params);
		}
	}

	/**
	 * 查询卡池
	 * 
	 * @param params
	 * @param pager
	 * @return
	 */
	public List<WftCardActive> getCardActiveList(Map<String, Object> params,
			Pager pager) {
		List<WftCardActive> cardList = null;
		Object begVetime = params.get("begVetime");
		Object endVetime = params.get("endVetime");
		Object begIntime = params.get("begIntime");
		Object endIntime = params.get("endIntime");
		Object begTvalue = params.get("begTvalue");
		Object endTvalue = params.get("endTvalue");
		Object begStoptime = params.get("begStoptime");
		Object endStoptime = params.get("endStoptime");

		try {
			if (null != begVetime) {
				params.put("begVetime",
						SDF_YMD.parse(String.valueOf(begVetime)));
			}
			if (null != endVetime) {
				params.put("endVetime",
						SDF_YMD.parse(String.valueOf(endVetime)));
			}
			if (null != begIntime) {
				params.put("begIntime",
						SDF_YMD.parse(String.valueOf(begIntime)));
			}
			if (null != endIntime) {
				params.put("endIntime",
						SDF_YMD.parse(String.valueOf(endIntime)));
			}
			if (null != begTvalue) {
				params.put("begTvalue",
						Integer.parseInt(String.valueOf(begTvalue)) * 60);// 秒
			}
			if (null != endTvalue) {
				params.put("endTvalue",
						Integer.parseInt(String.valueOf(endTvalue)) * 60);// 秒
			}
			if (null != begStoptime) {
				params.put("begStoptime",
						SDF_YMD.parse(String.valueOf(begStoptime)));
			}
			if (null != endStoptime) {
				params.put("endStoptime",
						SDF_YMD.parse(String.valueOf(endStoptime)));
			}
		} catch (ParseException pe) {
			LOG.error("", pe);
		}

		if(null != pager){
			Integer count = this.wftCardActiveMapper.countByParam(params);
			count = null != count ? count : 0;
			pager.setRecCount(count);
			if (0 < count) {
				cardList = this.wftCardActiveMapper.pageFindByParam(params, pager);
			}
		}else{
			cardList = this.wftCardActiveMapper.pageFindByParam(params, null);
		}
		

		// 为了前端页面正常显示日期、百分数
		params.put("begVetime", begVetime);
		params.put("endVetime", endVetime);
		params.put("begIntime", begIntime);
		params.put("endIntime", endIntime);
		params.put("begTvalue", begTvalue);
		params.put("endTvalue", endTvalue);
		params.put("begStoptime", begStoptime);
		params.put("endStoptime", endStoptime);

		return cardList;
	}
	
	/**
	 * 启用卡
	 * 
	 * @param channel
	 * @param id
	 * @return
	 */
	public boolean enableCardFromActive(String channel, Integer id){
		WftCardActive ca = this.wftCardActiveMapper.findById(channel, id);
		if(null != ca){
			try{
				ca = new WftCardActive();
				ca.setId(id);
				ca.setChannel(channel);
				ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
				this.wftCardActiveMapper.update(ca);
				
				WftCardStore cs = new WftCardStore();
				cs.setId(id);
				cs.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
				this.wftCardStoreMapper.update(cs);
				
				return true;
			}catch(Exception e){
				LOG.error("", e);
			}
		}
		return false;
	}

	/**
	 * 停用卡
	 * 
	 * @param channel
	 * @param id
	 * @return
	 */
	public boolean stopCardFromActive(String channel, Integer id) {
		WftCardActive ca = this.wftCardActiveMapper.findById(channel, id);
		if (null != ca) {
			// 从缓存中移除卡
			RemoveCardResp res = this.cardCacheRmiService.removeCard(channel,
					ca.getOpid(), 0, 0, id);
			LOG.info("remove card from cache resp:" + res.getResult());
			if (Resp.SUCCESS == res.getResult() || Resp.FAIL == res.getResult()) {
				try {
					ca = new WftCardActive();
					ca.setId(id);
					ca.setChannel(channel);
					ca.setCache(WftCardActive.OUT_CACHE);
					ca.setCstatus(WftCardActive.CSTATUS_STOP);
					ca.setStoptime(new Date());
					this.wftCardActiveMapper.update(ca);

					WftCardStore cs = new WftCardStore();
					cs.setId(id);
					cs.setCstatus(WftCardActive.CSTATUS_STOP);
					cs.setStoptime(new Date());
					this.wftCardStoreMapper.update(cs);
					return true;
				} catch (Exception e) {
					LOG.error("", e);
				}
			}
		}
		return false;
	}
	
	/**
	 * 移除卡池中的卡
	 * 
	 * @param channel
	 * @param id
	 * @param boolean
	 */
	public boolean deleteCardFromActive(String channel, Integer id){
		WftCardActive ca = this.wftCardActiveMapper.findById(channel, id);
		if(null != ca){
			try{
				WftCardStore cs = new WftCardStore();
				cs.setId(id);
				cs.setInplace(WftCardStore.CARD_IN_STORE);
				cs.setTvalue(ca.getTvalue());
				cs.setPwd(ca.getPwd());
				cs.setVetime(ca.getVetime());
				cs.setStoptime(ca.getStoptime());
				cs.setStopcode(ca.getStopcode());
				this.wftCardStoreMapper.update(cs);
				
				this.wftCardActiveMapper.delete(channel, id);
				return true;
			}catch(Exception e){
				LOG.error("", e);
			}
		}
		return false;
	}

	/**
	 * 更新卡数据
	 * 
	 * @param channel
	 * @param card
	 */
	public void update(String channel, WftCardActive card) {
		card.setChannel(channel);
		this.wftCardActiveMapper.update(card);
	}

	public List<WftCardStore> uploadAndCheckForAdd(MultipartFile xlsFile,
			List<CardAddModel> camList) throws IOException {
		List<WftCardStore> csList = new ArrayList<WftCardStore>();
		Set<String> cnoSet = new HashSet<String>();
		camList = null != camList ? camList : new ArrayList<CardAddModel>();

		InputStream a = xlsFile.getInputStream();
		HSSFWorkbook wb = new HSSFWorkbook(a);
		Sheet sheet = null;
		Row row = null;

		String no = null;
		String op = null;
		String prv = null;
		String flag = null;
		String type = null;
		String unum = null;
		String vbtime = null;
		String vetime = null;
		String mvalue = null;
		String bvalue = null;
		String pwd = null;

		List<String> noList = this.wftCardStoreMapper.findAllNo(WftOperator.OP_ID_CMCC);
		LOG.info("cmcc card size:" + noList.size());
		
		StringBuffer msg = null;
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {// sheet
			sheet = wb.getSheetAt(i);
			if (null == sheet) {
				continue;
			}

			for (int j = 1; j <= sheet.getLastRowNum(); j++) {// row
				row = sheet.getRow(j);
				if (null == row) {
					continue;
				}

				CardAddModel cam = new CardAddModel();
				WftCardStore cs = new WftCardStore();
				msg = new StringBuffer();

				// 验证卡号
				Cell cell = row.getCell(0);
				no = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					no = cell.getStringCellValue();
				}
				LOG.info("check for add card:" + j + ", " + no);
				cam.setNo(no);
				if (WifiTongUtils.isEmpty(no)) {// 卡号为空
					msg.append("卡号为空；");
				} else {
					no = no.trim();
					cs.setNo(no);
					if (!cnoSet.add(no)) {
						msg.append("文件中卡号重复；");
					} else {
						if (noList.contains(no)) { //this.getCardByNo(no)
							msg.append("卡号已存在；");
						}
					}
				}

				// 验证运营商
				cell = row.getCell(1);
				op = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					op = cell.getStringCellValue();
				}
				cam.setOp(op);
				if (WifiTongUtils.isEmpty(op)) {
					msg.append("运营商为空；");
				} else {
					op = op.trim();
					WftOperator operator = this.baseDataService
							.getOperatorByName(op);
					if (null != operator) {
						cs.setOpid(operator.getId());
						cs.setSsid(operator.getSsid());
					} else {
						msg.append("运营商不正确；");
					}
				}

				// 验证省份
				cell = row.getCell(2);
				prv = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					prv = cell.getStringCellValue();
				}
				cam.setPrv(prv);
				if (WifiTongUtils.isEmpty(prv)) {
					msg.append("省份为空；");
				} else {
					prv = prv.trim();
					WftProvince province = this.baseDataService
							.getProvinceByName(prv);
					if (null != province) {
						cs.setPrvid(province.getId());
					} else {
						msg.append("省份不正确；");
					}
				}

				// 验证漫游
				cell = row.getCell(3);
				flag = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					flag = cell.getStringCellValue();
				}
				cam.setFlag(flag);
				if (WifiTongUtils.isEmpty(flag)) {
					msg.append("漫游为空；");
				} else {
					flag = flag.trim();
					if ("可以".equals(flag)) {
						// cs.setRflag(1);
					} else if ("不可以".equals(flag)) {
						// cs.setRflag(2);
					} else {
						msg.append("漫游不正确；");
					}
				}

				// 验证卡类型
				cell = row.getCell(4);
				type = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					type = cell.getStringCellValue();
				}
				cam.setType(type);
				if (WifiTongUtils.isEmpty(type)) {
					msg.append("类型为空；");
				} else {
					if (type.equals("包月卡")) {
						cs.setCtype(WftCardStore.CARD_TYPE_BAOYUE);
					} else if (type.equals("不可跨月时长卡")) {
						cs.setCtype(WftCardStore.CARD_TYPE_IN_MONTH_LEN);
					} else if (type.equals("可跨月时长卡")) {
						cs.setCtype(WftCardStore.CARD_TYPE_LEN);
					} else if (type.equals("电子卡")) {
						cs.setCtype(WftCardStore.CARD_TYPE_ELEC);
					} else if (type.equals("账期卡")) {
						cs.setCtype(WftCardStore.CARD_TYPE_ZHANGQI);
					} else {
						msg.append("卡类型不正确；");
					}
				}

				// 可同时分配次数
				cell = row.getCell(5);
				unum = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					unum = cell.getStringCellValue();
				}
				cam.setUnum(unum);
				if (WifiTongUtils.isEmpty(unum)) {
					msg.append("分配次数为空；");
				} else {
					try {
						// cs.setUnum(Integer.parseInt(unum));
					} catch (NumberFormatException nfe) {
						msg.append("分配次数不正确；");
					}
				}

				// 验证起止日期
				cell = row.getCell(6);
				vbtime = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					vbtime = cell.getStringCellValue();
				}
				cam.setVbtime(vbtime);
				cell = row.getCell(7);
				vetime = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					vetime = cell.getStringCellValue();
				}
				cam.setVetime(vetime);

				if (!"账期卡".equals(type)) {// 账期卡不需要起止日期
					if (WifiTongUtils.isEmpty(vbtime)) {
						msg.append("起始日期为空；");
					} else if (WifiTongUtils.isEmpty(vetime)) {
						msg.append("截止日期为空；");
					} else {
						try {
							if (SDF_YMDHMS.parse(vbtime).before(
									SDF_YMDHMS.parse(vetime))) {
								cs.setVbtime(SDF_YMDHMS.parse(vbtime));
								cs.setVetime(SDF_YMDHMS.parse(vetime));
							} else {
								msg.append("起始日期大于截止日期；");
							}
						} catch (ParseException pe) {
							msg.append("起始日期或截止日期不正确；");
						}
					}
				}

				// 验证金额
				cell = row.getCell(8);
				mvalue = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					mvalue = cell.getStringCellValue();
				}
				cam.setMvalue(mvalue);
				if (WifiTongUtils.isEmpty(mvalue)) {
					msg.append("金额不能为空；");
				} else {
					try {
						// cs.setMvalue(Integer.parseInt(mvalue));
					} catch (NumberFormatException nfe) {
						msg.append("金额不正确；");
					}
				}

				// 验证时长
				cell = row.getCell(9);
				bvalue = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					bvalue = cell.getStringCellValue();
				}
				cam.setBvalue(bvalue);
				if (WifiTongUtils.isEmpty(bvalue)) {
					msg.append("时长为空；");
				} else {
					try {
						String bvalues[] = bvalue.split("小时");
						int h = 0;
						int m = 0;
						if (bvalues.length > 1) {
							h = Integer.parseInt(bvalues[0]);
							String bvaluefen[] = bvalues[1].split("分");
							m = Integer.parseInt(bvaluefen[0]);
						} else {
							if (bvalue.contains("分")) {
								String bvaluefen[] = bvalue.split("分");
								m = Integer.parseInt(bvaluefen[0]);
							} else {
								h = Integer.parseInt(bvalues[0]);
							}

						}
						cs.setBvalue(h * 3600 + m * 60);// 购买时长：秒
						cs.setTvalue(h * 3600 + m * 60);// 剩余时长：秒
					} catch (Exception e) {
						msg.append("时长不正确；");
					}
				}

				// 验证密码
				cell = row.getCell(10);
				pwd = null;
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					pwd = cell.getStringCellValue();
				}
				cam.setPwd(pwd);
				if (WifiTongUtils.isEmpty(pwd)) {
					msg.append("密码为空；");
				} else {
					cs.setPwd(PwdEncrypt.encryptCardPwd(pwd));// 密码入库时要加密
				}

				if (0 == msg.toString().length()) {// 验证通过
					cs.setUstatus(0);
					cs.setCstatus(WftCardStore.CARD_CSTATUS_UNABLE);
					cs.setInplace(0);
					cs.setAtime(new Date());
					cs.setUtvalue(0);
					cs.setUcount(0);
					cs.setUscount(0);
					cs.setUsp(0D);

					csList.add(cs);
				} else {
					cam.setMsg(msg.toString());
					LOG.info(String
							.format("check for add card to cardStore fail: no=%s, errMsgk=%s",
									no, msg.toString()));
				}
				camList.add(cam);
			}
		}

		return csList;
	}

	/**
	 * 添加卡到卡库
	 * 
	 * @param csList
	 * @param isEnable
	 *            true则同时调入卡池
	 * @param channel
	 *            渠道code
	 * @param prtime
	 *            预启用时间
	 */
	public void addCardsToStore(List<WftCardStore> csList, boolean isEnable,
			String channel, Date prtime) {
		if (null == csList || 0 == csList.size()) {
			return;
		}

		if (isEnable) {// 加卡到卡库，并调入卡池
			LOG.info("Start transfer cards action immediately. channel=" + channel);
			for (WftCardStore cs : csList) {
				LOG.info("Start transfer cards into card_store..");
				cs.setAtime(new Date());
				cs.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
				cs.setInplace(1);
				cs.setIntime(new Date());
				cs.setChannel(channel);
				this.wftCardStoreMapper.insert(cs);// 需要id，所以不能批量插入
				LOG.info("End transfer cards into card_store..");
			}
			this.batchInsertCardActive(channel, csList);
		} else {// 加卡到卡库，并设置预启用时间
			LOG.info("Start transfer cards action.channel=" + channel);
			List<WftCardStore> insertList = new ArrayList<WftCardStore>();
//			for (WftCardStore cs : csList) {
			for (int i = 0; i < csList.size(); i++){
				WftCardStore cs = csList.get(i);
				cs.setCstatus(WftCardStore.CARD_CSTATUS_ENABLE);
				cs.setPrtime(prtime);
				cs.setChannel(channel);
				cs.setAtime(new Date());
				insertList.add(cs);
				if (i > 0 && (i%500 == 0)){
					LOG.info("Start into card-store, Data size:" + insertList.size());
					this.wftCardStoreMapper.batchInsert(insertList);
					insertList.clear();
				}
			}
			if (insertList != null && insertList.size() > 0){ //清除剩余数据
				LOG.info("Clear into card-store, Data size:" + insertList.size());
				this.wftCardStoreMapper.batchInsert(insertList);
			}
			LOG.info("End transfer cards into card_store..");
		}

	}
	
	public List<Integer> removeCacheCard(String channel){
		List<Integer> reMoveList = new ArrayList<Integer>();
		List<WftCardActive> list =this.wftCardActiveMapper.findByVetime(channel);
		LOG.info("remove cache card size =" + list.size());
		for(WftCardActive wftCardActive:list){
			Integer cid = wftCardActive.getId();
			LOG.info("remove cache card id=" + cid);
		    RemoveCardResp removeCardResp =	cardCacheRmiService.removeCard(channel, 2, 0, 0, cid);
		    if(removeCardResp.getResult()==1){
		    	reMoveList.add(cid);
		    	WftCardActive newCard =  new WftCardActive();
		    	newCard.setCache(2);
		    	newCard.setChannel(channel);
		    	newCard.setId(cid);
		    	wftCardActiveMapper.update(newCard);
		    }
		}
		return reMoveList;
	}
	

	private void batchInsertCardActive(String channel, List<WftCardStore> csList) {
		List<WftCardActive> caList = new ArrayList<WftCardActive>(csList.size());
		for (WftCardStore cs : csList) {
			WftCardActive ca = new WftCardActive();
			ca.setId(cs.getId());
			ca.setOpid(cs.getOpid());
			ca.setSsid(cs.getSsid());
			ca.setPrvid(cs.getPrvid());
			ca.setCtype(cs.getCtype());
			ca.setNo(cs.getNo());
			ca.setPwd(cs.getPwd());
			ca.setVbtime(cs.getVbtime());
			ca.setVetime(cs.getVetime());
			ca.setBvalue(cs.getBvalue());
			ca.setTvalue(cs.getBvalue());
			ca.setUstatus(cs.getUstatus());
			ca.setCstatus(WftCardStore.CARD_CSTATUS_AVAILABLE);
			ca.setIntime(new Date());
			ca.setUtvalue(0);
			ca.setUcount(0);
			ca.setUscount(0);
			ca.setCache(WftCardActive.OUT_CACHE);
			caList.add(ca);
		}
		//更新批量插入机制
		List<WftCardActive> insertList = new ArrayList<WftCardActive>();
		for (int i = 0; i< caList.size();i++){
			WftCardActive ca = caList.get(i);
			insertList.add(ca);
			if (i > 0 && (i%500 == 0) ){
				LOG.info("Start into the Card-pool-" + channel + ",Data size:" + insertList.size());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("channel", channel);
				params.put("list", insertList);
				this.wftCardActiveMapper.batchInsert(params);
				insertList.clear();
			}
		}
		if (insertList != null && insertList.size() > 0){
			LOG.info("Clear into the Card-pool-" + channel + ",Data size:" + insertList.size());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("channel", channel);
			params.put("list", insertList);
			this.wftCardActiveMapper.batchInsert(params);
		}
	}
}
