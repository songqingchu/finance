package com.taobao.finance.fetch.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.FileUtil;

public class Fetch_TotalRate {
    
	
	
	public static Map<String,Float> getTotalRate(String date){
		Map<String,Float> m=new HashMap<String,Float>();
		List<String> l=FileUtil.fileList;
		for(String s:l){
			String file=FetchUtil.FILE_STOCK_STATIS_BASE+date+"\\"+s;
			List<String> content=FetchUtil.readAllLineFileListAbsolute(file);
			for(String line:content){
				String [] data=line.split("\t");
				if(line.endsWith("*")){
					Float total=0F;
					for(int i=1;i<data.length-1;i++){
						total=total+Float.parseFloat(data[i]);
					}
					m.put(data[0].trim().replace("=", ""), total);
				}else{
					Float total=0F;
					for(int i=1;i<data.length;i++){
						total=total+Float.parseFloat(data[i]);
					}
					m.put(data[0].trim().replace("=", ""), total);
				}
			}
		}
		return m;
	}
	
	public static Map<String,Integer> getTotalDay(String date){
		Map<String,Integer> m=new HashMap<String,Integer>();
		List<String> l=FileUtil.fileList;
		for(String s:l){
			String file=FetchUtil.FILE_STOCK_STATIS_BASE+date+"\\"+s;
			List<String> content=FetchUtil.readAllLineFileListAbsolute(file);
			for(String line:content){
				String [] data=line.split("\t");
				if(line.endsWith("*")){
					int count=data.length-3;
					m.put(data[0].trim().replace("=", ""), count);
				}else{
					int count=data.length-1;
					m.put(data[0].trim().replace("=", ""), count);
				}
			}
		}
		return m;
	}
	public static void main(String args[]){
		System.out.println(getTotalRate("2013.09.30"));
	}
}
