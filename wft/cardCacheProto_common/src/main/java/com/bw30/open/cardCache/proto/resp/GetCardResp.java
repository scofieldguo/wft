package com.bw30.open.cardCache.proto.resp;

import com.bw30.open.cardCache.proto.Resp;
import com.bw30.open.common.model.CardCacheBean;

public class GetCardResp extends Resp {
	private static final long serialVersionUID = 1636918919650544325L;
	
	private CardCacheBean cardCacheBean; // 缓存卡
	
	public GetCardResp(){
		super();
	}

	public CardCacheBean getCardCacheBean() {
		return cardCacheBean;
	}

	public void setCardCacheBean(CardCacheBean cardCacheBean) {
		this.cardCacheBean = cardCacheBean;
	}
	
	
}
