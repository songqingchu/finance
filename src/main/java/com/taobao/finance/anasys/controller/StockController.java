package com.taobao.finance.anasys.controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.finance.check.impl.Check_AV10;
import com.taobao.finance.check.impl.Check_AV20;
import com.taobao.finance.check.impl.Check_AV5;
import com.taobao.finance.check.impl.Check_AVCU;
import com.taobao.finance.check.impl.Check_BigTrend;
import com.taobao.finance.check.impl.Check_TP;
import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.common.Store;
import com.taobao.finance.comparator.Comparator;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GUser;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;


@Controller
public class StockController {
	private static final Logger logger = Logger.getLogger("fileLogger");
	public static Map<String,Boolean> download=new HashMap<String,Boolean>();
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");
	@Autowired
	private Store store;
	
	@Autowired
	private ThreadService threadService;
	
	@RequestMapping(value = "/getToday.do", method = RequestMethod.GET)
	public String stats(HttpServletRequest request,@RequestParam Boolean force) {
		boolean workday=FetchUtil.checkWorkingDay();
		if(workday){
			final Date d=new Date();
			Integer status=store.getDownloadStatus(d);
			if(status!=null){
				if(force!=null&&force==true&&status!=1){
					store.setDownloading(d);
					new Thread(){
						public void run(){
							store.updateHistory();
							store.updateTmp();
							store.setDownloaded(d);
						}
					}.start();
					request.setAttribute("message", "开始下载！");
					return "choose";
				}
				
				if(status==1){
					request.setAttribute("message", "正在下载中！");
					return "download";
				}
				if(status==2){
					request.setAttribute("message", "今日数据已经下载完成！");
					return "download";
				}
			}else{
				store.setDownloading(d);
				new Thread(){
					public void run(){
						store.updateHistory();
						store.updateTmp();
						store.setDownloaded(d);
					}
				}.start();
				request.setAttribute("message", "开始下载！");
			}
		}else{
			if(force){
				final Date d=new Date();
				Integer status=store.getDownloadStatus(d);
				if(status==null){
					store.setDownloading(d);
					new Thread(){
						public void run(){
							store.updateHistory();
							store.updateTmp();
							store.setDownloaded(d);
						}
					}.start();
					request.setAttribute("message", "开始下载！");
					return "download";
				}
				if(status==1){
					request.setAttribute("message", "正在下载中！");
					return "download";
				}
				if(status==2){
					request.setAttribute("message", "今日数据已经下载完成！");
					return "download";
				}			
			}
			request.setAttribute("workday", workday);
			request.setAttribute("message", "非工作日不用下载！");
		}
		return "download";
	}
	
	
	@RequestMapping(value = "/ananyse.do", method = RequestMethod.GET)
	public String choose(HttpServletRequest request,@RequestParam Boolean force) {
		final Date d=new Date();
		Integer status=store.getChooseStatus(d);
		if(status!=null){
			if(force!=null&&force.equals(true)&&status!=1){
				store.setChoosing(d);
				new Thread(){
					public void run(){
						store.ananyse();
						store.setChoosed(d);
					}
				}.start();
				request.setAttribute("message", "开始分析！");
				return "choose";
			}
			if(status==1){
				request.setAttribute("message", "正在分析中！");
				return "choose";
			}
			if(status==2){
				request.setAttribute("message", "今日数据已经分析完成！");
				return "choose";
			}
		}else{
			store.setChoosing(d);
			new Thread(){
				public void run(){
					store.ananyse();
					store.setChoosed(d);
				}
			}.start();
			request.setAttribute("message", "开始分析！");
			
		}
		return "choose";
	}
	
	@RequestMapping(value = "/record.do", method = RequestMethod.GET)
	public String record(HttpServletRequest request) throws IOException, ParseException {
		logger.info("请求增加记录");
		
		GUser user=(GUser) request.getSession().getAttribute("user");
		Map<String,Object> m=MockUtil.mockStats(user.getId());
		
		
		boolean working=FetchUtil.checkWorkingDay();
		List<StatsDO> l=(List<StatsDO>)m.get("data");
		if(l.size()>0){
			StatsDO d=l.get(l.size()-1);
			request.setAttribute("t", d);
			Integer lastVRate=d.getvRate();
			Integer value=d.getValue();
			request.setAttribute("lastValue", value);
			request.setAttribute("lastVRate", lastVRate);
		}
		
		
		request.setAttribute("data", l);
		request.setAttribute("size", l.size());
		request.setAttribute("working", working);
		return "record";
	}
	
