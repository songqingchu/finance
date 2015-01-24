package com.taobao.finance.filter;

import com.taobao.finance.dataobject.Stock;

public class Filter {

	public static class NormalFilter implements com.taobao.finance.filter2.Filter{
		@Override
		public boolean filter(Stock s) {
			if(s.getSymbol().startsWith("300")||s.getSymbol().startsWith("sz300")){
				return false;
			}
			
			if(s.getTwoMonth()!=null){
				if(s.getMonth()>0&&s.getTwoMonth()>0&&s.getMonth()<s.getTwoMonth()){
					if(s.getRate()==null){
						return false;
					}
					if(s.getRate1()==null){
						return false;
					}
					if(s.getRate2()==null){
						return false;
					}
					if(s.getRate1()<0){
						if(Float.parseFloat(s.getEndPrice1())-
								Float.parseFloat(s.getStartPrice1())>0){
							return false;
						}
						if(Float.parseFloat(s.getEndPrice2())-
								Float.parseFloat(s.getStartPrice2())>0){
							return false;
						}
						if(s.getSymbol().equals("sh600186")){
							s.get_10changes();
						}
						/*if(s.getRate()>=0){
							if(s.getRate()>0.05F){
								//return false;
								s.get_10changes();
							}else{
								return true;
								//s.get_10changes();
							}
						}else{
							if(1-Float.parseFloat(s.getEndPrice())/Float.parseFloat(s.getStartPrice())<0.05F){
								//return true;
								s.get_10changes();
							}
						}*/
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())>0){
							return true;
							//s.get_10changes();
						}
						
					}
				}
			}else{
				if(s.getMonth()>0){
					if(s.getRate()==null){
						return false;
					}
					if(s.getRate1()==null){
						return false;
					}
					if(s.getRate2()==null){
						return false;
					}
					if(s.getRate1()<0){
						if(Float.parseFloat(s.getEndPrice1())-
								Float.parseFloat(s.getStartPrice1())>0){
							return false;
						}
						if(Float.parseFloat(s.getEndPrice2())-
								Float.parseFloat(s.getStartPrice2())>0){
							return false;
						}
						if(s.getSymbol().equals("sh600186")){
							s.get_10changes();
						}
						/*if(s.getRate()>=0){
							if(s.getRate()>0.05F){
								//return false;
								s.get_10changes();
							}else{
								return true;
								//s.get_10changes();
							}
						}else{
							if(1-Float.parseFloat(s.getEndPrice())/Float.parseFloat(s.getStartPrice())<0.05F){
								//return true;
								s.get_10changes();
							}
						}*/
						if(Float.parseFloat(s.getEndPrice())-
								Float.parseFloat(s.getStartPrice())>0){
							return true;
							//s.get_10changes();
						}
						
					}
				}
			}
			
			return false;
		}
	}
	
	
	
	
}
