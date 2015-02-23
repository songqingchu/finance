package com.taobao.finance.choose.local.thread.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.base.Local_Choose_MultiThread_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public class ZhenFu15_Choose extends Local_Choose_MultiThread_Base{

	public List<Stock> choose() {
		long begin = System.currentTimeMillis();
		int threadNum = Runtime.getRuntime().availableProcessors();
		List<List<Stock>> l = ThreadUtil.split(threadNum);
		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		CompletionService<List<Stock>> con = new ExecutorCompletionService<List<Stock>>(
				service);
		List<Callable<List<Stock>>> ll = prepareTask(l);
		for (Callable<List<Stock>> c : ll) {
			con.submit(c);
		}
		List<Stock> r = new ArrayList<Stock>();
		int i = 1;
		while (i <= threadNum) {
			try {
				r.addAll(con.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			i++;
		}
		long end = System.currentTimeMillis();
		System.out.println("��ʱ��" + (end - begin));
		service.shutdown();
		save(r);
		//r = filter(r);
		for (Stock s : r) {
			if(s.getDate()!=null){
				System.out.println(s.getCode()+"   "+FetchUtil.FILE_FORMAT.format(s.getDate()));
			}
			
		}
		System.out.println(r.size());
        return r;
	}
	
	public static void main(String args[]){
		//Hisdata_Base.setLocalAnasysIncludeToday(false);
        new ZhenFu15_Choose().choose();
	}
	public  List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll){
		List<Callable<List<Stock>>> lt=new ArrayList<Callable<List<Stock>>>();
		for(List<Stock> l:ll){
			ZhenFu15_Task t=new ZhenFu15_Task(l,1);
			lt.add(t);
		}
		return lt;
	}
	public String getPath() {
		return FetchUtil.FILE_STOCK_CHOOSE_BASE+"cb\\";
	}
}



class ZhenFu15_Task implements Callable<List<Stock>> {

	private List<Stock> l;
    private int idx=1;
	
	public ZhenFu15_Task(List<Stock> l,int idx) {
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
			System.out.println("����"+i);
			i++;
			if(s.getCode().equals("300288")){
				s.get_10changes();
			}
			List<Stock> history = prepareData(s.getSymbol(), null);
			if (history == null) {
				continue;
			}
			if (history.size() < 30) {
				continue;
			}
			boolean match = false;
			for(Stock st:history){
				Float end=st.getEndPriceFloat();
				Float start=st.getStartPriceFloat();
				Float high=st.getHighPriceFloat();
				Float low=st.getLowPriceFloat();
				if(high/low>1.15F){
					if(end/start>1.02F){
						l.add(st);
						break;
					}
				}
			}
		}

		return l;
	}
}