	@RequestMapping(value = "/addRecord.do", method = RequestMethod.POST)
	public String record(HttpServletRequest request,HttpServletResponse response,
			@RequestParam( "date" ) String date,
			@RequestParam( "value" ) Integer value,@RequestParam( "change" ) Integer change,
			@RequestParam( "ayc" ) Integer ayc,@RequestParam( "asc" ) Integer asc,
			@RequestParam( "nyc" ) Integer nyc,@RequestParam( "nsc" ) Integer nsc,
			@RequestParam( "ayv" ) Integer ayv,@RequestParam( "asv" ) Integer asv,
			@RequestParam( "nyv" ) Integer nyv,@RequestParam( "nsv" ) Integer nsv,
			@RequestParam( "ayp" ) Integer ayp,@RequestParam( "asp" ) Integer asp,
			@RequestParam( "nyp" ) Integer nyp,@RequestParam( "nsp" ) Integer nsp,
			@RequestParam( "ayr" ) Float ayr,@RequestParam( "asr" ) Float asr,
			@RequestParam( "nyr" ) Float nyr,@RequestParam( "nsr" ) Float nsr,
			@RequestParam( "lastValue" ) Integer lastValue,@RequestParam( "lastVRate" ) Integer lastVRate
			) throws IOException, ParseException {

		logger.info("请求增加记录");
		StatsDO d=new StatsDO();
		date=date.replace("/", ".");
		d.setDate(date);
		d.setValue(value);
		d.setChange(change);
		if(lastValue==null){
			lastValue=value;
			lastVRate=1;
		}
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
		
		d.setAyRate((int)(ayr*100F));
		d.setAsRate((int)(asr*100F));
		d.setNyRate((int)(nyr*100F));
		d.setNsRate((int)(nsr*100F));
		
		GUser user=(GUser) request.getSession().getAttribute("user");
		File f = new File(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");  
		if(!f.exists()){
			FetchUtil.createFile(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");
			BufferedWriter br=new BufferedWriter(new FileWriter(f,true));
			br.write("date,value,change,rate,ayc,asc,nyc,nsc,ayv,asv,nyv,nsv,ayp,asp,nyp,nsp,ayr,asr,nyr,ns\n");
			String line=d.toFileString();
			br.write(line+"\n");
			br.close();
			
		}else{
			BufferedWriter br=new BufferedWriter(new FileWriter(f,true));
			String line=d.toFileString();
			br.write(line+"\n");
			br.close();
		}
		
		response.sendRedirect(request.getContextPath() + "/record.do");  
		return null;
	}
	
	@RequestMapping(value = "/statsData.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> statsData(HttpServletRequest request) throws IOException, ParseException {
		logger.info("requesting home");
		GUser user=(GUser) request.getSession().getAttribute("user");
		if(user!=null){
			Integer id=user.getId();
		}
		Map<String,Object> m=MockUtil.mockStats(user.getId());
		return m;
	}
	
	@RequestMapping(value = "/stats.do", method = RequestMethod.GET)
	public String stats(HttpServletRequest request) {
		GUser user=(GUser) request.getSession().getAttribute("user");
		File f = new File(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");  
		if(!f.exists()){
			request.setAttribute("exist", true);
		}else{
			request.setAttribute("exist", false);
		}

		return "stats";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + "/publicPool.do");  
		return null;
	}
	@RequestMapping(value = "/publicPool.do", method = RequestMethod.GET)
	public String publicPool(HttpServletRequest request) {
		Set<String> s=store.publicPool.keySet();
		List<String> symbolList=new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			callList.add(new RealTask(sys));
		}
        List<Object> r=threadService.service(callList);		
        List<Stock> result=new ArrayList<Stock>();
        for(Object l:r){
        	List<Stock> slice=(List<Stock>)l;
        	result.addAll(slice);
        }
		if(r.size()>0){
			Collections.sort(result,new Comparator.RateDescComparator());
		}
		request.setAttribute("r", result);
		request.setAttribute("set", s);
		return "publicPool";
	}
	static class RealTask implements Callable<Object>{
		public List<Object> l; 
		public RealTask(List<Object> l){
			this.l=l;
		}
		public Object call(){
			List<Stock> r=new ArrayList<Stock>();
			for(Object s:l){
				Stock st=Fetch_SingleStock.fetch((String)s);
				if(st!=null){
					r.add(st);
				}
			}
			return r;
		}
	}
	
	
	@RequestMapping(value = "/addPublicPool.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addPublicPool(@RequestParam String symbols,
			@RequestParam String replace){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "ok");
		if(replace.equals("1")){
			store.publicPool.clear();
		}
		if(StringUtils.isNotBlank(symbols)){
			String[] syms=StringUtils.split(symbols,",");
			for(String s:syms){
				if(StringUtils.isNotBlank(s)){
					if(s.startsWith("0")){
						s=s.replaceFirst("0", "sz");
						store.publicPool.put(s, null);
					}
					if(s.startsWith("1")){
						s=s.replaceFirst("1", "sh");
						store.publicPool.put(s, null);
					}
				}
			}
		}
		return map;
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
		System.out.println("big:"+big.size());
		System.out.println("acvu:"+big.size());
		System.out.println("av5:"+big.size());
		System.out.println("av10:"+big.size());
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


		request.setAttribute("size", size);
		request.setAttribute("big", big);
		request.setAttribute("acvu", acvu);
		request.setAttribute("av5", av5);
		request.setAttribute("av10", av10);
		request.setAttribute("tp", tp);
		request.setAttribute("bigSize", big.size());
		request.setAttribute("acvuSize", acvu.size());
		request.setAttribute("av5Size", av5.size());
		request.setAttribute("av10Size", av10.size());
		
		request.setAttribute("bigStr", "'"+StringUtils.join(bigStr,"','")+"'");
		request.setAttribute("acvuStr",  "'"+StringUtils.join(acvuStr,"','")+"'");
		request.setAttribute("av5Str",  "'"+StringUtils.join(av5Str,"','")+"'");
		request.setAttribute("av10Str", "'"+ StringUtils.join(av10Str,"','")+"'");
			
		return "stockPool";
	}
	
