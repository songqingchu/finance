package com.taobao.finance.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;


public class CheckUtil {


	public static boolean checkIsPlatform(List<Stock> l) {
		if (l == null) {
			return false;
		}
		if (l.size() < 6) {
			return false;
		}

		Long maxV = 0l;
		boolean success = true;
		Float high = 0.0F;
		Float low = 1000.0F;
		for (Stock s : l) {
			Float p = Float.parseFloat(s.getEndPrice());
			if (s.getTradeNum() > maxV) {
				maxV = s.getTradeNum();
			}
			if (p > high) {
				high = p;
			}
			if (p < low) {
				low = p;
			}
		}
		if (high / low > 1.1) {
			return false;
		}
		return true;
	}


	public static List<Date> checkContinueLittleSun(List<Stock> l) {
		List<Date> d = new ArrayList<Date>();
		List<Date> d3 = check3ContinueLittleSun(l);
		List<Date> d2 = check2ContinueLittleSun(l);
		d.addAll(d3);
		d.addAll(d2);

		return d;
	}

	public static List<Date> checkContinueLittleSun2(List<Stock> l) {
		List<Date> d = new ArrayList<Date>();
		List<Date> d3 = check3ContinueLittleSun(l);
		l.remove(0);
		List<Date> d2 = check2ContinueLittleSun(l);
		d.addAll(d3);
		d.addAll(d2);

		return d;
	}


	public static List<Date> check3ContinueLittleSun(List<Stock> l) {
		List<Date> d = new ArrayList<Date>();
		if (l.size() < 8) {
			return d;
		}
		for (int i = 1; i < l.size() - 6; i++) {
			Stock yesterday = l.get(l.size() - i - 3);
			Stock today = l.get(l.size() - i - 2);
			Stock tomorrow = l.get(l.size() - i - 1);
			Stock tomorrow2 = l.get(l.size() - i);

			Float t0Begin = Float.parseFloat(yesterday.getStartPrice());
			Float t0End = Float.parseFloat(yesterday.getEndPrice());

			Float t1Begin = Float.parseFloat(today.getStartPrice());
			Float t1End = Float.parseFloat(today.getEndPrice());

			Float t2Begin = Float.parseFloat(tomorrow.getStartPrice());
			Float t2End = Float.parseFloat(tomorrow.getEndPrice());

			Float t3Begin = Float.parseFloat(tomorrow2.getStartPrice());
			Float t3End = Float.parseFloat(tomorrow2.getEndPrice());

			Long total = 0L;
			for (int j = l.size() - i - 3; j > l.size() - i - 8; j--) {
				total = total + l.get(j).getTradeNum();
			}

			Long v1 = today.getTradeNum();
			Long v2 = tomorrow.getTradeNum();
			Long v3 = tomorrow2.getTradeNum();


			if ((v1 + v2 + v3) / 3 >= total * 1.5 / 5) {
				if (t3End > t3Begin && t2End > t2Begin && t1End > t1Begin) {
					if (t3End > t2End && t2End > t1End) {
						if (v1 < v2 && v1 < v3) {
							if (t3End / t1End > 1.04 && t3End / t1End < 1.09) {
								d.add(l.get(l.size() - i - 2).getDate());
							}
						}
					}
				}
			}
		}

		return d;
	}


	public static List<Date> check2ContinueLittleSun(List<Stock> l) {
		List<Date> d = new ArrayList<Date>();
		if (l.size() < 7) {
			return d;
		}
		for (int i = 1; i < l.size() - 6; i++) {
			Stock yesterday = l.get(l.size() - i - 2);
			Stock today = l.get(l.size() - i - 1);
			Stock tomorrow = l.get(l.size() - i);

			Float t0Begin = Float.parseFloat(yesterday.getStartPrice());
			Float t0End = Float.parseFloat(yesterday.getEndPrice());

			Float t1Begin = Float.parseFloat(today.getStartPrice());
			Float t1End = Float.parseFloat(today.getEndPrice());

			Float t2Begin = Float.parseFloat(tomorrow.getStartPrice());
			Float t2End = Float.parseFloat(tomorrow.getEndPrice());

			Long total = 0l;
			for (int j = l.size() - i - 2; j > l.size() - i - 7; j--) {
				total = total + l.get(j).getTradeNum();
			}

			Long v1 = today.getTradeNum();
			Long v2 = tomorrow.getTradeNum();


			if ((v1 + v2) / 2 >= total * 1.5 / 5) {
				if (t2End > t2Begin && t1End > t1Begin) {
					if (t2End > t1End) {
						if (v1 < v2) {
							if (t2End / t0End < 1.07) {
								d.add(l.get(l.size() - i - 1).getDate());
							}
						}
					}
				}
			}
		}

		return d;
	}


