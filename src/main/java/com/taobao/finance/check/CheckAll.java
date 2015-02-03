package com.taobao.finance.check;

import java.util.ArrayList;
import java.util.List;

import com.taobao.finance.check.impl.Check_AV10;
import com.taobao.finance.check.impl.Check_AV20;
import com.taobao.finance.check.impl.Check_AV5;
import com.taobao.finance.check.impl.Check_AVCU;
import com.taobao.finance.check.impl.Check_BigTrend;
import com.taobao.finance.check.impl.Check_TP;

public class CheckAll {	
	public static void check(String symbol){
		
	    List<Check> l=new ArrayList<Check>();
	    l.add(new Check_AV5());
	    l.add(new Check_AV10());
	    l.add(new Check_AV20());
	    l.add(new Check_AVCU());
	    l.add(new Check_BigTrend()); 
	    l.add(new Check_TP()); 
	    for(Check c:l){
	    	c.check(symbol);
	    }
	}
	
	
	public static void main(String args[]){
		check("300019");
	}
}
