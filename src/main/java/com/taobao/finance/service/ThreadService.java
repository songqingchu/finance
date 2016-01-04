package com.taobao.finance.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Component;

import com.taobao.finance.dataobject.Report;

@Component
public class ThreadService {

	ExecutorService service = Executors.newFixedThreadPool(16);
	CompletionService<Object> con = new ExecutorCompletionService<Object>(service);
	
	public  ThreadService(){
		//executeAt15EveryDay();
	}
	
	public  void executeAt15EveryDay() {  
	    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
	    long oneDay = 24 * 60 * 60 * 1000;  
	    long initDelay  = getTimeMillis("15:10:00") - System.currentTimeMillis();  
	    initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;  
	  
	}  
	
	private static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}  
	
	
	public  List<Object> service(List<Callable<Object>> tasks){
		System.out.println("进入同步方法!");
		List<Object> r=new ArrayList<Object>();
		List<Future<Object>> result=new ArrayList<Future<Object>>();
		
		
		for(int i=0;i<tasks.size();i++){
			try {
				Future<Object> f=con.take();
				r.add(f.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("退出同步方法!");
		return r;
	}
	
	
	public  List<Object> service2(List<Callable<Object>> tasks){
		System.out.println("进入同步方法!");
		List<Object> r=new ArrayList<Object>();
		List<Future<Object>> result=new ArrayList<Future<Object>>();
		
		for(Callable<Object> o:tasks){
			result.add(con.submit(o));	
		}
		
		for(int i=0;i<tasks.size();i++){
			try {
				Future<Object> f=con.take();
				List<Report> l=(List<Report>)f.get();
				r.addAll(l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("退出同步方法!");
		return r;
	}
}
