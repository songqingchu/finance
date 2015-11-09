package com.taobao.finance.task;

import java.util.List;
import java.util.concurrent.Callable;
import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock_THS;

public class UnformalDataTask implements Callable<Object>{
	private List<Object> list;
	public UnformalDataTask(List<Object> list){
		this.list=list;
	}
	
	public Object call(){
		for(Object o:list){
			String s=(String)o;
			if(s.contains("600060")){
				s.length();
			}
			//Stock today = Fetch_SingleStock_Sina.fetch(s);
			Stock today = Fetch_SingleStock_THS.fetch(s);
    		if(today==null){
    			continue;
    		}
    		Hisdata_Base.saveTmp(s,today);
		}
		return 1;
	}
}