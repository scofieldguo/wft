package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.wftAdm.service.CardApiService;

/**
 * 定时任务：卡充值
 * 
 * @author Jack
 * 
 *         2014年8月11日
 */
public class CardRechargeTask {
	private static final Logger LOG = Logger.getLogger(CardRechargeTask.class);

	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private CardApiService cardApiService;
	
	private Integer threadNum;
	
	private static Iterator<WftCardActive> cardIterator = null;
	
	private static Integer currThreadNum = 0;

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}
	
	public void setCardApiService(CardApiService cardApiService) {
		this.cardApiService = cardApiService;
	}
	
	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}

	public static synchronized void subThreadNum(){
		LOG.info("cmcc thread subThreadNum=" + currThreadNum);
		currThreadNum--;
	}
	
	/**
	 * 获取要充值的卡
	 * @return
	 */
	public static synchronized WftCardActive getCardActive(){
		WftCardActive ca = null;
		if(cardIterator.hasNext()){
			ca = cardIterator.next();
			cardIterator.remove();
		}
		return ca;
	}

	public void runTask() {
		Date now = new Date();
		long time = System.currentTimeMillis();
		LOG.info(String.format("card recharge task begin:%s",
				SDF_YMD_HMS.format(now)));
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					if(channel.getStatus()==0) continue;
					LOG.info(String
							.format("get recharge ctcc card for channel[%s]: ctccbalance=%s",
									channel.getCode(),
									channel.getCtccbalance()));
					if (-1 == channel.getCtccbalance()) {//不充值
						LOG.info(String
								.format("can not recharge ctcc card for channel[%s]: ctccbalance=%s",
										channel.getCode(),
										channel.getCtccbalance()));
						continue;
					}
					
					if("10011".equals(channel.getCode())){
						/**
						 * 为了消耗卡池
						 * 万能钥匙只有快过期的才充值，小于临界值的不充值
						 * */
						this.checkForRechargeCtcc(channel.getCode(),-1000000, channel.getCtypeforrechargectcc());
					}else{
						this.checkForRechargeCtcc(channel.getCode(),
								channel.getCtccbalance(), channel.getCtypeforrechargectcc());
					}
				}
			}

		} catch (Exception e) {
			LOG.error("card recharge task run error", e);
			return;
		}

		time = (System.currentTimeMillis() - time) / (1000 * 60);// 分钟
		LOG.info(String.format("card recharge task end:%s, times = %s(minute)",
				SDF_YMD_HMS.format(new Date()), time));
	}

	/**
	 * 充值：时长小于阈值，或者卡即将失效（有效期前一天）
	 */
	private void checkForRechargeCtcc(String channel,
			Integer thresholdForRecharge, String ctype) throws Exception {
		LOG.info("check for recharge ctcc card: channel=" + channel);
		List<WftCardActive> cardList = this.wftCardActiveMapper
				.findCtccCardForRecharge(channel, thresholdForRecharge);
		if (null == cardList || 0 == cardList.size()) {
			LOG.info("no ctcc card need to recharge: channel=" + channel);
			return;
		}
		
		cardIterator = cardList.iterator();
		
		currThreadNum = this.threadNum;
		try {
			for (int i = 0; i < this.threadNum; i++) {
				new CardRechargeThread(channel, ctype, wftCardActiveMapper, cardApiService).start();
			}
			
			while(0 < currThreadNum){
				Thread.sleep(2 * 60 * 1000);
			}
			
		}catch (Exception e) {
			LOG.error("xian cheng error", e);
		}

	}

}
