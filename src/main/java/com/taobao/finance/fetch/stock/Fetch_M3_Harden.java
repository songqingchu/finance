package com.taobao.finance.fetch.stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.filter2.Filter;
import com.taobao.finance.util.FetchUtil;


/**
 * 量化模型2:连续拉涨停，此模型相当靠谱
 * @author songhong.ljy
 */
public class Fetch_M3_Harden {
	
	public static Filter hardernYouFilter=new com.taobao.finance.filter.Filter.NormalFilter();

	public static String url="http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?" +
			"sort=changepercent&asc=0&node=hs_a&_s_r_a=page&page=1&num=3000&";

	/**
	 * 抓取数据
	 * @param url
	 * @return
	 */
	public static List<Stock> fetch() {
		List<Stock> l=new ArrayList<Stock>();
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				l=FetchUtil.parseHardenStockFromSina(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
	
	/**
	 * 涨幅大于9.95
	 * @param l
	 * @return
	 */
	public static List<Stock>  filtStrictHarden(List<Stock> l){
		List<Stock> r=new ArrayList<Stock>();
		for(Stock s:l){
			if(s.isHarden()){
				r.add(s);
			}
		}
		return r;
	}
	
	public static List<Stock> getData(){
		Map<String,Stock> m=new HashMap<String,Stock>();
    	m.putAll(Fetch_BaseData.map);
    	List<Stock> l=new ArrayList<Stock>();
    	l.addAll(m.values());
    	l=filtStrictHarden(l);
    	
		Collections.sort(l, new Stock.HardenTimesDescComparator());
		
		List<String> content=new ArrayList<String>();
		List<String> strict=new ArrayList<String>();
		List<String> notStrict=new ArrayList<String>();
		List<Stock> r=new ArrayList<Stock>();
		for(Stock s:l){
			System.out.println(s.toHardenString());
			content.add(s.toHardenString());
			if(s.isStrictHarden()){
				strict.add(s.toHardenString());
			}else{
				notStrict.add(s.toHardenString());
			}
			r.add(s);
		}
    	return r;
	}
	
	

	

	public static void main(String args[]){
		//getData();
		List<String> l2=new ArrayList<String>();
		List<String> l=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"rongQuan.txt");
		for(String s:l){
			if(s.startsWith("6")){
				l2.add("sh"+s);
			}else{
				l2.add("sz"+s);
			}
		}
		FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"rongQuan.txt", l2);
	}

}
