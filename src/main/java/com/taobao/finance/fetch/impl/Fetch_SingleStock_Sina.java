package com.taobao.finance.fetch.impl;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	

	public static void main(String args[]) {

      Stock s=fetch("sh600128",null,null);
      System.out.println(s.getEndPrice());
	}

}
