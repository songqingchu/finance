package com.taobao.finance.fetch.stock;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_CheckIncludeRecent;
import com.taobao.finance.fetch.impl.Fetch_GetLastWorkDay;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.util.FetchUtil;



/**
 * ��ȡÿ�ջ������ݣ�����ͳ�����ݣ�����ͳ�����ݣ�
 * @author Administrator
 */
@SuppressWarnings("deprecation")
public class Fetch_BaseData {
	public static String urlShort = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27xXIv7u5O5YUCEb9y%27%5D/StatisticsService.getShortList?num=3000&sort=_3changes&asc=0&node=adr_hk&page=1";
	public static String urlLong = "http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/IO.XSRV2.CallbackList%5B%27h$ovBT3ZMORCHEo8%27%5D/StatisticsService.getLongList?num=3000&sort=_10changes&asc=1&node=adr_hk&page=1";
	public static Map<String,Stock> map=new HashMap<String,Stock>();	
	public static List<String> rongQuan=new ArrayList<String>();
	public static Map<String,Stock> holding=new HashMap<String,Stock>();
	public static List<Stock> l=new ArrayList<Stock>();
	

	static{
		rongQuan=FetchUtil.readFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_RONGQUAN);
		holding=FetchUtil.readFileMapAbsoluteForHolding(FetchUtil.FILE_STOCK_ANASYS_BASE+"\\holding.txt");

