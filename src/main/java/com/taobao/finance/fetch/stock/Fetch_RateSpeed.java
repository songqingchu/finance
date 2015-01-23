package com.taobao.finance.fetch.stock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_GetLastWorkDay;
import com.taobao.finance.util.FetchUtil;

public class Fetch_RateSpeed {
    public static String url="http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?sort=changepercent&asc=0&node=hs_a&_s_r_a=page&page=1&num=100";
	
	
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	
	static{
		Date d=Fetch_GetLastWorkDay.getLastWorkDay();
		String dstr=FetchUtil.FILE_FORMAT.format(d);
		Map<String,Stock> m1=FetchUtil.
		readFileMapAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+dstr+"\\"+"hardenAll.txt");
		Map<String,Stock> m2=FetchUtil.
		readFileMapAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+dstr+"\\"+"hardenYouAll.txt");
		map.putAll(m1);
		map.putAll(m2);
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
				jsonStr.hashCode();
				list=FetchUtil.parseStockRateSpeed(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Stock> r=new ArrayList<Stock>();
		for(Stock s:list){
			if(!map.containsKey(s.getSymbol())){
				r.add(s);
			}
		}
		return r;
	}
	
	public static void main(String args[]){
		List<Stock> l=fetch();
		for(Stock s:l){
			System.out.println(s.toExamString());
		}
	}
}
