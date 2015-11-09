package com.taobao.finance.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.fetch.impl.Fetch_ServeralStock_Sina;

public class CheckTTLTask implements Callable<Object>{
	private List<Object> list;
	
	public CheckTTLTask(List<Object> list){
		this.list=list;
	}
	
	public Object call(){
		List<Object> rList=new ArrayList<Object>();
		for(Object o:list){
			long begin=System.currentTimeMillis();
			Proxy p=(Proxy)o;
			List<Stock> r = Fetch_ServeralStock_Sina.fetch("sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",p.getIp(),p.getPort());
            	
			long end=System.currentTimeMillis();
			if(r.size()!=14){
				begin=System.currentTimeMillis();
				r = Fetch_ServeralStock_Sina.fetch("sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",p.getIp(),p.getPort());
				end=System.currentTimeMillis();
				if(r.size()!=14){
					begin=System.currentTimeMillis();
					r = Fetch_ServeralStock_Sina.fetch("sh600128,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600013,sh600014,sh600015,sh600016,sh600017",p.getIp(),p.getPort());
					end=System.currentTimeMillis();
					if(r.size()!=14){
						p.setLastTtl(-1L);
						System.out.println("检测"+p.getIp()+"代理已经失效！");
					}else{
						p.setLastTtl((end-begin));
						rList.add(p);
					}
				}else{
					p.setLastTtl((end-begin));
					rList.add(p);
				}
				
			}else{
				p.setLastTtl((end-begin));
				rList.add(p);
			}
			System.out.println("检测"+p.getIp()+"代理TTL:"+p.getLastTtl());
		}
		return list;
	}
}