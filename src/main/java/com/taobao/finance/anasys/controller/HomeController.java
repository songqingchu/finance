package com.taobao.finance.anasys.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.dataobject.Stock;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


	
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
	
	@RequestMapping(value = "/c.do", method = RequestMethod.GET)
	public String validataUser3(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", true);
		List<Stock> big=new BigTrend_Choose_MultiThread().choose();
		List<Stock> acvu=new AVCU_Choose_MultiThread().choose();
		List<Stock> av5=new AV5_Trend_Choose_MultiThread().choose();
		List<Stock> av10=new AV10_Trend_Choose_MultiThread().choose();
		List<Stock> tp=new TP_Choose_MultiThread().choose();
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
		return "c";
	}

}
