package com.taobao.finance.comparator;

import com.taobao.finance.dataobject.Stock;

public class Comparator {

	public static class RateAndRealRateComparator implements
			java.util.Comparator<Stock> {

		public int compare(Stock o1, Stock o2) {
			return o1.getRealRate() - o2.getRealRate() > 0 ? -1 : 1;
		}

	}

	public static class HoldereComparator implements
			java.util.Comparator<Stock> {

		public int compare(Stock o1, Stock o2) {
			return o1.getSuo()*10000 - o2.getSuo()*10000 > 0 ? 1 : -1;
		}

	}

	public static class RateDescComparator implements
			java.util.Comparator<Stock> {
		public int compare(Stock o1, Stock o2) {
			// System.out.println(o1.getName()+":"+o2.getName());
			if (o1.getConcern().equals(o2.getConcern())) {
				return o1.getRate().compareTo(o2.getRate());
				
				//return o1.getRate() - o2.getRate() >= 0 ? -1 : 1;
			} else {
				return o1.getConcern().compareTo(o2.getConcern());
				//return o1.getConcern() - o2.getConcern() <= 0 ? -1 : 1;
			}

			/*
			 * if(o1==o2){ return -1; } if (o1.getConcern() - o2.getConcern() >
			 * 0) { return 1; } if (o1.getConcern() - o2.getConcern() == 0) {
			 * return o1.getRate() - o2.getRate() >= 0 ? -1 : 1; } if
			 * (o1.getConcern() - o2.getConcern() <0) { return o1.getRate() -
			 * o2.getRate() >= 0 ? -1 : 1; } return 1;
			 */
		}
	}

	public static class ChooseComparator implements java.util.Comparator<Stock> {
		public int compare(Stock o1, Stock o2) {
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			if (o1.getTing() == true && o2.getTing() == true) {
				return 1000000;
			}
			if (o1.getTing() == true && o2.getTing() == false) {
				return 1000000;
			}
			if (o1.getTing() == false && o2.getTing() == true) {
				return -1;
			}
			if (o1 == o2) {
				return 1;
			}
			return 1;
		}
	}

	public static class HotMonneyComparator implements
			java.util.Comparator<Stock> {

		public int compare(Stock o1, Stock o2) {
			if (o1.getSymbol().equals(o2.getSymbol())) {
				if (o1.getHardenDate().before(o2.getHardenDate())) {
					return 1;
				} else if (o1.getHardenDate().after(o2.getHardenDate())) {
					return -1;
				} else {
					return 0;
				}
			} else {
				if (o1.getSymbol().compareTo(o2.getSymbol()) > 0) {
					return 1;
				} else if (o1.getSymbol().compareTo(o2.getSymbol()) < 0) {
					return -1;
				} else {
					return 0;
				}
			}
		}

	}
}
