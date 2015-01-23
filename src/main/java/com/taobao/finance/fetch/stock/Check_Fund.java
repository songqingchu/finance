package com.taobao.finance.fetch.stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.finance.dataobject.DailyData;
import com.taobao.finance.fetch.impl.Fetch_FundHolding;
import com.taobao.finance.fetch.impl.Fetch_TopFund;
import com.taobao.finance.util.FetchUtil;

public class Check_Fund {
	
	public static List<String> r=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"rongQuan.txt");
	
	public static String getRData(String ss){
		
	    List<String> l=Fetch_FundHolding.fetchAllFunds(ss);
	    Float all=0F;
	    int allCount=0;
	    for(String s:l){
	    	String[] data=s.split("\t");
	    	String symbol=data[0];
	    	if(symbol.startsWith("6")){
	    		symbol="sh"+symbol;
	    	}else{
	    		symbol="sz"+symbol;
	    	}
	    	if(r.contains(symbol.trim())){
	    		allCount++;
	    		all=all+Float.parseFloat(data[1]);
	    	}
	    }
	    return allCount+"\t"+FetchUtil.formatRate(all/100);
	}
	public static void main(String args[]){
		String sss=null;
		//sss="630005";
		
		if(sss!=null){
			System.out.println(getRData(sss));
		}else{
			List<Option> lo=new ArrayList<Option>();
			
			Fetch_TopFund f=new Fetch_TopFund();
			List<DailyData> list=f.fetchAllFunds();
			List<String> re=new ArrayList<String>();
			for(DailyData d:list){
				String s=d.getFundCode();
				String data=getRData(s);
				re.add(s+"\t"+data+"\t"+d.getFundName());
				Option o=new Option();
				o.setSymbol(s);
				o.setName(d.getFundName());
				o.setCount(Integer.parseInt(data.split("\t")[0]));
				o.setQuota(Float.parseFloat(data.split("\t")[1]));
				lo.add(o);
			}
			Collections.sort(lo);
			for(Option d:lo){
				System.out.println(d.getSymbol()+"\t"+d.getCount()+"\t"
						+d.getQuota()+"\t"+d.getName());
			}
		}
	}
	
	static class Option implements Comparable<Option>{
		private String symbol;
		private String name;
		private int count;
		private Float quota;
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public Float getQuota() {
			return quota;
		}
		public void setQuota(Float quota) {
			this.quota = quota;
		}
		@Override
		public int compareTo(Option o) {
			// TODO Auto-generated method stub
			if(this.getQuota()-o.getQuota()>0F){
				return 1;
			}else{
				return -1;
			}
		}
		
	}
}
