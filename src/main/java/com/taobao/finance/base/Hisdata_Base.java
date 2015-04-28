package com.taobao.finance.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;


/**
 * @author Administrator
 */
@Component
public class Hisdata_Base {
	private static final Logger logger = Logger.getLogger("fileLogger");
	public static String FILE_STOCK_HIS_BASE = FetchUtil.FILE_STOCK_HISTORY_BASE;
	public static String FILE_STOCK_TEMP_BASE = FetchUtil.FILE_STOCK_TMP_BASE;
	public static Boolean INCLUDE_TODAY=true;
	public static AtomicInteger fetched=new AtomicInteger(0);
	public static AtomicInteger unformalFetched=new AtomicInteger(0);
	
	@Autowired
	private Store store;
	
	@Autowired
	private ThreadService threadService;
	
	public static void save(String code,List<Stock> list){
		String url=FILE_STOCK_HIS_BASE+code+".txt";
		//String url="E:\\stock\\history\\"+code+".txt";
		File f=new File(url);
		if(!f.exists()){
			createFile(url);
		}
		Date d=getLastDate(f);
		appendData(f, list, d);
		
	}
	
	public static void saveTmp(String code,Stock s){
		String url=FILE_STOCK_TEMP_BASE+code+".txt";
		File f=new File(url);
		if(!f.exists()){
			createFile(url);
		}
		updateTempData(f, s);
		
	}
	
