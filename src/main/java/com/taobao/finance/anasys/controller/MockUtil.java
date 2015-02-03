package com.taobao.finance.anasys.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;

public class MockUtil {

	
	public static Map<String,Object> mockData() throws IOException, ParseException{
		Map<String,Object> m=new HashMap<String,Object>();
		List<StatsDO> mine=read();
		DateFormat df=new SimpleDateFormat("yyyy.MM.dd");
		Date startDate=df.parse(mine.get(0).getDate());
		List<StatsDO> sh=readIndex(startDate, null, "sh000001");
		List<StatsDO> sz=readIndex(startDate, null, "sz399001");
		
		List<String> dList=new ArrayList<String>();
		List<Double> mL=new ArrayList<Double>();
		List<Double> shL=new ArrayList<Double>();
		List<Double> szL=new ArrayList<Double>();
		
		List<Integer> sRate=new ArrayList<Integer>();
		List<Integer> r=new ArrayList<Integer>();
		List<Integer> yRate=new ArrayList<Integer>();
		List<Integer> back=new ArrayList<Integer>();
		for(StatsDO s:mine){
			dList.add(s.getDate());
			mL.add(s.getV());
			sRate.add(s.getsRate());
			r.add(s.getR());
			yRate.add(s.getyRate());
			back.add(s.getBack());
		}
		for(StatsDO s:sh){
			shL.add(s.getV());
		}
		for(StatsDO s:sz){
			szL.add(s.getV());
		}

		m.put("date",dList );
		m.put("mine", mL);
		m.put("sh", shL);
		m.put("sz", szL);
		
		m.put("sRate",sRate );
		m.put("yRate", yRate);
		m.put("r", r);
		m.put("back", back);
		return m;
	}
	
	public static  List<StatsDO> read() throws IOException, ParseException{
		List<StatsDO> l=new ArrayList<StatsDO> ();
		@SuppressWarnings("deprecation")
		XSSFWorkbook xwb = new XSSFWorkbook("C:\\Documents and Settings\\Administrator\\git\\finance\\src\\main\\resources\\统计.xlsx");  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		XSSFRow row;  
		
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum()+2; i < sheet.getPhysicalNumberOfRows(); i++) {  
		    row = sheet.getRow(i);  
		    String date=row.getCell(0).getStringCellValue(); 
		    if(row.getCell(1)==null){
		    	break;
		    }
		    if(i==13){
		    	row.cellIterator();
		    }
			String value=row.getCell(1).getRawValue(); 
			String ayc=row.getCell(2).getRawValue(); 
			String asc=row.getCell(3).getRawValue();
			String nyc=row.getCell(4).getRawValue(); 
			String nsc=row.getCell(5).getRawValue(); 
			
			String ay=row.getCell(6).getRawValue(); 
			String as=row.getCell(7).getRawValue(); 
			String ny=row.getCell(8).getRawValue(); 
			String ns=row.getCell(9).getRawValue(); 
			
		    if(StringUtils.isNotBlank(value)){
		    	StatsDO s=new StatsDO();
		    	s.setDate(date);
		    	s.setValue(Double.parseDouble(value));
		        s.setAyCount(Integer.parseInt(ayc));
		        s.setAsCount(Integer.parseInt(asc));
		        s.setNyCount(Integer.parseInt(nyc));
		        s.setNsCount(Integer.parseInt(nsc));
		        
		        s.setaY(Double.parseDouble(ay));
		        s.setaS(Double.parseDouble(as));
		        s.setnY(Double.parseDouble(ny));
		        s.setnS(Double.parseDouble(ns));
		        
		        //胜率
		        if(s.getAyCount()+s.getAsCount()+s.getNsCount()+s.getNyCount()==0){
		        	s.setsRate(50);
		        }else{
		        	s.setsRate((s.getAyCount()+s.getNyCount())*100/(s.getAyCount()+s.getAsCount()+s.getNsCount()+s.getNyCount()));
		        }	        
		        //动态R
		        if((0-s.getaS()-s.getnS()==0)){
		        	s.setR(100);
		        }else{
		        	s.setR((s.getaY().intValue()+s.getnY().intValue())*100/(s.getaS().intValue()+s.getnS().intValue()));
		        }
		        
		    	l.add(s);
		    }
		}  
		Double startValue=l.get(0).getValue();
		Date startDate=StatsDO.df.parse(l.get(0).getDate());
		for(int i=0;i<l.size();i++){
			StatsDO s=l.get(i);
			
			//最大回撤
			Integer max=0;
			int maxIndex=0;
			Integer min=1000000000;
			for(int j=0;j<=i;j++){
				StatsDO ss=l.get(j);
				if(ss.getValue()>max){
					max=ss.getValue().intValue();
					maxIndex=j;
				}
			}
			for(int j=maxIndex;j<=i;j++){
				StatsDO ss=l.get(j);
				if(ss.getValue()<min){
					min=ss.getValue().intValue();
				}
			}
			Integer back=(max-min)*100/max;
			s.setBack(back);
			
			//v
			Double v=s.getValue().doubleValue()*100/startValue-100;
			s.setV(v);
			
			//动态v
			Date endDate=StatsDO.df.parse(s.getDate());
			Calendar c=Calendar.getInstance();
			c.setTime(startDate);    
	        long time1 = c.getTimeInMillis();                 
	        c.setTime(endDate);    
	        long time2 = c.getTimeInMillis();         
	        long dayCount=(time2-time1)/(1000*3600*24);  
	        if(dayCount==0){
		        s.setyRate(20);
	        }else{
	        	Double rV=v/dayCount*360;
		        s.setyRate(rV.intValue());
	        }  
		}
		return l;
	}
	
	
	public static void format(StatsDO s){
	
	}
	
	public static  List<StatsDO> readIndex(Date startDate,Date endDate,String symbol){
		List<StatsDO> r=new ArrayList<StatsDO>();
		List<Stock> l=Hisdata_Base.readHisDataMerge(symbol, startDate);
		Double start=Double.parseDouble(l.get(0).getEndPrice());
		for(Stock s:l){
			StatsDO st=new StatsDO();
			//st.setDate(df.format(s.getDate()));
			Double price=Double.parseDouble(s.getEndPrice());
			st.setV(price*100/start-100);
			r.add(st);
		}
		return r;
	}
	
	public static void main(String args[]) throws IOException, ParseException{
	    mockData();
	}
}
