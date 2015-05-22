package com.taobao.finance.fetch.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.dataobject.Tick;
import com.taobao.finance.util.FetchUtil;

public class Fetch_Zhubi {
	public static String url = "http://vip.stock.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?symbol=";
	public static String getUrl(String stock) {
		return url + stock;
	}

	public static List<Tick> fetch(String code,String filter) {
		List<Tick> l=null;
		HttpClient client = new HttpClient();
		Stock s = null;
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		Date d=new Date();
		DateFormat df=new SimpleDateFormat("yyyy.MM.dd");
		String dateStr=df.format(d);
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				l=FetchUtil.parseZhubi(jsonStr, code,dateStr,filter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		l.size();
		return l;
	}

	

	public void check(String symbol){
		List<Tick> r=new ArrayList<Tick>();
		while(true){
			String date=null;
			if(r.size()==0){
				date=null;
			}else{
				date=r.get(r.size()-1).getTimeStr();
			} 
			System.out.println("download Tick");
			List<Tick> l=fetch("sz300019",date);
			System.out.println("download Tick end:"+l.size()+","+r.size());
			merge(r,l);
			check(r);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void merge(List<Tick> all,List<Tick> ticks){
		all.addAll(ticks);
	}
	
    public void check(List<Tick> all){

	}
	
	public static void main(String args[]) {

         new Fetch_Zhubi().check("sz300019");

	}
}
