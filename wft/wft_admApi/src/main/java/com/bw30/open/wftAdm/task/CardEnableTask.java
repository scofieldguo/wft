package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.wftAdm.service.CardService;

/**
 * 定时任务：启用到预启用时间的卡，调入卡池
 * 
 * @author Jack
 *
 * 2014年8月29日
 */
public class CardEnableTask {
	private static final Logger LOG = Logger.getLogger(CardEnableTask.class);

	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Resource
	private CardService cardService;

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public void runEnable() {
		Date now = new Date();
		long time = System.currentTimeMillis();
		LOG.info(String.format("check card prtime for avaliable task begin:%s",
				SDF_YMD_HMS.format(now)));
		try {
			this.cardService.checkForEnableCard(now);
		} catch (Exception e) {
			LOG.error("check card prtime for avaliable task run error", e);
			return;
		}

		time = (System.currentTimeMillis() - time) / 1000;// 秒
		LOG.info(String
				.format("check card prtime for avaliable task end:%s, times = %s(second)",
						SDF_YMD_HMS.format(new Date()), time));
	}

}
