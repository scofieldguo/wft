package com.bw30.open.wftAdm.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bw30.cmcc.wlan.service.operator.ICmccWlanService;
import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.dao.mapper.WftChannelMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftChannel;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wft.common.PwdEncrypt;
import com.bw30.open.wftAdm.service.CardService;

public class RestartCmccCardTask {
	private static final Logger LOG = Logger
			.getLogger(RestartCmccCardTask.class);

	@Resource
	private WftChannelMapper wftChannelMapper;
	@Resource
	private WftCardActiveMapper wftCardActiveMapper;
	@Resource
	private ICmccWlanService cmccWlanService;
	@Resource
	private CardService cardService;
	private Integer minute;

	public void reStart() {
		
		LOG.info("restart cmmc card task start");
		List<WftChannel> channelList = this.wftChannelMapper.findAll();
		if (null != channelList && 0 < channelList.size()) {
			for (WftChannel channel : channelList) {
				this.doConnForChannel(channel.getCode(), minute);
			}
		}
	}

	private void doConnForChannel(String channel, Integer minute) {
		List<WftCardActive> cList = this.wftCardActiveMapper
				.findCmccStopCardBeforeMinute(channel, WftOperator.OP_ID_CMCC,
						WftCardActive.CSTATUS_STOP, minute);
		LOG.info(String.format("restart cmcc card channel=%s, size=%s", channel, cList ==null? 0:cList.size()));
		if (null != cList && 0 < cList.size()) {
			for (WftCardActive card : cList) {
				WftCardActive ca = new WftCardActive();
				String pwd = "";
				try {
							pwd = cmccWlanService.reloadPassword(card.getNo());
							LOG
									.info(String
											.format(
													"restart cmcc card cmcc password reload response timeout no=%s pwd=%s",
													card.getNo(), pwd));
							if (pwd != null && !pwd.equals("")) {
								pwd = PwdEncrypt.encryptCardPwd(pwd);
								ca.setId(card.getId());
								ca.setPwd(pwd);
								ca.setCstatus(WftCardActive.CSTATUS_AVAILABLE);
								cardService.update(channel, ca);
							}
				} catch (Exception e) {
					LOG.error("cmcc password reload response timeout no=" + card.getNo()
							+ "pwd=" + pwd, e);
					// TODO: handle exception
				}
			}
		}
	}


	public void setWftChannelMapper(WftChannelMapper wftChannelMapper) {
		this.wftChannelMapper = wftChannelMapper;
	}

	public void setWftCardActiveMapper(WftCardActiveMapper wftCardActiveMapper) {
		this.wftCardActiveMapper = wftCardActiveMapper;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public void setCmccWlanService(ICmccWlanService cmccWlanService) {
		this.cmccWlanService = cmccWlanService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}
	
}
