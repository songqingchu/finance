package com.taobao.finance.util;

import java.util.ArrayList;
import java.util.List;

import com.taobao.finance.dataobject.Stock;


/**
 * 微调参数，兼顾精确模型，以及不遗漏数据，增加校验
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
	
	
	/**
     *  1  六天涨幅不超过20
     *  2  最近六天内，不要暴涨暴跌【-4，+7】
     *  3  六天必须有涨幅
     *  4  如果最高价不是近日股价，那么现价和最高价只差不能高于3
     *  5  13日最低价不能出现在最近4日中
     *  6  最近13日振幅不能大于25
     *  7  最近3日10均线上翘，最近6日5日均线上翘
	 */
	public static boolean check5(List<Stock> l) {
		Float rate=1.25F;
		int day=13;
		
		if (l.size() < 37) {
			return false;
		}

		Float end1 = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float end6 = Float.parseFloat(l.get(l.size() - 6).getEndPrice());
		//期间幅度不能过大
		if (end1 / end6 > 1.20F) {
			return false;
		}

		List<Float> av5 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();

		Float max = 0F;
		Float min = 10000F;
		int maxIndex = 0;
		int minIndex = 0;

		//考察周期的长度
		for (int i = 1; i < day + 1; i++) {
			Float today5 = getAve(l, 5, l.size() - i);
			Float today10 = getAve(l, 10, l.size() - i);
			Float today20 = getAve(l, 20, l.size() - i);
			Float p = Float.parseFloat(l.get(l.size() - i).getEndPrice());
			Float p2 = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			//不要暴涨暴跌,不要在最近6天内
			if (p / p2 < 0.96F&&i<6) {
				return false;
			}
			if (p / p2 > 1.07F&&i<6) {
				return false;
			}
			
			if (p > max) {
				max = p;
				maxIndex = i;
			}
			if (p < min) {
				min = p;
				minIndex = i;
			}
            //均线
			av5.add(today5);
			av10.add(today10);
			av20.add(today20);
		}

		//必须要有涨幅
		if (end1 / min < 1.01F) {
			return false;
		}

		if (maxIndex == 2 || maxIndex == 3) {
			if (end1 / max < 0.97F) {
				return false;
			}
		}
		if (maxIndex > 3&&max/end1>1.03F) {
			return false;
		}
		if (minIndex < 4) {
			return false;
		}

		//很大的振幅
		if (max / min > rate) {
			return false;
		}

		for (int i = 0; i < 3; i++) {
			if (av10.get(i) < av10.get(i + 1)) {
				return false;
			}
		}
		for (int i = 0; i < 5; i++) {
			if (av5.get(i) < av5.get(i + 1)) {
				return false;
			}
		}
		return true;
	}
}
