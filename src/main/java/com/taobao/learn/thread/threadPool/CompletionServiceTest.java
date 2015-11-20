package com.taobao.learn.thread.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompletionServiceTest {
	
	ExecutorService service = Executors.newFixedThreadPool(16);
	CompletionService<Object> con = new ExecutorCompletionService<Object>(service);
	
	public List<Object> service(List<Callable<Object>> tasks){
		List<Object> r=new ArrayList<Object>();
		List<Future<Object>> result=new ArrayList<Future<Object>>();
		
		for(Callable<Object> o:tasks){
			result.add(con.submit(o));	
		}
		
		for(Future<Object> f:result){
			try {
				r.add(f.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
}
