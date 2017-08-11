package com.bw30.open.common.support;

import org.apache.log4j.Logger;

public class ParameterException extends Exception {

	/**
	 * @author chenhong on 2010-01-10 参数异常
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(ParameterException.class);

	public ParameterException(String msg) {
		logger.error(msg);
	}

}
