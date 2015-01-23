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
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.filter2.Filter;
import com.taobao.finance.util.FetchUtil;

public class Fetch_M1_D2 {
	public static String urlShort = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27xXIv7u5O5YUCEb9y%27%5D/StatisticsService.getShortList?num=3000&sort=_3changes&asc=0&node=adr_hk&page=1";
	public static String urlLong = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27h$ovBT3ZMORCHEo8%27%5D/StatisticsService.getLongList?num=3000&sort=_10changes&asc=1&node=adr_hk&page=1";
	public static Filter normalFilter=new Fetch_M1_D2.NormalFilter();
	public static Filter holdingDoubleFilter=new Fetch_M1_D2.HoldingDoubleFilter();
	public static Filter holdingTripleFilter=new Fetch_M1_D2.HoldingTripleFilter();
	public static Filter holdingCFilter=new Fetch_M1_D2.HoldingConvertFilter();
	public static Filter holdingC2Filter=new Fetch_M1_D2.HoldingConvert2Filter();
	public static Filter holdingStrictC2Filter=new Fetch_M1_D2.StrictHoldingConvert2Filter();
	public static Map<String,Stock> map=new HashMap<String,Stock>();
	
	public static List<String> rongQuan=new ArrayList<String>();
	public static List<String> holding=new ArrayList<String>();
	static{
		rongQuan=FetchUtil.readFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
		holding=FetchUtil.readFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"\\holding.txt");
	}
	public static List<Stock> l=new ArrayList<Stock>();
	static{
		Calendar c=Calendar.getInstance();
		Date dd=c.getTime();
		String dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(dd)+"\\"+FetchUtil.FILE_DUO_X;
		File f=new File(dir);
		if(f.exists()){
			map=FetchUtil.readFileListAbsoluteX(dir);
		}else{
			if(dd.getHours()>16){
				getData();
			}else{
				c.add(Calendar.DATE, -1);
				dd=c.getTime();
				dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(dd)+"\\"+FetchUtil.FILE_DUO_X;
				f=new File(dir);
				if(f.exists()){
					map=FetchUtil.readFileListAbsoluteX(dir);
				}
			}
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
    public static void getData(){
    	if(l.size()==0){
    		l=fetchToday();
    		int i=1;
        	for(Stock s:l){
        		System.out.println("处理："+i+"/"+l.size());
        		String symbol=s.getSymbol();
        		Stock st=Fetch_SingleStock.fetch(symbol);
        		s.setStartPrice(st.getStartPrice());
        		s.setEndPrice(st.getEndPrice());
        		
        		List<Stock> list=Fetch_StockHistory.fetch(symbol);
        		s.setStartPrice1(list.get(0).getStartPrice());
        		s.setEndPrice1(list.get(0).getEndPrice());
        		s.setStartPrice2(list.get(1).getStartPrice());
        		s.setEndPrice2(list.get(1).getEndPrice());
        		s.setStartPrice3(list.get(2).getStartPrice());
        		s.setEndPrice3(list.get(2).getEndPrice());
        		s.setStartPrice4(list.get(3).getStartPrice());
        		s.setEndPrice4(list.get(3).getEndPrice());
        		setRate(s);
        		i++;
        	}
        	Date dd=new Date();
        	if(dd.getHours()<18){
        		Calendar c=Calendar.getInstance();
        		c.add(Calendar.DATE, -1);
        		dd=c.getTime();
        	}
        	String d=FetchUtil.FILE_FORMAT.format(dd);
        	List<String> save=new ArrayList<String>();
        	for(Stock st:l){
        		save.add(st.toStringX());
        	}
        	FetchUtil.save(FetchUtil.FILE_DUO_X, d, save);
        	
        	print(l);
    	}else{
    		print(l);
    	}
    	
    	
        
    	
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
     * 保存:如果是晚上6点以前，就是头一天的多方数据，如果是晚上6点以后，就是今天的多方数据
     * @param l
     */
    public static void print(List<Stock> l){  	
    	List<Stock> r=new ArrayList<Stock>();
    	
    	System.out.println("普通反转");
    	r=filter(normalFilter, l);
    	FetchUtil.print(r);
    	
    	
    	System.out.println("两连跌");
    	r=filter(holdingDoubleFilter, l);
    	FetchUtil.print(r);
    	
    	
    	System.out.println("三连跌");
    	r=filter(holdingTripleFilter, l);
    	FetchUtil.print(r);
    	
    	System.out.println("二跌一转");
    	r=filter(holdingCFilter, l);
    	FetchUtil.print(r);
    	
    	System.out.println("二跌二转");
    	r=filter(holdingStrictC2Filter, l);
    	FetchUtil.print(r);
    	
    	System.out.println("一跌二转");
    	r=filter(holdingC2Filter, l);
    	FetchUtil.print(r);
    	
    	
    	List<String> li=new ArrayList<String>();
    	
    	List<String> rq=new ArrayList<String>();
    	for(Stock s:r){
    		if(!s.isRongquan()){
    			li.add(s.toStringX());
    		}else{
    			rq.add(s.getSymbol());
    		}
    	}
    	System.out.println("反转模型排除融券股票"+(r.size()-li.size())+"只");
        System.out.println(rq);
                
        
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
	

   
	
	

	/**
	 * 普通模型
	 * @author songhong.ljy
	 */
	static class NormalFilter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			
			if(s.getMonth()>0&&s.getTwoMonth()>0&&s.getMonth()<s.getTwoMonth()){
				if(s.getRate()==null){
					return false;
				}
				if(s.getRate1()==null){
					return false;
				}
				if(s.getRate2()==null){
					return false;
				}
				if(s.getRate1()<0){
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>0){
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
							Float.parseFloat(s.getStartPrice())>0){
						return true;
						//s.get_10changes();
					}
					
				}
			}
			return false;
		}
	}
	
	/**
	 * 两连跌
	 * @author songhong.ljy
	 */
	static class HoldingDoubleFilter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			if(!holding.contains(s.getSymbol())){
				return false;
			}
			if(s.getMonth()>0&&s.getTwoMonth()>0){
				if(s.getRate1()<0){
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())<=0){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	
	/**
	 * 三连跌
	 * @author songhong.ljy
	 */
	static class HoldingTripleFilter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			if(!holding.contains(s.getSymbol())){
				return false;
			}
			if(s.getMonth()>0&&s.getTwoMonth()>0){
				if(s.getRate1()<0){
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())<=0){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	/**
	 * 两连跌，反转
	 */
	static class HoldingConvertFilter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			if(!holding.contains(s.getSymbol())){
				return false;
			}
			
			if(s.getMonth()>0&&s.getTwoMonth()>0){
				if(s.getRate1()<0){
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice())-
							Float.parseFloat(s.getStartPrice())>=0){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	/**
	 * 两日下跌，两日反弹
	 */
	static class StrictHoldingConvert2Filter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			if(!holding.contains(s.getSymbol())){
				return false;
			}
			
			Float month=s.getMonth();
			Float twoMonth=s.getTwoMonth();
			if(s.getMonth()>0&&s.getTwoMonth()>0){
				if(s.getRate2()<0){
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice3())-
							Float.parseFloat(s.getStartPrice3())>0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())>=0){
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())>0){
							return true;
						}
					}
				 }
			}
			return false;
		}
	}
	
	/**
	 *  一日下跌，两日反弹 
	 */
	static class HoldingConvert2Filter implements Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			if(!holding.contains(s.getSymbol())){
				return false;
			}
			
			Float month=s.getMonth();
			Float twoMonth=s.getTwoMonth();
			if(s.getMonth()>0&&s.getTwoMonth()>0){
					if(Float.parseFloat(s.getEndPrice2())-
							Float.parseFloat(s.getStartPrice2())>=0){
						return false;
					}
					if(Float.parseFloat(s.getEndPrice1())-
							Float.parseFloat(s.getStartPrice1())>=0){
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())>0){
							return true;
						}
					}
			}
			return false;
		}
	}
}

