package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;


/**
 * 量化模型2:连续拉涨停，此模型相当靠谱
 * @author songhong.ljy
 */
public class Fetch_M3_Harden {

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
	 * 涨幅大于9.97
	 * @param l
	 * @return
	 */
	public static List<Stock>  filtStrictHarden(List<Stock> l){
		List<Stock> r=new ArrayList<Stock>();
		for(Stock s:l){
			if(s.getRate()>0.0997F){
				r.add(s);
			}
		}
		return r;
	}
	
	public static void getData(){
		List<Stock> l=fetch();
		l=filtStrictHarden(l);
		for(Stock s:l){
		    if(s.getSymbol().equals("sh600661")){
		    	s.getSymbol();
		    }
			List<Stock> history=Fetch_StockHistory.fetch(s.getSymbol());
			
			setRate(history);
			history.remove(history.size()-1);
			history.add(0,s);
			int count=getContinusHardon(history);
			s.setHardenTimes(count);
			Stock today=Fetch_SingleStock.fetch(s.getSymbol());
			if((Float.parseFloat(today.getEndPrice())-Float.parseFloat(today.getStartPrice()))/Float.parseFloat(today.getStartPrice())<0.01F){
				s.setStrictHarden(true);
			}
		}
		
		Collections.sort(l, new Stock.HardenTimesDescComparator());
		
		List<String> content=new ArrayList<String>();
		List<String> strict=new ArrayList<String>();
		List<String> notStrict=new ArrayList<String>();
		for(Stock s:l){
			System.out.println(s.toHardenString());
			content.add(s.toHardenString());
			if(s.isStrictHarden()){
				strict.add(s.toHardenString());
			}else{
				notStrict.add(s.toHardenString());
				
			}
		}
		
		Date dd=new Date();
    	if(dd.getHours()<18){
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, -1);
    		dd=c.getTime();
    	}
    	String d=FetchUtil.FILE_FORMAT.format(dd);
		FetchUtil.save(FetchUtil.FILE_HARDEN_C,d, content);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F,d, content);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F1,d, strict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F2,d, strict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F3,d, strict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F4,d, strict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F_YOU1,d, notStrict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F_YOU2,d, notStrict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F_YOU3,d, notStrict);
		FetchUtil.save(FetchUtil.FILE_HARDEN_F_YOU4,d, notStrict);
	}
	
	/**
	 * 持续涨停天数
	 * @param l
	 * @return
	 */
	public static int getContinusHardon(List<Stock> l){
		int count =0;
		for(int i=0;i<l.size();i++){
			Stock s=l.get(i);
			if(s.getRate()*100>9.8F){
				count++;
			}else{
				break;
			}
		}
		return count;
	}
	
	/**
	 * 设置涨幅
	 * @param l
	 */
	public static void setRate(List<Stock> l){
		for(int i=0;i<l.size()-1;i++){
			Stock a=l.get(i);
			Stock b=l.get(i+1);
			a.setRate(Float.parseFloat(a.getEndPrice())/Float.parseFloat(b.getEndPrice())-1);
		}
	}

	public static void main(String args[]){
		getData();
	}
	

   
	
	

}
