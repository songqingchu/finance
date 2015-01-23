package com.taobao.finance.fetch.stock;

import java.text.SimpleDateFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;

public class Fetch_TodayAveNum {

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=OB&Reference=xml&limit=0&page=1&rt=0.040396281038590054&stk=";
	public static String getUrlBatch(String symbol) {
		String newUrl = "";
		newUrl = url + symbol;
		return newUrl;
	}
	

	
	public static String getUrlTotal(String symbol,int page) {
		String newUrl = "";
		newUrl = url + symbol+"&page="+page;
		return newUrl;
	}

	public static String fetch(String url) {
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				return jsonStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ������
	 * @param code
	 * @return
	 */
	public static Long getTotalNum(String code){
		Stock s=Fetch_SingleStock.fetch(code);
		return s.getTradeNum()/100;
	}
	
	/**
	 * �ܱ���
	 * @param code
	 * @return
	 */
	public static Integer getTotalBatch(String code){
		code=transSymbol(code);
		String newUrl=getUrlBatch(code);
		String r=fetch(newUrl);
		int page=getTotalPage(r);
		String lastPageUrl=newUrl.replace("page=1", "page="+page);
		r=fetch(lastPageUrl);
		int lastBatchNum=getLastPageBatch(r);
		return page*200-200+lastBatchNum;
	}
	
	
	public static Long getAveNum(String code){
		Long num=getTotalNum(code);
		int batch=getTotalBatch(code);
		return num/batch;
	}
	
	public static Integer getLastPageBatch(String s){
		s=s.replace("var jsTimeSharingData=", "");
		s=s.replace(";", "");
		JSONObject j=JSON.parseObject(s);
		JSONArray a=(JSONArray)j.get("data");
		return a.size();
	}
	
	public static Integer getTotalPage(String s){
		s=s.replace("var jsTimeSharingData=", "");
		s=s.replace(";", "");
		JSONObject j=JSON.parseObject(s);
		int a=(Integer)j.get("pages");
		return a;
	}
	
    public static String transSymbol(String s){
    	if(s.contains("sh")){
    		s=s.replace("sh", "");
    		return s+"1";
    	}
    	if(s.contains("sz")){
    		s=s.replace("sz", "");
    		return s+"2";
    	}
    	return null;
    }
	
	public static void main(String args[]){
		System.out.println(getAveNum("sh600483"));
	}
}
