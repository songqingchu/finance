package com.taobao.finance.fetch.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.finance.dataobject.Stock;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015Äê5ÔÂ28ÈÕ
 */
public class Fetch_SingleStock_DZH {
	public static String url = "http://cj.gw.com.cn/stockHq.php?code=";

	public static String getUrl(String stock) {
		stock=stock.toUpperCase();
		String s=url+stock;
		return s;
	}

	public static Stock fetch(String code) {
		HttpClient client = new HttpClient();
		Stock s = null;
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				JSONArray o=(JSONArray)JSONObject.parse(jsonStr);
				JSONObject ob=(JSONObject)o.get(0);
				String ss=(String)ob.get("lp");
				s=new Stock();
				s.setSymbol(code);
				s.setRate(Float.parseFloat(ss));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}


	public static void main(String args[]) {
      Stock s=fetch("sz002084");
      s.getDate();
	}
}