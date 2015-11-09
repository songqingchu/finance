package com.taobao.finance.fetch.impl;

import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.util.FetchUtil;



	
public class Fetch_ShiZhi{

	public static String url = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27b4VIm$HArIJ1qfKO%27%5D/Market_Center.getHQNodeDataNew?page=1&num=5000&sort=nmc&asc=0&node=hs_a";

	public static String getUrl(String stock) {
		return url + stock+".html";
	}


	public static Map<String,String> fetch(String proxy, Integer port) {
		HttpClient client = new HttpClient();
		
		if (proxy != null && port != null) {
			HostConfiguration config = new HostConfiguration();
			config.setProxy(proxy, port);
			client.setHostConfiguration(config);
		}
		
		Map<String,String> s = null;
		HttpMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				jsonStr=jsonStr.replace("IO.XSRV2.CallbackList['b4VIm$HArIJ1qfKO'](", "");
				jsonStr=jsonStr.replace(")", "");
				s = FetchUtil.parseShiZhi(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	

	public static void main(String args[]) {

       fetch(null,null);
     // s.getDate();
	}

}
