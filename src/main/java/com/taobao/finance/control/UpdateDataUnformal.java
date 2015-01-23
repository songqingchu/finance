package com.taobao.finance.control;

import com.taobao.finance.base.Hisdata_Base;

public class UpdateDataUnformal {

	public static void main(String args[]){
		
		long start=System.currentTimeMillis();
		Hisdata_Base.updateDataHistoryDataUnFormal();
		long end=System.currentTimeMillis();
	}
}
