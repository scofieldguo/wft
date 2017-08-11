package com.bw30.open.wftAdm.service.card.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	
	private static ExecutorService ThreadPool;
	
	private final static int POOL_SIZE = 20;
	
	public static ExecutorService getThreadPool(){
		if (ThreadPool == null){
			ThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
		}
		return ThreadPool;
	}
	
}
