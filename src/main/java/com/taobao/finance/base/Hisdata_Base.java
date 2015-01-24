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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.util.FetchUtil;

/**
 * @author Administrator
 */
public class Hisdata_Base {

	public static String FILE_STOCK_HIS_BASE = "E:\\stock\\history\\";
	public static String FILE_STOCK_TEMP_BASE = "E:\\stock\\tmp\\";
	public static Boolean INCLUDE_TODAY=true;
	public static AtomicInteger fetched=new AtomicInteger(0);
	public static void save(String code,List<Stock> list){
		String url=FILE_STOCK_HIS_BASE+code+".txt";
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
				String [] ss=lastLine.split(" ");
				try {
					String date=ss[0].replace("-", ".");
					d=FetchUtil.FILE_FORMAT.parse(date);
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
		if(ll.size()==0){
			return ll;
		}
		
		if(!INCLUDE_TODAY){
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
    		System.out.println("����"+i+"/"+allMap.size());
    		String symbol=s.getSymbol();
    		List<Stock> history=Fetch_StockHistory.fetch3(symbol);
            Collections.reverse(history);
    		save(symbol,history);
    		i++;
		}
		System.out.println("---------������ʷ�������---------");
	}
	
	/**
	 * ��������
	 */
	public static void updateDataHistoryData(){
		Map<String,Stock> allMap=Fetch_AllStock.map;
		List<List<Stock>> r=divide(allMap, 16);
		List<Thread> lt=new ArrayList<Thread>();
		for(int i=0;i<r.size();i++){
			Thread t=new HisDataTask(r.get(i));
			t.start();
			lt.add(t);
		}
		for(Thread t:lt){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	static class HisDataTask extends Thread{
		private List<Stock> list;
		public HisDataTask(List<Stock> list){
			this.list=list;
		}
		
		public void run(){
			for(Stock s:list){
	    		String symbol=s.getSymbol();
	    		if(symbol.contains("sh000001")){
	    			symbol.length();
	    		}
	    		List<Stock> history=Fetch_StockHistory.fetch(symbol);
	    		if(history==null){
	              continue;    			
	    		}
	            Collections.reverse(history);
	    		save(symbol,history);
	    		System.out.println("======================="+fetched.getAndIncrement());
			}
		}
	}
	
	static class UnformalDataTask extends Thread{
		private List<Stock> list;
		public UnformalDataTask(List<Stock> list){
			this.list=list;
		}
		
		public void run(){
			for(Stock s:list){
				if(s.getSymbol().contains("600060")){
					s.getSymbol();
				}
				Stock today = Fetch_SingleStock.fetch(s.getSymbol());
	    		if(today==null){
	    			continue;
	    		}
	    		saveTmp(s.getSymbol(),today);
	    		System.out.println("======================="+fetched.getAndIncrement());
			}
		}
	}
	
    public static List<List<Stock>> divide(Map<String,Stock> allMap,int num){
    	List<List<Stock>> r=new ArrayList<List<Stock>>();
    	List<Stock> all=new ArrayList<Stock>();
    	all.addAll(allMap.values());
    	Collections.sort(all,new Comparator<Stock>(){
    		public int compare(Stock s1,Stock s2){
    			return (s1.getSymbol().compareTo(s2.getSymbol()));
    		}
    	});
    	int size=0;
    	size=allMap.size()/num;
    	List<Stock> slice=new ArrayList<Stock>();
    	for(int i=0;i<all.size();i++){
    		if(slice.size()<size){
    			slice.add(all.get(i));
    		}else {
    			slice=new ArrayList<Stock>();
    			slice.add(all.get(i));
    		}
    		if(slice.size()==size||i==all.size()-1){
    			r.add(slice);
    		}
    	}
    	return r;
    	
    }
	public static void updateDataHistoryDataUnFormal(){
		Map<String,Stock> allMap=Fetch_AllStock.map;
		List<List<Stock>> r=divide(allMap, 30);
		List<Thread> lt=new ArrayList<Thread>();
		for(List<Stock> l:r){
			Thread t=new UnformalDataTask(l);
    		t.start();
    		lt.add(t);
		}
		/*for(Thread t:lt){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
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
