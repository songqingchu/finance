package com.taobao.finance.fetch.impl;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.util.FetchUtil;



public class Fetch_SingleStock_Sina {

	public static String url = "http://hq.sinajs.cn/list=";

	public static String getUrl(String stock) {
		return url + stock;
	}


	public static Stock fetch(String code,String proxy, Integer port) {
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

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				s = FetchUtil.parseTodayStockFromSina(jsonStr);
			}
		} catch (SocketTimeoutException e) {
			//System.out.println("代理:" + proxy + "," + port + " 响应超时！");
		} catch (SocketException e) {
			//System.out.println("代理:" + proxy + "," + port + " 无法获取数据！");
		} catch (ConnectTimeoutException e) {
			//System.out.println("代理:" + proxy + "," + port + " 连接超时！");
		} catch (NoHttpResponseException e) {
			//System.out.println("代理:" + proxy + "," + port + " 没有响应！");
		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {

			} else if (e instanceof SocketException) {

			} else if (e instanceof ConnectTimeoutException) {

			} else if (e instanceof NoHttpResponseException) {

			} else {
				e.printStackTrace();
			}

		}
		return s;
	}

	
	public static Stock fetch(final String code, List<Proxy> list) {
		final List<Stock> result = new ArrayList<Stock>();
		CountDownLatch latch = new CountDownLatch(1);
		for (final Proxy p : list) {
			new FetchSingleThread(latch, result, code, 0, p).start();
		}
		try {
			latch.await(5, TimeUnit.SECONDS);
			//System.out.println("等待3秒完毕！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	

	public static void main(String args[]) {

      Stock s=fetch("sh600128",null,null);
      System.out.println(s.getEndPrice());
	}

}

class FetchSingleThread extends Thread {
	CountDownLatch latch;
	List<Stock> l;
	String code;
	Integer size;
	Proxy p;

	public FetchSingleThread(CountDownLatch latch, List<Stock> l, String code,Integer size, Proxy p) {
		this.latch = latch;
		this.l = l;
		this.code = code;
		this.size = size;
		this.p = p;
	}

	public void run() {
		Stock r = Fetch_SingleStock_Sina.fetch(code,p.getIp(),p.getPort());
		if (r != null) {
				synchronized (l) {
					if (l.size()==0) {
						//l = r;
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
