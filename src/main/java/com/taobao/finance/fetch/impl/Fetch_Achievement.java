package com.taobao.finance.fetch.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Report;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.ReportTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public class Fetch_Achievement {
	public static String url = "http://data.eastmoney.com/bbsj/stockcode/yjbb.html";

	public static String getUrl(String stock) {
		String newUrl=url.replace("code", stock);
		return newUrl;
	}

	public static List<Report> fetch(String code,String proxy,
			Integer port) {
		List<Report> l = new ArrayList<Report>();
		HttpClient client = new HttpClient();

		if (proxy != null && port != null) {
			HostConfiguration config = new HostConfiguration();
			config.setProxy(proxy, port);
			client.setHostConfiguration(config);
		}

		Stock s = null;
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		String dateStr = df.format(d);
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				l = FetchUtil.parseAchievement(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		l.size();
		return l;
	}
	
	
	public static List<Report> fetch(final String code, List<Proxy> list) {
		final List<Report> result = new ArrayList<Report>();
		CountDownLatch latch = new CountDownLatch(1);
		for (final Proxy p : list) {
			new FetchSingle2Thread(latch, result, code, 0, p).start();
		}
		try {
			latch.await(5, TimeUnit.SECONDS);
			//System.out.println("等待3秒完毕！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	

	public static List<Report> fetchAll(ThreadService threadService){
		List<Stock> stockList=new ArrayList<Stock>();
		stockList.addAll(Fetch_AllStock.map.values()); 
		List<Report> l=new ArrayList<Report>();
	    List<List<Object>> list=ThreadUtil.divide(stockList, 32);
	    List<Callable<Object>> tList=new ArrayList<Callable<Object>>();
	    
		for(List<Object> li:list){
			ReportTask t=new ReportTask(li);
			tList.add(t);
		}
		if(threadService==null){
			threadService=new ThreadService();
		}
		List<Object> rList=threadService.service2(tList);
		List<Report> reList=new LinkedList<Report>();
		
		for(Object r:rList){
			Report re=(Report)r;
			reList.add(re);
		}
		
		Collections.sort(reList);
		String line="";
		for(Report re:reList){
			line=re.getPingNum()+"&nbsp;&nbsp;"+re.getDateString()+"&nbsp;&nbsp;<a href='"+re.getLink()+"'>"+re.getTitle()+"</a><br>";
			//System.out.println(re.getPingNum()+"    "+re.getDateString()+"   "+re.getTitle()+":    "+re.getLink());
			re.setHtml(line);
		}
		return reList;
	}
	
	
	

	public static void main(String args[]) {

		/*List<Report> l=new Fetch_Report_Jupai().fetchAll(null);
		File f=new File("D:\\report.txt");
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(f));
			for(Report r:l){
				bw.write(r.getHtml()+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		l.size();*/
		
		
		List<Report> l=Fetch_Achievement.fetch("300059", null, null);
		l.size();

	}
}

class FetchSingle3Thread extends Thread {
	CountDownLatch latch;
	List<Report> l;
	String code;
	Integer size;
	Proxy p;

	public FetchSingle3Thread(CountDownLatch latch, List<Report> l, String code,Integer size, Proxy p) {
		this.latch = latch;
		this.l = l;
		this.code = code;
		this.size = size;
		this.p = p;
	}

	public void run() {
		List<Report> r=Fetch_Report_Jupai.fetch(code,p.getIp(),p.getPort());
		if (r != null) {
				synchronized (l) {
					if (l.size()==0) {
						//l = r;
						if(r.size()>0){
							l.addAll(r);
						}
						
						//System.out.println("首先获取到结果！");
						latch.countDown();
					} else {
						//System.out.println("已经获取到结果，丢弃！");
					}
				}
		}
	}
}

