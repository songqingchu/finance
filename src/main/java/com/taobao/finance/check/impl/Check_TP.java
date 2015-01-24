package com.taobao.finance.check.impl;

import java.util.List;

import com.taobao.finance.check.Check;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtil;

public class Check_TP extends Check{
	public String name="TP";
	@Override
	public boolean match(List<Stock> history) {
		return CheckUtil.checkTP(history);
	}

	public static void main(String args[]){
		new Check_AV5().check("sh600213");
	}

	@Override
	public void printName() {
		System.out.println(this.name+"----------------------");
	}
}