	public static boolean checkCLY(List<Stock> l) {
		if (l.size() < 8) {
			return false;
		}
		if (l.get(0).getCode().equals("000519")) {
			l.size();
		}
		Stock yesterday = l.get(l.size() - 4);
		Stock today = l.get(l.size() - 3);
		Stock tomorrow = l.get(l.size() - 2);
		Stock tomorrow2 = l.get(l.size() - 1);

		Float t0Begin = Float.parseFloat(yesterday.getStartPrice());
		Float t0End = Float.parseFloat(yesterday.getEndPrice());

		Float t1Begin = Float.parseFloat(today.getStartPrice());
		Float t1End = Float.parseFloat(today.getEndPrice());

		Float t2Begin = Float.parseFloat(tomorrow.getStartPrice());
		Float t2End = Float.parseFloat(tomorrow.getEndPrice());

		Float t3Begin = Float.parseFloat(tomorrow2.getStartPrice());
		Float t3End = Float.parseFloat(tomorrow2.getEndPrice());

		Long v1 = today.getTradeNum();
		Long v2 = tomorrow.getTradeNum();
		Long v3 = tomorrow2.getTradeNum();


		if (t3End > t3Begin && t2End > t2Begin && t1End > t1Begin) {
			if (t3End > t2End && t2End > t1End) {
				if (v1 < v2 && v1 < v3) {
					if (t3End / t1Begin > 1.03 && t3End / t1Begin < 1.09) {


						/*
						 * Integer total=0; for(int i=4;i<10;i++){
						 * total=total+l.get(l.size()-i).getTradeNum(); }
						 */
						// if((v1+v2+v3)/3>total*1.4/5){
						Float rrr1 = t1End / t0End;
						Float rrr2 = t2End / t1End;
						Float rrr3 = t3End / t2End;

						if (rrr1 < 1.025 && rrr1 > 0.975) {
							if (rrr2 < 1.03 && rrr2 > 1.00) {
								if (rrr3 < 1.05 && rrr3 > 1.00) {
									return true;
								}
							}
							// }
						}
					}
				}
			}
		}

		if (t3End > t3Begin && t2End > t2Begin) {
			if (t3End > t2End) {
				if (v1 < v2 && v2 < v3) {
					if (t3End / t1End > 1.03 && t3End / t1End < 1.09) {
						Long total = 0l;
						for (int i = 3; i < 9; i++) {
							total = total + l.get(l.size() - i).getTradeNum();
						}
						if (today.getCode().equals("000756")) {
							today.get_10changes();
						}
						Float rrr1 = t1End / t0End;
						Float rrr2 = t2End / t1End;

						if (rrr1 < 1.03 && rrr1 > 0.975) {
							if (rrr2 < 1.05 && rrr2 > 1.01) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean checkCB(List<Stock> l) {
		if (l.size() < 80) {
			return false;
		}
		int size = l.size();
		boolean has = false;

		for (int i = 1; i < size - 3; i++) {
			int ctn = 0;
			Stock today = l.get(size - i - 1);

			if (today.getCode().equals("002190")) {
				today.get_10changes();
			}
			Stock tomorrow = l.get(size - i);
			Float t1End = Float.parseFloat(today.getEndPrice());
			Float t2End = Float.parseFloat(tomorrow.getEndPrice());
			if (t2End / t1End > 1.097) {
				for (int j = i; j < size; j++) {
					today = l.get(size - j - 1);
					tomorrow = l.get(size - j);
					t1End = Float.parseFloat(today.getEndPrice());
					t2End = Float.parseFloat(tomorrow.getEndPrice());
					if (t2End / t1End > 1.097) {
						ctn++;
					} else {
						break;
					}
				}

			}
			if (ctn < 4) {
				ctn = 0;
			} else {
				if (i < 90) {
					return true;
				} else {
					ctn = 0;
				}
			}
		}
		return false;
	}
	
	public static boolean checkCB2(List<Stock> l) {
		if (l.size() < 90) {
			return false;
		}
		int size = l.size();
		List<Float> rList=new ArrayList<Float>();
		for(int i=size-90;i<l.size();i++){
			Stock today = l.get(size - i - 1);
			Stock tomorrow = l.get(size - i);
			Float t1End = Float.parseFloat(today.getEndPrice());
			Float t2End = Float.parseFloat(tomorrow.getEndPrice());
			rList.add(t2End/t1End);
		}
		int size2=rList.size();
		boolean prevGood=false;
		int continueCount=0;
		for(int i=0;i<size2;i++){
			Float r=rList.get(i);
			if(r>9.96F){
				prevGood=true;
			}else{
				prevGood=false;
				continueCount=0;
			}
			if(prevGood){
				continueCount++;
				if(continueCount>=4){
					return true;
				}
			}			
		}
		return false;
	}
	
	
	
	public static boolean checkCB3(List<Stock> l) {
		if (l.size() < 90) {
			return false;
		}
		int size = l.size();
		List<Float> rList=new ArrayList<Float>();
		for(int i=size-90;i<l.size();i++){
			Stock today = l.get(size - i - 1);
			Stock tomorrow = l.get(size - i);
			Float t1End = Float.parseFloat(today.getEndPrice());
			Float t2End = Float.parseFloat(tomorrow.getEndPrice());
			rList.add(t2End/t1End);
		}
		int size2=rList.size();
		boolean prevGood=false;
		int continueCount=0;
		for(int i=0;i<size2;i++){
			Float r=rList.get(i);
			if(r>9.96F){
				prevGood=true;
			}else{
				prevGood=false;
				continueCount=0;
			}
			if(prevGood){
				continueCount++;
				if(continueCount>=4){
					if(i>=size-20){
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	


	public static boolean checkBV(Stock today, Stock tomorow) {
		Float t1End = Float.parseFloat(today.getEndPrice());
		Float t2End = Float.parseFloat(tomorow.getEndPrice());

		Long v1 = today.getTradeNum();
		Long v2 = tomorow.getTradeNum();

		Float r = t2End / t1End;

		if (r < 1.03) {
			return false;
		}


		Float vrate = r * v2.floatValue() / v1.floatValue();
		tomorow.setVrate(vrate);
		if (v1 * 2 < v2) {
			return true;
		}
		return false;
	}

	public static boolean checkChannel(List<Stock> his) {
		if (his.size() < 60) {
			return false;
		}
		Map<Integer, Float> map = new HashMap<Integer, Float>();
		List<A> l = new ArrayList<A>();
		for (int i = 5; i < his.size(); i++) {
			Float end = Float.parseFloat(his.get(i).getEndPrice());
			l.add(new A(end, i));
			map.put(i, end);
		}
		Collections.sort(l);
		Float max = l.get(0).price;
		int maxIndex = l.get(0).index;
		Float sMax = 0F;
		int sMaxIndex = 0;
		for (int i = 1; i < l.size(); i++) {
			A a = l.get(i);
			if (maxIndex - a.index > 6) {
				sMax = a.price;
				sMaxIndex = a.index;
				break;
			}
		}
		Float nowPrice = (max - sMax) * (his.size() - maxIndex)
				/ (maxIndex - sMaxIndex) + max;
		Float today = Float.parseFloat(his.get(his.size() - 1).getEndPrice());
		Float n = today / nowPrice;
		if (n > 0.99F & n < 1.07F) {
			return true;
		}
		return false;
	}

	static class A implements Comparable {
		Float price;
		int index;

		public A(Float price, int index) {
			this.price = price;
			this.index = index;
		}

		public int compareTo(Object o) {
			A a = (A) o;
			if (this.price - a.price > 0)
				return -1;
			else
				return 1;
		}
	}

	public static boolean checkPT(List<Stock> l) {

		if (l.size() < 10) {
			return false;
		}
		if (l.get(0).getCode().equals("600298")) {
			l.size();
		}
		int size = l.size();
		for (int i = size - 1; i > size - 3; i--) {
			if (checkPlatform(l, i - 1)) {
				if (checkValidPT(l, i - 1)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkP_Buy(List<Stock> l) {

		if (l.size() < 10) {
			return false;
		}
		if (l.get(0).getCode().equals("300051")) {
			l.size();
		}
		Stock s1 = l.get(l.size() - 1);
		Stock s0 = l.get(l.size() - 2);
		Float b1 = Float.parseFloat(s1.getStartPrice());
		Float e1 = Float.parseFloat(s1.getEndPrice());
		Float h1 = Float.parseFloat(s1.getHighPrice());

		Float e0 = Float.parseFloat(s0.getEndPrice());
		if (e1 / e0 < 1.03) {
			return false;
		}
		if ((h1 - e1) / e0 < 0.03) {
			return false;
		}

		if (checkPlatform(l, l.size() - 2)) {
			return true;
		}
		return false;
	}

	public static boolean checkP(List<Stock> l) {
		if (l.size() < 10) {
			return false;
		}
		int size = l.size();
		if (l.get(0).getCode().equals("300026")) {
			l.size();
		}
		if (checkPlatform2(l, size - 1)) {
			return true;
		}

		return false;
	}


	public static boolean checkAve(List<Stock> l) {
		if (l.size() < 20) {
			return false;
		}
		Float today5 = getAve(l, 5);
		Float today10 = getAve(l, 10);
		Float today20 = getAve(l, 20);

		Float y2 = Float.parseFloat(l.get(l.size() - 5).getEndPrice());
		Float yesterday = Float.parseFloat(l.get(l.size() - 2).getEndPrice());
		Float today = Float.parseFloat(l.get(l.size() - 1).getEndPrice());

		if (l.get(0).getCode().equals("002673")) {
			today.getClass();
		}
		if (today / yesterday < 1F) {
			return false;
		}
		if (yesterday / y2 < 0.99F) {
			return false;
		}

		Float tt = today5 - today10;
		if (today10 > today20) {
			if (tt < 0.005 && tt > -0.01) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkAV20(List<Stock> l, Float rate, int time,
			int av10t, int av20t) {
		if (l.size() < 50) {
			return false;
		}

		Float end1 = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float end20 = Float.parseFloat(l.get(l.size() - time).getEndPrice());
		if (end1 / end20 > rate) {
			return false;
		}

		List<Float> av5 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();

		for (int i = 1; i < time + 1; i++) {
			Float today5 = getAve(l, 5, l.size() - i);
			Float today10 = getAve(l, 10, l.size() - i);
			Float today20 = getAve(l, 20, l.size() - i);
			av5.add(today5);
			av10.add(today10);
			av20.add(today20);
		}
		if (end1 > av5.get(0) * 1.01F) {
			return false;
		}
		int count20 = 0;
		int count10 = 0;
		for (int i = 0; i < time - 1; i++) {
			if (av20.get(i) >= av20.get(i + 1)) {
				count20++;
			}
			if (av10.get(i) >= av10.get(i + 1)) {
				count10++;
			}
		}
		if (count20 < av20t) {
			return false;
		}
		if (count10 < av10t) {
			return false;
		}
		Float avv5 = av5.get(0);
		Float avv10 = av10.get(0);
		Float end = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		if (avv5 / avv10 < 0.005 * end && avv5 / avv10 > 0.995 * end) {
			return true;
		}

		return true;
	}

	/**
	 * 1 
	 * @param l
	 * @return
	 */
	public static boolean checkAV20XRate(List<Stock> l) {
		if (l.size() < 60) {
			return false;
		}

		List<Float> av20 = new ArrayList<Float>();

		for (int i = 1; i < 36; i++) {
			Float today20 = getAve(l, 20, l.size() - i);
			av20.add(today20);
		}

		Float r = (av20.get(0) - av20.get(20)) / 20;

		int count20 = 0;

		int angerCount20 = 0;
		for (int i = 0; i < 31; i++) {
			if (av20.get(i) + 0.01 >= av20.get(i + 1)) {
				count20++;
			}
		}
		if (count20 < 28) {
			return false;
		}

		/*for (int i = 0; i < 15; i++) {
			Float r2 = (av20.get(i) - av20.get(i + 10)) / 5;
			Float p = r2 / r;
			if (p > 1.35F || p < 0.65F) {
				angerCount20++;
			}
		}*/
		/*if (angerCount20 > 4) {
			return false;
		}*/

		Float av10 = getAve(l, 10, l.size() - 1);

		Float today = Float.parseFloat(l.get(l.size() - 1).getLowPrice());
		// Float yesterday = Float.parseFloat(l.get(l.size() -
		// 2).getLowPrice());
		if (today < av10) {
			return true;
		}

		return false;
	}

	public static boolean checkPF(List<Stock> l) {
		if (l.size() < 60) {
			return false;
		}

		List<Float> av20 = new ArrayList<Float>();

		for (int i = 1; i < 26; i++) {
			Float today20 = getAve(l, 20, l.size() - i);
			av20.add(today20);
		}

		Float r = (av20.get(0) - av20.get(20)) / 20;

		int count20 = 0;

		int angerCount20 = 0;
		for (int i = 0; i < 20; i++) {
			if (av20.get(i) + 0.01 >= av20.get(i + 1)) {
				count20++;
			}
		}
		if (count20 < 16) {
			return false;
		}

		return checkPlatform(l, l.size() - 2);
	}

	public static boolean checkAV60XRate(List<Stock> l) {
		if (l.size() < 100) {
			return false;
		}

		List<Float> av60 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av5 = new ArrayList<Float>();

		for (int i = 1; i < 32; i++) {
			Float today60 = getAve(l, 60, l.size() - i);
			av60.add(today60);
			Float today20 = getAve(l, 20, l.size() - i);
			av20.add(today20);
			if (i < 5) {
				Float today10 = getAve(l, 10, l.size() - i);
				av10.add(today10);
				Float today5 = getAve(l, 5, l.size() - i);
				av5.add(today5);
			}
		}

		Float r = (av60.get(0) - av60.get(20)) / 20;

		int count60 = 0;
		int count20 = 0;

		int angerCount20 = 0;
		for (int i = 0; i < 30; i++) {
			if (av60.get(i) + 0.01 >= av60.get(i + 1)) {
				count60++;
			}
			if (av20.get(i) + 0.01 >= av20.get(i + 1)) {
				count20++;
			}
		}
		if (count60 < 29) {
			return false;
		}
		if (count20 < 25) {
			return false;
		}
		if (av60.get(0) / av60.get(29) < 1.15F) {
			return false;
		}

		for (int i = 0; i < 20; i++) {
			Float r2 = (av60.get(i) - av60.get(i + 5)) / 5;
			Float p = r2 / r;
			if (p > 1.35F || p < 0.65F) {
				angerCount20++;
			}
		}
		/*
		 * if (angerCount20>8) { return false; }
		 */

		Float today = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float yesterdayLow = Float
				.parseFloat(l.get(l.size() - 2).getLowPrice());
		Float bigYesterdayLow = Float.parseFloat(l.get(l.size() - 3)
				.getLowPrice());
		Float superBigYesterdayLow = Float.parseFloat(l.get(l.size() - 4)
				.getLowPrice());

		if (yesterdayLow < av5.get(1) && yesterdayLow < av10.get(1)
				&& yesterdayLow < av20.get(1)) {
			if (today > av5.get(0) && today > av10.get(0)
					&& today > av20.get(0)) {
				return true;
			}
		}

		if (bigYesterdayLow < av5.get(2) && bigYesterdayLow < av10.get(2)
				&& bigYesterdayLow < av20.get(2)) {
			if (today > av5.get(0) && today > av10.get(0)
					&& today > av20.get(0)) {
				return true;
			}
		}

		if (superBigYesterdayLow < av5.get(3)
				&& superBigYesterdayLow < av10.get(3)
				&& superBigYesterdayLow < av20.get(3)) {
			if (today > av5.get(0) && today > av10.get(0)
					&& today > av20.get(0)) {
				return true;
			}
		}

		return false;
	}


	/**
	 * 瓒呯骇瓒嬪娍
	 * @param l
	 * @return
	 */
	public static boolean checkBigTrend(List<Stock> l) {
		if (l.size() < 100) {
			return false;
		}
		List<Float> av60 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av5 = new ArrayList<Float>();

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
		if (angerCount10>1) { 
			return false; 
		}
		return true;
	}
	
	
	
	
	/**
	 * 瓒呯骇瓒嬪娍
	 * @param l
	 * @return
	 */
	public static boolean checkBigTrend2(List<Stock> l) {
		if (l.size() < 100) {
			return false;
		}
		List<Float> av60 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av5 = new ArrayList<Float>();

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

		int count20 = 0;
        
		for (int i = 0; i < 18; i++) {
			if (av20.get(i)> av20.get(i + 1)) {
				count20++;
			}
		}

		if (count20 < 15) {
			return false;
		}

		if (av20.get(0) / av20.get(10) < 1.15F) {
			return false;
		}

		int angerCount10=0;
		int angerCount20=0;
		for (int i = 0; i < 8; i++) {
			Float p = (av20.get(i) / av20.get(i + 5));
			if (p < 1.05F) {
				angerCount20++;
			}
		}
		
		for (int i = 0; i < 8; i++) {
			Float p = (av10.get(i) / av10.get(i + 5));
			if (p < 1.05F) {
				angerCount10++;
			}
		}
		
		if (angerCount10>2&&angerCount20>2) { 
			return false; 
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * ten day  ave check
	 * @param l
	 * @return
	 */
	public static boolean checkAV10XRate(List<Stock> l) {
		if (l.size() < 80) {
			return false;
		}
		List<Float> av10 = new ArrayList<Float>();
		for (int i = 1; i < 37; i++) {
			Float today10 = getAve(l, 10, l.size() - i);
			av10.add(today10);
		}
		Float r = (av10.get(0) - av10.get(20)) / 20;

		int continueCount10 = 0;
		int angerCount10 = 0;
		for (int i = 0; i < 20; i++) {
			if (av10.get(i) + 0.01 >= av10.get(i + 1)) {
				continueCount10++;
			}
		}
		//10鏃ョ嚎鏈濅笂澶╂暟
		if (continueCount10 < 16) {
			return false;
		}

		//10绾夸笉瑕侀櫋澧為櫋闄�
		for (int i = 0; i < 20; i++) {
			Float r2 = (av10.get(i) - av10.get(i + 5)) / 5;
			Float p = r2 / r;
			if (p > 1.4F || p < 0.6F) {
				angerCount10++;
			}
		}
		if (angerCount10 > 4) {
			return false;
		}

		/*Float todayAv10 = getAve(l, 10, l.size() - 1);
		Float today = Float.parseFloat(l.get(l.size() - 1).getLowPrice());
		// Float yesterday = Float.parseFloat(l.get(l.size() -
		// 2).getLowPrice());
		if (today < todayAv10) {
			return true;
		}*/
		return true;
	}

	public static boolean checkTP(List<Stock> l) {
		if (l.size() < 70) {
			return false;
		}
		String s = l.get(l.size() - 1).toString();

		int maxIndex = -1;
		Float max = 0F;
		Float min = 200F;
		for (int i = 5; i < 70; i++) {
			Float t = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			if (t > max) {
				max = t;
				maxIndex = l.size() - i - 1;
			}
			if (t > max * 0.98F && t < max) {
				if (maxIndex - (l.size() - i - 1) > 5) {
					max = t;
					maxIndex = l.size() - i - 1;
				}
			}

		}

		for (int i = 5; i < 70; i++) {
			Float t = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			if (t < min && (l.size() - i - 1) > maxIndex) {
				min = t;
			}
		}
		if (l.size() - maxIndex < 20) {
			return false;
		}
		if (max / min < 1.04F) {
			return false;
		}
		for (int i = 0; i < 1; i++) {
			Float t = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			if (t / max < 1.09F && t / max > 0.98F) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkAV20Trend(List<Stock> l, Float rate, int day,int av5t, int av10t, int av20t) {
		if (l.size() < 50) {
			return false;
		}

		Float end1 = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float end20 = Float.parseFloat(l.get(l.size() - 20).getEndPrice());
		if (end1 / end20 > rate) {
			return false;
		}

		List<Float> av5 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();

		Float max = 0F;
		Float min = 10000F;
		int maxIndex = 0;
		int minIndex = 0;

		for (int i = 1; i < day + 1; i++) {
			Float today5 = getAve(l, 5, l.size() - i);
			Float today10 = getAve(l, 10, l.size() - i);
			Float today20 = getAve(l, 20, l.size() - i);
			Float p = Float.parseFloat(l.get(l.size() - i).getEndPrice());
			Float p2 = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			if (p / p2 < 0.96F) {
				return false;
			}
			if (p / p2 > 1.06F) {
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

			av5.add(today5);
			av10.add(today10);
			av20.add(today20);
		}

		if (end1 / min < 1.05F) {
			return false;
		}

		if (maxIndex == 2 || maxIndex == 3) {
			if (end1 / max < 0.98F) {
				return false;
			}
		}
		if (maxIndex > 3) {
			if (end1 / max < 1F) {
				return false;
			}
		}

		if (max / min > rate) {
			return false;
		}

		int count20 = 0;
		int count10 = 0;
		int continueCount5 = 0;
		int j = -1;
		for (int i = 0; i < day - 1; i++) {
			if (av20.get(i) >= av20.get(i + 1)) {
				count20++;
			}
			if (av10.get(i) >= av10.get(i + 1)) {
				count10++;
			}
			Float p = Float.parseFloat(l.get(l.size() - i - 1).getEndPrice());
			if (p - av5.get(i) >= 0.005 * (0 - p)) {
				continueCount5++;
			}
		}
		if (count20 < av20t) {
			return false;
		}
		if (count10 < av10t) {
			return false;
		}
		if (continueCount5 <= av5t) {
			return false;
		}

		return true;
	}

	
	
	
	
	
	public static boolean checkWave(List<Stock> l, Float rate, int av10t,
			int av20t) {
		if (l.size() < 50) {
			return false;
		}

		Float end1 = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float end20 = Float.parseFloat(l.get(l.size() - 20).getEndPrice());
		if (end1 / end20 > rate) {
			return false;
		}

		List<Float> av5 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();
		List<Float> av20 = new ArrayList<Float>();

		for (int i = 1; i < 21; i++) {
			Float today5 = getAve(l, 5, l.size() - i);
			Float today10 = getAve(l, 10, l.size() - i);
			Float today20 = getAve(l, 20, l.size() - i);
			av5.add(today5);
			av10.add(today10);
			av20.add(today20);
		}
		int count20 = 0;
		int count10 = 0;
		int continueCount5 = 0;
		int j = -1;
		boolean match = false;
		for (int i = 0; i < 19; i++) {
			if (av20.get(i) >= av20.get(i + 1)) {
				count20++;
			}
			if (av10.get(i) >= av10.get(i + 1)) {
				count10++;
			}
			if (av5.get(i) < av5.get(i + 1)) {

				if (j == i - 1 || j == -1) {
					continueCount5++;
				} else {
					continueCount5 = 1;
				}

				j = i;
				if (continueCount5 >= 4 && j <= 4) {
					match = true;
				}
			}
		}
		if (count20 < av20t) {
			return false;
		}
		if (count10 < av10t) {
			return false;
		}

		if (match && continueCount5 < 6) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * AVCU鐢ㄤ簬鍙戠幇璧锋定鐨勬棭鏈熼樁娈碉紝浠ュ洓鏃ュ舰鎬佷负鐗瑰緛
	 * 1   鍗佹棩娑ㄥ箙涓嶅ぇ浜�20
	 * 2   鑲′环搴旀瘮鍊掓暟绗洓鏃ワ紝鍊掓暟绗簩鏃ラ珮
	 * 3 4鏃ユ定骞呬笉灏�2
	 * 3   鍗曟棩娑ㄥ箙涓嶈兘澶т簬5锛屼笉鑳藉皬浜�2
	 * 4  涓婃定鏃ユ湡涓嶈兘灏忎簬3
	 * 5  涓嬭穼鏃ユ湡涓嶈兘澶т簬1
	 * 6  鍥涙棩娑ㄥ箙涓嶈兘澶т簬8
	 * @param l
	 * @return
	 */
	public static boolean checkAVCU(List<Stock> l) {
		if (l.size() < 26) {
			return false; 
		}		
		Float p10 = Float.parseFloat(l.get(l.size() - 10).getEndPrice());
		Float e = Float.parseFloat(l.get(l.size() - 1).getEndPrice());
		Float e2 = Float.parseFloat(l.get(l.size() - 2).getEndPrice());
		Float e4 = Float.parseFloat(l.get(l.size() - 4).getEndPrice());
		Float r10=e/p10;
	    
		//case1
		if (r10 > 1.20F) {
			return false;
		}
		//case2
		if (e2 > e) {
			return false;
		}
		if (e4 > e) {
			return false;
		}
		if (e/e4<1.02F) {
			return false;
		}
		

		List<Float> av5 = new ArrayList<Float>();
		List<Float> av10 = new ArrayList<Float>();

		
		Float begin = 0F;
		Float end = 0F;
		int downCount = 0;
		int upCount = 0;
		for (int i = 5; i > 1; i--) {
			Float today = Float.parseFloat(l.get(l.size() - i).getEndPrice());
			Float tomorrow = Float.parseFloat(l.get(l.size() - i + 1)
					.getEndPrice());
			if (i == 4) {
				begin = today;
			}
			if (i == 1) {
				end = tomorrow;
			}
			Float r = tomorrow / today;
			if (r > 1.05F) {
				return false;
			}
			if (r > 1F) {
				upCount++;
			}
			if (r < 1F && r >= 0.98F) {
				downCount++;
			}
			if (r < 0.98F) {
				return false;
			}
		}
		//case4
		if (downCount > 1) {
			return false;
		}
		//case5
		if (upCount < 3) {
			return false;
		}
	    //case6
		if (end / begin > 1.08F) {
			return false;
		}

		boolean match = true;

		for (int i = 0; i < 15; i++) {
			Float today5 = getAve(l, 5, l.size()-i-1);
			Float today10 = getAve(l, 10, l.size()-i-1);
			av5.add(today5);
			av10.add(today10);
		}

		Collections.reverse(av5);
		Collections.reverse(av10);


		int count5 = 0;
		for (int i = 7; i < 14; i++) {
			Float today5 = av5.get(i);
			Float tomorrow5 = av5.get(i + 1);

			if (today5 > tomorrow5) {
				count5++;
			}
		}
		if (count5>= 3) {
			return false;
		}

		return match;
	}

	
	
	public static Float getAve(List<Stock> l, int day) {
		Float ave = 0F;
		Float total = 0F;
		for (int i = l.size() - 1; i > l.size() - day - 1; i--) {
			total = total + Float.parseFloat(l.get(i).getEndPrice());
		}
		ave = total / day;
		return ave;
	}

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

	public static boolean checkValidPT(List<Stock> l, int index) {
		Long maxV = 0l;
		Float high = 0.0F;
		Float low = 1000.0F;
		Float total = 0F;
		for (int i = index; i >= index - 5; i--) {
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			if (s.getTradeNum() > maxV) {
				maxV = s.getTradeNum();
			}
			if (p > high) {
				high = p;
			}
		}

		for (int i = l.size() - 1; i > index; i--) {
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			total = total + p;
		}

		Float av = total / (l.size() - 1 - index);
		if (av / high > 1.04) {
			return true;
		}
		return false;
	}

	public static boolean checkValidPT2(List<Stock> l, int index) {

		Float total = 0F;
		Float total2 = 0F;
		for (int i = l.size() - index; i >= l.size() - index - 3; i--) {
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			total2 = total2 + p;
		}

		for (int i = l.size() - 1; i > l.size() - index; i--) {
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			total = total + p;
		}

		Float av = total / (l.size() - 1 - index);
		if (av / total2 * 4 > 1.04) {
			return true;
		}
		return false;
	}

	public static boolean checkPlatform(List<Stock> l, int index) {
		if (l.get(0).getCode().equals("002450")) {
			l.size();
		}
		if (index < 5) {
			return false;
		}
		Long maxV = 0l;
		Float high = 0.0F;
		Float low = 1000.0F;
		int maxindex = -1;
		int minindex = -1;
		int cnt = 0;
		int flag = 0;
		boolean shun = false;
		int idx = 0;
		Float total1 = 0F;
		for (int i = index; i >= index - 6; i--) {
			idx = i;
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			if (p > Float.parseFloat(l.get(i - 1).getEndPrice())) {
				if (cnt == -1) {
					flag = 0;
				}
				cnt = 1;
				if (cnt == 1) {
					flag++;
				}
				if (flag >= 3) {
					shun = true;
				}

			} else {
				if (cnt == 1) {
					flag = 0;
				}
				cnt = -1;
				if (cnt == -1) {
					flag--;
				}
				if (flag <= -3) {
					shun = true;
				}
			}
			if (s.getTradeNum() > maxV) {
				maxV = s.getTradeNum();
			}
			if (p > high) {
				high = p;
				maxindex = i;
			}
			if (p < low) {
				low = p;
				minindex = i;
			}

		}
		if (high / low > 1.05) {
			return false;
		}
		/*
		 * if (shun) { return false; }
		 */
		/*
		 * if (maxindex - minindex >= 3 || minindex - maxindex >= 3) { return
		 * false; }
		 */

		Float today = l.get(l.size() - 1).getEndPriceFloat();
		if (today > ((high + low) / 2) * 1.03) {
			return true;
		}
		return false;
	}

	public static boolean checkPlatform2(List<Stock> l, int index) {
		if (l.get(0).getCode().equals("002533")) {
			l.size();
		}
		if (index < 5) {
			return false;
		}
		Long maxV = 0l;
		Float high = 0.0F;
		Float low = 1000.0F;
		int maxindex = -1;
		int minindex = -1;
		int cnt = 0;
		int flag = 0;
		boolean shun = false;
		int idx = 0;
		Float total1 = 0F;
		for (int i = index; i >= 0; i--) {
			if (i < 1) {
				return false;
			}
			idx = i;
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			if (p > Float.parseFloat(l.get(i - 1).getEndPrice())) {
				if (cnt == -1) {
					flag = 0;
				}
				cnt = 1;
				if (cnt == 1) {
					flag++;
				}
				if (flag >= 3) {
					shun = true;
				}

			} else {
				if (cnt == 1) {
					flag = 0;
				}
				cnt = -1;
				if (cnt == -1) {
					flag--;
				}
				if (flag <= -3) {
					shun = true;
				}
			}

			if (s.getTradeNum() > maxV) {
				maxV = s.getTradeNum();
			}
			if (p > high) {
				high = p;
				maxindex = i;
			}
			if (p < low) {
				low = p;
				minindex = i;
			}
			if (high / low > 1.05) {
				break;
			} else {
				total1 = total1 + p;
			}
		}

		if (index - idx < 5) {
			return false;
		}
		/*
		 * if(shun){ return false; }
		 */

		if (maxindex - minindex >= 3 || minindex - maxindex >= 3) {
			return false;
		}
		Float total = 0F;
		for (int i = idx - 1; i > idx - 5; i--) {
			total = total + Float.parseFloat(l.get(i).getEndPrice());
		}
		Float av1 = total / 4;
		Float av2 = total1 / (index - idx);
		if (av1 / av2 < 0.95) {
			return true;
		}
		return false;
	}

	public static boolean checkPlatformTanLan(List<Stock> l, int index,
			int length) {
		if (l.get(0).getCode().equals("002450")) {
			l.size();
		}
		if (index < 5) {
			return false;
		}
		Long maxV = 0l;
		Float high = 0.0F;
		Float low = 1000.0F;
		int maxindex = -1;
		int minindex = -1;
		int cnt = 0;
		int flag = 0;
		boolean shun = false;
		for (int i = index; i >= index - length; i--) {
			Stock s = l.get(i);
			Float p = Float.parseFloat(s.getEndPrice());
			if (p > Float.parseFloat(l.get(i - 1).getEndPrice())) {
				if (cnt == -1) {
					flag = 0;
				}
				cnt = 1;
				if (cnt == 1) {
					flag++;
				}
				if (flag >= 3) {
					shun = true;
				}

			} else {
				if (cnt == 1) {
					flag = 0;
				}
				cnt = -1;
				if (cnt == -1) {
					flag--;
				}
				if (flag <= -3) {
					shun = true;
				}
			}
			if (s.getTradeNum() > maxV) {
				maxV = s.getTradeNum();
			}
			if (p > high) {
				high = p;
				maxindex = i;
			}
			if (p < low) {
				low = p;
				minindex = i;
			}
		}

		if (high / low > 1.05) {
			return false;
		}
		if (shun) {
			return false;
		}
		if (maxindex - minindex >= 4 || minindex - maxindex >= 4) {
			return false;
		}
		return true;
	}

	/**
	 * 锟斤拷锟狡斤拷锟斤拷
	 * 
	 * @param l
	 * @return
	 */
	public static boolean checkGoldenV(List<Stock> l) {
		List<Date> l2 = new ArrayList<Date>();
		if (l == null) {
			return false;
		}
		if (l.size() < 13) {
			return false;
		}
		if (l.get(0).getCode().equals("000519")) {
			l.size();
		}

		Stock yesterday = l.get(l.size() - 4);
		Stock today = l.get(l.size() - 3);
		Stock tomorow = l.get(l.size() - 2);
		Stock tomorow2 = l.get(l.size() - 1);

		Long v1 = today.getTradeNum();
		Long v2 = tomorow.getTradeNum();
		Long v3 = tomorow2.getTradeNum();

		Float t0End = Float.parseFloat(yesterday.getEndPrice());
		Float t1End = Float.parseFloat(today.getEndPrice());
		Float t2End = Float.parseFloat(tomorow.getEndPrice());
		Float t3End = Float.parseFloat(tomorow2.getEndPrice());

		Float t0Begin = Float.parseFloat(yesterday.getStartPrice());
		Float t1Begin = Float.parseFloat(today.getStartPrice());
		Float t2Begin = Float.parseFloat(tomorow.getStartPrice());
		Float t3Begin = Float.parseFloat(tomorow2.getStartPrice());

		Float r1 = t1End / t0End;
		Float r2 = t2End / t1End;
		Float r3 = t3End / t2End;

		if (!(r3 < r1 && r2 < r1)) {
			return false;
		}

		if (t1End - t1Begin < 0) {
			return false;
		}
		if (t2End - t2Begin < 0) {
			return false;
		}
		if (t3End - t3Begin < 0) {
			return false;
		}
		if (!(t3End > t2End && t2End > t1End)) {
			return false;
		}

		/**
		 * 锟斤拷锟斤拷
		 */
		if (!(v1 > v2 && v1 > v3)) {
			return false;
		}
		Float rr1 = v1.floatValue() / v2.floatValue();
		Float rr2 = v1.floatValue() / v3.floatValue();

		/**
		 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷3
		 */
		if (r1 < 1.05) {
			return false;
		}
		if (!(t1End <= t2End && t2End <= t3End)) {
			return false;
		}
		if (rr1 * rr2 < 2) {
			return false;
		}

		return true;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @param l
	 * @return
	 */
	public static boolean checkPDVD(List<Stock> l) {
		if (l == null) {
			return false;
		}
		if (l.size() < 13) {
			return false;
		}

		Stock yesterday = l.get(l.size() - 4);
		Stock today = l.get(l.size() - 3);
		Stock tomorow = l.get(l.size() - 2);
		Stock tomorow2 = l.get(l.size() - 1);

		Long v1 = today.getTradeNum();
		Long v2 = tomorow.getTradeNum();
		Long v3 = tomorow2.getTradeNum();

		Float t1End = Float.parseFloat(today.getEndPrice());
		Float t2End = Float.parseFloat(tomorow.getEndPrice());
		Float t3End = Float.parseFloat(tomorow2.getEndPrice());

		Float t1Begin = Float.parseFloat(today.getStartPrice());
		Float t2Begin = Float.parseFloat(tomorow.getStartPrice());
		Float t3Begin = Float.parseFloat(tomorow2.getStartPrice());

		if (today.getCode().equals("002488")) {
			today.get_10changes();
		}
		if (checkBV(yesterday, today)) {
			if (t2End < t1End && t3End < t1End && t3End > t1Begin) {
				if (v3 < v2 && v2 < v1) {
					return true;
				}
			}
		}
		if (checkBV(today, tomorow)) {
			if (t3End < t2End && t3End > t2Begin) {
				if (v3 < v2) {
					return true;
				}
			}
		}

		return false;
	}

	public static void main(String args[]) {
		List<Stock> l = Hisdata_Base.readHisData("sh600967", null);
		List<Date> d = checkContinueLittleSun(l);
		if (d.size() > 0) {
			for (Date da : d) {
				System.out.println(FetchUtil.FILE_FORMAT.format(da));
			}
		}
	}
}
