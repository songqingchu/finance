package com.taobao.finance.util;

import java.util.ArrayList;
import java.util.List;
import com.taobao.finance.dataobject.Stock;


/**
 * 微调参数，兼顾精确模型，以及不遗漏数据
 * @author Administrator
 */
public class CheckUtils {

	public static Float getAve(List<Stock> l, int day, int start) {
		Float ave = 0F;
		Float total = 0F;
		for (int i = start; i >= start - day + 1; i--) {
			Float s=Float.parseFloat(l.get(i).getEndPrice());
			total = total + s;
		}
		ave = total / day;
		return ave;
	}
	
	
	/**
	 * 参数调小，尽量早发现
	 */
	public static boolean checkBigTrend(List<Stock> l) {
		if (l.size() < 100) {
			return false;
		}
		List<Float> av60 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av5 = new ArrayList<Float>();

		//25条数据
		for (int i = 1; i < 26; i++) {
			Float today60 = getAve(l, 60, l.size() - i);	
			Float today20 = getAve(l, 20, l.size() - i);		
			Float today10 = getAve(l, 10, l.size() - i);
			Float today5 = getAve(l, 5, l.size() - i);
			av60.add(today60);
			av20.add(today20);
			av10.add(today10);
			av5.add(today5);
		}
       
		Float r = (av10.get(0) - av10.get(24)) / 30;
		int count60 = 0;
		int count20 = 0;
        int count10=0;
        int cross=0;
        
		for (int i = 0; i < 24; i++) {
			if (av60.get(i)> av60.get(i + 1)) {
				count60++;
			}
			if (av20.get(i)> av20.get(i + 1)) {
				count20++;
			}
			if (av10.get(i)> av10.get(i + 1)) {
				count10++;
			}
			//击穿
			if(l.get(l.size()-i-1).getEndPriceFloat()<av10.get(i)){
				cross++;
			}
		}

		if (cross >4) {
			return false;
		}
		if (count60 < 16) {
			return false;
		}
		if (count20 < 18) {
			return false;
		}
		if (count10 < 20) {
			return false;
		}
		
		if (av10.get(0) / av10.get(10) < 1.1F) {
			return false;
		}

		int angerCount10=0;
		for (int i = 0; i < 15; i++) {
			Float r2 = (av10.get(i) - av10.get(i + 5)) / 5;
			Float p = r2 / r;
			if (p < 0.15F) {
				angerCount10++;
			}
		}
		if (angerCount10>2) { 
			return false; 
		}
		return true;
	}
}
