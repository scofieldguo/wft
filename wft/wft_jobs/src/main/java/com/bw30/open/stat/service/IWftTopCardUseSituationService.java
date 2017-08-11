package com.bw30.open.stat.service;

import java.util.Date;

public interface IWftTopCardUseSituationService {

	void stat(String dayYMD, String hour, String minute, Date startMinute,
			Date endMinute);

}
