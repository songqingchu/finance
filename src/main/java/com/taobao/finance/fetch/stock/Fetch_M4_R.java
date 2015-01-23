package com.taobao.finance.fetch.stock;

import java.io.File;
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
import com.taobao.finance.fetch.impl.Fetch_CheckWorkDay;
import com.taobao.finance.fetch.impl.Fetch_M2_StockHoldingByTopFund;
import com.taobao.finance.filter2.Filter;
import com.taobao.finance.util.FetchUtil;

public class Fetch_M4_R {

public static String urlShort = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27xXIv7u5O5YUCEb9y%27%5D/StatisticsService.getShortList?num=3000&sort=_3changes&asc=0&node=adr_hk&page=1";
public static String urlLong = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27h$ovBT3ZMORCHEo8%27%5D/StatisticsService.getLongList?num=3000&sort=_10changes&asc=1&node=adr_hk&page=1";
public static Filter rongQuanFilter=new Fetch_M4_R.RongQuanFilter();
public static Map<String,Stock> map=new HashMap<String,Stock>();
public static List<String> rongQuan=new ArrayList<String>();

static{
	rongQuan=FetchUtil.readFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
	boolean isWorkDay=Fetch_CheckWorkDay.checkWorkDay();
	String dFile=null;
	String dir = "";
	if(isWorkDay){
		Date d = new Date();
		dir = FetchUtil.FILE_STOCK_ANASYS_BASE + FetchUtil.FILE_FORMAT.format(d);
		File f = new File(dir);
		if (!f.exists()) {
			dir="";
		}
	}else{
		Date d = new Date();
		boolean exists=false;
		for (int i = -1; i > -10; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_ANASYS_BASE + FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			if (f.exists()) {
				exists=true;
				break;
			}
		}
		if(exists==false){
			dir="";
		}
	}
    if(dir!=""){
    	map=FetchUtil.readFileMapAbsoluteForHolding(dir+"\\"+FetchUtil.FILE_KONG);
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
	for(Stock stock:values){
		a.add(stock);
	}
	Collections.sort(a);
	return a;
}


/**
 * 获取多方数据 
 */
public static List<Stock> getData(boolean refresh){
	/*List<Stock> li=new ArrayList<Stock>();
	if(map.size()!=0 && (refresh==false)){
		li.addAll(map.values());
	}else{
		map.clear();
		li=fetchToday();
    	li=filter(filter1, li);
    	save(li);
	}
	Map<String,Stock> allMap=Fetch_M2_StockHoldingByTopFund.allMap;
	Map<String,Stock> allName=Fetch_AllStock.map;
	for(Stock s:li){
		if(rongQuan.contains(s.getSymbol())){
			s.setRongquan(true);
		}
		if(allMap.containsKey(s.getSymbol())){
			Stock st=allMap.get(s.getSymbol());
			if(st==null){
				continue;
			}
			s.setHoldByFound(st.getHoldByFound());
		}
		if(allName.containsKey(s.getSymbol())){
			Stock st=allName.get(s.getSymbol());
			if(st==null){
				continue;
			}
		    s.setName(st.getName());
		}
		map.put(s.getSymbol(), s);
	}
	return li; */
	
	
	Map<String,Stock> m=new HashMap<String,Stock>();
	m.putAll(Fetch_BaseData.map);
	List<Stock> l=new ArrayList<Stock>();
	l.addAll(m.values());
	l=filter(rongQuanFilter, l);
	return l;
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
	
	for(Stock s:l){
		li.add(s.getSymbol());
	}
    
	FetchUtil.save(FetchUtil.FILE_DUO, d, li);
    
    FetchUtil.save(FetchUtil.FILE_FILT_KONG_1, d, li);
    FetchUtil.save(FetchUtil.FILE_FILT_KONG_2, d, li);
    FetchUtil.save(FetchUtil.FILE_FILT_KONG_3, d, li);
    FetchUtil.save(FetchUtil.FILE_FILT_KONG_4, d, li);
    FetchUtil.save(FetchUtil.FILE_FILT_KONG, d, li);
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
	getData(true);
}


static class RongQuanFilter implements Filter{
	@Override
	public boolean filter(Stock s) {

		/**
		 * 过滤创业板
		 */
		if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
			return false;
		}
		
		if(!s.isRongquan()){
			return false;
		}
		
			
			if(s.getTwoMonth()!=null){
				if(s.getMonth()<0&&s.getTwoMonth()<0&&s.getMonth()>s.getTwoMonth()){
					if(s.getRate()==null){
						return false;
					}
					if(s.getRate1()==null){
						return false;
					}
					if(s.getRate2()==null){
						return false;
					}
					if(s.getRate1()>0){
						if(Float.parseFloat(s.getEndPrice1())-
								Float.parseFloat(s.getStartPrice1())<0){
							return false;
						}
						if(Float.parseFloat(s.getEndPrice2())-
								Float.parseFloat(s.getStartPrice2())<0){
							return false;
						}
						if(s.getSymbol().equals("sh600186")){
							s.get_10changes();
						}
						/*if(s.getRate()>=0){
							if(s.getRate()>0.05F){
								//return false;
								s.get_10changes();
							}else{
								return true;
								//s.get_10changes();
							}
						}else{
							if(1-Float.parseFloat(s.getEndPrice())/Float.parseFloat(s.getStartPrice())<0.05F){
								//return true;
								s.get_10changes();
							}
						}*/
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())<0){
							return true;
							//s.get_10changes();
						}
						
					}
				}
			}else{
				if(s.getMonth()>0){
					if(s.getRate()==null){
						return false;
					}
					if(s.getRate1()==null){
						return false;
					}
					if(s.getRate2()==null){
						return false;
					}
					if(s.getRate1()>0){
						if(Float.parseFloat(s.getEndPrice1())-
								Float.parseFloat(s.getStartPrice1())<0){
							return false;
						}
						if(Float.parseFloat(s.getEndPrice2())-
								Float.parseFloat(s.getStartPrice2())<0){
							return false;
						}
						if(s.getSymbol().equals("sh600186")){
							s.get_10changes();
						}
						/*if(s.getRate()>=0){
							if(s.getRate()>0.05F){
								//return false;
								s.get_10changes();
							}else{
								return true;
								//s.get_10changes();
							}
						}else{
							if(1-Float.parseFloat(s.getEndPrice())/Float.parseFloat(s.getStartPrice())<0.05F){
								//return true;
								s.get_10changes();
							}
						}*/
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())<0){
							return true;
							//s.get_10changes();
						}
						
					}
				}
			}
			
			return false;
		}
}
}