package com.taobao.finance.fetch.stock;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class Anasys_Pass {

	public static Map<String,Stock> fourMap=new HashMap<String,Stock>();
	public static Map<String,Stock> threeMap=new HashMap<String,Stock>();
	public static Map<String,Stock> twoMap=new HashMap<String,Stock>();
	public static Map<String,Stock> oneMap=new HashMap<String,Stock>();
	static{
		Calendar now=Calendar.getInstance();
		//now.add(Calendar.DATE, -2);
		while(true){
			now.add(Calendar.DATE, -1);
			String dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(now.getTime());
			File f=new File(dir);
			if(f.exists()){
				List<String> four=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_4);
				List<String> three=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_3);
				List<String> two=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_2);
				List<String> one=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_1);
				readFile(four,fourMap,now);
				readFile(three,threeMap,now);
				readFile(two,twoMap,now);
				readFile(one,oneMap,now);
			}
			
			if(now.get(Calendar.YEAR)==2013&&now.get(Calendar.MONTH)==8&&now.get(Calendar.DATE)==2){
				break;
			}
		}
		//oneMap.size();
	}
	
	public static void readFile(List<String> containsAllLine,Map<String,Stock> map,Calendar c){
		for(String data:containsAllLine){
			if(data==null){
				continue;
			}
			if(data.isEmpty()){
				continue;
			}
			String[] datas = data.split("\t");
			String key=FetchUtil.FILE_FORMAT.format(c.getTime())+"-"+datas[0];
			int totalCount=0;
			Float totalRate=0F;
			if(data.endsWith("-")){
				for(int i=1;i<datas.length-1;i++){
					totalCount++;
					totalRate=totalRate+Float.parseFloat(datas[i]);
				}
				totalCount--;
			}else{
				for(int i=1;i<datas.length;i++){
					totalCount++;
					totalRate=totalRate+Float.parseFloat(datas[i]);
				}
			}
			Stock s=new Stock();
			s.setSymbol(datas[0]);
			s.setTotalDay(totalCount);
			s.setTotalRate(totalRate);
			s.setRate(Float.parseFloat(datas[1]));
			map.put(key, s);
		}
	}
	
	public  static  String  getAverageCountAndRate(Map<String,Stock> map){
		Set<String> s=map.keySet();
		Float totalCount=0F;
		Float rate=0F;
		Float avRate=0F;
		Float totalRate=0F;
		Float totalRight=0F;
		for(String code :s ){
			Stock st=map.get(code);
			avRate=avRate+st.getRate();
			if(st.getRate()>0){
				rate=rate+st.getRate();
			}
			totalCount=totalCount+st.getTotalDay();
			totalRate=totalRate+st.getTotalRate();
			if(st.getRate()>0){
				totalRight+=1;
			}else{
				
			}
		}
		return FetchUtil.formatRate(totalRight/map.size())
				+"\t"+FetchUtil.formatRate(((totalRight-(map.size()-totalRight))/map.size()))
				+"\t"+FetchUtil.formatRate(rate/map.size())
				+"\t"+FetchUtil.formatRate(avRate/map.size())
				+"\t"+ FetchUtil.formatRate(totalCount/map.size())
				+"\t"+ FetchUtil.formatRate(totalRate/map.size());
		
	}
	
	
	public static void  anasys(){
		System.out.println("         \t正确率\t正确率\t收益率\t收益率\t上涨天数\t连续涨幅");
		System.out.println("进取模型：\t"+getAverageCountAndRate(oneMap));
		System.out.println("稳健模型：\t"+getAverageCountAndRate(twoMap));
		System.out.println("保险模型：\t"+getAverageCountAndRate(threeMap));
		System.out.println("保守模型：\t"+getAverageCountAndRate(fourMap));
	}
	public static void main(String args[]){
		anasys();
	}
}
