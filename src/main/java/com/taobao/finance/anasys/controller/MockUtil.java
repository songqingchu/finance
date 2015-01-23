package com.taobao.finance.anasys.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		for(StatsDO s:mine){
			dList.add(s.getDate());
		}
		for(StatsDO s:mine){
			mL.add(s.getV());
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
		return m;
	}
	
	public static  List<StatsDO> read() throws IOException{
		List<StatsDO> l=new ArrayList<StatsDO> ();
		@SuppressWarnings("deprecation")
		XSSFWorkbook xwb = new XSSFWorkbook("E:\\stock\\统计.xlsx");  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		XSSFRow row;  
		
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {  
		    row = sheet.getRow(i);  
		    String date=row.getCell(0).getStringCellValue(); 
		    if(row.getCell(1)==null){
		    	break;
		    }
			String value=row.getCell(1).getRawValue();  
			String r=row.getCell(2).getRawValue(); 
			String rate=row.getCell(3).getRawValue(); 

		    if(StringUtils.isNotBlank(value)){
		    	StatsDO s=new StatsDO();
		    	s.setDate(date);
		    	s.setValue(Double.parseDouble(value));
		    	s.setR(Double.parseDouble(r));
		    	s.setRate(Double.parseDouble(rate));
		    	l.add(s);
		    }
		}  
		Double startValue=l.get(0).getValue();
		for(StatsDO s:l){
			s.setV(s.getValue().doubleValue()*100/startValue-100);
		}
		return l;
	}
	
	public static  List<StatsDO> readIndex(Date startDate,Date endDate,String symbol){
		List<StatsDO> r=new ArrayList<StatsDO>();
		List<Stock> l=Hisdata_Base.readHisDataMerge(symbol, startDate);
		Double start=Double.parseDouble(l.get(0).getEndPrice());
		DateFormat df=new SimpleDateFormat("yyyy.MM.dd");
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
