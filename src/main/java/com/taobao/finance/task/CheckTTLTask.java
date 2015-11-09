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
			List<Stock> r = Fetch_ServeralStock_Sina.fetch("sh600001,sz399001,sz399006",p.getIp(),p.getPort());
			//List<Stock> r = Fetch_ServeralStock_Sina.fetch("sh600001,sz399001,sz399006",null,null);	
			long end=System.currentTimeMillis();
			if(r.size()==0){
				begin=System.currentTimeMillis();
				r = Fetch_ServeralStock_Sina.fetch("sh600001,sz399001,sz399006",p.getIp(),p.getPort());
				end=System.currentTimeMillis();
				if(r.size()==0){
					begin=System.currentTimeMillis();
					r = Fetch_ServeralStock_Sina.fetch("sh600001,sz399001,sz399006",p.getIp(),p.getPort());
					end=System.currentTimeMillis();
					if(r.size()==0){
						p.setLastTtl(-1L);
						//System.out.println("检测"+p.getIp()+"代理已经失效！");
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
			//System.out.println("检测"+p.getIp()+"代理TTL:"+p.getLastTtl());
		}
		return rList;
	}
}