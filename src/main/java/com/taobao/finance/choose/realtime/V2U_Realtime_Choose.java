package com.taobao.finance.choose.realtime;

import java.util.Date;
import java.util.List;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.Base_Choose;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;

/**
 * ÿ������ʵʱ��⣺1. �ºͷ���ģ�ͣ�������۲�����ǰ5�վ��۵�4%���ºͷ�����
 *                  2. ����ģ��
 *                  3. ��������
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
		System.out.print("\n���ƣ�"+l.size());
	}

	
	public  void printAnanyseResultDetail(){
		System.out.print("\n\n\n\n\n");
		List<Stock> l=chooseBV();
		for (Stock st : l) {
			System.out.println(st.getCode()+":  "+st.getVrate());
		}
		System.out.print("���ƣ�"+l.size());
	}

	public static void main(String args[]) {
		new V2U_Realtime_Choose().printAnanyseResult();
	}
	
	
}
