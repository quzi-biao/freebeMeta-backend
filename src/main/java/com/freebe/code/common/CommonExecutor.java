package com.freebe.code.common;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonExecutor {
	private static final Executor executor = Executors.newScheduledThreadPool(100);
	
	public static final void execute(Runnable bounty) {
		log.info("启动线程： " + bounty);
		executor.execute(bounty);
	}
	
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
