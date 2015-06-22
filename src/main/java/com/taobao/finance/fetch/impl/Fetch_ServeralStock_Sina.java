package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;



import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_ServeralStock_Sina {

	public static String url = "http://hq.sinajs.cn/list=";

	public static String getUrl(String stock) {
		return url + stock;
	}

	public static List<Stock> fetch(String code) {
		HttpClient client = new HttpClient();
		List<Stock> l=new ArrayList<Stock>();
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
				//jsonStr=StringUtils.replace(jsonStr, "var hq_str_", "");
				String[] datas=StringUtils.split(jsonStr,";");
				if(jsonStr.contains("sz002250")){
					jsonStr.length();
				}
				for(String data:datas){
					if(data.contains("sz002250")){
						data.length();
					}
					s = FetchUtil.parseTodayStockFromSina(data);
					if(s!=null){
						l.add(s);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public static void main(String args[]) {

	}

}
