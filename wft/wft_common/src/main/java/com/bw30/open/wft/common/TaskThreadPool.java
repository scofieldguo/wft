package com.bw30.open.wft.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 线程池
 *
 * @author zhouwei@bw30.com
 * @version 2013-3-14 下午01:41:55
 *
 */
public class TaskThreadPool {
	public void init() {
		synchronized (lock) {
			BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
					queueSize);
			threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTimeInMillSeconds,
					TimeUnit.MILLISECONDS, workQueue);
		}
	}

	public int addTask(Runnable task) {
		// 返回值定义：0-添加成，1-添加失败，2-无空闲线程
		synchronized (lock) {
			if (task == null) {
				logger.warn("task is null");
				return 0;
			}
			// 判断是否有空闲线程
			int activeCount = threadPoolExecutor.getActiveCount();
			int maxCount = threadPoolExecutor.getMaximumPoolSize();
			logger.warn("Add task[" + task + "队列中等待执行的任务数目"+threadPoolExecutor.getQueue().size());
//			if (activeCount >= maxCount) {
//				logger.warn("Add task[" + task + "] FAIL, work queue full" + maxCount+ "队列中等待执行的任务数目"+threadPoolExecutor.getQueue().size());
//				return 2;
//			}
			logger.debug("Add task[" + task + "] SUCCESS");
			threadPoolExecutor.execute(task);
			return 0;
		}
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public void setKeepAliveTimeInMillSeconds(long keepAliveTimeInMillSeconds) {
		this.keepAliveTimeInMillSeconds = keepAliveTimeInMillSeconds;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	private byte[] lock = new byte[0];
	private int corePoolSize = 5;
	private int maximumPoolSize = 100;
	private long keepAliveTimeInMillSeconds = 1000;
	private int queueSize = 2048;
	private ThreadPoolExecutor threadPoolExecutor;
	private static final Logger logger = Logger.getLogger(TaskThreadPool.class);
}
