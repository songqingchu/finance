package com.taobao.finance.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.stock.Fetch_AllStock;

public class VGolden_Recent_Anasys {
	
	public static List<Date> check(List<Stock> l){
    	List<Date> l2=new ArrayList<Date>();
    	if(l==null){
    		return l2;
    	}
    	if(l.size()<3){
    		return l2;
    	}
    	int size=l.size();
    	int[] a=new int[size];
    	if(size>0){
    		for(int i=0;i<size-2;i++){
    			Stock today=l.get(i);
    			Stock tomorow=l.get(i+1);
    			Stock tomorow2=l.get(i+2);
    			
    			Long v1=today.getTradeNum();
    			Long v2=tomorow.getTradeNum();
    			
                Float t1End=Float.parseFloat(today.getEndPrice());
    			Float t2End=Float.parseFloat(tomorow.getEndPrice());
    			
    			Float r1=(t2End-t1End)/t1End*100;

    			Float rr1=v2.floatValue()/v1.floatValue();
				
    			if(v2==0){
    				continue;
    			}
				if(v1<v2&&r1>6){
    				if(rr1>2){
    					if(i<size-3){
    						
    						Stock s1=l.get(i+2);
    						Stock s2=l.get(i+3);
    						if(s1.getSymbol().contains("sz002602")){
    							s1.get_10changes();
    						}
    						if(((s1.getTradeNum().floatValue()/10000)*(s2.getTradeNum().floatValue()/10000))/
    								((v2.floatValue()/10000)*(v2.floatValue()/10000))<0.5){
    							//l2.add(today.getDate());
    							a[i+1]=1;
    						}
    					}else if(i==size-3){
    						Stock s1=l.get(i+2);
    						if(s1.getTradeNum()<v2){
    							//l2.add(today.getDate());
    							a[i+1]=1;
    						}
    					}else if(i==size-2){
    						a[i+1]=1;
    					}else{
    						//l2.add(today.getDate());
							a[i+1]=1;
    					}
    				}
    			}
    		}
    	}

    	for(int i=size-1;i>1;i--){
    		if(a[i]==1){
    			for(int j=i-1;j>=1;j--){
    				a[j]=0;
    			}
    		}
    	}
    	for(int i=size-1;i>0;i--){
    		if(a[i]==1){
    			if(i==size-2){
    				l2.add(l.get(i).getDate());
    			}else if(size-i>10){
    				continue;
    			}else{
    				Float startPrice=Float.parseFloat(l.get(i+1).getStartPrice());
    				Long v=l.get(i+1).getTradeNum();
    				boolean success=true;
    				for(int j=i+2;j<=size-1;j++){
    					Stock st=l.get(j);
        				if(Float.parseFloat(st.getEndPrice())<startPrice){
        					success=false;
        				}
        				if(j<i+4&&j<size-1){
        					if(l.get(j).getTradeNum()>=v){
        					    success=false;
        					}
        				}
        				
        			}
    				if(success){
    					l2.add(l.get(i).getDate());
    				}
    			}
    			
    		}
    	}
    	return l2;
    }
  
	public static void main(String args[]){
		Map<String,Stock> allMap=Fetch_AllStock.map;
		int i= 1;
		Map<Stock,List<Date>> m=new HashMap<Stock,List<Date>>();
		
		Collection<Stock> ss=allMap.values();
		for(Stock s:ss){
			String symbol=s.getSymbol();
    		List<Stock> history=Hisdata_Base.readHisData(symbol, null);
    		List<Date> ld=check(history);
    		
    		if(ld.size()>0){
    			s.setGoldenDate(ld.get(0));
    			m.put(s, ld);
    		}
    		i++;
		}
		Set<Stock> s=m.keySet();
		
		List<Stock> l=new ArrayList<Stock>();
		l.addAll(s);
		Collections.sort(l,new Comparator<Stock>(){
			public int compare(Stock s1,Stock s2){
				if(s1.getGoldenDate().before(s2.getGoldenDate())){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		
		for(Stock st:l){
			System.out.print(st.getCode());
			//System.out.print(st.getCode()+"  "+ st.getNameEclipseFormat()+"  "+FetchUtil.FILE_FORMAT.format(st.getGoldenDate())+"  ");
			System.out.println("");
		}
		System.out.println(s.size());
		
	}
}

