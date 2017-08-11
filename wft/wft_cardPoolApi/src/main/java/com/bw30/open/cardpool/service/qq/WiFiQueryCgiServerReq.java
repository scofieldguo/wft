package com.bw30.open.cardpool.service.qq;

/**
 * qq wifi查询
 * @author Jack
 *
 * 2014年9月3日
 */
public class WiFiQueryCgiServerReq {
	private OfflineResultReq offlineResult;
	
	public WiFiQueryCgiServerReq(){
		
	}

	public OfflineResultReq getOfflineResult() {
		return offlineResult;
	}

	public void setOfflineResult(OfflineResultReq offlineResult) {
		this.offlineResult = offlineResult;
	}
	
}
