package com.taobao.finance.choose.local;

import java.util.Date;
import java.util.List;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.Base_Choose;
import com.taobao.finance.dataobject.Stock;

/**
 * 连续两天或者连续三天小阳分析
 */
public class CLY_Local_Choose extends Base_Choose{
	public  List<Stock> prepareData(String symbol,Date d){
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}
	
	public void printAnanyseResult() {
		List<Stock> l=chooseCLY();
		System.out.print("\n\n\n\n\n");
		for (Stock st : l) {
			System.out.println(st.getCode());
		}
		System.out.print("\n共计："+l.size());
	}
	
	public  void printAnanyseResultDetail(){
		List<Stock> l=chooseCLY();
		for (Stock st : l) {
			System.out.println(st.getName());
		}
		System.out.print("共计："+l.size());
	}

	public static void main(String args[]) {
		
		new CLY_Local_Choose().printAnanyseResult();
		
		//new CLY_Local_Choose().printAnanyseResultDetail();
		
	}
}
