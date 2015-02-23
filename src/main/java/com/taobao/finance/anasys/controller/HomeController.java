package com.taobao.finance.anasys.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;


@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private Store store;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> home() throws IOException, ParseException {
		logger.info("requesting home");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> m=MockUtil.mockData();
		map.put("series", m.get("date"));
		map.put("mine", m.get("mine"));
		map.put("sh", m.get("sh"));
		map.put("sz", m.get("sz"));
		map.put("sRate", m.get("sRate"));
		map.put("yRate", m.get("yRate"));
		map.put("r", m.get("r"));
		map.put("back", m.get("back"));
		return map;
	}
	
	@RequestMapping(value = "/statsData.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> statsData() throws IOException, ParseException {
		logger.info("requesting home");
		Map<String,Object> m=MockUtil.mockStats();
		return m;
	}
	
	@RequestMapping(value = "/stats.do", method = RequestMethod.GET)
	public String stats() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		return "stats";
	}
	
	
	
	@RequestMapping(value = "/bb.json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> home2() throws IOException, ParseException {
		logger.info("requesting home");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> m=MockUtil.mockData2();
		map.put("series", m.get("date"));
		map.put("sRate", m.get("sRate"));
		map.put("r", m.get("r"));
		
		return map;
	}

	@RequestMapping(value = "/a.do", method = RequestMethod.GET)
	public String validataUser() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		return "a";
	}
	
	@RequestMapping(value = "/b.do", method = RequestMethod.GET)
	public String validataUser2() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		return "b";
	}
	
	@RequestMapping(value = "/d.do", method = RequestMethod.GET)
	public String validataUser4() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		return "d";
	}
	
	@RequestMapping(value = "/f.do", method = RequestMethod.GET)
	public String validataUser5(@RequestParam String symbol,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		request.setAttribute("symbol", symbol);
		return "e";
	}
	
	@RequestMapping(value = "/e.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> validataUser6(@RequestParam String symbol) throws IOException, ParseException {
		Map<String,Object> map=MockUtil.mockData3(symbol);
		return map;
	}
	
	
	@RequestMapping(value = "/record.do", method = RequestMethod.GET)
	public String record(HttpServletRequest request) throws IOException, ParseException {
		Map<String,Object> m=MockUtil.mockStats();
		boolean working=FetchUtil.checkWorkingDay();
		List<StatsDO> l=(List<StatsDO>)m.get("data");
		StatsDO d=l.get(l.size()-1);
		Integer lastVRate=d.getvRate();
		Integer value=d.getValue();
		request.setAttribute("t", d);
		request.setAttribute("data", l);
		request.setAttribute("lastValue", value);
		request.setAttribute("lastVRate", lastVRate);
		request.setAttribute("working", working);
		return "record";
	}
	
	@RequestMapping(value = "/addRecord.do", method = RequestMethod.POST)
	public String record(@RequestParam( "date" ) String date,
			@RequestParam( "value" ) Integer value,@RequestParam( "change" ) Integer change,
			@RequestParam( "ayc" ) Integer ayc,@RequestParam( "asc" ) Integer asc,
			@RequestParam( "nyc" ) Integer nyc,@RequestParam( "nsc" ) Integer nsc,
			@RequestParam( "ayv" ) Integer ayv,@RequestParam( "asv" ) Integer asv,
			@RequestParam( "nyv" ) Integer nyv,@RequestParam( "nsv" ) Integer nsv,
			@RequestParam( "ayp" ) Integer ayp,@RequestParam( "asp" ) Integer asp,
			@RequestParam( "nyp" ) Integer nyp,@RequestParam( "nsp" ) Integer nsp,
			@RequestParam( "ayr" ) Integer ayr,@RequestParam( "asr" ) Integer asr,
			@RequestParam( "nyr" ) Integer nyr,@RequestParam( "nsr" ) Integer nsr,
			@RequestParam( "lastValue" ) Integer lastValue,@RequestParam( "lastVRate" ) Integer lastVRate
			) throws IOException, ParseException {

		logger.info("请求增加记录");
		StatsDO d=new StatsDO();
		date=date.replace("/", ".");
		d.setDate(date);
		d.setValue(value);
		d.setChange(change);
		d.setvRate((value-change)*lastVRate/lastValue);
		
		d.setAyCount(ayc);
		d.setAsCount(asc);
		d.setNyCount(nyc);
		d.setNsCount(nsc);
		
		d.setAyValue(ayv);
		d.setAsValue(asv);
		d.setNyValue(nyv);
		d.setNsValue(nsv);
		
		d.setAyPosition(ayp);
		d.setAsPosition(asp);
		d.setNyPosition(nyp*100/value);
		d.setNsPosition(nsp*100/value);
		
		d.setAyRate(ayr);
		d.setAsRate(asr);
		d.setNyRate(nyr);
		d.setNsRate(nsr);
		
		File f = new File("C:\\Documents and Settings\\Administrator\\git\\finance\\src\\main\\resources\\stats.csv");  
		BufferedWriter br=new BufferedWriter(new FileWriter(f,true));
		String line=d.toFileString();
		br.write("\n"+line);
		br.close();
		
		return "record";
	}
	
	@RequestMapping(value = "/chart.do", method = RequestMethod.GET)
	public String chart(HttpServletRequest request) {
		
		return "c";
	}
	
	
	@RequestMapping(value = "/choose.do", method = RequestMethod.GET)
	public String choose(HttpServletRequest request) {
		
		List<Stock> big=null;
		List<Stock> acvu=null;
		List<Stock> av5=null;
		List<Stock> av10=null;
		List<Stock> tp=null;
		if(store.containsKey("big")){
			big=store.get("big");
		}else{
			big=new BigTrend_Choose_MultiThread().choose();
		}
		
		if(store.containsKey("acvu")){
			acvu=store.get("acvu");
		}else{
			acvu=new AVCU_Choose_MultiThread().choose();
		}
		if(store.containsKey("av5")){
			av5=store.get("av5");
		}else{
			av5=new AV5_Trend_Choose_MultiThread().choose();
		}
		if(store.containsKey("av10")){
			av10=store.get("av10");
		}else{
			av10=new AV10_Trend_Choose_MultiThread().choose();
		}
		/*if(store.containsKey("tp")){
			tp=store.get("tp");
		}else{
			tp=new TP_Choose_MultiThread().choose();
		}*/

		if(!store.containsKey("big")){
			store.put("big", big);
		}
		if(!store.containsKey("acvu")){
			store.put("acvu",acvu );
		}
		if(!store.containsKey("av5")){
			store.put("av5", av5);
		}
		if(!store.containsKey("av10")){
			store.put("av10",av10 );
		}
		if(!store.containsKey("tp")){
			store.put("tp",tp );
		}

		List<String> bigStr=new ArrayList<String>();
		List<String> acvuStr=new ArrayList<String>();
		List<String> av5Str=new ArrayList<String>();
		List<String> av10Str=new ArrayList<String>();
		
		for(Stock s:big){
			bigStr.add(s.getSymbol());
		}
		for(Stock s:acvu){
			acvuStr.add(s.getSymbol());
		}
		for(Stock s:av5){
			av5Str.add(s.getSymbol());
		}
		for(Stock s:av10){
			av10Str.add(s.getSymbol());
		}
		
		
		int size=0;
		if(big.size()>size){
			size=big.size();
		}
		if(acvu.size()>size){
			size=acvu.size();
		}
		if(av5.size()>size){
			size=av5.size();
		}
		if(av10.size()>size){
			size=av10.size();
		}
/*		if(tp.size()>size){
			size=tp.size();
		}*/

		request.setAttribute("size", size);
		request.setAttribute("big", big);
		request.setAttribute("acvu", acvu);
		request.setAttribute("av5", av5);
		request.setAttribute("av10", av10);
		request.setAttribute("tp", tp);
		
		request.setAttribute("bigStr", "'"+StringUtils.join(bigStr,"','")+"'");
		request.setAttribute("acvuStr",  "'"+StringUtils.join(acvuStr,"','")+"'");
		request.setAttribute("av5Str",  "'"+StringUtils.join(av5Str,"','")+"'");
		request.setAttribute("av10Str", "'"+ StringUtils.join(av10Str,"','")+"'");
			
		return "c";
	}

	
	
}
