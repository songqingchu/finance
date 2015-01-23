package com.taobao.finance.choose.realtime;

import java.util.Date;
import java.util.List;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.Base_Choose;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;

/**
 * 每天两点实时检测：1. 温和放量模型（三天均价不高于前5日均价的4%，温和放量）
 *                  2. 倍量模型
 *                  3. 价增量减
 * 
 * @author Administrator
 */

public class V2U_Realtime_Choose extends Base_Choose{
	
	public  List<Stock> prepareData(String symbol,Date d){
		List<Stock> l=Hisdata_Base.readHisData(symbol, d);
		Stock today = Fetch_SingleStock.fetch(symbol);
		l.add(today);
		return l;
	}
	
	public void printAnanyseResult() {
		System.out.print("\n\n\n\n\n");
		List<Stock> l=chooseBV();
		for (Stock st : l) {
			System.out.println(st.getCode());
		}
		System.out.print("\n共计："+l.size());
	}

	
	public  void printAnanyseResultDetail(){
		System.out.print("\n\n\n\n\n");
		List<Stock> l=chooseBV();
		for (Stock st : l) {
			System.out.println(st.getCode()+":  "+st.getVrate());
		}
		System.out.print("共计："+l.size());
	}

	public static void main(String args[]) {
		new V2U_Realtime_Choose().printAnanyseResult();
	}
	
	
}
