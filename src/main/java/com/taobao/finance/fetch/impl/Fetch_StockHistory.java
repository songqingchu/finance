package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_StockHistory {

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

	public static String getUrl(String stock) {
		return url + stock;
	}

	/**
	 * ץȡ����
	 * 
	 * @param url
	 * @return
	 */
	public static List<Stock> fetch(String code) {
		HttpClient client = new HttpClient();
		List<Stock> s = null;
		String newUrl = getUrl(code);
		if(code.contains("000001")){
			code.length();
		}
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
		
	        //getMethod.releaseConnection();      
	        //����Ƿ��ض���
	        int statuscode = getMethod.getStatusCode();
			if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) ||
	            (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) ||
	            (statuscode == HttpStatus.SC_SEE_OTHER) ||
	            (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
	            //��ȡ�µ�URL��ַ
	            Header header = getMethod.getResponseHeader("location");
	            if (header != null) {
	                String newuri = header.getValue();
	                if ((newuri == null) || (newuri.equals("")))
	                    newuri = "/";
	                GetMethod redirect = new GetMethod(url_base+newuri+"?code="+code);
	                client.executeMethod(redirect);
	                
	                if (getMethod.getStatusCode() == 200) {
	    				String jsonStr = getMethod.getResponseBodyAsString();
	    				code=code.replace("&page=2", "");
	    				code=code.replace("&page=3", "");
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
		getMethod.releaseConnection();
		return s;
	}

	/**
	 * ץȡ����������
	 * @param code
	 * @return
	 */
	public static List<Stock> fetch3(String code) {
		List<Stock> s=new ArrayList<Stock>();
		for(int j=1;j<8;j++){
			Map<String,String> params=prepareParams(code,j);
			String url=toUrl(params);
			url=code+"&"+url;
			List<Stock> l=null;
			l=fetch(url);
			if(l!=null){
				s.addAll(l);
			}	
		}
		return s;
	}

	
	public static Map<String,String> prepareParams(String code,int page){
		Map<String,String> param=new HashMap<String,String>();
		Calendar start=Calendar.getInstance();
		start.add(Calendar.MONTH, -10);
		Calendar end=Calendar.getInstance();
	
		param.put("start_year",start.get(Calendar.YEAR)+"");
		param.put("start_month",(start.get(Calendar.MONTH)+1)+"");
		param.put("start_day",start.get(Calendar.DAY_OF_MONTH)+"");
		param.put("end_year",end.get(Calendar.YEAR)+"");
		param.put("end_month",(end.get(Calendar.MONTH)+1)+"");
		param.put("end_day",end.get(Calendar.DAY_OF_MONTH)+"");
		param.put("code",null);
		param.put("his_type","day");
		param.put("page", page+"");
		return param;
	}
	
	public static String toUrl(Map<String,String> m){
		String url="";
		for(String s:m.keySet()){
			if(StringUtils.isNotBlank(m.get(s))){
				url=url+s+"="+m.get(s)+"&";
			}
		}
		return url;
	}
	
	
	public static void main(String args[]) {

		List<Stock> history=Fetch_StockHistory.fetch3("sh000001");
        Collections.reverse(history);
        Hisdata_Base.save("sh000001",history);
        
        history=Fetch_StockHistory.fetch3("sz399001");
        Collections.reverse(history);
        Hisdata_Base.save("sz399001",history);
        
        history=Fetch_StockHistory.fetch3("sz399006");
        Collections.reverse(history);
        Hisdata_Base.save("sz399006",history);
        
        history=Fetch_StockHistory.fetch3("sz399101");
        Collections.reverse(history);
        Hisdata_Base.save("sz399101",history);
        
	}

}
