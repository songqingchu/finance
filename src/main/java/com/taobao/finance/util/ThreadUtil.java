package com.taobao.finance.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;

public class ThreadUtil {

	public static List<List<Stock>> split(int n){
		List<List<Stock>> l=new ArrayList<List<Stock>>();
		Map<String, Stock> allMap = Fetch_AllStock.map;

		Integer size=allMap.size();
		Float perSize=(size.floatValue())/n;
		Double p=Math.floor(perSize);
		Stock[] all=new Stock[size];
		allMap.values().toArray(all);
		for(int i=1;i<=n;i++){
			List<Stock> ll=new ArrayList<Stock>();
			int start=p.intValue()*(i-1);
			int end=(start+p.intValue())<size?(start+p.intValue()):size;
			for(int j=start;j<end;j++){
				ll.add(all[j]);
			}
			l.add(ll);
		}
		return l;
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
	
	
	public static List<List<Object>> divide(List l,int num){
		List<List<Object>> r=new ArrayList<List<Object>>();
		if(l.size()<num){
		    r.add(l);
			return r;
		}
    	int size=0;
    	size=l.size()/num;
    	List<Object> slice=new ArrayList<Object>();
    	for(int i=0;i<l.size();i++){
    		if(slice.size()<size){
    			slice.add(l.get(i));
    		}else {
    			slice=new ArrayList<Object>();
    			slice.add(l.get(i));
    		}
    		if(slice.size()==size||i==l.size()-1){
    			r.add(slice);
    		}
    	}
    	return r;
    }
	
	
	
	public static void main(String args[]){
	    split(4);	
	}
	
}
