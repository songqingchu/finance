package com.taobao.finance.fetch.impl;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONObject;
import com.taobao.finance.dataobject.Stock;

public class Fetch_SingleStock_THS {
	public static String url = "http://stockpage.10jqka.com.cn/spService/place/Header/realHeader";

	public static String getUrl(String stock) {
		stock=stock.replace("sz", "");
		stock=stock.replace("sh", "");
		String s=url.replace("place", stock);
		return s;
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
				String response=new String(jsonStr.getBytes("gb2312"));
				JSONObject o=(JSONObject)JSONObject.parse(response);
				String ss=(String)o.get("xj");
				s=new Stock();
				s.setSymbol(code);
				s.setStartPrice(ss);
				s.setHighPrice(ss);
				s.setEndPrice(ss);
				s.setEndPrice(ss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}


	public static void main(String args[]) {
      Stock s=fetch("sz300019",null,null);
      s.getDate();
	}
}
