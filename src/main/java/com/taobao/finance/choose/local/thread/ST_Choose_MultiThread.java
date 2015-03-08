package com.taobao.finance.choose.local.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;
import com.taobao.finance.util.FetchUtil;

public class ST_Choose_MultiThread extends Local_Choose_MultiThread_Base{

	public static void main(String args[]){
		//Hisdata_Base.setLocalAnasysIncludeToday(false);
        new ST_Choose_MultiThread().choose();
	}
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			ST_Task t=new ST_Task(l);
			lt.add(t);
		}
		return lt;
	}
	public String getPath() {
		return FetchUtil.FILE_STOCK_CHOOSE_BASE+"tp\\";
	}
}



class ST_Task implements Callable<List<Stock>> {

	private List<Stock> l;

	public ST_Task(List<Stock> l) {
		this.l = l;
	}
	
	public  List<Stock> prepareData(String symbol,Date d){
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		for (Stock s : this.l) {
			if (s.getName().contains("S")) {
				l.add(s);
			}
		}

		return l;
	}
}

