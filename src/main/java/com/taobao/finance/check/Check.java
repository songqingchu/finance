package com.taobao.finance.check;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public abstract class Check {

	public  String name="";
	public void check(String symbol){
		List<Date> dl=new ArrayList<Date>();
		List<Stock> l=Hisdata_Base.readHisDataMerge(symbol, null);
		for(int i=0;i<60;i++){
			if(l.size()-1-i<0){
				break;
			}
			l.remove(l.size()-1);
			if(match(l)){
				dl.add(l.get(l.size()-1).getDate());
			}
		}
		printName();
		for(Date d:dl){
			System.out.println(FetchUtil.FILE_FORMAT.format(d));
		}
	}
	
	public abstract boolean match(List<Stock> l);
	
	public abstract void printName();

}
