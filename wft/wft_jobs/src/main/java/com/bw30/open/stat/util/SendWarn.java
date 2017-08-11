package com.bw30.open.stat.util;

import com.bw.monitorsystem.clientapi.IMonitorNotify;
import com.bw.monitorsystem.clientapi.MonitorSystemNotifierFactory;
import com.bw.monitorsystem.clientapi.IMonitorNotify.NotifyLevel;
import com.bw.monitorsystem.clientapi.IMonitorNotify.NotifyType;

public class SendWarn {
	private IMonitorNotify notify = null; 
	
	public void initWarn(){
		notify=MonitorSystemNotifierFactory.getInstance("connWarn");
		notify.startUp("http://218.106.247.194:8800/ms/trap.do");
	}

	public void sendWarn(String message){
		notify.notice(NotifyType.CUSTOM_STATUS_NOTIFY,NotifyLevel.NOTIFY_ERROR_LEVEL,message);
	}
}
