package com.bw30.open.common.rmi.service;

import com.bw30.open.common.model.sale.WftSaleCardRecord10011;

public interface ISaleCardRecord10011Service {
	
	void insert(WftSaleCardRecord10011 record);
	void update(WftSaleCardRecord10011 record);
	void updateBalance(WftSaleCardRecord10011 record);
}
