package com.taobao.finance.graphic;

import java.util.List;

import com.taobao.finance.dataobject.Stock;

public class FetchThread extends Thread{
	StockService stockService=new StockService();
	public StockFrame frame;
	
	public FetchThread(StockFrame frame){
		this.frame=frame;
	}
	
	public void run(){
		List<Stock> li=stockService.getRealPrice(null);
		frame.showStock(li);
	}
}
