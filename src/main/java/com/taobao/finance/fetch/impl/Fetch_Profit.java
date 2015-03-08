package com.taobao.finance.fetch.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_Profit {
	public static String url = "http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/symbol.phtml";

	public static String getUrl(String symbol) {
		symbol=symbol.replace("sz", "");
		symbol=symbol.replace("sh", "");
		String s=url.replace("symbol", symbol);
		return s;
	}


	public static Stock fetch(String code) {
		HttpClient client = new HttpClient();
		Stock s = null;
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod("http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/002084.phtml");
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				byte [] r=getMethod.getResponseBody();
				String ss=new String(r,"GB2312");
				System.out.println(ss);
				s = FetchUtil.parseProfit(jsonStr, code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	

	public static void main(String args[]) {

      fetch("sz002084");
      
	}

}
