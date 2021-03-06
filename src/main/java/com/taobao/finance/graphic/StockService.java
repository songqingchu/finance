package com.taobao.finance.graphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_ServeralStock_Sina;

public class StockService {

	public static Map<String,Proxy> proxyPool=new HashMap<String,Proxy>();
	public static List<Proxy> proxyList=new ArrayList<Proxy>();
	
	public static List<Proxy> getProxy(){
		if(proxyList==null){
			return null;
		}
		if(proxyList.size()==0){
			return null;
		}
		if(proxyList.size()<5){
			return proxyList;
		}
		List<Proxy> l=new ArrayList<Proxy>();
		if(proxyList.size()<10){
			l.add(proxyList.get(0));
			l.add(proxyList.get(1));
			l.add(proxyList.get(2));
			l.add(proxyList.get(3));
			l.add(proxyList.get(4));
			return l;
		}
		
		Set<Integer> set=new HashSet<Integer>();
		while(set.size()<5){
			try{
				Integer random=new Random().nextInt(proxyList.size()-1);
				set.add(random);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		for(Integer r:set){
			l.add(proxyList.get(r));
		}
		return l;
	}
	
	public List<Stock> getConcern(){
		List<Stock> l=new ArrayList<Stock>();
		Stock s=Fetch_AllStock.map.get("sh600001");
		l.add(s);
		return l;
	}
	
	public List<Stock> getRealPrice(List<Stock> list){
		List<Stock> l=Fetch_ServeralStock_Sina.fetch(
				  "sh000001,"//上证
				+"sz399001,"//深指
				+"sz399101,"//中小        
				+"sz399006,"//创业
				//+"sz002596,"//海南
				//+"sh600212,"//江泉
				+"sh600622,"//嘉宝
				+"sz000702,"//正虹
				//+"sh600209",//罗顿
				+"sh600568",//中珠
				
				getProxy());
		return l;
	}
}
