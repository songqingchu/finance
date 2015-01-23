package com.taobao.finance.choose.local.thread.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;

public class BigTrend_Choose_MultiThread extends Local_Choose_MultiThread_Base {

	public static void main(String args[]) {
		// Hisdata_Base.setLocalAnasysIncludeToday(false);
		new BigTrend_Choose_MultiThread().choose();
	}

	public List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll) {
		List<Callable<List<Stock>>> lt = new ArrayList<Callable<List<Stock>>>();
		for (List<Stock> l : ll) {
			BigTrend_Task t = new BigTrend_Task(l);
			lt.add(t);
		}
		return lt;
	}

	public String getPath() {
		return "E:\\stock\\choose\\big\\";
	}
}

class BigTrend_Task implements Callable<List<Stock>> {

	private List<Stock> l;

	public BigTrend_Task(List<Stock> l) {
		this.l = l;
	}

	public List<Stock> prepareData(String symbol, Date d) {
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	public List<Stock> call() throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		for (Stock s : this.l) {
			try {
				if (s.getCode().equals("600352")) {
					s.get_10changes();
				}
				if (s.getName().equals("浙江龙")) {
					s.get_10changes();
				}
				List<Stock> history = prepareData(s.getSymbol(), null);
				if (history == null) {
					continue;
				}
				if (history.size() < 2) {
					continue;
				}
				boolean match = CheckUtil.checkBigTrend(history);
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
