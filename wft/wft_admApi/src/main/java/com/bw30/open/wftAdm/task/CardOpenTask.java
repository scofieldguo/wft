package com.bw30.open.wftAdm.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.CardApiService;

/**
 * 定时任务：开卡
 * 
 * @author Jack
 * 
 *         2014年8月11日
 */
public class CardOpenTask {
	private static final Logger LOG = Logger.getLogger(CardOpenTask.class);

	private static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
//	private volatile static Integer locked = 1;
	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private CardApiService cardApiService;

	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public void setCardApiService(CardApiService cardApiService) {
		this.cardApiService = cardApiService;
	}

	public void runTask() {
		Date now = new Date();
		long time = System.currentTimeMillis();
		LOG.info(String.format("card open task begin:%s",
				SDF_YMD_HMS.format(now)));
		wftCardActiveMapper.updateStop("10009");//平安WiFi停掉时长<=0的卡（不用查询时长接口，直接停）
		try {
			List<WftChannel> channelList = this.wftChannelMapper.findAll();
			if (null != channelList && 0 < channelList.size()) {
				for (WftChannel channel : channelList) {
					this.checkForOpenCtcc(channel.getCode(),
							channel.getCtccnum(), channel.getCtypeforopenctcc());
				}
			}

		} catch (Exception e) {
			LOG.error("card open task run error", e);
			return;
		}

		time = (System.currentTimeMillis() - time) / (1000 * 60);// 分钟
		LOG.info(String.format("card open task end:%s, times = %s(minute)",
				SDF_YMD_HMS.format(new Date()), time));
	}

	/**
	 * 开电信卡
	 */
	private void checkForOpenCtcc(String channel, Integer thresholdForOpen, String ctype)
		throws Exception {
		LOG.info("[open ctcc card]check for open ctcc card：channel=" + channel);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("opid", 2);
		params.put("cstatus", WftCardActive.CSTATUS_AVAILABLE);
		params.put("channel", channel);
		params.put("begVetime", new Date());
		Integer num = this.wftCardActiveMapper.countByParam(params);// 可用电信卡数目
		num = (null != num ? num : 0);
		LOG.info("[open ctcc card]the size of avaliable ctcc card in cardActive_" + channel
				+ ":" + num);
		
		int n = thresholdForOpen - num;
		LOG.info(String.format("[open ctcc card]need open card num:%d  channel:%s",n ,channel));
		if (n <= 0) {
			LOG.info("[open ctcc card]avaliable ctcc card is enough: channel=" + channel);
		} else {// 可用电信卡数不足，需开卡
//			synchronized (locked){
//				Integer sum = 0;
				LOG.info(String
						.format("[open ctcc card]avaliable ctcc card is not enough, so need to open: channel=%s, n=%s",
								channel, n));
				// 调用电信开卡接口
				for (int i = 0; i < n; i++) {
					String cno = this.cardApiService.openCard(channel,
							WftOperator.OP_ID_CTCC, ctype, null);
					if (null != cno) {
						LOG.info(String.format(
								"[open ctcc card]open ctcc card success:channel=%s, cno=%s,ctype=%s",
								channel, cno,ctype));
//						sum++;
					} else {
						LOG.error(String.format("[open ctcc card]open ctcc card fail:channel=%s,ctype=%s",
								channel,ctype));
					}
				}
				/**
				 * 更新账户使用时长
				 * 针对平安
				 * */
//				if("10009".equals(channel)||"10010".equals(channel))
//					updateAccount(channel,sum,ctype);
//			}
		}
	}

//	private void updateAccount(String channel,Integer sum,String ctype) {
//		LOG.info(String.format("[open ctcc card] sum:%d",sum));
//		OpenPlatformAccount account = this.accountMapper.getAccountByUserId(Integer.parseInt(channel));
//		if (null != account) {
//			LOG.info(String.format("[open ctcc card] old account balance:%d",account.getUseHours()));
//			WftCardType cardType = wftCardTypeMapper.findCardCodeop(ctype);
//			if(cardType!=null){
//				LOG.info(String.format("[open ctcc card] card type %s value %d",ctype,cardType.getBalance()));
//				String openCardBalance = String.valueOf(sum*cardType.getBalance().intValue());
//				BigInteger bi = new BigInteger(account.getUseHours());
//				bi = bi.add(new BigInteger(openCardBalance));
//				account = new OpenPlatformAccount();
//				account.setId(Integer.parseInt(channel));
//				account.setUsetime(bi.toString());
//				this.accountMapper.update(account);
//				LOG.info(String.format("[open ctcc card] update account valueCount:%d",openCardBalance));
//			}
//		}else{
//			LOG.info(String.format("[open ctcc card] old account null"));
//		}
//	}
}
