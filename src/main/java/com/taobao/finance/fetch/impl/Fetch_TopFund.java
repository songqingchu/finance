package com.taobao.finance.fetch.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 抓取今年业绩前50名基金，分析基金的创业板持仓情况
 * @author Administrator
 */
public class Fetch_TopFund {
	
	private String allFundsUrl="http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=gp&rs=&gs=0&sc=1nzf&st=desc&sd=2012-08-26&ed=2013-08-26&pi=1&pn=50&v=0.023998898919671774";

	public static Map<String,Stock> s2n=new HashMap<String,Stock>();
	static{
		s2n=FetchUtil.readFileMapAbsolute("d:\\stock\\stockAll.txt");
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
	
	
	public static void main(String args[]){
		Fetch_TopFund f=new Fetch_TopFund();
		List<DailyData> list=f.fetchAllFunds();
		List<String> stock=new ArrayList<String>();
	    List<String> c_stock=new ArrayList<String>();
	    List<String> z_stock=new ArrayList<String>();
	    Map<String,Integer> stock_rank=new HashMap<String,Integer>();
	    Map<String,List<String>> fund_holding=new HashMap<String,List<String>>();
	    
		/**
		 * top 20 fund
		 */
		for(int i=0;i<50;i++){
			String code=list.get(i).getFundCode();
			List<String> holding=Fetch_FundHolding.fetchAllFunds(code);
			fund_holding.put(code,holding);
			int c_count=0;
			Float c_quota=0f;
			Float top_10_quota=0f;
			for(String hold:holding){
				if(hold.startsWith("300")){
					c_count++;
					String[] datas=hold.split("\t");
					c_quota=c_quota+Float.parseFloat(datas[1]);
				}
				String[] datas=hold.split("\t");
				top_10_quota=top_10_quota+Float.parseFloat(datas[1]);
			}
			System.out.println(list.get(i).getFundName()+":\t"+(float)c_count/(float)10+":\t"+(float)c_quota/(float)top_10_quota);
			for(String s:holding){
				stock.add(s);
			}
			//stock.addAll(holding);
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
			}else{
				z_stock.add(s);
			}
			if(!stock_rank.containsKey(s)){
				stock_rank.put(s, 1);
			}else{
				stock_rank.put(s,stock_rank.get(s)+1);
			}
		}
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
