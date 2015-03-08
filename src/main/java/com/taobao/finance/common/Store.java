package com.taobao.finance.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.HisDataTask;
import com.taobao.finance.task.UnformalDataTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: www.dianwoba.com
 * </p>
 * 
 * @author lijiayang
 * @date 2015年2月13日
 */
@Component
@DependsOn("fetchUtil")
public class Store {
	public Map<String, List<Stock>> store = new HashMap<String, List<Stock>>();
	public Map<String, Integer> download = new HashMap<String, Integer>();
	public Map<String, Integer> choose = new HashMap<String, Integer>();
	public Map<String,Stock> publicPool=new HashMap<String,Stock>();
	public Map<String, Boolean> checkWorkingRecord = new HashMap<String, Boolean>();
	
	
	public static Boolean workingDay;
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");

	public static int DOWNLOAD_STATUS_DOWNLOADING = 1;
	public static int DOWNLOAD_STATUS_DOWNLOADED = 2;
	
	@Autowired
	private ThreadService threadService;
	
	
	public Store() {
	}

	public void updateTmp(){
		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			UnformalDataTask t=new UnformalDataTask(sys);
			callList.add(t);
		}
		List<Object> r=threadService.service(callList);
		r.size();
	}
	
	public void updateHistory(){
		Fetch_AllStock.getData();		
		Set<String> s=Fetch_AllStock.map.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			HisDataTask t=new HisDataTask(sys,false);
			callList.add(t);
		}
		List<Object> r=threadService.service(callList);
		r.size();
	}
	
	public void ananyse() {
		List<Stock> big = new BigTrend_Choose_MultiThread().choose();
		List<Stock> acvu = new AVCU_Choose_MultiThread().choose();
		List<Stock> av5 = new AV5_Trend_Choose_MultiThread().choose();
		List<Stock> av10 = new AV10_Trend_Choose_MultiThread().choose();
		List<Stock> tp = new TP_Choose_MultiThread().choose();
		store.put("big", big);
		store.put("acvu", acvu);
		store.put("av5", av5);
		store.put("av10", av10);
		store.put("tp", tp);
	}

	public boolean containsKey(String key) {
		return store.containsKey(key);
	}

	public List<Stock> get(String key) {
		return store.get(key);
	}

	public void put(String key, List<Stock> l) {
		store.put(key, l);
	}

	public Integer getDownloadStatus(Date d) {
		String dateStr = DF.format(d);
		Integer status = download.get(dateStr);
		return status;
	}

	public void setDownloading(Date d) {
		String dateStr = DF.format(d);
		download.put(dateStr, 1);
	}

	public void setDownloaded(Date d) {
		String dateStr = DF.format(d);
		download.put(dateStr, 2);
	}

	public Integer getChooseStatus(Date d) {
		String dateStr = DF.format(d);
		Integer status = choose.get(dateStr);
		return status;
	}

	public void setChoosing(Date d) {
		String dateStr = DF.format(d);
		choose.put(dateStr, 1);
	}

	public void setChoosed(Date d) {
		String dateStr = DF.format(d);
		choose.put(dateStr, 2);
	}
}