	public static void appendData(File f,List<Stock> l,Date d){
		BufferedWriter bw=null;
		try {
			bw = new BufferedWriter(new FileWriter(f,true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 List<Stock> filter=new ArrayList<Stock>();
        if(d!=null){ 
		  for(Stock s:l){
			  if(s.getDate().after(d)){
				  filter.add(s);
			  }
		  }
		}else{
			filter=l;
		}
        for(Stock s:filter){
        	String line=s.toHistoryString();
        	line=line.replace(",", "");
        	try {
				bw.append(line+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void updateTempData(File f,Stock s){
		BufferedWriter bw=null;
		try {
			bw = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        	String line=s.toTmpString();
        	try {
				bw.append(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
        
        try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Date getLastDate(File f){
		Date d=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			String lastLine=null;
			line = br.readLine();
			
			while (line != null) {
				lastLine=line;
				line = br.readLine();
			}
			if(lastLine!=null){
				DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
				String [] ss=lastLine.split(" ");
				try {
					String date=ss[0].replace("-", ".");
					d=df.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				return null;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static boolean saveAbsolute(String file, List<String> content) {
		File f = new File(file);
		if(f.exists()){
			f.delete();
		}else{
			createFile(file);
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String line : content) {
				bw.write(line + "\r\n");
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static void createFile(String file){
	    String dir="";
		if(file.contains("/")){
			String [] ids=file.split("/");
			for(int i=0;i<ids.length-1;i++){
				dir=dir+"/"+ids[i];
			}
		}else{
			String [] ids=StringUtils.split(file,"\\");
			for(int i=0;i<ids.length-1;i++){
				if(i==ids.length-2){
					dir=dir+ids[i];
				}else{
					dir=dir+ids[i]+"\\";
				}
			}
			
		}
		File f=new File(dir);
		if(!f.exists()){
			f.mkdirs();
			f.canExecute();
		}
		f=new File(file);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Stock> readHisData(String code,Date d) {
		List<Stock> l=new ArrayList<Stock>();
		
		String url=FILE_STOCK_HIS_BASE+code+".txt";
		File f=new File(url);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			if(d==null){
				while (line != null) {
					Stock s=parseHisData(code, line);
					line = br.readLine();
					l.add(s);
				}
			}else{
				while (line != null) {
					Stock s=parseHisData(code, line);
					if(!s.getDate().before(d)){
						l.add(s);
					}
					line = br.readLine();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static void setLocalAnasysIncludeToday(Boolean include){
		INCLUDE_TODAY=include;
	}
	public static List<Stock> readHisDataMerge(String code,Date d) {
		
		String url=FILE_STOCK_TEMP_BASE+code+".txt";
		List<Stock> ll=readHisData(code, d);
		/*if(ll.size()==0){
			return ll;
		}
		*/
		if(!INCLUDE_TODAY){
			return ll;
		}
		if(ll.size()==0){
			return ll;
		}
		Stock s=ll.get(ll.size()-1);
		
		File f=new File(url);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
            if(line != null) {
			   Stock st=parseTmpData(code, line);
			   if(!(st.getStartPriceFloat().equals(s.getStartPriceFloat())
					   && st.getEndPriceFloat().equals(s.getEndPriceFloat())
					   && st.getHighPriceFloat().equals(s.getHighPriceFloat())
					   && st.getLowPriceFloat().equals(s.getLowPriceFloat()))){
				   Date dd=s.getDate();
				   Calendar c=Calendar.getInstance();
				   c.setTime(dd);
				   c.add(Calendar.DATE, 1);
//				   st.setDate(c.getTime());
				   ll.add(st);
			   }
            }
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ll;
	}
	
	public static Stock parseHisData(String code,String data){
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		Stock s=new Stock();
		s.setSymbol(code);
		String[] ss=data.split(" ");
		String date=ss[0].replace("-", ".");
		try {
			s.setDate(format.parse(date));
		} catch (Exception e) {
			System.out.println("------"+date);
			System.out.println("------"+code);
			System.out.println(data);
			System.out.println(ss[0].replace("-", "."));
			e.printStackTrace();
		}
		s.setStartPrice(ss[1]);
		s.setHighPrice(ss[2]);
		s.setLowPrice(ss[3]);
		s.setEndPrice(ss[4]);
		s.setTradeNum(Long.parseLong(ss[5]));
		return s;
	}
	
	public static Stock parseTmpData(String code,String data){
		Stock s=new Stock();
		s.setSymbol(code);
		String[] ss=data.split(" ");
		s.setStartPrice(ss[0]);
		s.setHighPrice(ss[1]);
		s.setLowPrice(ss[2]);
		s.setEndPrice(ss[3]);
		s.setTradeNum(Long.parseLong(ss[4]));
		s.setDate(new Date());
		return s;
	}
	
	
	public static List<Stock> getHistoryBase(String code,Date date){
		List<Stock> l=new ArrayList<Stock>();
		
		return l;
	}
	
	public static void getBaseData(){
		Map<String,Stock> allMap=Fetch_AllStock.map;
		int i= 1;		
		Collection<Stock> ss=allMap.values();
		for(Stock s:ss){
    		System.out.println("锟斤拷锟斤拷"+i+"/"+allMap.size());
    		String symbol=s.getSymbol();
    		List<Stock> history=Fetch_StockHistory.fetch3(symbol);
            Collections.reverse(history);
    		save(symbol,history);
    		i++;
		}
		System.out.println("---------锟斤拷锟斤拷锟斤拷史锟斤拷锟斤拷锟斤拷锟�---------");
	}
	
	public static void updateDataHistoryAll(){
		logger.info("寮�濮嬩笅杞藉巻鍙叉暟鎹�");
		Fetch_AllStock.getData();
		Map<String,Stock> allMap=Fetch_AllStock.map;
		updateDataHistoryData(allMap,false);
		logger.info("涓嬭浇鍘嗗彶鏁版嵁缁撴潫");
	}

	public static void updateDataHistoryDelta(){
		Map<String,Stock> allMap=Fetch_AllStock.map;
		Map<String,Stock> newMap=new HashMap<String,Stock>();
		Map<String,Stock> map=new HashMap<String,Stock>();
		File f=new File(FetchUtil.FILE_STOCK_HISTORY_BASE);
		String files[]=f.list();
		for(String file:files){
			file=file.replace(".txt", "");
			map.put(file, null);
		}
		for(String s:allMap.keySet()){
			if(!map.containsKey(s)){
				Stock ss=new Stock();
				ss.setSymbol(s);
				newMap.put(s, ss);
			}
		}
		Stock ss=new Stock();
		ss.setSymbol("sh000001");
		ss.setName("涓婅瘉鎸囨暟");
		newMap.put(ss.getSymbol(), ss);
		
		
		updateDataHistoryData(newMap,true);
	}
	

	public static void updateDataHistoryData(Map<String,Stock> map,boolean longTime){
		ExecutorService service = Executors.newFixedThreadPool(16);
		CompletionService<Object> con = new ExecutorCompletionService<Object>(service);
			
		
		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			HisDataTask t=new HisDataTask(sys,false);
			con.submit(t);
		}	

		int i = 1;
		while (i <= 16) {
			try {
				con.take().get();
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		service.shutdown();
	}
	
	
	
	
	static class HisDataTask implements Callable<Object>{
		private List<Object> list;
		private boolean longTime;
		public HisDataTask(List<Object> list,boolean longTime){
			this.list=list;
			this.longTime=longTime;
		}
		
		public Object call(){
			for(Object s:list){
	    		String symbol=(String)s;
	    		if(symbol.contains("300019")){
	    			symbol.length();
	    		}
	    		List<Stock> history=null;
	    		if(longTime){
	    			history=Fetch_StockHistory.fetch3(symbol);
	    		}else{
	    			history=Fetch_StockHistory.fetch(symbol);
	    		}
	    		if(history==null){
	              continue;    			
	    		}
	            Collections.reverse(history);
	    		save(symbol,history);
	    		System.out.println("======================="+fetched.getAndIncrement());
			}
			return 1;
		}
	}
	
	static class UnformalDataTask implements Callable<Object>{
		private List<Object> list;
		public UnformalDataTask(List<Object> list){
			this.list=list;
		}
		
		public Integer call(){
			for(Object o:list){
				String s=(String)o;
				if(s.contains("600060")){
					s.length();
				}
				Stock today = Fetch_SingleStock.fetch(s);
	    		if(today==null){
	    			continue;
	    		}
	    		saveTmp(s,today);
	    		System.out.println("======================="+unformalFetched.getAndIncrement());
			}
			return 1;
		}
	}
	
    
	public static void updateDataHistoryDataUnFormal(){
		logger.info("寮�濮嬩笅杞介潪姝ｅ紡鏁版嵁");
		Fetch_AllStock.getData();
		ExecutorService service = Executors.newFixedThreadPool(16);
		CompletionService<Object> con = new ExecutorCompletionService<Object>(service);
			

		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			Callable<Object> t=new UnformalDataTask(sys);
			con.submit(t);
		}	
		
		int i = 1;
		while (i <= 16) {
			try {
				con.take().get();
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		service.shutdown();
		logger.info("涓嬭浇闈炴寮忔暟鎹粨鏉�");
	}
	
	
	
	
	public static void main(String args[]){
		
	//	updateDataHistoryData();
		
		/*List<Stock> l=readHisDataMerge("sz000001", null);
		l.size();*/
		try {
			System.out.println(FetchUtil.FILE_FORMAT.parse("2014.07.16"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
