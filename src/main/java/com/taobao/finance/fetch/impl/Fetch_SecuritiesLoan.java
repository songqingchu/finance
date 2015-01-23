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


/**
 * 抓取融券标的
 * @author songhong.ljy
 */
public class Fetch_SecuritiesLoan {
	public static String rongquanURL = "http://etrade.cs.ecitic.com/webtrade/rzrq/marginTradeFate.do?method=searchStock&tempDate=Sun%20Aug%2025%2011:08:10%20CST%202013&pagesize=20&stockCode=&exchangeCode=-1&timestampt=1377400079234&currentPage=";
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	public static List<String> all=new ArrayList<String>();
	public static List<String> zhu=new ArrayList<String>();
	public static List<String> zho=new ArrayList<String>();
	public static List<String> chu=new ArrayList<String>();
	
	static{
		File f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
		if(f.exists()){
			all=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
		}
		f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHU);
		if(f.exists()){
			zhu=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHU);
		}
		f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHO);
		if(f.exists()){
			zho=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHO);
		}
		f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_CHU);
		if(f.exists()){
			chu=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_CHU);
		}
	}
	
	/**
	 * 抓取数据
	 * @param url
	 * @return
	 */
	public static List<String> fetch(String url) {
		List<String> list = null;
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
				list=FetchUtil.parse_Rongquan_To_DailyData(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 抓取中信融券标的股票
	 * @param url
	 * @return
	 */
	public static List<Stock> fetchRongquanStock(String url) {
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
				list=FetchUtil.parseSinaStock(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

    /**
     * 抓取今天的统计数据
     * @return
     */
    public static List<String> fetchRonqquan(){
    	List<String> shortStock=new ArrayList<String>();
    	if(all.size()>0){
    		for(String s:map.keySet()){
    			shortStock.add(s);
    		}
    		return shortStock;
    	}
		
		for(int i=1;i<=36;i++){
			if(i==27){
				map.size();
			}
			List<String> stock=fetch(rongquanURL+i);
			shortStock.addAll(stock);
		}
		List<String> zhub=new ArrayList<String>();
		List<String> zhox=new ArrayList<String>();
		List<String> chuy=new ArrayList<String>();
		for(String s:shortStock){
			if(s.startsWith("sz300")){
				chuy.add(s);
			}else if(s.startsWith("sz002")){
				zhox.add(s);
			}else{
				zhub.add(s);
			}
		}
		all.addAll(shortStock);
		zhu.addAll(zhub);
		zho.addAll(zhox);
		chu.addAll(chuy);
		
		FetchUtil.save(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN, shortStock);
		FetchUtil.save(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHU, zhub);
		FetchUtil.save(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_ZHO, zhox);
		FetchUtil.save(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN_CHU, chuy);
		return shortStock;
    }
    

	public static void main(String args[]){
		List<String> a=fetchRonqquan();
		//FetchUtil.save(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN, a);
	}
}

