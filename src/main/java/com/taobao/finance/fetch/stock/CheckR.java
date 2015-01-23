package com.taobao.finance.fetch.stock;

import java.util.ArrayList;
import java.util.List;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.util.FetchUtil;

public class CheckR {
	public static List<String> r=FetchUtil.readAllLineFileListAbsolute(FetchUtil.FILE_STOCK_ANASYS_BASE+"rongQuan.txt");
	
	public static void main(String args[]){
	   List<String> f=new ArrayList<String>();
	   List<Stock> st=new ArrayList<Stock>();
	   Float totalRate=0F;
	   int valid=0;
	   for(String s:r){
		   if(s.startsWith("sz002")||s.startsWith("sz300")){
			   f.add(s);
			   Stock d=Fetch_SingleStock.fetch(s);
			   if(d.getRate()<0.1&&d.getRate()>-0.1){
				   totalRate=totalRate+d.getRate();
				   st.add(d);
				   valid++;
			   }
			   
		   }
	   }
	   System.out.println(totalRate/valid);
	   for(Stock s:st){
		   System.out.println(s.getName()+"\t"+s.getRateString());
	   }
	}
}