		Calendar c=Calendar.getInstance();
		Date dd=c.getTime();
		String dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(dd)+"\\"+FetchUtil.FILE_DUO_X;
		File f=new File(dir);
		/**
		 * ���ȼ��ص����ļ�
		 */
		if(f.exists()){
			map=FetchUtil.readFileListAbsoluteX(dir);
		}else{
			for (int i = -1; i > -10; i--) {
				  c = Calendar.getInstance();
				  c.add(Calendar.DATE, i);
				  dd=c.getTime();
				  dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(dd)+"\\"+FetchUtil.FILE_DUO_X;
				  f=new File(dir);
				  if(f.exists()){
				     map=FetchUtil.readFileListAbsoluteX(dir);
				     break;
				  }
			}
		}
	}

	/**
	 * ץȡ����
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
     * ����ͳ�����ݺͳ���ͳ��������join
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
			stock.setStatis(true);
			map.put(stock.getSymbol(), stock);
			if(rongQuan.contains(stock.getSymbol())){
				stock.setRongquan(true);
			}else{
				stock.setRongquan(false);
			}
		}
		for(Stock stock:longStock){
			Stock stock2=map.get(stock.getSymbol());
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
     * ��ȡ��������
     */
    public static Map<String,Stock> getData(boolean refresh){
    	Date dd=Fetch_GetLastWorkDay.getLastWorkDay();
    	Map<String,Stock> rMap=new HashMap<String,Stock>();
    	if(map.size()!=0&&refresh==false){
        	return map;
    	}else{
    		map.clear();
    		l=fetchToday();
    		int i=1;
    		boolean includeRecent=false;
      		includeRecent=checkIncludeRecentDay();	
      		
      		Map<String,Stock> allMap=Fetch_AllStock.map;
      		for(String s:allMap.keySet()){
      			if(!map.containsKey(s)){
      				l.add(allMap.get(s));
      				allMap.get(s).setStatis(false);
      			}
      		}
      		
        	for(Stock s:l){
        		System.out.println("����"+i+"/"+l.size());
        		String symbol=s.getSymbol();
        		
        		
        		if(rongQuan.contains(s.getSymbol())){
        			s.setRongquan(true);
        		}else{
        			s.setRongquan(false);
        		}
        		
        		if(holding.keySet().contains(s.getSymbol())){
        			s.setHoldByFound(holding.get(s.getSymbol()).getHoldByFound());
        		}
        		
        		List<Stock> history=Fetch_StockHistory.fetch(symbol);
        		try{
        		if(includeRecent){
            		s.setStartPrice(history.get(0).getStartPrice());
            		s.setEndPrice(history.get(0).getEndPrice());
            		s.setStartPrice1(history.get(1).getStartPrice());
            		s.setEndPrice1(history.get(1).getEndPrice());
            		s.setStartPrice2(history.get(2).getStartPrice());
            		s.setEndPrice2(history.get(2).getEndPrice());
            		s.setStartPrice3(history.get(3).getStartPrice());
            		s.setEndPrice3(history.get(3).getEndPrice());
            		s.setStartPrice4(history.get(4).getStartPrice());
            		s.setEndPrice4(history.get(4).getEndPrice());
            		
        		}else{
        			Stock st=Fetch_SingleStock.fetch(symbol);
            		s.setStartPrice(st.getStartPrice());
            		s.setEndPrice(st.getEndPrice());
            		
            		s.setStartPrice1(history.get(0).getStartPrice());
            		s.setEndPrice1(history.get(0).getEndPrice());
            		s.setStartPrice2(history.get(1).getStartPrice());
            		s.setEndPrice2(history.get(1).getEndPrice());
            		s.setStartPrice3(history.get(2).getStartPrice());
            		s.setEndPrice3(history.get(2).getEndPrice());
            		s.setStartPrice4(history.get(3).getStartPrice());
            		s.setEndPrice4(history.get(3).getEndPrice());
        		}
        		if(!s.getStatis()){
        			s.setStartPrice30(history.get(25).getStartPrice());
            		s.setEndPrice30(history.get(25).getEndPrice());
        		}
        		}catch(Exception e){
        			continue;
        		}
        		setRate(s);
        		if(s.getSymbol()=="sz002217"){
        			s.hashCode();
        		}
        		/*String code=s.getSymbol().replace("sh", "");
    			code=code.replace("sz", "");
    			try {
					String info=Fetch_StockInfo.filter(code,dd);
					if(info!=null&&info!=""){
						s.setInfo(info);
						s.setChongzhu(true);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}*/
				
        		if(s.isHarden()){
        			setHardenTimes(s,history);
        			Stock today=Fetch_SingleStock.fetch(s.getSymbol());
        			
        			if((Float.parseFloat(today.getEndPrice())-Float.parseFloat(today.getStartPrice()))/Float.parseFloat(today.getStartPrice())<0.01F){
        				s.setStrictHarden(true);
        			}else{
        				s.setStrictHarden(false);
        			}
        		}
        		i++;
        		map.put(s.getSymbol(), s);
        	}
        	save(l);
    	}
    	return rMap;
    }
    
    
    /**
     * ������������
     * @param s
     * @param history
     */
    public static void setHardenTimes(Stock s,List<Stock> history){
    	Boolean includeRecent=Fetch_CheckIncludeRecent.checkIncludeRecent();
		setRate(history);
		if(!includeRecent){
		  history.remove(history.size()-1);
		  history.add(0,s);
		}
		int count=getContinusHardon(history);
		s.setHardenTimes(count);
	}
    
	/**
	 * ������ͣ����
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
	 * �����Ƿ�
	 * @param l
	 */
	public static void setRate(List<Stock> l){
		for(int i=0;i<l.size()-1;i++){
			Stock a=l.get(i);
			Stock b=l.get(i+1);
			a.setRate(Float.parseFloat(a.getEndPrice())/Float.parseFloat(b.getEndPrice())-1);
		}
	}
    
    public static void save(List<Stock> l){
    	Date dd=Fetch_GetLastWorkDay.getLastWorkDay();
    	/*if(dd.getHours()<18){
    		Calendar c=Calendar.getInstance();
    		c.add(Calendar.DATE, -1);
    		dd=c.getTime();
    	}*/
    	String d=FetchUtil.FILE_FORMAT.format(dd);
    	List<String> save=new ArrayList<String>();
    	for(Stock st:l){
    		
    		save.add(st.toStringX());
    	}
    	FetchUtil.save(FetchUtil.FILE_DUO_X, d, save);
    }
    
    
    public static String getDouble(String s){
    	if(s==null){
    		return null;
    	}else{
    		String lastBit=s.substring(s.length()-1,s.length());
    		String value=s.substring(0,s.length()-1);
    		if(Integer.parseInt(lastBit)>=5){
    			Double d=Double.parseDouble(value)+0.01D;
    			return d.toString();
    		}else{
    			return value;
    		}
    	}
    }
    
    public static boolean checkIncludeRecentDay(Stock sina,Stock compass){
    	String sinaStart=sina.getStartPrice();
    	String sinaHigh=sina.getHighPrice();
    	if(sinaStart.length()==6){
    		sinaStart=sinaStart+"0";
    	}
        if(sinaHigh.length()==6){
    		sinaHigh=sinaHigh+"0";
    	}
    	String compassStart=compass.getStartPrice();
    	String compassHigh=compass.getHighPrice();
    	sinaStart=getDouble(sinaStart);
    	if(sinaStart.length()==6){
    		sinaStart=sinaStart+"0";
    	}
    	sinaStart=sinaStart.substring(0,7);
    	sinaHigh=getDouble(sinaHigh);
    	
        if(sinaHigh.length()==6){
    		sinaHigh=sinaHigh+"0";
    	}
    	sinaHigh=sinaHigh.substring(0,7);
    	compassHigh=compassHigh.replace(",", "");
    	compassStart=compassStart.replace(",", "");
    	if(sinaStart.equals(compassStart)&&sinaHigh.equals(compassHigh)){
    		return true;
    	}
    	return false;
    }
    
    public static boolean checkIncludeRecentDay(){
    	Stock st=Fetch_SingleStock.fetch("sh000001");
    	List<Stock> list=Fetch_StockHistory.fetch("sh000001");
    	
    	return true;
    	//return checkIncludeRecentDay(st, list.get(0));
    }
    
    /**
     * �����ǵ���
     */
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
    	if(s.getEndPrice30()!=null&&s.getEndPrice30()!=null){
    		Float f=Float.parseFloat(s.getEndPrice30())/Float.parseFloat(s.getEndPrice())-1;
    		s.set_30changes(f.toString());
    	}
    }
    
    public static List<Date> check(List<Stock> l){
    	List<Date> l2=new ArrayList<Date>();
    	if(l==null){
    		return l2;
    	}
    	int size=l.size();
    	if(size>0){
    		for(int i=size-1;i>0;i--){
    			Stock today=l.get(i);
    			Stock tomorow=l.get(i-1);
    			Float t1Begin=Float.parseFloat(today.getStartPrice());
    			Float t1Low=Float.parseFloat(today.getLowPrice());
    			Float t1End=Float.parseFloat(today.getEndPrice());
    			
    			Float t2Begin=Float.parseFloat(tomorow.getStartPrice());
    			Float t2Low=Float.parseFloat(tomorow.getLowPrice());
    			Float t2End=Float.parseFloat(tomorow.getEndPrice());
    			
    			Long v1=today.getTradeNum();
    			Long v2=tomorow.getTradeNum();
    			
    			if(t1Begin.equals(t1Low)&&t2Begin.equals(t2Low)){
    				if(t1End>t2Low&&t1End<t2End&&t1Begin<t2Begin){
    					if(v1<v2){
    						l2.add(today.getDate());
    					}
    				}
    			}
    		}
    	}
    	return l2;
    }
  
	public static void main(String args[]){
		//getData(false);
		Map<String,Stock> allMap=Fetch_AllStock.map;
		int i= 1;
		Map<Stock,List<Date>> m=new HashMap<Stock,List<Date>>();
		
		Collection<Stock> ss=allMap.values();
		for(Stock s:ss){
    		System.out.println("����"+i+"/"+l.size());
    		String symbol=s.getSymbol();
    		List<Stock> history=Fetch_StockHistory.fetch(symbol);
    		List<Date> ld=check(history);
    		if(ld.size()>0){
    			m.put(s, ld);
    		}
    		i++;
		}
		Set<Stock> s=m.keySet();
		for(Stock st:s){
			List<Date> ld=m.get(st);
			System.out.print(st.getName()+":");
			for(Date d:ld){
				System.out.print(FetchUtil.FILE_FORMAT.format(d)+"  ");
			}
			System.out.println("");
		}
		
	}
}

