package com.taobao.finance.common.cache.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.cache.ICacheService;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GHis;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GStock;
import com.taobao.finance.entity.GTask;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.fetch.impl.Fetch_Proxy_Server;
import com.taobao.finance.service.GProxyService;
import com.taobao.finance.service.GStockService;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.CheckTTLTask;
import com.taobao.finance.util.ThreadUtil;

@Component
@DependsOn("fetchUtil")
public class Store2 {
	private static final Logger logger = Logger.getLogger("taskLogger");
	public Map<String, Object> store = new HashMap<String, Object>();
	public Map<String, Integer> download = new HashMap<String, Integer>();
	public Map<String, Integer> choose = new HashMap<String, Integer>();
	public Map<String, Stock> publicPool = new HashMap<String, Stock>();
	public Map<String, Boolean> checkWorkingRecord = new HashMap<String, Boolean>();
	public List<GPublicStock> publicStock = new ArrayList<GPublicStock>();
	public Map<String,GPublicStock> publicStockMap = new HashMap<String,GPublicStock>();
	public List<GHis> history = new ArrayList<GHis>();
	
	public Map<Integer,GHis> hisMap=new HashMap<Integer,GHis>();
	public Map<String, Stock> recent = new HashMap<String, Stock>();
	public Map<String, List<Stock>> hot = new HashMap<String, List<Stock>>();
	public Map<String, Object> kdata = new HashMap<String, Object>();
	public List<Map<String, Object>> kdata2 = new ArrayList<Map<String, Object>>();
	public List<GStock> holders;
	public Map<String,GStock> holderMap=new HashMap<String,GStock>();
	public static Boolean workingDay = null;
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
	private ICacheService cacheService;
	
	ScheduledExecutorService executor=Executors.newScheduledThreadPool(2);
	
	public static Map<String,Proxy> proxyPool=new HashMap<String,Proxy>();
	public static List<Proxy> proxyList=new ArrayList<Proxy>();
	
	private GTask today;

	public Store2() {

	}
	
	public static List<Proxy> getProxy(){
		if(proxyList==null){
			return null;
		}
		if(proxyList.size()==0){
			return null;
		}
		if(proxyList.size()<5){
			return proxyList;
		}
		List<Proxy> l=new ArrayList<Proxy>();
		l.add(proxyList.get(0));
		l.add(proxyList.get(1));
		l.add(proxyList.get(2));
		l.add(proxyList.get(3));
		l.add(proxyList.get(4));
		return l;
	}

	
	public void setTimerTask(){
		//executor.schedule(new GetProxyTask(threadService, null,cacheService,this), 6, TimeUnit.SECONDS);
		executor.scheduleWithFixedDelay(new GetProxyTask(threadService, null,cacheService,this),0, 120, TimeUnit.SECONDS);
	}
	
	
	public static class InsertTask implements Callable<Object> {
		List<Object> r;
		GStockService gStockService;
		
		public InsertTask(List<Object> r,GStockService gStockService){
			this.r=r;
			this.gStockService=gStockService;
		}
		
		@Override
		public Object call() throws Exception {
			for(Object s:r){
				GStock st=(GStock)s;
				this.gStockService.saveOrUpdate(st);
			}
			return r;
		}
	}
	
	

	@PostConstruct
	public void init() {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		if(this.cacheService.contains("proxy_pool")){
			this.proxyPool=(Map<String,Proxy>)this.cacheService.get("proxy_pool");
		}else{
			this.proxyPool=new HashMap<String,Proxy>();
		}
		
		if(this.cacheService.contains("proxy_list")){
			this.proxyList=(List<Proxy>)this.cacheService.get("proxy_list");
		}else{
			
		}
		
		
		this.setTimerTask();
		
		
	}

}


class GetProxyTask implements Runnable {
	
	ThreadService threadService;
	GProxyService gProxyService;
	ICacheService cacheService;
	Store2 store2;
	
