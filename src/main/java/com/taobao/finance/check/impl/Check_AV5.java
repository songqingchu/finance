package com.taobao.finance.check.impl;

import java.util.List;

import com.taobao.finance.check.Check;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.CheckUtils;

public class Check_AV5 extends Check{

	public String name="AV5";
	@Override
	public boolean match(List<Stock> l) {
		return CheckUtils.check5(l);
	}
	@Override
	public void printName() {
		System.out.println(this.name+"---------------------");
	}
	
	public static void main(String args[]){
		new Check_AV5().check("600875");
	}
}
