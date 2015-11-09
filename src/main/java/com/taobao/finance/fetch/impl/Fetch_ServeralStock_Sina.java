package com.taobao.finance.fetch.impl;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;

import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.CheckTTLTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public class Fetch_ServeralStock_Sina {

	public static String url = "http://hq.sinajs.cn/list=";

	public static String getUrl(String stock) {
		return url + stock;
	}

	public static List<Stock> fetch(String code, String proxy, Integer port) {
		HttpClient client = new HttpClient();
		List<Stock> l = new ArrayList<Stock>();

		if (proxy != null && port != null) {
			HostConfiguration config = new HostConfiguration();
			config.setProxy(proxy, port);
			client.setHostConfiguration(config);
		}
		client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
		client.getHttpConnectionManager().getParams().setSoTimeout(3000);

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
				// jsonStr=StringUtils.replace(jsonStr, "var hq_str_", "");
				String[] datas = StringUtils.split(jsonStr, ";");
				if (jsonStr.contains("sz002250")) {
					jsonStr.length();
				}
				for (String data : datas) {
					if (data.contains("sz002250")) {
						data.length();
					}
					s = FetchUtil.parseTodayStockFromSina(data);
					if (s != null) {
						l.add(s);
					}
				}

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
		return l;
	}

	public static List<Stock> fetch(final String code, List<Proxy> list) {
		final List<Stock> result = new ArrayList<Stock>();
		CountDownLatch latch = new CountDownLatch(1);
		for (final Proxy p : list) {
			new FetchThread(latch, result, code, 0, p).start();
		}
		try {
			latch.await(5, TimeUnit.SECONDS);
			//System.out.println("等待3秒完毕！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static  void test(){
		List<Proxy> pList = Fetch_Proxy_Server.fetch("1",null,null);

		List<List<Object>> dev = ThreadUtil.divide(pList, 16);
		ThreadService service = new ThreadService();
		List<Callable<Object>> taskList = new ArrayList<Callable<Object>>();
		for (List<Object> d : dev) {
			CheckTTLTask task = new CheckTTLTask(d);
			taskList.add(task);
		}
		List<Object> proxyList = (List<Object>) service.service(taskList);

		List<Proxy> result = new ArrayList<Proxy>();
		for (Object o : proxyList) {
			List<Proxy> li = (List<Proxy>) o;
			result.addAll(li);
		}

		
		System.out.println("\n\n\n可用代理列表");
		List<Proxy> filter = new ArrayList<Proxy>();
		for (Proxy p : result) {
			/*if(p.getIp().contains("59.78.160.244")){
				filter.add(p);
			}*/
			if (p.getLastTtl() > 0) {
				filter.add(p);
				System.out.println(p.getIp()+","+p.getPort()+":"+p.getLastTtl());
			}
		}

		System.out.println("\n\n");
		List<Stock> l = fetch(
				"sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",
				filter);
		if (l != null) {
			for (Stock s : l) {
				System.out.println(s.getName() + ":" + s.getEndPrice());
			}
		}
	}

	public static void main(String args[]) {
		test();
		
		/*List<Stock> l = fetch(
				"sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",
				"59.78.160.244",8080);
		if (l != null) {
			for (Stock s : l) {
				System.out.println(s.getName() + ":" + s.getEndPrice());
			}
		}*/
		
	}
}

class FetchThread extends Thread {
	CountDownLatch latch;
	List<Stock> l;
	String code;
	Integer size;
	Proxy p;

	public FetchThread(CountDownLatch latch, List<Stock> l, String code,Integer size, Proxy p) {
		this.latch = latch;
		this.l = l;
		this.code = code;
		this.size = size;
		this.p = p;
	}

	public void run() {
		List<Stock> r = Fetch_ServeralStock_Sina.fetch(code,p.getIp(),p.getPort());
		if (r != null) {
			if (r.size() != 0) {
				synchronized (l) {
					if (l.size()==0) {
						//l = r;
						l.addAll(r);
						//System.out.println("首先获取到结果！");
						latch.countDown();
					} else {
						//System.out.println("已经获取到结果，丢弃！");
					}
				}
			}
		}
	}
}