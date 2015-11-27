package com.taobao.learn.thread.threadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

	public static void main(String args[]){
		ThreadPoolExecutor pool=new ThreadPoolExecutor(
				10,//corePoolSize,
				20,//maximumPoolSize, 
				5,//keepAliveTime, 
				TimeUnit.SECONDS,//unit, 
				new LinkedBlockingQueue<Runnable>(),//workQueue, 
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						// TODO Auto-generated method stub
						return null;
					}
				}, 
				new DiscardOldestPolicy());
		
		
		pool.execute(new Thread(){
			public void run(){
				
			}
		});
		
		
	}
}
