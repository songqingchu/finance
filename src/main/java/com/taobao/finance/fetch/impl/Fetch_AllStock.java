package com.taobao.finance.fetch.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_AllStock {

	public static String url="http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=3000&page=1&jsName=quote_123&style=33&_g=0.5912618585862219";
	
	
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	public static Map<String,String> nameMap=new HashMap<String,String>();
	
	static{
		File f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+"stockAll.txt");
		if(f.exists()){
			map=FetchUtil.readFileMapAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"stockAll.txt");
			for(Stock s:map.values()){
				nameMap.put(s.getName(),s.getSymbol());
			}
		}
	}
	public static List<Stock> fetch() {
		List<Stock> list = null;
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
				list=FetchUtil.parseSinaStock2(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void save(List<Stock> l){
		List<String> save=new ArrayList<String>();
		for(Stock s:l){
			save.add(s.toSymbolAndNameString());
		}
		FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"stockAll.txt", save);
	}
	public static void getData(){
		List<Stock> l=fetch();
		Stock ss=new Stock();
		ss.setSymbol("sh000001");
		ss.setName("上证指数");
		l.add(ss);
		
		ss=new Stock();
		ss.setSymbol("sz399001");
		ss.setName("深证成指");
		l.add(ss);
		
		ss=new Stock();
		ss.setSymbol("sz399006");
		ss.setName("创业板指");
		l.add(ss);
		
		ss=new Stock();
		ss.setSymbol("sz399101");
		ss.setName("中小板指");
		l.add(ss);
		
		save(l);
		Map<String,Stock> m=new HashMap<String,Stock>();
		for(Stock s:l){
			m.put(s.getSymbol(), s);
		}
		map=m;
	}
	
	
	public static void main(String args[]){
		getData();
	}
}
