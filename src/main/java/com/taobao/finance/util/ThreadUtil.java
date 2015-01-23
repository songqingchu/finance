package com.taobao.finance.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.stock.Fetch_AllStock;

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
	
	public static void main(String args[]){
	    split(4);	
	}
	
}
