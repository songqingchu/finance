package com.taobao.finance.fetch.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;


/**
 * 检查今天是否开市
 * @author Administrator
 */
public class Fetch_CheckWorkDay {

    public static String url = "http://hq.sinajs.cn/list=";
	
	
	public static String getUrl(String stock) {
		return url + stock;
	}
	
	public static  boolean checkOne(String code){
		List<Stock> history=Fetch_StockHistory.fetch(code);
		Stock today=Fetch_SingleStock.fetch(code);
		Stock lastDay=history.get(0);	
		if(today.getStartPrice()==null){
			return true;
		}
		String todayStartStr=today.getStartPrice();
		String lastBit=todayStartStr.substring(todayStartStr.length()-1,todayStartStr.length());
		todayStartStr=todayStartStr.substring(0,todayStartStr.length()-1);
		if(Integer.parseInt(lastBit)>=5){
			Double d=Double.parseDouble(todayStartStr);
			d=d+0.01D;
			todayStartStr=d.toString();
		}
		
		today.setStartPrice(todayStartStr);
		
		if(today.getStartPrice().equals(lastDay.getStartPrice().replace(",", ""))){
			return false;
		}
		return true;
	}
	
	public static  boolean checkWorkDay(){
		Date date=new Date();
		if(date.getHours()<9){
			
		}else{
			HttpClient client = new HttpClient();
			Date d=null;
			String newUrl = getUrl("sh000001");
			HttpMethod getMethod = new GetMethod(newUrl);
			getMethod.setFollowRedirects(false);
			getMethod
					.addRequestHeader(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

			try {
				client.executeMethod(getMethod);
				if (getMethod.getStatusCode() == 200) {
					String jsonStr = getMethod.getResponseBodyAsString();
					d = FetchUtil.parseLastDateFromSina(jsonStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(d.getDate()==date.getDate()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static void main(String args[]){
		System.out.println(checkWorkDay());
	}
}
