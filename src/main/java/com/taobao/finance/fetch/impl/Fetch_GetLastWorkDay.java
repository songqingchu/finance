package com.taobao.finance.fetch.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_GetLastWorkDay {
	
	public static String url = "http://hq.sinajs.cn/list=";
	
	
	public static String getUrl(String stock) {
		return url + stock;
	}

	
	/**
	 * 抓取数据
	 * @param url
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getLastWorkDay() {
		Date last=null;
		Date dd=new Date();
		if(dd.getHours()<9){
			HttpClient client = new HttpClient();
			Date d=null;
			String newUrl = getUrl("sh000001");
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
					d = FetchUtil.parseLastDateFromSina(jsonStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return d;	
		}else{
			List<Stock> list=Fetch_StockHistory.fetch("sh000001");
			last=list.get(0).getDate();
		}
		return last;
	}
	
	public static void main(String args[]){
		System.out.println(getLastWorkDay());
	}
}
