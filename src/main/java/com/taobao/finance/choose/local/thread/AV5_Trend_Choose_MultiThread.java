package com.taobao.finance.choose.local.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtils;
import com.taobao.finance.util.FetchUtil;


/**
 * @author Administrator
 */
public class AV5_Trend_Choose_MultiThread extends Local_Choose_MultiThread_Base{

	public static void main(String args[]){
		//Hisdata_Base.setLocalAnasysIncludeToday(false);
        new AV5_Trend_Choose_MultiThread().choose();
	}
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			AV5_Trend_Task t=new AV5_Trend_Task(l);
			lt.add(t);
		}
		return lt;
	}

	public String getPath() {
		return FetchUtil.FILE_STOCK_CHOOSE_BASE+"av5\\";
	}
}



class AV5_Trend_Task implements Callable<List<Stock>> {

	private List<Stock> l;

	public AV5_Trend_Task(List<Stock> l) {
		this.l = l;
	}
	
	public  List<Stock> prepareData(String symbol,Date d){
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		int i=0;
		for (Stock s : this.l) {
			System.out.println(i++);
			if(s.getSymbol().contains("300405")){
				s.get_10changes();
			}
			List<Stock> history = prepareData(s.getSymbol(), null);
			if (history == null) {
				continue;
			}
			if (history.size() < 2) {
				continue;
			}
			boolean match = CheckUtils.check5(history);
			if (match) {
				s.setVrate(history.get(history.size() - 1).getVrate());
				l.add(s);
			}
		}

		return l;
	}
}
