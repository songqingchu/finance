package com.taobao.finance.task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;

public class HisDataTask implements Callable<Object>{
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
    		Hisdata_Base.save(symbol,history);
		}
		return 1;
	}
}