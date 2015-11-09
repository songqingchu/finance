package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.CheckTTLTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public class Fetch_Proxy_Server {
	public static String url = "http://www.xicidaili.com/nn/";

	public static String getUrl(String page) {
		return url + page;
	}


	public static List<Proxy> fetch(String code,String proxy, Integer port) {
		List<Proxy> l=null;
		HttpClient client = new HttpClient();
		
		if (proxy != null && port != null) {
			HostConfiguration config = new HostConfiguration();
			config.setProxy(proxy, port);
			client.setHostConfiguration(config);
		}
        
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				l = FetchUtil.parseProxy(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	
	public static List<Proxy> fetch(final String code, List<Proxy> list) {
		if(list==null){
			return fetch(code,null,null);
		}
		final List<Proxy> result = new ArrayList<Proxy>();
		CountDownLatch latch = new CountDownLatch(1);
		for (final Proxy p : list) {
			new FetchProxyThread(latch, result, code, 0, p).start();
		}
		try {
			latch.await(5, TimeUnit.SECONDS);
			System.out.println("等待3秒完毕！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	@SuppressWarnings("unchecked")
	public static void main(String args[]) {

	  List<Proxy> l=fetch("1",null,null);
	  
	  ThreadService service=new ThreadService();
	  List<List<Object>> dev=ThreadUtil.divide(l, 16);
	  List<Callable<Object>> taskList=new ArrayList<Callable<Object>>();
	  for(List<Object> d:dev){
		  CheckTTLTask task=new CheckTTLTask(d);
		  taskList.add(task);
	  }
	  List<Object> proxyList=(List<Object>)service.service(taskList);
	  
	  List<Proxy> result=new ArrayList<Proxy>();
	  for(Object o:proxyList){
		  List<Proxy> li=(List<Proxy>)o;
		  result.addAll(li);
	  }
	  
	  List<Proxy> filter=new ArrayList<Proxy>();
	  for(Proxy p:result){
		  if(p.getLastTtl()>0){
			  filter.add(p);
		  }
	  }
	  
	  if(filter.size()>0){
		  Collections.sort(filter);
	  }
	  
	  for(Proxy p:filter){
		  if(p.getLastTtl()>0){
			  System.out.println(p.getIp()+","+p.getPort()+":"+p.getLastTtl());
		  }
	  }
	  
	  List<Stock> stockList=Fetch_ServeralStock_Sina.fetch("sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",
				filter);
	  if(stockList!=null){
			for(Stock s:stockList){
				System.out.println(s.getName()+":"+s.getEndPrice());
			}
	  }else{
		  System.out.println("获取股票数据失败！");
	  }
	}
}


class FetchProxyThread extends Thread {
	CountDownLatch latch;
	List<Proxy> l;
	String code;
	Integer size;
	Proxy p;

	public FetchProxyThread(CountDownLatch latch, List<Proxy> l, String code,Integer size, Proxy p) {
		this.latch = latch;
		this.l = l;
		this.code = code;
		this.size = size;
		this.p = p;
	}

	public void run() {
		List<Proxy> r = Fetch_Proxy_Server.fetch(code, p.getIp(),p.getPort());
		if (r != null) {
			if (r.size() != 0) {
				synchronized (l) {
					if (l.size()==0) {
						l.addAll(r);
						latch.countDown();
					} else {
						System.out.println("已经获取到结果，丢弃！");
					}
				}
			}
		}
	}
}
