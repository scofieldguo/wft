package com.bw30.open.common.rmi.service;

import java.util.List;

import com.bw30.open.common.model.WftApType;

public interface IApTypeService {
	public WftApType findById(String ssid);
	
	public List<WftApType> findByOpid(Integer opid);
	
}
