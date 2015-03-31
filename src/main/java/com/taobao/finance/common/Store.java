package com.taobao.finance.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GTask;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.service.GPublicStockService;
import com.taobao.finance.service.GTaskService;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.HisDataTask;
import com.taobao.finance.task.UnformalDataTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;
@Component
@DependsOn("fetchUtil")
public class Store {
	public Map<String, List<Stock>> store = new HashMap<String, List<Stock>>();
	public Map<String, Integer> download = new HashMap<String, Integer>();
	public Map<String, Integer> choose = new HashMap<String, Integer>();
	public Map<String,Stock> publicPool=new HashMap<String,Stock>();
	public Map<String, Boolean> checkWorkingRecord = new HashMap<String, Boolean>();
	public List<GPublicStock> publicStock=new ArrayList<GPublicStock>();
	public List<GPublicStock> history=new ArrayList<GPublicStock>();
	
	
	public static Boolean workingDay;
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");
	public static DateFormat DF2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public static int DOWNLOAD_STATUS_DOWNLOADING = 1;
	public static int DOWNLOAD_STATUS_DOWNLOADED = 2;
	public static int CHOOSEN_STATUS_CHOOSING = 1;
	public static int CHOOSEN_STATUS_CHOOSEN = 2;
	public static int downloaded = 0;
	public static int choosen = 0;
	
	@Autowired
	private ThreadService threadService;
	
	@Autowired
	private GTaskService gTaskService;
	
	private GTask today;
	
	public Store() {
		
	}
	
	@PostConstruct
	public void init(){
		if(workingDay==null){
			workingDay=FetchUtil.checkWorkingDay();
		}
		today=gTaskService.queryLastTask();
		if(today!=null){
			if(workingDay){
				Date d=new Date();
				if(today.getDate().getDate()==d.getDate()){
					downloaded=today.getDownload();
					choosen=today.getChoose();
				}else{
					downloaded=0;
					choosen=0;
				}
			}else{
				Date d=new Date();
				if(today.getDate().getDate()==d.getDate()){
					
				}else{
					downloaded=today.getDownload();
					choosen=today.getChoose();
				}
			}
		}else{
			downloaded=0;
			choosen=0;
		}
		
		
		
		publicStock=this.gPublicStockService.queryAll();
		history=this.gPublicStockService.queryHistory();
		Thread d=new Thread(){
			public void run(){
				try{
				   while(true){
					   today=gTaskService.queryLastTask();
					   
					   Date d=new Date();
					   String endDateStr=DF.format(d)+" 15:00:00";
					   String beginDateStr=DF.format(d)+" 09:30:00";
					   String beginDateStr2=DF.format(d)+" 09:00:00";
					   String beginDateStr3=DF.format(d)+" 10:00:00";
					   Date closeTime=DF2.parse(endDateStr);
					   Date beginTime=DF2.parse(beginDateStr);
					   Date beginTime2=DF2.parse(beginDateStr2);
					   Date beginTime3=DF2.parse(beginDateStr3);
					   if(d.after(beginTime)){
						   workingDay=FetchUtil.checkWorkingDay();
					   }
					   
					   if(d.after(beginTime)&&d.before(beginTime3)){
						   if(workingDay){
							   downloaded=0;
						   }
					   }
					   
					   
					   if(d.after(closeTime)){
                    	   boolean canChoose=false;
                    	   if(workingDay){
                    		   if(today!=null){
                    			   if(today.getDate().getDate()==d.getDate()){
                        			   if(today.getDownload()==2){
                        				   canChoose=true;
                        			   }
                        		   }else{
                        			   canChoose=false;
                        		   }
                    		   }else{
                    			   canChoose=false;
                    		   }  
                    	   }
                    	   
                    	   if(canChoose){
    						   choosen=CHOOSEN_STATUS_CHOOSING;
    						   ananyse();
    						   choosen=CHOOSEN_STATUS_CHOOSEN;
    						   today.setChoose((byte)CHOOSEN_STATUS_CHOOSEN);
   							   gTaskService.update(today);
    					   }
					   }
					   
					   
                       if(d.after(closeTime)){
                    	   boolean canDownload=false;
                    	   if(workingDay){
                    		   if(today!=null){
                    			   if(today.getDate().getDate()==d.getDate()){
                        			   if(today.getDownload()==1||today.getDownload()==2){
                        				   canDownload=false;
                        			   }
                        		   }else{
                        			   canDownload=true;
                        		   }
                    		   }else{
                    			   canDownload=true;
                    		   }
                    	   }
                    	   
                    	   
                    	   if(canDownload){
                    		   GTask t=new GTask();
   							   Date dd=new Date();
   							   String dstr=DF.format(dd);
   						   	   try {
   								  t.setDate(DF.parse(dstr));
   							   } catch (ParseException e) {
   							  	  e.printStackTrace();
   							   }
   							   t.setDownload(GTask.DOWNLOADING);
   							   t.setWorking(GTask.WORKING);
   							   t.setChoose(GTask.NON_CHOOSE);
   							   t=gTaskService.insert(t);
   							   downloaded=1;
   							
                    		   updateHistory();
   							   updateTmp();
   							   ananyse();
   							
   							   t.setDownload(GTask.DOWNLOADED);
   							   gTaskService.update(t);
   							   downloaded=2;
                    	   }
					   }
					   
					   Thread.sleep(60*1000*15);
				   }
				}catch(Exception e){
				   e.printStackTrace();
				}
			}
		};
		d.setName("check_thread");
		d.start();
	}
	
