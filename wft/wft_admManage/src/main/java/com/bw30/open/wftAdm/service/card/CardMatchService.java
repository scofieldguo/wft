package com.bw30.open.wftAdm.service.card;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.bw30.open.common.dao.mapper.BillCompareMapper;
import com.bw30.open.common.model.BillCompare;
import com.bw30.open.wft.common.Pager;
import com.bw30.open.wft.common.translate.model.ExcelModel;
import com.bw30.open.wft.common.translate.reader.impl.MatchReaderFactory;
import com.bw30.open.wftAdm.service.card.thread.CardThread;
import com.bw30.open.wftAdm.service.card.thread.ThreadPoolUtil;

public class CardMatchService {

	private final Logger logger = Logger.getLogger(CardMatchService.class);

	@Resource
	private BillCompareMapper billCompareMapper;

	/**
	 * 获取数据
	 * 
	 * @param xlsFile
	 * @return
	 */
	public List<ExcelModel> getDatas(MultipartFile xlsFile, Integer id,
			String date) {
		List<ExcelModel> list = null;
		try {
			InputStream is = xlsFile.getInputStream();
			MatchReaderFactory matchReader = new MatchReaderFactory(id, is);
			list = matchReader.getResult();
			logger.info("GET Data List success, size is=" + list.size());
		} catch (IOException e) {
			logger.error("GET Data is error, Cause by " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 转换对象
	 * 
	 * @param models
	 * @return
	 */
	public List<BillCompare> toBillCompares(List<ExcelModel> models, Integer id) {
		List<BillCompare> list = new ArrayList<BillCompare>();
		for (ExcelModel model : models) {
			BillCompare billCompare = new BillCompare();
			billCompare.setFrom_id(id);
			billCompare.setMobile_num(model.getMobileNum());
			billCompare.setRecord_date(model.getDate());
			billCompare.setOnline_time(model.getOnline_time());
			billCompare.setOffline_time(model.getOffline_time());
			int flag = (model.getFlag() == null || model.getFlag().length() < 1) ? 0
					: 1;
			billCompare.setMerge_flag(flag);
			list.add(billCompare);
		}
		return list;
	}

	/**
	 * 获取订单列表 Modify by raymond:只显示运营商账单
	 * 
	 * @param pager
	 * @return
	 */
	public List<BillCompare> getBillCompares(Pager pager) {
		List<BillCompare> list = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromId", 1);
		if (null != pager) {
			Integer count = this.billCompareMapper.countByParam(paramMap);
			count = null != count ? count : 0;
			pager.setRecCount(count);
			if (0 < count) {
				list = this.billCompareMapper.pageFindByParam(paramMap, pager);
			}
		} else {
			list = this.billCompareMapper.pageFindByParam(paramMap, null);
		}
		return list;
	}

	/**
	 * 批量导入数据
	 * 
	 * @param list
	 */
	public void addBill(List<BillCompare> list) {
		List<BillCompare> newList = new ArrayList<BillCompare>();
		for (BillCompare billCompare : list) {
			newList.add(billCompare);
			if ((newList != null) && (newList.size() > 0)
					&& (newList.size() % 500 == 0)) {
				billCompareMapper.batchInsert(newList);
				newList.clear();
			}
		}
		if (newList != null && newList.size() > 0) { // clear the last data
			billCompareMapper.batchInsert(newList);
		}
	}

	public BillCompareMapper getBillCompareMapper() {
		return billCompareMapper;
	}

	public void setBillCompareMapper(BillCompareMapper billCompareMapper) {
		this.billCompareMapper = billCompareMapper;
	}

	/**
	 * 比对账单
	 */
	public int compareBill() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fromId", 1);
		List<BillCompare> list = billCompareMapper.pageFindByParam(param, null);
		for (BillCompare billData : list) {
			CardThread cardTask = new CardThread(billData,
					this.billCompareMapper);
			ThreadPoolUtil.getThreadPool().execute(cardTask);
		}
		// getThreadPool().shutdown();
		int count = (list == null) ? 0 : list.size();
		return count;

	}

	/**
	 * 关闭进程
	 */
	public void poolShutDown() {
		ThreadPoolUtil.getThreadPool().shutdown();
	}

	/**
	 * 比较已经比对账单列表
	 * 
	 * @param compareFlag
	 * @return
	 */
	public List<BillCompare> getComparedList(boolean compareFlag) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fromId", 1);
		if (compareFlag) {
			param.put("compareFlag", compareFlag);
		}
		List<BillCompare> list = billCompareMapper.pageFindByParam(param, null);
		return list;

	}

	/**
	 * 获取列表总体时长
	 * 
	 * @param list
	 * @return
	 */
	public long getTotalTime(List<BillCompare> list) {
		long total = 0;
		for (BillCompare billCompare : list) {
			long offset = getOffset(billCompare.getOnline_time(),
					billCompare.getOffline_time());
			total += offset;
		}
		return total;
	}

	/**
	 * 批量删除
	 */
	public void delete() {
		billCompareMapper.deleteAll();
	}

	private int getOffset(String start, String end) {
		Long startLong = stringToTime(start);
		Long endLong = stringToTime(end);
		int offset = (int) (endLong - startLong);
		return offset;
	}

	private Long stringToTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
