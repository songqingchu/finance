package com.taobao.finance.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.Store;
import com.taobao.finance.task.ChooseTask;

@Component
public class ThreadService {

	@Autowired
	public Store store;
	public  ThreadService(){
		executeAt15EveryDay();
	}
	
	public  void executeAt15EveryDay() {  
	    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
	    long oneDay = 24 * 60 * 60 * 1000;  
	    long initDelay  = getTimeMillis("15:10:00") - System.currentTimeMillis();  
	    initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;  
	  
	    executor.scheduleAtFixedRate(  
	            new ChooseTask(store),  
	            initDelay,  
	            oneDay,  
	            TimeUnit.MILLISECONDS);  
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
}
