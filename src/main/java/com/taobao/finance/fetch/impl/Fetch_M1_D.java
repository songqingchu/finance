package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
 *  抓取多方数据
 * @author Administrator
 */
public class Fetch_M1_D  {
	public static String urlShort = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27xXIv7u5O5YUCEb9y%27%5D/StatisticsService.getShortList?num=3000&sort=_3changes&asc=0&node=adr_hk&page=1";
	public static String urlLong = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27h$ovBT3ZMORCHEo8%27%5D/StatisticsService.getLongList?num=3000&sort=_10changes&asc=1&node=adr_hk&page=1";
	public static Filter filter1=new Fetch_M1_D.Filter1();
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	
	public static List<String> rongQuan=new ArrayList<String>();
	static{
		rongQuan=FetchUtil.readFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
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
     * 短期统计数据和长期统计数据做join
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
			if(rongQuan.contains(stock.getSymbol())){
				stock.setRongquan(true);
			}else{
				stock.setRongquan(false);
			}
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
		int i=0;
		for(Stock stock:values){
        		System.out.println("处理："+i+"/"+values.size());
        		String symbol=stock.getSymbol();
        		Stock st=Fetch_SingleStock.fetch(symbol);
        		stock.setStartPrice(st.getStartPrice());
        		stock.setEndPrice(st.getEndPrice());
        		
        		List<Stock> list=Fetch_StockHistory.fetch(symbol);
        		stock.setStartPrice1(list.get(0).getStartPrice());
        		stock.setEndPrice1(list.get(0).getEndPrice());
        		stock.setStartPrice2(list.get(1).getStartPrice());
        		stock.setEndPrice2(list.get(1).getEndPrice());
        		stock.setStartPrice3(list.get(2).getStartPrice());
        		stock.setEndPrice3(list.get(2).getEndPrice());
        		stock.setStartPrice4(list.get(3).getStartPrice());
        		stock.setEndPrice4(list.get(3).getEndPrice());
        		setRate(stock);
        		i++;
			a.add(stock);
		}
		Collections.sort(a);
		
		
		
		Date dd=new Date();
    	if(dd.getHours()<18){
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, -1);
    		dd=c.getTime();
    	}
    	String d=FetchUtil.FILE_FORMAT.format(dd);
    	
		List<String> save=new ArrayList<String>();
		for(Stock st:a){
    		save.add(st.toStringX());
    	}
    	FetchUtil.save(FetchUtil.FILE_DUO_X, d, save);
		return a;
    }
    
    public static void setRate(Stock s){
    	
    	if(s.getEndPrice()!=null&&s.getEndPrice1()!=null){
    		s.setRate(Float.parseFloat(s.getEndPrice())/Float.parseFloat(s.getEndPrice1())-1);
    	}
    	if(s.getEndPrice1()!=null&&s.getEndPrice2()!=null){
    		s.setRate1(Float.parseFloat(s.getEndPrice1())/Float.parseFloat(s.getEndPrice2())-1);
    	}
    	if(s.getEndPrice2()!=null&&s.getEndPrice3()!=null){
    		s.setRate2(Float.parseFloat(s.getEndPrice2())/Float.parseFloat(s.getEndPrice3())-1);
    	}
    	if(s.getEndPrice3()!=null&&s.getEndPrice4()!=null){
    		s.setRate3(Float.parseFloat(s.getEndPrice3())/Float.parseFloat(s.getEndPrice4())-1);
    	}
    }
    
	
    /**
     * 获取多方数据 
     */
    public static void getData(){
    	List<Stock> l=fetchToday();
    	l=filter(filter1, l);
    	FetchUtil.print(l);
    	save(l);
    	
    }
    
    /**
     * 保存:如果是晚上6点以前，就是头一天的多方数据，如果是晚上6点以后，就是今天的多方数据
     * @param l
     */
    public static void save(List<Stock> l){  	
    	Date dd=new Date();
    	if(dd.getHours()<18){
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, -1);
    		dd=c.getTime();
    	}
    	String d=FetchUtil.FILE_FORMAT.format(dd);
    	List<String> li=new ArrayList<String>();
    	
    	List<String> rq=new ArrayList<String>();
    	for(Stock s:l){
    		if(!s.isRongquan()){
    			li.add(s.getSymbol());
    		}else{
    			rq.add(s.getSymbol());
    		}
    	}
    	System.out.println("反转模型排除融券股票"+(l.size()-li.size())+"只");
        System.out.println(rq);
        
    	FetchUtil.save(FetchUtil.FILE_DUO, d, li);
        
        FetchUtil.save(FetchUtil.FILE_FILT_DUO_1, d, li);
        FetchUtil.save(FetchUtil.FILE_FILT_DUO_2, d, li);
        FetchUtil.save(FetchUtil.FILE_FILT_DUO_3, d, li);
        FetchUtil.save(FetchUtil.FILE_FILT_DUO_4, d, li);
        FetchUtil.save(FetchUtil.FILE_FILT_DUO, d, li);
	}
    
    
    /**
     * 过模型
     * @param f
     * @param list
     * @return
     */
    public static List<Stock> filter(Filter f,List<Stock> list){
    	List<Stock> result=new ArrayList<Stock>();
    	for(Stock s:list){
    		if(f.filter(s)){
    			result.add(s);
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
			/**
			 * 不能买ST
			 */
			if(s.getName().contains("ST")){
				return false;
			}
			/**
			 * 过滤创业板
			 */
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			/**
			 * 阶段上升趋势，比较安全
			 * 
			 */
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
						if(s.getTwo()>0){
							return true;
						}
						if(s.getTwo()<0&&Math.abs(s.getTwo())<0.03F){
							return true;
						}
					}
				}else{
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
}

