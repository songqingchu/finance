package com.taobao.finance.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock_Sina;
import com.taobao.finance.fetch.impl.Fetch_SingleStock_THS;


public class RealTask implements Callable<Object>{
	public List<Object> l; 
	public RealTask(List<Object> l){
		this.l=l;
	}
	public Object call(){
		List<Stock> r=new ArrayList<Stock>();
		for(Object s:l){
			Stock st=Fetch_SingleStock_Sina.fetch((String)s,null,null);
			//Stock st=Fetch_SingleStock_THS.fetch((String)s);
			if(st!=null){
				r.add(st);
			}
		}
		return r;
	}
}
