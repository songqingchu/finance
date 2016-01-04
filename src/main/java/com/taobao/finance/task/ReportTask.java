package com.taobao.finance.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Report;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_Report_Jupai;

public class ReportTask implements Callable<Object>{
	public static AtomicInteger count=new AtomicInteger(0);
	public List<Object> l; 
	public ReportTask(List<Object> l){
		this.l=l;
	}
	public Object call(){
		//System.out.println("抓取披露公告任务开始！");
		List<Report> r=new ArrayList<Report>();
		for(Object s:l){
			Stock st=(Stock)s;
			List<Report> rL=null;
			if(Store.getProxy()==null){
				System.out.println("第"+count.incrementAndGet()+"个任务");
				rL=Fetch_Report_Jupai.fetch(st.getCode(),null,null);
			}else{
				rL=Fetch_Report_Jupai.fetch(st.getCode(),Store.getProxy());
			}
			
			if(st!=null){
				r.addAll(rL);
			}
		}
		return r;
	}
}