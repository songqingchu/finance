package com.taobao.finance.choose.local.thread.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;
import com.taobao.finance.util.FetchUtil;

public class SuperTrend_Choose_MultiThead extends Local_Choose_MultiThread_Base {

	public static void main(String args[]) {
		// Hisdata_Base.setLocalAnasysIncludeToday(false);
		new SuperTrend_Choose_MultiThead().choose();
	}

	public List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll) {
		List<Callable<List<Stock>>> lt = new ArrayList<Callable<List<Stock>>>();
		for (List<Stock> l : ll) {
			SuperTrend_Task t = new SuperTrend_Task(l);
			lt.add(t);
		}
		return lt;
	}

	public String getPath() {
		return FetchUtil.FILE_STOCK_CHOOSE_BASE+"big\\";
	}
}

class SuperTrend_Task implements Callable<List<Stock>> {

	private List<Stock> l;

	public SuperTrend_Task(List<Stock> l) {
		this.l = l;
	}

	public List<Stock> prepareData(String symbol, Date d) {
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		int i=0;
		for (Stock s : this.l) {
			try {
				if (s.getCode().equals("600352")) {
					s.get_10changes();
				}
				if (s.getName().equals("浙江龙")) {
					s.get_10changes();
				}
				System.out.println(i++);
				List<Stock> history = prepareData(s.getSymbol(), null);
				if (history == null) {
					continue;
				}
				if (history.size() < 2) {
					continue;
				}
				boolean match = CheckUtil.checkBigTrend2(history);
				if (match) {
					s.setVrate(history.get(history.size() - 1).getVrate());
					l.add(s);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return l;
	}
}
