package com.taobao.finance.anasys.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taobao.finance.common.Store;


@Controller
public class HomeController {

	private static final Logger logger = Logger.getLogger("fileLogger");
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
	

	
	@RequestMapping(value = "/chart.do", method = RequestMethod.GET)
	public String chart(HttpServletRequest request) {
		return "c";
	}
}
