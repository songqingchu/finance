package com.taobao.finance.fetch.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.dataobject.Store;
import com.taobao.finance.util.FetchUtil;

public class Fetch_HotMoney {
public static String url="http://stockdata.stock.hexun.com/lhb/agency.aspx?type=365";
	
	
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	

	public static List<Store> fetch() {
		List<Store> list = null;
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				jsonStr=jsonStr.replace("IO.XSRV2.CallbackList['xXIv7u5O5YUCEb9y'](", "");
				jsonStr=jsonStr.replace("IO.XSRV2.CallbackList['h$ovBT3ZMORCHEo8'](", "");
				jsonStr=jsonStr.substring(0,jsonStr.length()-1);
				list=FetchUtil.parseStoreFromHexun(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String args[]){
		List<Store> l=fetch();
		for(Store s:l){
			System.out.println(s);
		}
	}
}