	public GetProxyTask(ThreadService threadService,GProxyService gProxyService,ICacheService cacheService,Store2 store) {
        this.threadService=threadService;
        this.gProxyService=gProxyService;
        this.cacheService=cacheService;
        this.store2=store;
	}

	public void run() {
		try{
			//System.out.println("开始执行代理抓取任务！");
			Map<String,Proxy> oldPool=null;
			if(this.cacheService.contains("proxy_pool")){
				oldPool=(Map<String,Proxy>)this.cacheService.get("proxy_pool");
			}else{
				oldPool=new HashMap<String,Proxy>();
			}
			
			List<Proxy> oldFilterList=null;
			if(this.cacheService.contains("proxy_list")){
				oldFilterList=(List<Proxy>)this.cacheService.get("proxy_list");
			}else{
				
			}
			
			store2.proxyList=oldFilterList;
			store2.proxyPool=oldPool;
			
			Map<String,Proxy> newPool=new HashMap<String,Proxy>();
			
			List<Proxy> proxyList = Fetch_Proxy_Server.fetch("1",Store2.getProxy());;

			if(proxyList.size()==0){
				proxyList = Fetch_Proxy_Server.fetch("1",Store2.getProxy());;
			}
			
			List<Proxy> proxyList2 = Fetch_Proxy_Server.fetch("2",Store2.getProxy());;

			if(proxyList2.size()==0){
				proxyList2 = Fetch_Proxy_Server.fetch("2",Store2.getProxy());;
			}
			
			if(proxyList2.size()!=0){
				proxyList.addAll(proxyList2);
			}
			
			List<Proxy> newList = new ArrayList<Proxy>();
			List<Proxy> oldList = new ArrayList<Proxy>();
			
			
			oldList.addAll(oldPool.values());
		
			
			for(Proxy p:proxyList){
				if(!oldPool.containsKey(p.getIp())){
					newList.add(p);
				}
			}
				
			List<Proxy> filter = new ArrayList<Proxy>();
			
			List<Proxy> result=null;
			if(newList.size()!=0){
				result = this.check(newList);
				for (Proxy p : result) {
					if (p.getLastTtl() > 0) {
						filter.add(p);
						newPool.put(p.getIp(), p);
					}
				}
			}
			
			
			if(oldList.size()!=0){
				result = this.check(oldList);
				for (Proxy p : result) {
					if (p.getLastTtl() > 0) {
						filter.add(p);
						newPool.put(p.getIp(), p);
					}
				}
			}

			if (filter.size() > 0) {
				Collections.sort(filter);
			}

			for (Proxy p : filter) {
				if (p.getLastTtl() > 0) {
					//System.out.println(p.getIp() + "," + p.getPort() + ":"+ p.getLastTtl());
					//proxyPool.put(p.getIp(), p);
				}
			}

			this.cacheService.set("proxy_pool", newPool);
			this.cacheService.set("proxy_list", filter);
			
			System.out.println("\n\n本次代理池:"+filter.size());
			if(filter.size()>0){
				for(Proxy p:filter){
					System.out.println(p);
				}
			}
			store2.proxyList=filter;
			store2.proxyPool=newPool;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public List<Proxy> check(List<Proxy> check){
		List<Proxy> result = new ArrayList<Proxy>();
		if(check.size()==0){
			return result;
		}
		List<List<Object>> dev = ThreadUtil.divide(check, 16);
		List<Callable<Object>> taskList = new ArrayList<Callable<Object>>();
		for (List<Object> d : dev) {
			CheckTTLTask task = new CheckTTLTask(d);
			taskList.add(task);
		}
		
		List<Object> proxyList = (List<Object>) threadService.service(taskList);

		
		for (Object o : proxyList) {
			List<Proxy> li = (List<Proxy>) o;
			if(li.size()>0){
				result.addAll(li);
			}
		}
		return result;
	}
}