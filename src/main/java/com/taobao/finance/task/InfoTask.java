package com.taobao.finance.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Introduce;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_Info;

public class InfoTask implements Callable<Object>{
	public static AtomicInteger count=new AtomicInteger(0);
	public List<Object> l; 
	public InfoTask(List<Object> l){
		this.l=l;
	}
	public Object call(){
		//System.out.println("抓取披露公告任务开始！");
		List<Introduce> r=new ArrayList<Introduce>();
		for(Object s:l){
			Stock st=(Stock)s;
			Introduce rL=null;
			if(Store.getProxy()==null){
				System.out.println("第"+count.incrementAndGet()+"个任务:"+st.getSymbol());
				rL=Fetch_Info.fetch(st.getSymbol(),null,null);
			}else{
				rL=Fetch_Info.fetch(st.getCode(),Store.getProxy());
			}
			
			if(rL!=null){
				r.add(rL);
			}
		}
		return r;
	}
}