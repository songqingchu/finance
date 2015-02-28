package com.taobao.finance.check;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public abstract class Check {

	public String name="";
	public List<String> check(String symbol){
		List<String> dateList=new ArrayList<String>();
		symbol=fullName(symbol);
		List<Date> dl=new ArrayList<Date>();
		List<Stock> l=Hisdata_Base.readHisDataMerge(symbol, null);
		for(int i=0;i<150;i++){
			if(l.size()-1<0){
				break;
			}
			String s=FetchUtil.FILE_FORMAT.format(l.get(l.size()-1).getDate());
			//System.out.println(s);
			if(s.contains("2015.01.12")){
				s.length();
			}
			if(match(l)){
				dl.add(l.get(l.size()-1).getDate());
			}
			l.remove(l.size()-1);
		}
		printName();
		for(Date dd:dl){
			String s=FetchUtil.FILE_FORMAT.format(dd);
			System.out.println(s);
			dateList.add(s);
		}
		return dateList;
	}
	
	public static String fullName(String code){
		if(!(code.startsWith("sh")||code.startsWith("sz"))){
			if(code.startsWith("300")||code.startsWith("00")){
				code="sz"+code;
			}else{
				code="sh"+code;
			}
		}
		return code;
	}
	
	public abstract boolean match(List<Stock> l);
	
	public abstract void printName();

}
