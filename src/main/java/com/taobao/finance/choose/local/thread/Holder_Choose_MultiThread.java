package com.taobao.finance.choose.local.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GStock;
import com.taobao.finance.util.CheckUtil;
import com.taobao.finance.util.FetchUtil;

public class Holder_Choose_MultiThread extends Local_Choose_MultiThread_Base{

	
	public Store store;
	public Holder_Choose_MultiThread(Store store){
		this.store=store;
	}
	
	
	public static void main(String args[]){
        //new Holder_Choose_MultiThread().choose();
	}
	
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			Holder_Task t=new Holder_Task(l,store);
			lt.add(t);
		}
		return lt;
	}
	public String getPath() {
		return FetchUtil.FILE_STOCK_CHOOSE_BASE+"cb\\";
	}
}



class Holder_Task implements Callable<List<Stock>> {
	public static final Logger logger = Logger.getLogger("taskLogger");
	private List<Stock> l;
	public Store store;
	public Holder_Task(List<Stock> l,Store store) {
		this.l = l;
		this.store=store;
	}
	
	public  GStock prepareData(String symbol){
		return store.holderMap.get(symbol);
	}

	@Override
	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		for (Stock s : this.l) {
			GStock st = prepareData(s.getSymbol());
			if (st == null) {
				continue;
			}
			if(StringUtils.isBlank(st.getRecord())){
				continue;
			}
			Float value = CheckUtil.checkHolder(st.getRecord());
			s.setSuo(value);
		}

		return l;
	}
}
