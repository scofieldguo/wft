package com.bw30.openplatform.action;

import java.util.Timer;
import java.util.TimerTask;


public class Test {
	public static void main(String[] args) {
		Timer timer=new Timer();
		timer.schedule(new myTask(),1000,1000);
	}

	static	class myTask extends TimerTask {
			@Override
			public void run() {
				System.out.println("===========");
			}
		};
}
