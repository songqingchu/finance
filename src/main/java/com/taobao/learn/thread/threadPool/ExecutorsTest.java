package com.taobao.learn.thread.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExecutorsTest {

	
	public static void main(String args[]){
		ExecutorService fix=Executors.newFixedThreadPool(10, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		
		Executors.newScheduledThreadPool(10, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		
		
		
	}
}
