package com.taobao.finance.control;
import com.taobao.finance.base.Hisdata_Base;

public class UpdateData {
	public static void main(String args[]){
		long start=System.currentTimeMillis();
		Hisdata_Base.updateDataHistoryData();
		long end=System.currentTimeMillis();
	}
}