	@RequestMapping(value = "/kData.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> validataUser6(@RequestParam String symbol) throws IOException, ParseException {
		
		Calendar c=Calendar.getInstance();
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int minits=c.get(Calendar.MINUTE);
		Date d=new Date();
		String dateStr=DF.format(d);
		Boolean checkedWorkingDay=store.checkWorkingRecord.get(dateStr);
		if(checkedWorkingDay==null){
			Boolean workingDay=FetchUtil.checkWorkingDay();
			store.workingDay=workingDay;
			store.checkWorkingRecord.put(dateStr, true);
		}
		boolean shi=true;
		if(hour<9&&hour>=15){
			shi=false;
		}
		if(hour==9&&minits<30){
			shi=false;
		}
		Map<String,Object> map=MockUtil.mockData3(symbol,store.workingDay,shi);
		return map;
	}	
	
	
	@RequestMapping(value = "/match.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> match(@RequestParam String symbol) throws IOException, ParseException {
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> bigList=new Check_BigTrend().check(symbol);
		List<String> acvuList=new Check_AVCU().check(symbol);
		List<String> av5List=new Check_AV5().check(symbol);
		List<String> av10List=new Check_AV10().check(symbol);
		List<String> av20List=new Check_AV20().check(symbol);
		List<String> tpList=new Check_TP().check(symbol);
		if(bigList.size()>5){
			bigList=bigList.subList(0, 4);
		}
		if(acvuList.size()>5){
			acvuList=acvuList.subList(0, 4);
		}
		if(av5List.size()>5){
			av5List=av5List.subList(0, 4);
		}
		if(av10List.size()>5){
			av10List=av10List.subList(0, 4);
		}
		if(av20List.size()>5){
			av20List=av20List.subList(0, 4);
		}
		if(tpList.size()>5){
			tpList=tpList.subList(0, 4);
		}
		String big=StringUtils.join(bigList,",&nbsp;&nbsp;");
		String acvu=StringUtils.join(acvuList,",&nbsp;&nbsp;");
		String av5=StringUtils.join(av5List,",&nbsp;&nbsp;");
		String av10=StringUtils.join(av10List,",&nbsp;&nbsp;");
		String av20=StringUtils.join(av20List,",&nbsp;&nbsp;");
		String tp=StringUtils.join(tpList,",&nbsp;&nbsp;");
		
		map.put("big", big); 
		map.put("acvu", acvu);    
		map.put("av5", av5);
		map.put("av10", av10);
		map.put("av20", av20); 
		map.put("tp", tp); 
		String result="";
		
		result+="<b>BIG&nbsp;</b>:"+big+"<br>";
		result+="<b>ACVU</b>:"+acvu+"<br>";
		result+="<b>AV5&nbsp;</b>:"+av5+"<br>";
		result+="<b>AV10</b>:"+av10+"<br>";
		result+="<b>AV20</b>:"+av20+"<br>";
		result+="<b>TP&nbsp;&nbsp;</b>:"+tp+"<br> ";
		
		map.put("result", result); 
		return map;
	}
}
