package com.bw30.open.wftAdm.task;

import java.util.Date;

import org.apache.log4j.Logger;

import com.bw30.open.common.dao.mapper.WftCardActiveMapper;
import com.bw30.open.common.model.WftCardActive;
import com.bw30.open.common.model.WftOperator;
import com.bw30.open.wftAdm.service.CardApiService;

public class CardRechargeThread extends Thread {
	private static final Logger LOG = Logger
			.getLogger(CardRechargeThread.class);

	private String channel;
	private String ctype;
	private WftCardActiveMapper wftCardActiveMapper;
	private CardApiService cardApiService;

	public CardRechargeThread(String channel, String ctype,
			WftCardActiveMapper wftCardActiveMapper,
			CardApiService cardApiService) {
		this.channel = channel;
		this.ctype = ctype;
		this.wftCardActiveMapper = wftCardActiveMapper;
		this.cardApiService = cardApiService;
	}

	public void run() {
		WftCardActive card = CardRechargeTask.getCardActive();
		while (null != card) {
			try {
				Integer newBalance = this.cardApiService.recharge(channel,
						card.getId(), card.getNo(), WftOperator.OP_ID_CTCC,
						this.ctype, null);
				LOG.info(String.format(
						"recharge success:channel=%s, id=%s, no=%s, tvalue=%s",
						this.channel, card.getId(), card.getNo(), newBalance));

				if (-60 > newBalance) {//-300
					LOG.info(String
							.format("stop card: recharge:channel=%s, id=%s, cno=%s, tvalue=%s",
									this.channel, card.getId(), card.getNo(),
									newBalance));
					WftCardActive ca = new WftCardActive();
					ca.setId(card.getId());
					ca.setChannel(this.channel);
					ca.setCstatus(WftCardActive.CSTATUS_STOP);
					ca.setStoptime(new Date());
					this.wftCardActiveMapper.update(ca);
				}
			} catch (Exception e) {
				LOG.error("card recharge error", e);
			}

			card = CardRechargeTask.getCardActive();
		}

		CardRechargeTask.subThreadNum();
	}

}