	@Autowired
	private GPublicStockService gPublicStockService;

	
	public void removeFromPublic(String symbol){
		List<GPublicStock> l=new ArrayList<GPublicStock>();
		for(GPublicStock s:this.publicStock){
			if(!s.getSymbol().equals(symbol)){
				l.add(s);
			}
		}
		this.publicStock=l;
	}
	
	public void reloadPublicStock(){
		publicStock=this.gPublicStockService.queryAll();
	}
	
	public void reloadHistoryStock(){
		history=this.gPublicStockService.queryHistory();
	}
	
	public void reloadStatus(){
		//List<GTask> l=this.gTaskService.queryLastTenTask();
	}
	
	
	
	public void updateTmp(){
		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			UnformalDataTask t=new UnformalDataTask(sys);
			callList.add(t);
		}
		List<Object> r=threadService.service(callList);
		r.size();
	}
	
	public void updateHistory(){
		Fetch_AllStock.getData();		
		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			HisDataTask t=new HisDataTask(sys,false);
			callList.add(t);
		}
		List<Object> r=threadService.service(callList);
		r.size();
	}
	
	public void ananyse() {
		List<Stock> big = new BigTrend_Choose_MultiThread().choose();
		List<Stock> acvu = new AVCU_Choose_MultiThread().choose();
		List<Stock> av5 = new AV5_Trend_Choose_MultiThread().choose();
		List<Stock> av10 = new AV10_Trend_Choose_MultiThread().choose();
		List<Stock> tp = new TP_Choose_MultiThread().choose();
		store.put("big", big);
		store.put("acvu", acvu);
		store.put("av5", av5);
		store.put("av10", av10);
		store.put("tp", tp);
	}

	public boolean containsKey(String key) {
		return store.containsKey(key);
	}

	public List<Stock> get(String key) {
		return store.get(key);
	}

	public void put(String key, List<Stock> l) {
		store.put(key, l);
	}

	public int getDownloadStatus() {
		return downloaded;
	}

	public void setDownloading() {
		downloaded=1;
	}

	public void setDownloaded() {
		downloaded=2;
	}

	public int getChooseStatus() {
		return choosen;
	}

	public void status(Date d) {
		String dateStr = DF.format(d);
		choose.put(dateStr, 1);
	}

	public void setChoosed() {
		choosen=2;
	}
	
	public void setChoosing() {
		choosen=1;
	}
}
