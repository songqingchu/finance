package com.taobao.finance.anasys.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.util.FetchUtil;

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
	
	public static Map<String,Object> mockStats() throws IOException, ParseException{
		Map<String,Object> m=new HashMap<String,Object>();
		List<StatsDO> mine=readChart();
		DateFormat df=new SimpleDateFormat("yyyy.MM.dd");
		Date startDate=df.parse(mine.get(0).getDate());
		List<StatsDO> sh=readIndex(startDate, null, "sh000001");
		List<StatsDO> sz=readIndex(startDate, null, "sz399001");
		
		List<String> dList=new ArrayList<String>();
		List<Integer> mL=new ArrayList<Integer>();
		List<Integer> shL=new ArrayList<Integer>();
		List<Integer> szL=new ArrayList<Integer>();
		List<Integer> year=new ArrayList<Integer>();
		
		List<Integer> sRate=new ArrayList<Integer>();
		List<Integer> r=new ArrayList<Integer>();
		List<Integer> pRate=new ArrayList<Integer>();
		List<Integer> yRate=new ArrayList<Integer>();
		List<Integer> yyRate=new ArrayList<Integer>();
		List<Integer> ysRate=new ArrayList<Integer>();
		
		for(StatsDO s:mine){
			dList.add(s.getDate());
			mL.add(s.getvRate()-1000);
			
			
			Date endDate=StatsDO.df.parse(s.getDate());
			Calendar c=Calendar.getInstance();
			c.setTime(startDate);    
	        long time1 = c.getTimeInMillis();                 
	        c.setTime(endDate);    
	        long time2 = c.getTimeInMillis();         
	        long dayCount=(time2-time1)/(1000*3600*24);  
	        if(dayCount==0){
	        	year.add(20);
	        }else{
	        	Integer yearV=(s.getvRate()-1000)*365/(int)dayCount;
	        	year.add(yearV);
	        }  
	        
			
			
			sRate.add(s.getsRate());
			r.add(s.getR());
			pRate.add(s.getpRate());
			yRate.add(s.getyRate());
			yyRate.add(s.getYyRate());
			ysRate.add(s.getYsRate());
		}
		for(StatsDO s:sh){
			shL.add(s.getvRate());
		}
		for(StatsDO s:sz){
			szL.add(s.getvRate());
		}

		m.put("series",dList );
		m.put("mine", mL);
		m.put("sh", shL);
		m.put("sz", szL);
		m.put("year", year);
		
		m.put("sRate",sRate );
		m.put("rRate", r);
		m.put("pRate",pRate );
		
		m.put("yRate", yRate);
		m.put("yyRate", yyRate);
		m.put("ysRate", ysRate);
		m.put("data", mine);
	//	m.put("r", r);
		

		return m;
	}
	
	
	public static Map<String,Object> mockData2() throws IOException, ParseException{
		Map<String,Object> m=new HashMap<String,Object>();
		List<StatsDO> mine=read2();
		DateFormat df=new SimpleDateFormat("yyyy.MM.dd");
		Date startDate=df.parse(mine.get(0).getDate());

		
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
	
	public static Map<String,Object> mockData3(String symbol,Boolean working,Boolean shi) throws IOException, ParseException{
		Map<String,Object> m=new HashMap<String,Object>();
		
		List<Stock> l=Hisdata_Base.readHisDataMerge(symbol, null);
		
		if(working){
			if(shi){
				Stock s=Fetch_SingleStock.fetch(symbol);
				l.add(s);
			}
		}

		
		Object[][] av5=new Object[260][2];
		Object[][] av10=new Object[260][2];
		Object[][] av20=new Object[260][2];
		Object[][] data=new Object[260][5];
		Object[][] vol=new Object[260][2];
		
		
		
		if(l.size()>300){
			for (int i = 1; i < 261; i++) {
				Stock s=l.get(l.size() - i);
				Float v5 = getAve(l, 5, l.size() - i);
				Float v10 = getAve(l, 10, l.size() - i);
				Float v20 = getAve(l, 20, l.size() - i);
				
				Date d=s.getDate()==null?new Date():s.getDate();
				av5[260-i][0]=d;
				av5[260-i][1]=v5;
				
				av10[260-i][0]=d;
				av10[260-i][1]=v10;
				
				av20[260-i][0]=d;
				av20[260-i][1]=v20;
				
				vol[260-i][0]=d;
				vol[260-i][1]=s.getTradeNum();
				
				data[260-i][0]=d;
				data[260-i][1]=Float.parseFloat(s.getStartPrice());
				data[260-i][2]=Float.parseFloat(s.getLowPrice());
				data[260-i][3]=Float.parseFloat(s.getHighPrice());
				data[260-i][4]=Float.parseFloat(s.getEndPrice());
			}
		}
	

		m.put("av5",av5 );
		m.put("av10", av10);
		m.put("av20", av20);
		m.put("vol", vol);	
		m.put("data",data );
		Stock s=l.get(l.size()-1);
		Stock st=Fetch_AllStock.map.get(s.getSymbol());
		String name=null;
		if(st!=null){
			name=st.getName();
		}else{
			name=s.getSymbol();
		}
		
		m.put("name", name);
		m.put("start", s.getStartPriceFloat());
		m.put("high", s.getHighPriceFloat());
		m.put("low", s.getLowPriceFloat());
		m.put("end", s.getEndPriceFloat());
		return m;
	}
	
	
	
	
	public static  List<StatsDO> read() throws IOException, ParseException{
		List<StatsDO> l=new ArrayList<StatsDO> ();
		/*@SuppressWarnings("deprecation")
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
		}*/
		return l;
	}
	
	
	public static  List<StatsDO> read2() throws IOException, ParseException{
		List<StatsDO> l=new ArrayList<StatsDO> ();
		@SuppressWarnings("deprecation")
		XSSFWorkbook xwb = new XSSFWorkbook("C:\\Documents and Settings\\Administrator\\git\\finance\\src\\main\\resources\\统计.xlsx");  
		XSSFSheet sheet = xwb.getSheetAt(2);  
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
			String ayc=row.getCell(1).getRawValue(); 
			String asc=row.getCell(2).getRawValue();
			String nyc=row.getCell(3).getRawValue(); 
			String nsc=row.getCell(4).getRawValue(); 
			
			String ay=row.getCell(5).getRawValue(); 
			String as=row.getCell(6).getRawValue(); 
			String ny=row.getCell(7).getRawValue(); 
			String ns=row.getCell(8).getRawValue(); 
			
		    if(StringUtils.isNotBlank(ayc)){
		    	StatsDO s=new StatsDO();
		    	s.setDate(date);
		        s.setAyCount(Integer.parseInt(ayc));
		        s.setAsCount(Integer.parseInt(asc));
		        s.setNyCount(Integer.parseInt(nyc));
		        s.setNsCount(Integer.parseInt(nsc));
		        
		        s.setAyValue(Integer.parseInt(ay));
		        s.setAsValue(Integer.parseInt(as));
		        s.setNyValue(Integer.parseInt(ny));
		        s.setNsValue(Integer.parseInt(ns));
		        
		        //胜率
		        if(s.getAyCount()+s.getAsCount()+s.getNsCount()+s.getNyCount()==0){
		        	s.setsRate(50);
		        }else{
		        	s.setsRate((s.getAyCount()+s.getNyCount())*100/(s.getAyCount()+s.getAsCount()+s.getNsCount()+s.getNyCount()));
		        }	        
		        //动态R
		        if((0-s.getAsValue()-s.getNsValue()==0)){
		        	s.setR(100);
		        }else{
		        	s.setR((s.getAyValue().intValue()+s.getNyValue().intValue())*100/(s.getAsValue().intValue()+s.getNsValue().intValue()));
		        }
		        
		    	l.add(s);
		    }
		}  
		return l;
	}
	
	
	
	public static  List<StatsDO> readChart() throws IOException, ParseException{
		List<StatsDO> l=new ArrayList<StatsDO> ();
		File f = new File(FetchUtil.FILE_USER_STATS_BASE+"stats.csv");  
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line=br.readLine();
		while((line=br.readLine())!=null){
			String[] data=line.split(",");
			
			String date=data[0]; 
			String value=data[1]; 
			String change=data[2]; 
			String rate=data[3]; 
			
			String ayc=data[4]; 
			String asc=data[5]; 
			String nyc=data[6]; 
			String nsc=data[7]; 
			
			String ayv=data[8]; 
			String asv=data[9];  
			String nyv=data[10]; 
			String nsv=data[11];  
			
			String ayp=data[12]; 
			String asp=data[13]; 
			String nyp=data[14]; 
			String nsp=data[15]; 
			
			String ayr=data[16];  
			String asr=data[17];  
			String nyr=data[18]; 
			String nsr=data[19]; 
			
		    if(StringUtils.isNotBlank(ayc)){
		    	StatsDO s=new StatsDO();
		    	s.setDate(date);
		    	s.setValue(Integer.parseInt(value));
		    	s.setChange(Integer.parseInt(change));
		    	s.setvRate(Integer.parseInt(rate));
		    	
		        s.setAyCount(Integer.parseInt(ayc));
		        s.setAsCount(Integer.parseInt(asc));
		        s.setNyCount(Integer.parseInt(nyc));
		        s.setNsCount(Integer.parseInt(nsc));
		        
		        s.setAyValue(Integer.parseInt(ayv));
		        s.setAsValue(Integer.parseInt(asv));
		        s.setNyValue(Integer.parseInt(nyv));
		        s.setNsValue(Integer.parseInt(nsv));
		        
		        s.setAyPosition(Integer.parseInt(ayp));
		        s.setAsPosition(Integer.parseInt(asp));
		        s.setNyPosition(Integer.parseInt(nyp));
		        s.setNsPosition(Integer.parseInt(nsp));
		        
		        s.setAyRate(Integer.parseInt(ayr));
		        s.setAsRate(Integer.parseInt(asr));
		        s.setNyRate(Integer.parseInt(nyr));
		        s.setNsRate(Integer.parseInt(nsr));
		        
		        
		    	l.add(s);
		}
		}
		br.close();
		return l;
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
			st.setvRate((int)(price*100/start-100));
			r.add(st);
		}
		return r;
	}
	
	public static void main(String args[]) throws IOException, ParseException{
	    mockData();
	}
}
