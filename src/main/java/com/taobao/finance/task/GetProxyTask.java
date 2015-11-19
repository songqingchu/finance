package com.taobao.finance.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.taobao.finance.common.Store;
import com.taobao.finance.common.cache.ICacheService;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.fetch.impl.Fetch_Proxy_Server;
import com.taobao.finance.service.GProxyService;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.util.ThreadUtil;

public class GetProxyTask implements Runnable {
	
	ThreadService threadService;
	GProxyService gProxyService;
	ICacheService cacheService;
	Store store;
	
	public GetProxyTask(ThreadService threadService,GProxyService gProxyService,ICacheService cacheService,Store store) {
        this.threadService=threadService;
        this.gProxyService=gProxyService;
        this.cacheService=cacheService;
        this.store=store;
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
			
			store.proxyList=oldFilterList;
			store.proxyPool=oldPool;
			
			Map<String,Proxy> newPool=new HashMap<String,Proxy>();
			
			List<Proxy> proxyList = Fetch_Proxy_Server.fetch("1",Store.getProxy());;

			if(proxyList.size()==0){
				proxyList = Fetch_Proxy_Server.fetch("1",Store.getProxy());;
			}
			
			List<Proxy> proxyList2 = Fetch_Proxy_Server.fetch("2",Store.getProxy());;

			if(proxyList2.size()==0){
				proxyList2 = Fetch_Proxy_Server.fetch("2",Store.getProxy());;
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
			store.proxyList=filter;
			store.proxyPool=newPool;
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