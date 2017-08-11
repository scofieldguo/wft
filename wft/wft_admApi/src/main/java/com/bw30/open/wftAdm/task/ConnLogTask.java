package com.bw30.open.wftAdm.task;

import org.apache.log4j.Logger;

public class ConnLogTask {

	private static final Logger CONN_LOGGER= Logger.getLogger("CONNSESSION");
	
	public void writeLog(){
		CONN_LOGGER.info("CONNSESSION log is start");
	}
}
