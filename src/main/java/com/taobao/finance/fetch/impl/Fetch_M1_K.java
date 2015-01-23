package com.taobao.finance.fetch.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
 * 抓取空方数据
 * @author Administrator
 */
public class Fetch_M1_K  {
	public static String urlShort = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27xXIv7u5O5YUCEb9y%27%5D/StatisticsService.getShortList?num=3000&sort=_3changes&asc=0&node=adr_hk&page=1";
	public static String urlLong = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27h$ovBT3ZMORCHEo8%27%5D/StatisticsService.getLongList?num=3000&sort=_10changes&asc=1&node=adr_hk&page=1";
	public static Filter filter1=new Fetch_M1_K.Filter1();
	public static Filter filter2=new Fetch_M1_K.Filter2();
	public static Map<String,Stock> map=new HashMap<String,Stock>();

	public static Map<String,Stock> rongQuan=new HashMap<String,Stock>();
	
	static {
		File f=new File("D:\\rongquan.txt");
		try {
			BufferedReader br=new BufferedReader(new FileReader(f));
            String line=null;
            line=br.readLine();
			while(line!=null){
				String s[]=line.split("\t");
				Stock stock=new Stock();
				stock.setCode(s[0]);
				stock.setSymbol(s[1]);
				rongQuan.put(s[1], stock);
				line=br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 抓取数据
	 * @param url
	 * @return
	 */
	public static List<Stock> fetch(String url) {
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
    public static List<Stock> fetchToday(){
		List<Stock> shortStock=fetch(urlShort);
		List<Stock> longStock=fetch(urlLong);
		for(Stock stock:shortStock){
			stock.set_2changes(stock.get_2changes());
			stock.set_3changes(stock.get_3changes());
			stock.set_4changes(stock.get_4changes());
			stock.set_5changes(stock.get_5changes());
			map.put(stock.getName(), stock);
		}
		for(Stock stock:longStock){
			Stock stock2=map.get(stock.getName());
			stock2.set_10changes(stock.get_10changes());
			stock2.set_20changes(stock.get_20changes());
			stock2.set_30changes(stock.get_30changes());
			stock2.set_60changes(stock.get_60changes());	
		}
		Collection<Stock> values=map.values();
		List<Stock> a=new ArrayList<Stock>();
		for(Stock stock:values){
			a.add(stock);
		}
		Collections.sort(a);
		return a;
    }
    
    public static void print(List<Stock> list){
    	System.out.println("符合总数："+list.size());
    	int i=0;
    	String s="";
    	
		for(Stock stock:list){
			/*String title=stock.getSymbol();
			if(title.contains("sz")){
				title=title.replace("sz", "");
				title="SZHQ"+title;
			}
			if(title.contains("sh")){
				title=title.replace("sh", "");
				title="SHHQ"+title;
			}
			s=s+title+"\n";
			i++;
			if(i>100){
				break;
			}
			System.out.println(stock.getSymbol()+
					"\t"+FetchUtil.format0Right(stock.getTwo())+
					"\t"+FetchUtil.format0Right(stock.getThree())+"\t"+FetchUtil.format0Right(stock.getFour())+
					"\t"+FetchUtil.format0Right(stock.getFive())+"\t"+FetchUtil.format0Right(stock.getTen())+
					"\t"+FetchUtil.format0Right(stock.getTwenty())+"\t"+FetchUtil.format0Right(stock.getMonth())+
					"\t"+FetchUtil.format0Right(stock.getTwoMonth())+"\t"+stock.getName()
			);*/
			System.out.println(stock.getSymbol());
		}
		System.out.println("\n\n");
		System.out.println(s);
    }
	
    
    public static void getData(){
    	List<Stock> l=fetchToday();
    	l=filter(filter2, l);
    	print(l);
    	
    }
    
    public static List<Stock> filter(Filter f,List<Stock> list){
    	List<Stock> result=new ArrayList<Stock>();
    	for(Stock s:list){
    		if(f.filter(s)){
    			String title=s.getName();
    			if(rongQuan.containsKey(title)){
    				result.add(s);	
    			}
    		}
    	}
    	return result;
    }
	
	public static void main(String args[]){
		getData();

	}
	

   
	
	
	static class Filter1 implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getName().contains("ST")){
				return false;
			}
			if(s.getMonth()>0&&s.getTwoMonth()>0&&s.getMonth()<s.getTwoMonth()){
				if(s.getTen()<0){
					int i=0;
					if(s.getTwo()>s.getThree()){
						i++;
					}
					if(s.getThree()>s.getFour()){
						i++;
					}
					if(s.getFour()>s.getFive()){
						i++;
					}
					if(i>=2){
						/**
						 * 转升
						 */
						if(s.getTwo()>0){
							return true;
						}
						if(s.getTwo()<0&&Math.abs(s.getTwo())<0.03F){
							return true;
						}
					}
				}
			}
			return false;
		}
	}
	
	
	static class Filter2 implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getName().contains("ST")){
				return false;
			}
			//if(s.getMonth()>0&&s.getTwoMonth()>0&&s.getMonth()<s.getTwoMonth()){
				if(s.getFive()>0){
					int i=0;
					if(s.getTwo()<s.getThree()){
						i++;
					}
					if(s.getThree()<s.getFour()){
						i++;
					}
					if(s.getFour()<s.getFive()){
						i++;
					}
					if(i>=2){
						/**
						 * 转升
						 */
						if(s.getTwo()<0){
							return true;
						}
						if(s.getTwo()>0&&Math.abs(s.getTwo())<0.03F){
							return true;
						}
					}
				}
			//}
			return false;
		}
	}
}

