package com.taobao.finance.anasys.controller;

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
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;

/**
 * Handles requests for the application home page.
 */
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
	
	
	@RequestMapping(value = "/c.do", method = RequestMethod.GET)
	public String validataUser3(HttpServletRequest request) {
		
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
		if(store.containsKey("tp")){
			tp=store.get("tp");
		}else{
			tp=new TP_Choose_MultiThread().choose();
		}

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
		if(tp.size()>size){
			size=tp.size();
		}

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
