package com.taobao.finance.fetch.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.taobao.finance.dataobject.Introduce;
import com.taobao.finance.dataobject.Report;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.InfoTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public class Fetch_Info {
	public static String url = "http://f10.eastmoney.com/f10_v2/CompanySurvey.aspx?code=";

	public static String getUrl(String stock) {
		String newUrl = url+stock;
		return newUrl;
	}

	public static Introduce fetch(String code, String proxy, Integer port) {
		Introduce l = null;
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
				l = FetchUtil.parseInfo(jsonStr);
				if(l!=null){
					l.setSymbol(code);
					/*Stock st=Fetch_AllStock.map.get(code);
					if(st!=null){
						l.setName(st.getName());
					}*/
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public static Introduce fetch(final String code, List<Proxy> list) {
		final List<Introduce> result = new ArrayList<Introduce>();
		CountDownLatch latch = new CountDownLatch(1);
		for (final Proxy p : list) {
			new FetchSingleInfoThread(latch, result, code, 0, p).start();
		}
		try {
			latch.await(5, TimeUnit.SECONDS);
			// System.out.println("等待3秒完毕！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}

	public static List<Introduce> fetchAll(ThreadService threadService) {
		List<Stock> stockList = new ArrayList<Stock>();
		stockList.addAll(Fetch_AllStock.map.values());
		List<List<Object>> list = ThreadUtil.divide(stockList, 32);
		List<Callable<Object>> tList = new ArrayList<Callable<Object>>();
		List<Callable<Object>> tList2 = new ArrayList<Callable<Object>>();

		for (List<Object> li : list) {
			InfoTask t = new InfoTask(li);
			tList.add(t);
		}
		if (threadService == null) {
			threadService = new ThreadService();
		}
		tList2.add(tList.get(0));
		List<Object> rList = threadService.service2(tList);
		List<Introduce> reList = new LinkedList<Introduce>();

		for (Object r : rList) {
			Introduce re = (Introduce) r;
			reList.add(re);
		}

		//Collections.sort(reList);
		/*String line = "";
		for (Introduce re : reList) {
			line = re.getPingNum() + "&nbsp;&nbsp;" + re.getDateString()
					+ "&nbsp;&nbsp;<a href='" + re.getLink() + "'>"
					+ re.getTitle() + "</a><br>";
			// System.out.println(re.getPingNum()+"    "+re.getDateString()+"   "+re.getTitle()+":    "+re.getLink());
			//re.setHtml(line);
		}*/
		return reList;
	}

	public static void main(String args[]) {

		List<Introduce> l = Fetch_Info.fetchAll(null);
		File f = new File("D:\\report3.txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for ( Introduce r : l) {
				bw.write(r.getText() + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		l.size();
	
		//Introduce l=Fetch_Info.fetch("sh601368", null, null);
		//l.getAddress();
		

	}
}

class FetchSingleInfoThread extends Thread {
	CountDownLatch latch;
	List<Introduce> l;
	String code;
	Integer size;
	Proxy p;

	public FetchSingleInfoThread(CountDownLatch latch, List<Introduce> l, String code,Integer size, Proxy p) {
		this.latch = latch;
		this.l = l;
		this.code = code;
		this.size = size;
		this.p = p;
	}

	public void run() {
		Introduce r=Fetch_Info.fetch(code,p.getIp(),p.getPort());
		if (r != null) {
				synchronized (l) {
					if (l.size()==0) {
						l.add(r);
						//System.out.println("首先获取到结果！");
						latch.countDown();
					} else {
						//System.out.println("已经获取到结果，丢弃！");
					}
				}
		}
	}
}
