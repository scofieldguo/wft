package com.bw30.open.stat.service;

import java.util.Date;

public interface IWftAcessCardStatService {

	void statCardUse(String dayYMD, String hour, Date startHour, Date endHour);

}
