package com.taobao.finance.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.taobao.finance.dataobject.Stock;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015年2月13日
 */
@Component
public class Store {
	public  Map<String,List<Stock>> store=new HashMap<String,List<Stock>>();
	
	public Store(){
		//new Thread(){
			//public void run(){
				/**List<Stock> big=new BigTrend_Choose_MultiThread().choose();
				List<Stock> acvu=new AVCU_Choose_MultiThread().choose();
				List<Stock> av5=new AV5_Trend_Choose_MultiThread().choose();
				List<Stock> av10=new AV10_Trend_Choose_MultiThread().choose();
				List<Stock> tp=new TP_Choose_MultiThread().choose();
				store.put("big", big);
				store.put("acvu",acvu );
				store.put("av5", av5);
				store.put("av10",av10 );
				store.put("tp",tp );
			*/	
		//	}
		//}.start();
		
		List<Stock> acvu=new ArrayList<Stock>();
		Stock s=new Stock();
		s.setSymbol("sh600804");
		acvu.add(s);
		
		s=new Stock();
		s.setSymbol("sz002268");
		acvu.add(s);
		
		s=new Stock();
		s.setSymbol("sz002267");
		acvu.add(s);
		
		s=new Stock();
		s.setSymbol("sh600845");
		acvu.add(s);
		
		List<Stock> big=new ArrayList<Stock>();
		s=new Stock();
		s.setSymbol("sz002224");
		big.add(s);
		
		List<Stock> av5=new ArrayList<Stock>();
		s=new Stock();
		s.setSymbol("sz002224");
		av5.add(s);
		
		List<Stock> av10=new ArrayList<Stock>();
		s=new Stock();
		s.setSymbol("sz000921");
		av10.add(s);
		
		store.put("big", big);
		store.put("acvu",acvu );
		store.put("av5", av5);
		store.put("av10",av10 );
	}
	
	public boolean containsKey(String key){
		return store.containsKey(key);
	}
	
	public List<Stock> get(String key){
		return store.get(key);
	}
	
	public void put(String key,List<Stock> l){
		store.put(key, l);
	}
}
