package com.taobao.finance.choose.local.thread.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;

public class Last_CriticalBand_Choose extends Local_Choose_MultiThread_Base{

	public static void main(String args[]){
		//Hisdata_Base.setLocalAnasysIncludeToday(false);
        new Last_CriticalBand_Choose().choose();
	}
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			LCB_Task t=new LCB_Task(l,9);
			lt.add(t);
		}
		return lt;
	}
	public String getPath() {
		return "E:\\stock\\choose\\cb\\";
	}
}



class LCB_Task implements Callable<List<Stock>> {

	private List<Stock> l;
    private int idx=1;
	
	public LCB_Task(List<Stock> l,int idx) {
		this.l = l;
		this.idx=idx;
	}
	
	public  List<Stock> prepareData(String symbol,Date d){
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		int i=1;
		for (Stock s : this.l) {
			System.out.println("¥¶¿Ì"+i);
			i++;
			if(s.getCode().equals("300288")){
				s.get_10changes();
			}
			List<Stock> history = prepareData(s.getSymbol(), null);
			if (history == null) {
				continue;
			}
			if (history.size() < 10) {
				continue;
			}
			Float last1=history.get(history.size()-idx).getEndPriceFloat();
			Float start1=history.get(history.size()-idx).getStartPriceFloat();
			Float last2=history.get(history.size()-idx-1).getEndPriceFloat();
			boolean match = false;
			if(last1.equals(start1)&&(last1/last2)>1.098F){
				match=true;
			}			
			if (match) {
				s.setVrate(history.get(history.size() - 1).getVrate());
				l.add(s);
			}
		}

		return l;
	}
}
