package com.bw30.open.data.rmi.service.impl;

import javax.annotation.Resource;

import com.bw30.open.common.dao.mapper.WftSaleCardRecord10011Mapper;
import com.bw30.open.common.model.sale.WftSaleCardRecord10011;
import com.bw30.open.common.rmi.service.ISaleCardRecord10011Service;

public class SaleCardRecord10011ServiceImpl implements ISaleCardRecord10011Service {
	@Resource
	private WftSaleCardRecord10011Mapper wftSaleCardRecord10011Mapper;
	@Override
	public void insert(WftSaleCardRecord10011 record) {
		wftSaleCardRecord10011Mapper.insert(record);
	}
	
	public void setWftSaleCardRecord10011Mapper(
			WftSaleCardRecord10011Mapper wftSaleCardRecord10011Mapper) {
		this.wftSaleCardRecord10011Mapper = wftSaleCardRecord10011Mapper;
	}

	@Override
	public void update(WftSaleCardRecord10011 record) {
		// TODO Auto-generated method stub
		wftSaleCardRecord10011Mapper.update(record);
	}

	@Override
	public void updateBalance(WftSaleCardRecord10011 record) {
		// TODO Auto-generated method stub
		wftSaleCardRecord10011Mapper.update(record);
	}
	
	
}
