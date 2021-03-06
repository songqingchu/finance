package com.taobao.finance.check.impl;

import java.util.List;

import com.taobao.finance.check.Check;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;

public class Check_AV10 extends Check{
	public String name="AV10";
	@Override
	public boolean match(List<Stock> history) {
		return CheckUtil.checkAV10XRate(history);
	}	
	@Override
	public void printName() {
		System.out.println(this.name+"--------------------");
	}
	
	public static void main(String args[]){
		new Check_AV10().check("sh600213");
	}

}
