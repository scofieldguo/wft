package com.bw30.open.wftAdm.service.card.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.bw30.open.common.dao.mapper.BillCompareMapper;
import com.bw30.open.common.model.BillCompare;

public class CardThread implements Runnable {

	// private BillCompare billCompare;

	private static AtomicInteger count = new AtomicInteger(0);

	private BillCompare billCompare;

	private BillCompareMapper billCompareMapper;

	public CardThread(BillCompare billCompare, BillCompareMapper billCompareMapper) {
		this.billCompare = billCompare;
		this.billCompareMapper = billCompareMapper;
	}

	public void run() {
		count.incrementAndGet();
		String onlineTimeStart = getOffsetTime(billCompare.getOnline_time(), -1);
		String onlineTimeEnd = getOffsetTime(billCompare.getOnline_time(), 1);
		Map<String, Object> subParam = new HashMap<String, Object>();
		subParam.put("mobileNum", billCompare.getMobile_num());
		subParam.put("onlineTimeStart", onlineTimeStart);
		subParam.put("onlineTimeEnd", onlineTimeEnd);
		subParam.put("fromId", 0);
		List<BillCompare> compareList = billCompareMapper.pageFindByParam(
				subParam, null);
		if (compareList != null && compareList.size() > 0) {
			BillCompare billData = null;
			if (compareList.size() > 1) {
				billData = checkBillCompares(billCompare, compareList);
			} else {
				billData = compareList.get(0);
			}
			billCompare.setCompare_id(billData.getId());
			billCompareMapper.update(billCompare);
		}
		System.out.println("线程处理完成count=" + count);
	}

	private BillCompare checkBillCompares(BillCompare bill,
			List<BillCompare> list) {
		int offsetTarget = getOffset(bill.getOnline_time(),
				bill.getOffline_time());
		int timeMin = 0;
		int offsetNum = 0;
		for (int i = 0; i < list.size(); i++) {
			BillCompare billData = list.get(i);
			int offset = getOffset(billData.getOnline_time(),
					billData.getOffline_time());
			if (Math.abs(offsetTarget - offset) <= timeMin || timeMin == 0) {
				timeMin = Math.abs(offsetTarget - offset);
				offsetNum = i;
			}
		}
		return list.get(offsetNum);
	}

	private String getOffsetTime(String time, int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, i);
			date = calendar.getTime();
			calendar = null; // 释放空间
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date == null) {
			return time;
		}
		return format.format(date);
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

	public static AtomicInteger getCount() {
		return count;
	}

	public static void setCount(AtomicInteger count) {
		CardThread.count = count;
	}
}
