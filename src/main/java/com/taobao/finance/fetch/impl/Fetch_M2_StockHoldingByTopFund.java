package com.taobao.finance.fetch.impl;

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
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.finance.dataobject.DailyData;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;


/**
 * 分析股票被基金持仓情况
 * @author songhong.ljy
 */
public class Fetch_M2_StockHoldingByTopFund {
	
	private String allFundsUrl="http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=gp&rs=&gs=0&sc=1nzf&st=desc&sd=2012-08-26&ed=2013-08-26&pi=1&pn=50&v=0.023998898919671774";
    public static Map<String,Stock> map=new HashMap<String,Stock>();
    public static Map<String,Stock> zhuMap=new HashMap<String,Stock>();
    public static Map<String,Stock> zhoMap=new HashMap<String,Stock>();
    public static Map<String,Stock> chuMap=new HashMap<String,Stock>();
    public static Map<String,Stock> allMap=new HashMap<String,Stock>();
    
    
	static{
		File f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE+"stockAll.txt");
		if(f.exists()){
			map=FetchUtil.readFileMapAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"stockAll.txt");
			zhuMap=FetchUtil.readFileMapAbsoluteForHolding(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_ZHU);
			chuMap=FetchUtil.readFileMapAbsoluteForHolding(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_CHU);
			zhoMap=FetchUtil.readFileMapAbsoluteForHolding(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_ZHO);
			allMap=FetchUtil.readFileMapAbsoluteForHolding(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING);
		}
	}
	
	public List<DailyData> fetchAllFunds() {
		List<DailyData> list=new ArrayList<DailyData>();
		HttpClient client = new HttpClient();
	    HttpMethod getMethod = new GetMethod(allFundsUrl);
	    
	    try {
	      client.executeMethod(getMethod);
	      if (getMethod.getStatusCode() == 200) {
	        String jsonStr = getMethod.getResponseBodyAsString();
	        jsonStr = jsonStr.replaceFirst("var rankData = ", "");
	        jsonStr = jsonStr.replaceFirst(";", "");
	        JSONObject jo = JSONObject.parseObject(jsonStr);
	        JSONArray funds = jo.getJSONArray("datas");
	        
	        int size=funds.size();
	        for(int i=0;i<size;i++){
	        	String s=(String)funds.get(i);
	        	if(StringUtils.isNotBlank(s)){
	        		DailyData d=FetchUtil.parse_TiantianFoud_To_DailyData(s);
                    if(d!=null){
                    	list.add(d);
                    }
	        	}
	        }
	      }
	    } catch (Exception e) {
          e.printStackTrace();
	    }
	    return list;
	}
	
	public static Map<String,Integer> getData(){
		Fetch_TopFund f=new Fetch_TopFund();
		List<DailyData> list=f.fetchAllFunds();
		List<String> stock=new ArrayList<String>();
	    Set<String> c_stock=new HashSet<String>();
	    Set<String> z_stock=new HashSet<String>();
	    Set<String> zx_stock=new HashSet<String>();
	    Map<String,Integer> stock_rank=new HashMap<String,Integer>();
	    Map<String,List<String>> fund_holding=new HashMap<String,List<String>>();
	    
		/**
		 * 基金前50名
		 */
		for(int i=0;i<50;i++){
			String code=list.get(i).getFundCode();
			List<String> holding=Fetch_FundHolding.fetchAllFunds(code);
			fund_holding.put(code,holding);
		    stock.addAll(holding);
		}
		
		
		for(int i=0;i<stock.size();i++){
			String line=stock.get(i);
			String[] data=line.split("\t");
			String s=data[0];
			//shenzhen
			if(s.startsWith("0")||s.startsWith("3")){
				s="sz"+s;
			}
			//shanghai
			if(s.startsWith("6")){
				s="sh"+s;
			}
			
			if(s.startsWith("sz300")){
				c_stock.add(s);
			}else if(s.startsWith("sz002")){
				zx_stock.add(s);
			}else{
				z_stock.add(s);
			}
			if(!stock_rank.containsKey(s)){
				stock_rank.put(s, 1);
			}else{
				stock_rank.put(s,stock_rank.get(s)+1);
			}
		}
		

		Set<String> stockKey=stock_rank.keySet();
		
		List<String> zhu=new ArrayList<String>();
		List<String> chu=new ArrayList<String>();
		List<String> zho=new ArrayList<String>();
		List<String> all=new ArrayList<String>();
		
		for(String key:stockKey){
			Stock s=map.get(key);
			if(s==null){
				continue;
			}
			all.add(key+"\t"+stock_rank.get(key)+"\t"+s.getName());
			if(key.startsWith("sz300")){
				chu.add(key+"\t"+stock_rank.get(key)+"\t"+s.getName());
			}else if(key.startsWith("sz002")){
				zho.add(key+"\t"+stock_rank.get(key)+"\t"+s.getName());
			}else{
				zhu.add(key+"\t"+stock_rank.get(key)+"\t"+s.getName());
			}
		}
		
		Date dd=new Date();
    	if(dd.getHours()<18){
    		Calendar cc=Calendar.getInstance();
    		cc.add(Calendar.DATE, -1);
    		dd=cc.getTime();
    	}
    	String d=FetchUtil.FILE_FORMAT.format(dd);
    	
        FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING, all);
    	
		FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_CHU, chu);
		
		FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_ZHO, zho);
		
		FetchUtil.saveAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_HOLDING_ZHU, zhu);
    	
    	return stock_rank;
    	
	}
	public static void main(String args[]){
		
		getData();
		
	}
	
	static class Option implements Comparable<Option>{
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		public String toString(){
			return this.name+"\t"+this.count;
		}
		String name;
		Integer count;
		
		public Option(String name,Integer count){
			this.name=name;
			this.count=count;
		}
		@Override
		public int compareTo(Option o) {
			// TODO Auto-generated method stub
			return o.count-this.count;
		}
		
	}
}
