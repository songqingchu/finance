package com.taobao.finance.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
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
	public static Map<String,List<Stock>> store=new HashMap<String,List<Stock>>();
	
	public Store(){
		new Thread(){
			public void run(){
				List<Stock> big=new BigTrend_Choose_MultiThread().choose();
				List<Stock> acvu=new AVCU_Choose_MultiThread().choose();
				List<Stock> av5=new AV5_Trend_Choose_MultiThread().choose();
				List<Stock> av10=new AV10_Trend_Choose_MultiThread().choose();
				List<Stock> tp=new TP_Choose_MultiThread().choose();
				store.put("big", big);
				store.put("acvu",acvu );
				store.put("av5", av5);
				store.put("av10",av10 );
				store.put("tp",tp );
			}
		}.start();
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
