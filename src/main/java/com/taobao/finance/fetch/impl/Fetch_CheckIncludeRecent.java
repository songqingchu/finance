package com.taobao.finance.fetch.impl;

import java.util.Date;
import java.util.List;

import com.taobao.finance.dataobject.Stock;

public class Fetch_CheckIncludeRecent {
	
	public static  Boolean checkIncludeRecent(Stock r,List<Stock> l){
		List<Stock> history=l;
		Stock today=r;
		Stock lastDay=history.get(0);	
		if(today.getStartPrice()==null){
			return true;
		}
		String todayStartStr=today.getStartPrice();
		String lastBit=todayStartStr.substring(todayStartStr.length()-1,todayStartStr.length());
		todayStartStr=todayStartStr.substring(0,todayStartStr.length()-1);
		if(Integer.parseInt(lastBit)>=5){
			Double d=Double.parseDouble(todayStartStr);
			d=d+0.01D;
			todayStartStr=d.toString();
		}
		
		today.setStartPrice(todayStartStr);
		
		if(today.getStartPrice().equals(lastDay.getStartPrice().replace(",", ""))){
			return true;
		}
		return false;
	}
	
	public static  Boolean checkIncludeRecent2(Stock r,List<Stock> l){
		List<Stock> history=l;
		Stock today=r;
		Stock lastDay=history.get(0);	
		if(today.getStartPrice()==null){
			return true;
		}
		String todayStartStr=today.getStartPrice();
		String lastBit=todayStartStr.substring(todayStartStr.length()-1,todayStartStr.length());
		todayStartStr=todayStartStr.substring(0,todayStartStr.length()-1);
		if(Integer.parseInt(lastBit)>=5){
			Double d=Double.parseDouble(todayStartStr);
			d=d+0.01D;
			todayStartStr=d.toString();
		}
		
		today.setStartPrice(todayStartStr);
		
		if(today.getStartPrice().equals(lastDay.getStartPrice().replace(",", ""))){
			return true;
		}
		return false;
	}
	
	public static  Boolean checkIncludeRecent(){
		Stock s=Fetch_SingleStock.fetch("sh000001");
		List<Stock> history=Fetch_StockHistory.fetch("sh000001");
		return checkIncludeRecent(s,history);
	}
	
	public static  boolean checkOne(String code){
		List<Stock> history=Fetch_StockHistory.fetch(code);
		Stock today=Fetch_SingleStock.fetch(code);
		Stock lastDay=history.get(0);	
		if(today.getStartPrice()==null){
			return true;
		}
		String todayStartStr=today.getStartPrice();
		String lastBit=todayStartStr.substring(todayStartStr.length()-1,todayStartStr.length());
		todayStartStr=todayStartStr.substring(0,todayStartStr.length()-1);
		if(Integer.parseInt(lastBit)>=5){
			Double d=Double.parseDouble(todayStartStr);
			d=d+0.01D;
			todayStartStr=d.toString();
		}
		
		today.setStartPrice(todayStartStr);
		
		if(today.getStartPrice().equals(lastDay.getStartPrice().replace(",", ""))){
			return false;
		}
		return true;
	}
	
	public static  boolean checkWorkDay(){
		boolean sh = checkOne("sh000001");
		boolean sz = checkOne("sz399001");
		if(sh || sz){
			return true;
		}
		return false;
	}
	
	
	
	public static void main(String args[]){
		System.out.println(checkIncludeRecent());
	}
}
