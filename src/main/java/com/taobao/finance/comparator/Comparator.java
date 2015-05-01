package com.taobao.finance.comparator;

import com.taobao.finance.dataobject.Stock;

public class Comparator {

	public static class RateAndRealRateComparator implements java.util.Comparator<Stock>{

		public int compare(Stock o1, Stock o2) {
			return o1.getRealRate()-o2.getRealRate()>0?-1:1;
		}
		
	}
	public static class RateDescComparator implements java.util.Comparator<Stock>{

		public int compare(Stock o1, Stock o2) {
			return o1.getRate()-o2.getRate()>0?-1:1;
		}
		
	}
	
	public static class HotMonneyComparator implements java.util.Comparator<Stock>{


		public int compare(Stock o1, Stock o2) {
			if(o1.getSymbol().equals(o2.getSymbol())){
			    if(o1.getHardenDate().before(o2.getHardenDate())){
			    	return 1;
			    }else if(o1.getHardenDate().after(o2.getHardenDate())){
			    	return -1;
			    }else{
			    	return 0;
			    }
			}else{
				if(o1.getSymbol().compareTo(o2.getSymbol())>0){
					return 1;
				}else if(o1.getSymbol().compareTo(o2.getSymbol())<0){
					return -1;
				}else{
					return 0;
				}
			}
		}
		
	}
}
