package com.bw30.openplatform.service;

import java.util.List;

import com.bw30.open.common.model.WftOperator;

public interface IWftOperatorService {

	List<WftOperator> findOperator();
	
	WftOperator findById(Integer id);

}
