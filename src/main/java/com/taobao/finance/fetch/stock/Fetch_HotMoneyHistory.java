package com.taobao.finance.fetch.stock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.comparator.Comparator;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.dataobject.Store;
import com.taobao.finance.util.FetchUtil;

public class Fetch_HotMoneyHistory {

	
	
	public static String url="http://stockdata.stock.hexun.com/lhb/selfagency.aspx?orgid=";
	
	public static String fetch(String code,int page) {
		
		HttpClient client = new HttpClient();
		String newUrl=url+code+"&page="+page;
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				
				return jsonStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return list;
		return null;
	}
	
	public static List<Stock> getStoreHistory(String code) throws Exception{
		String s=fetch(code,1);
		int totalPage=FetchUtil.parseStoreHistoryTotalPageFromHexun(s);
		List<Stock> result = new ArrayList<Stock>();
		for(int i=1;i<=3;i++){
			String r=fetch(code,i);
			List<Stock> list=FetchUtil.parseStoreHistoryFromHexun(r,code,"");
			result.addAll(list);
		}
		return result;
	}
	
	public static void main(String args[]) throws Exception{
		List<Stock> l=getStoreHistory("8019");
		Collections.sort(l,new Comparator.HotMonneyComparator());
		for(Stock s:l){
			System.out.println(s.toSymbolHotMoney());
		}
		l.size();
	}
}