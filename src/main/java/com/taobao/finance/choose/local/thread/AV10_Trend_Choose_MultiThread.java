package com.taobao.finance.choose.local.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;

public class AV10_Trend_Choose_MultiThread extends Local_Choose_MultiThread_Base{

	public static void main(String args[]){
		//Hisdata_Base.setLocalAnasysIncludeToday(false);
        new AV10_Trend_Choose_MultiThread().choose();
	}
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			AV10_Trend_Task t=new AV10_Trend_Task(l);
			lt.add(t);
		}
		return lt;
	}
	
	public String getPath() {
		return "E:\\stock\\choose\\av10\\";
	}
}



class AV10_Trend_Task implements Callable<List<Stock>> {

	private List<Stock> l;

	public AV10_Trend_Task(List<Stock> l) {
		this.l = l;
	}
	
	public  List<Stock> prepareData(String symbol,Date d){
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		for (Stock s : this.l) {
			if(s.getCode().equals("000096")){
				s.get_10changes();
			}
			List<Stock> history = prepareData(s.getSymbol(), null);
			if (history == null) {
				continue;
			}
			if (history.size() < 2) {
				continue;
			}
			boolean match = CheckUtil.checkAV10XRate(history);
			if (match) {
				s.setVrate(history.get(history.size() - 1).getVrate());
				l.add(s);
			}
		}

		return l;
	}
}

