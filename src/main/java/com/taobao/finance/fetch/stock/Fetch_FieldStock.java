package com.taobao.finance.fetch.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_FieldStock {
	
	public static String url="http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=1&num=400&sort=symbol&asc=1&node=";

	public static Map<String,Stock> map=new HashMap<String,Stock>();
	
	static{
		File f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+"canRong.txt");
		if(f.exists()){
			map=FetchUtil.readRong(FetchUtil.FILE_STOCK_ANASYS_BASE+"canRong.txt");
		}
	}
	public static String getUrl(String code){
		return url+code;
	}
	public static List<Stock> fetch(String code) {
		List<Stock> list = null;
		HttpClient client = new HttpClient();
		String newUrl=getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				list=FetchUtil.parseSinaFieldStock(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Stock> getData(String field){
		/**
		 * new_ljhy new_fdc
		 */
		if(field==null){
			List<Stock> list=new ArrayList<Stock>();
			list.addAll(map.values());
			return list;
		}
		List<Stock> list=fetch(field);
		List<Stock> r=new ArrayList<Stock>();
		for(Stock s:list){
			Stock st=map.get(s.getSymbol());
			if(st==null){
				continue;
			}
			if(st.getRongNum()<=0){
				continue;
			}
			s.setRongNum(st.getRongNum());
			r.add(s);
			System.out.println(s.getSymbol()+"\t"+s.getName()+"\t\t"+s.getRongNum().intValue());
		}
		return r;
	}
	
	public static void getZhAndCh(){
		Set<String> set=map.keySet();
		for(String s:set){
			Stock st=map.get(s);
			if(st.getRongNum()>0){
               if(s.startsWith("sz300")||s.startsWith("sz002")){
            	   System.out.println(st.getSymbol()+"\t"+st.getName()+"\t\t"+st.getRongNum().intValue());
               }
			}
		}
	}
	
	/**
	 * ½¨Öþ
	 */
	public static void getConstruction(){
		getData("new_fdc");
	}
	
	/**
	 * °×¾Æ
	 */
	public static void getWine(){
		getData("new_ljhy");
	}
	
	/**
	 * ¿óÒµ
	 */
	public static void getCone(){
		getData("new_mthy");
	}
	
	public static void getNews(){
		getConstruction();
	}
	public static void main(String args[]){
		getNews();
		
	}
}
