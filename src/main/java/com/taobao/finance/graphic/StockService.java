package com.taobao.finance.graphic;

import java.util.ArrayList;
import java.util.List;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_ServeralStock_Sina;

public class StockService {

	public List<Stock> getConcern(){
		List<Stock> l=new ArrayList<Stock>();
		Stock s=Fetch_AllStock.map.get("sh600001");
		l.add(s);
		return l;
	}
	
	public List<Stock> getRealPrice(List<Stock> list){
		//System.out.println("刷新页面！");                                                //海南瑞泽            青青稞酒           重庆啤酒           江泉实业          
		List<Stock> l=Fetch_ServeralStock_Sina.fetch("sh000001,sz399001,sz399101,sz399006,sz002596,sz002646,sh600132,sh600212",null,null);
		return l;
	}
}
