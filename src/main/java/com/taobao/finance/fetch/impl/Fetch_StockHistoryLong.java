package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_StockHistoryLong{

	public static String url_base = "http://yahoo.compass.cn/stock/frames/";
	public static String url = "http://yahoo.compass.cn/stock/frames/frmHistoryDetail.php?code=";
	public static Map<String,String> param=new HashMap<String,String>();
	
	static {
		param.put("start_year",null);
		param.put("start_month",null);
		param.put("start_day",null);
		param.put("end_year",null);
		param.put("end_month",null);
		param.put("end_day",null);
		param.put("code",null);
		param.put("his_type","day");
	}

	public static String getUrl(String stock,int page) {
		return url + stock+"&page="+page;
	}

	/**
	 * 抓取数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<Stock> fetch(String code,int page) {
		HttpClient client = new HttpClient();
		List<Stock> s = null;
		String newUrl = getUrl(code,page);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
		
	        //getMethod.releaseConnection();      
	        //检查是否重定向
	        int statuscode = getMethod.getStatusCode();
			if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) ||
	            (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) ||
	            (statuscode == HttpStatus.SC_SEE_OTHER) ||
	            (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
	            //读取新的URL地址
	            Header header = getMethod.getResponseHeader("location");
	            if (header != null) {
	                String newuri = header.getValue();
	                if ((newuri == null) || (newuri.equals("")))
	                    newuri = "/";
	                GetMethod redirect = new GetMethod(url_base+newuri+"?code="+code);
	                client.executeMethod(redirect);
	                
	                if (getMethod.getStatusCode() == 200) {
	    				String jsonStr = getMethod.getResponseBodyAsString();
	    				s = FetchUtil.parseStockHistoryFromCompass(jsonStr, code);
	    			}
	                
	                redirect.releaseConnection();
	            } else
	                System.out.println("Invalid redirect");
	        }
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				s = FetchUtil.parseStockHistoryFromCompass(jsonStr, code);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(s==null){
			s=new ArrayList<Stock>();
		}
		return s;
	}

	public static List<Stock> getData(String code,int page){
		List<Stock> l=new ArrayList<Stock>();
		for(int i =1;i<=page;i++){
			List<Stock> s=fetch(code,i);
			if(s==null){
				break;
			}else{
				l.addAll(s);
			}
		}
		return l;
	}
	

	public static void main(String args[]) {

         List<Stock> l=getData("sz002298",10);
         l.size();
	}

}
