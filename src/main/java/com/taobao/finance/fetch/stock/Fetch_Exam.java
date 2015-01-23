package com.taobao.finance.fetch.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_CheckIncludeRecent;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.util.FetchUtil;



public class Fetch_Exam {
	public static String url = "http://hq.sinajs.cn/list=";
	public static Set<String> store=new HashSet<String>();
	public static Map<String,Stock> mapStore=new HashMap<String,Stock>();
	
	
	public static Map<String, Stock> map = new HashMap<String, Stock>();
	
	public static List<String> fileList=new ArrayList<String>();
	static {
		/*fileList.add(FetchUtil.FILE_NORMAL_CONVERT);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT1);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT2);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT3);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT4);
		
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT1);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT2);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT3);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT4);
		
		fileList.add(FetchUtil.FILE_LOAN_CONVERT);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT1);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT2);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT3);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT4);*/
		
		fileList.add(FetchUtil.FILE_HARDEN);
		fileList.add(FetchUtil.FILE_HARDEN1);
		fileList.add(FetchUtil.FILE_HARDEN2);
		fileList.add(FetchUtil.FILE_HARDEN3);
		fileList.add(FetchUtil.FILE_HARDEN4);
		
		fileList.add(FetchUtil.FILE_HARDEN_YOU);
		fileList.add(FetchUtil.FILE_HARDEN_YOU1);
		fileList.add(FetchUtil.FILE_HARDEN_YOU2);
		fileList.add(FetchUtil.FILE_HARDEN_YOU3);
		fileList.add(FetchUtil.FILE_HARDEN_YOU4);
	}

	
	/**
	 * 读取过去20个开市日期需要跟踪的有效数据
	 */
	public static void getAllSymbol(){
		String dir = "";
		int realCount=0;
		for (int i = -1; i > -100; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_ANASYS_BASE + FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			if (f.exists()) {
				realCount++;
				readOneDay(dir);
			}
			if(realCount>20||i<-50){
				break;
			}
		}
	}
	
	/**
	 * 读取一天需要分析的文件
	 * @param dir
	 */
	public static void readOneDay(String dir){
		List<String> l=null;
		for(String file:fileList){
		  l=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+file);
		  l=filterValid(l);
		  store.addAll(l);
		}
	}
	
	/**
	 * 过滤不需要分析的文件
	 * @param l
	 * @return
	 */
	public static List<String> filterValid(List<String> l){
		List<String> r=new ArrayList<String>();
		for(String s:l){
			if(!s.endsWith("*")){
				r.add(s);
			}
		}
		return r;
	}
	
	
	public static Map<String,Stock> recordAllStock(){
		store.clear();
		getAllSymbol();
		Map<String,Stock> m=new HashMap<String,Stock>();
		for(String s:store){
			String symbol=s.replace("=", "");
			Stock st=Fetch_SingleStock.fetch(symbol);
			m.put(s, st);
		}
		updateAllStock(m);
		return m;
	}
	
	
	/**
	 * update today price
	 * @param m
	 */
	public static void updateAllStock(Map<String,Stock> m){
		String dir = "";
		int realCount=0;
		for (int i = -1; i > -100; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_STATIS_BASE + FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			if (f.exists()) {
				realCount++;
				updateOneDay(dir,m);
			}
			if(realCount>20||i<-50){
				break;
			}
		}
	}
	
	
	/**
	 * update one day file
	 * @param dir
	 */
	public static void updateOneDay(String dir,Map<String,Stock> m){
		List<String> l=null;
		for(String file:fileList){
		  String realFile=dir+"\\"+file;
		  l=FetchUtil.readAllLineFileListAbsolute(realFile);
		  //l=filterValid(l);
		  updateOneFile(realFile,l,m);
		}
	}
	
	/**
	 * update one file
	 * @param file
	 * @param content
	 * @param m
	 */
	public static void updateOneFile(String file,List<String> content,Map<String,Stock> m){
		List<String> save=new ArrayList<String>();
		boolean includeRecent=Fetch_CheckIncludeRecent.checkIncludeRecent();
		
		for(String s:content){
			if(s.endsWith("*")){
				save.add(s);
			}else{
				String[] dd=s.split("\t");
				if(dd.length==1){
					String symbol=dd[0].replace("=", "");
					Stock st=m.get(symbol);
					
					Float realRate=getRealRate(st,includeRecent);
					String sa="";
					if(realRate>0F){
						sa=s+"\t"+FetchUtil.format0Right4((realRate));
					}else{
						sa=s+"\t"+FetchUtil.format0Right4(realRate)+"\t"+"*";
					}
					save.add(sa);
				}else{
					String symbol=dd[0].replace("=", "");
					Stock st=m.get(symbol);
					String sa="";
					if(st.getRate()>=0F){
						sa=s+"\t"+FetchUtil.format0Right4(st.getRate());
					}else{
						sa=s+"\t"+FetchUtil.format0Right4(st.getRate())+"\t*";
					}
					save.add(sa);
				}
			}
		}
		FetchUtil.saveAbsolute(file, save);
	}
	
	public static Float getRealRate(Stock s,boolean includeRecent){
		List<Stock> history=Fetch_StockHistory.fetch(s.getSymbol());
		Float lastEndPrice=null;
		String str=null;
		if(includeRecent){
			str=history.get(1).getEndPrice();
		}else{
			str=history.get(0).getEndPrice();
		}
		lastEndPrice=Float.parseFloat(str);
		Float endPrice=Float.parseFloat(s.getEndPrice());
		Float startPrice=Float.parseFloat(s.getStartPrice());
		Float realRate=(endPrice-startPrice)/lastEndPrice;
		return realRate;
	}
	/**
	 * 过滤不需要分析的文件
	 * @param l
	 * @return
	 */
	public static List<String> filterValid2(List<String> l){
		List<String> r=new ArrayList<String>();
		for(String s:l){
			if(!s.endsWith("*")){
				r.add(s);
			}
		}
		return r;
	}
	

	public static String getUrl(String stock) {
		return url + stock;
	}

	/**
	 * 抓取数据
	 * 
	 * @param url
	 * @return
	 */
	public static Stock fetch(String code) {
		HttpClient client = new HttpClient();
		Stock s = null;
		String newUrl = getUrl(code);
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
				s = FetchUtil.parseTodayStockFromSina(jsonStr, code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}






	public static void main(String args[]) {
		recordAllStock();
	}
}

