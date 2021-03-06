package com.taobao.finance.anasys.controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.taobao.finance.choose.local.thread.Holder_Choose_MultiThread;
import com.taobao.finance.common.Store;
import com.taobao.finance.comparator.Comparator;
import com.taobao.finance.dataobject.Report;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GHis;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GRecord;
import com.taobao.finance.entity.GStock;
import com.taobao.finance.entity.GUser;
import com.taobao.finance.entity.GXing;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_Report_Jupai;
import com.taobao.finance.fetch.impl.Fetch_ServeralStock_Sina;
import com.taobao.finance.fetch.impl.Fetch_ShiZhi;
import com.taobao.finance.service.DataService;
import com.taobao.finance.service.GHisService;
import com.taobao.finance.service.GPublicStockService;
import com.taobao.finance.service.GRecordService;
import com.taobao.finance.service.GStockService;
import com.taobao.finance.service.GTaskService;
import com.taobao.finance.service.GXingService;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.util.FetchUtil;


@Controller
public class StockController {
	private static final Logger logger = Logger.getLogger("fileLogger");
	public static Map<String,Boolean> download=new HashMap<String,Boolean>();
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");
	@Autowired
	private Store store;
	@Autowired
	private ThreadService threadService;
	@Autowired
	private GPublicStockService gPublicStockService;
	@Autowired
	private GTaskService gTaskService;
	@Autowired
	private GXingService gXingService;
	@Autowired
	private DataService dataService;
	@Autowired
	private GStockService gStockService;
	@Autowired
	private GRecordService gRecordService;
	@Autowired
	private GHisService gHisService;
	
	
	
	@RequestMapping(value = "/rixing.do", method = RequestMethod.GET)
	public String rixing(HttpServletRequest request) {
		String id=request.getParameter("id");
		String key=request.getParameter("id");
		String content=request.getParameter("id");
		
		if(StringUtils.isNotBlank(id)){
			List<GXing> l=this.gXingService.queryAll();
			GXing xing=this.gXingService.queryById(Integer.parseInt(id));
			if(xing!=null){
				request.setAttribute("now", xing);
			}
			request.setAttribute("all", l);
		}else{
			//新增
			if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(content)){
				GXing xing=new GXing();
				xing.setKey(key);
				xing.setContent(content);
				xing.setDate(new Date());
				this.gXingService.insert(xing);
			}else{
			//列表
				List<GXing> l=this.gXingService.queryAll();
				request.setAttribute("all", l);
			}
		}
		
		return "rixing";
	}
	
	
	@RequestMapping(value = "/canwu.do", method = RequestMethod.GET)
	public String canwu(HttpServletRequest request) {
		
		return "canwu";
	}
	
	@RequestMapping(value = "/report.do", method = RequestMethod.GET)
	public String report(HttpServletRequest request) {
		List<Report> l=new Fetch_Report_Jupai().fetchAll(null);
		
		if(l.size()>0){
			Collections.sort(l);
		}
		request.setAttribute("l", l);
		return "report";
	}
	
	@RequestMapping(value = "/report2.do", method = RequestMethod.GET)
	public String report2(HttpServletRequest request) {
		return "report2";
	}
	
	
	@RequestMapping(value = "/holdersLong.do", method = RequestMethod.GET)
	public String holdersLong(HttpServletRequest request) {
		
		List<GStock> l=this.gStockService.queryAll();
		for(GStock s:l){
			String record=s.getRecord();
			if(StringUtils.isNotBlank(record)){
				record=StringUtils.replace(record, "-3-", "-03-");
				record=StringUtils.replace(record, "-6-", "-06-");
				record=StringUtils.replace(record, "-9-", "-09-");
				String[] datas=StringUtils.split(record,";");
				if(datas.length>0){
					List<String> li=new ArrayList<String>();
					for(String data:datas){
						if(!li.contains(data)){
							li.add(data);
						}
					}
					if(li.size()>0){
						String newRecord=StringUtils.join(li,";");
						s.setRecord(newRecord);
						this.gStockService.saveOrUpdate(s);
					}
				}
			}
		}
		
		
		/*Map<String,GStock> m=Fetch_Holders.getAllLong();
		for(String s:m.keySet()){
			GStock st=m.get(s);
			this.gStockService.insert(st);
		}*/
		return "operate";
	}
	
	
	@RequestMapping(value = "/holders.do", method = RequestMethod.GET)
	public String holders(HttpServletRequest request) {
		
		
		Map<String,String> m=Fetch_ShiZhi.fetch(null,null);
		for(String sy:m.keySet()){
			GStock s=store.holderMap.get(sy);
			if(s!=null){
				s.setLiuTong(m.get(sy));
				this.gStockService.update(s);
			}
		}
		/*List<GStock> l=Fetch_Holders.getAll(null);
		
		//Map<String,GStock> m=
		for(String s:m.keySet()){
			GStock st=m.get(s);
			this.gStockService.insert(st);
		}*/
		return "operate";
	}
	
	
	
	
	
	@RequestMapping(value = "/getToday.do", method = RequestMethod.GET)
	public String stats(HttpServletRequest request,@RequestParam Boolean force) {
		logger.info("request:get today data");

		if(Store.workingDay){
			int status=store.getDownloadStatus();
			if(status==0){
				store.setDownloading();
				new Thread(){
					public void run(){
						store.download();
						store.setDownloaded();
					}
				}.start();
				request.setAttribute("message", "开始下载！");
			}else{
				if(status==1){
					request.setAttribute("message", "正在下载中！");
					return "download";
				}
				if(status==2){
					request.setAttribute("message", "今日数据已经下载完成！");
					return "download";
				}
			}
		}else{
			if(force){
				int status=store.getDownloadStatus();
				if(status!=1){
					store.setDownloading();
					new Thread(){
						public void run(){
							store.download();
							store.setDownloaded();
						}
					}.start();
					request.setAttribute("message", "开始下载！");
					return "download";
				}
				if(status==1){
					request.setAttribute("message", "正在下载中！");
					return "download";
				}			
			}else{
				request.setAttribute("workday", Store.workingDay);
				request.setAttribute("message", "非工作日不用下载！");
			}
		}
		return "download";
	}
	
	
	
	
	
	@RequestMapping(value = "/ananyse.do", method = RequestMethod.GET)
	public String choose(HttpServletRequest request,@RequestParam Boolean force) {
		logger.info("request:ananyse");
		if(Store.workingDay){
			int status=store.getChooseStatus();
			if(status!=1){
				store.setChoosing();
				new Thread(){
					public void run(){
						store.ananyse();
						store.setChoosed();
					}
				}.start();
				request.setAttribute("message", "工作日手动分析,开始分析！");
			}
			if(status==1){
				request.setAttribute("message", "工作日手动分析,正在分析中！");
				return "choose";
			}
			if(status==2){
				request.setAttribute("message", "工作日手动分析,今日数据已经分析完成！");
				return "choose";
			}
		}else{
			if(force){
				int status=store.getChooseStatus();
				if(status!=1){
					store.setChoosing();
					new Thread(){
						public void run(){
							store.ananyse();
							store.setChoosed();
						}
					}.start();
					request.setAttribute("message", "非公作日强制手动分析,开始分析！");
					return "choose";
				}
				if(status==1){
					request.setAttribute("message", "非公作日强制手动分析,正在分析中！");
					return "choose";
				}
			}else{
				request.setAttribute("workday", Store.workingDay);
				request.setAttribute("message", "非工作日不用分析！");
			}			
		}
		return "choose";
	}
	
	
	
	@RequestMapping(value = "/record.do", method = RequestMethod.GET)
	public String record(HttpServletRequest request) throws IOException, ParseException {
		logger.info("request:view record");
		GUser user=(GUser) request.getSession().getAttribute("user");
		List<GRecord> l=this.gRecordService.queryAll(user.getId());
		//List<StatsDO> l=DataService.getRecords(user.getId());
		if(l.size()>0){
			GRecord d=l.get(l.size()-1);
			d.setLast(true);
			request.setAttribute("t", d);
			Integer lastVRate=d.getRate();
			Integer value=d.getMoney();
			request.setAttribute("lastValue", value);
			request.setAttribute("lastVRate", lastVRate);
		}		
		request.setAttribute("data", l);
		request.setAttribute("size", l.size());
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

		logger.info("request:add record");
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
		
		GRecord g=new GRecord();
		g.setDate(FetchUtil.FILE_FORMAT.parse(date));
		g.setMoney(value);
		g.setModi(change);
		g.setRate((value-change)*lastVRate/lastValue);
		g.setAyc(ayc);
		g.setAsc(asc);
		g.setNyc(nyc);
		g.setNsc(nsc);
		
		g.setAyv(ayv);
		g.setAsv(asv);
		g.setNyv(nyv);
		g.setNsv(nsv);
		
		g.setAyp(ayp);
		g.setAsp(asp);
		g.setNyp(nyp*100/value);
		g.setNsp(nsp*100/value);
		
		g.setAyr((int)(ayr*100F));
		g.setAsr((int)(asr*100F));
		g.setNyr((int)(nyr*100F));
		g.setNsr((int)(nsr*100F));
		g.setUser(user.getId());
		
		this.gRecordService.insert(g);
		
		
		/*File f = new File(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");  
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
		}*/
		response.sendRedirect(request.getContextPath() + "/record.do");  
		return null;
	}
	
	
	
	@RequestMapping(value = "/delLastLine.do", method = RequestMethod.GET)
	public void delLastLine(HttpServletRequest request,HttpServletResponse response
			) throws IOException, ParseException {
		logger.info("request:del last line");	
		GUser user=(GUser) request.getSession().getAttribute("user");
		File f = new File(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");  
		if(f.exists()){
			BufferedReader br=new BufferedReader(new FileReader(f));
			List<String> content=new ArrayList<String>();
			String line=null;
			while((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();	
			if(content.size()>1){
				content.remove(content.size()-1);
			}
			
			f.delete();
			f = new File(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");  
			if(!f.exists()){
				FetchUtil.createFile(FetchUtil.FILE_USER_STATS_BASE+user.getId()+".csv");
				BufferedWriter bw=new BufferedWriter(new FileWriter(f,true));
				for(String c:content){
					bw.write(c+"\n");
				}
				bw.close();
			}
		}
		response.sendRedirect(request.getContextPath() + "/record.do");  
	}
	
	
	
	@RequestMapping(value = "/statsData.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> statsData(HttpServletRequest request) throws IOException, ParseException {
		logger.info("request:get stats data");
		GUser user=(GUser) request.getSession().getAttribute("user");
		List<GRecord> list=this.gRecordService.queryAll(user.getId());
		Map<String,Object> m=DataService.mockStats(user.getId(),list);
		return m;
	}
	
	
	
	@RequestMapping(value = "/stats.do", method = RequestMethod.GET)
	public String stats(HttpServletRequest request) throws IOException, ParseException {
		logger.info("request:view stats chart");
		GUser user=(GUser) request.getSession().getAttribute("user");
		List<GRecord> list=this.gRecordService.queryAll(user.getId());  
		if(list.size()<0){
			request.setAttribute("exist", false);
		}else{
			request.setAttribute("exist", true);
		}
		return "stats";
	}
	
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + "/publicPool.do?currentCat=&currentSymbol="); 
		return null;
	}
	
	
	
	@RequestMapping(value = "/history.do", method = RequestMethod.GET)
	public String history(HttpServletRequest request) {
		logger.info("request:view public pool");
		List<GHis> all=store.history;
		if(all.size()>0){
			all.get(0).setPosition("head");
			all.get(all.size()-1).setPosition("tail");
		}
		request.setAttribute("all", all);
		return "history";
	}
	
	@RequestMapping(value = "/holderList.do", method = RequestMethod.GET)
	public String holderList(HttpServletRequest request) {
		logger.info("request:view public pool");
		List<Stock> holder = new Holder_Choose_MultiThread(store).choose();
		Collections.sort(holder,new Comparator.HoldereComparator());
		
		Map<String,Stock> all=Fetch_AllStock.map;
		request.setAttribute("all", holder);
		return "holder";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/publicPool.do", method = RequestMethod.GET)
	public String publicPool(HttpServletRequest request) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		logger.info("request:view public pool");
		List<GPublicStock> all=store.publicStock;
		
		List<GPublicStock> acvu=new ArrayList<GPublicStock>();
		List<GPublicStock> av5=new ArrayList<GPublicStock>();
		List<GPublicStock> av10=new ArrayList<GPublicStock>();
		List<GPublicStock> big=new ArrayList<GPublicStock>();
		List<GPublicStock> tp=new ArrayList<GPublicStock>();
		List<GPublicStock> ratio=new ArrayList<GPublicStock>();
		List<GPublicStock> cb=new ArrayList<GPublicStock>();
		List<GPublicStock> other=new ArrayList<GPublicStock>();
		
		//Set<String> s=store.publicPool.keySet();
		Set<String> set=new HashSet<String>();
		List<String> symbolList=new ArrayList<String>();
	
		for(GPublicStock s:all){
		    String type=s.getType();
		    if(type==null){
		    	String symbol=s.getSymbol();
		    	if(!(symbol.equals("sh000001"))&&!(symbol.equals("sz399001"))&&!(symbol.equals("sz399006"))&&!(symbol.equals("sz399101"))){
		    		other.add(s);
		    	}
		    }else{
		    	if(type.equals("acvu")){
		    		acvu.add(s);
		    	}
		    	if(type.equals("av5")){
		    		av5.add(s);
		    	}
		    	if(type.equals("av10")){
		    		av10.add(s);
		    	}
		    	if(type.equals("big")){
		    		big.add(s);
		    	}
		    	if(type.equals("tp")){
		    		tp.add(s);
		    	}
		    	if(type.equals("ratio")){
		    		ratio.add(s);
		    	}
		    	if(type.equals("cb")){
		    		cb.add(s);
		    	}
		    }
			symbolList.add(s.getSymbol());
		}
		set.addAll(symbolList);
		String symbols="";
		symbols=StringUtils.join(set,",");
		List<Stock> newL=Fetch_ServeralStock_Sina.fetch(symbols,Store.getProxy());
		
		/*List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			callList.add(new RealTask(sys));
		}
		long start=System.currentTimeMillis();
        List<Object> r=threadService.service(callList);	
        long end=System.currentTimeMillis();
        logger.info("获取实时行情耗时："+(end-start)+"毫秒");
        
        
        for(Object l:r){
        	List<Stock> slice=(List<Stock>)l;
        	result.addAll(slice);
        }*/
		List<Stock> result=new ArrayList<Stock>();
        result=newL;
        Map<String,Stock> allR=new HashMap<String,Stock>();
        for(Stock s:result){
        	allR.put(s.getSymbol(), s);
        }
        List<Stock> acvus=new ArrayList<Stock>();
		List<Stock> av5s=new ArrayList<Stock>();
		List<Stock> av10s=new ArrayList<Stock>();
		List<Stock> bigs=new ArrayList<Stock>();
		List<Stock> tps=new ArrayList<Stock>();
		List<Stock> ratios=new ArrayList<Stock>();
		List<Stock> cbs=new ArrayList<Stock>();

		acvu.addAll(other);
		
		Set<String> sss=allR.keySet();
		
		for(GPublicStock s:acvu){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				acvus.add(st);
			}
		}
		for(GPublicStock s:av5){
			Stock st=allR.get(s.getSymbol());
			Stock st2=allR.get("sz002053");
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				av5s.add(st);
			}
		}
		for(GPublicStock s:av10){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				av10s.add(st);
			}
		}
		for(GPublicStock s:big){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				bigs.add(st);
			}
		}
		for(GPublicStock s:tp){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				tps.add(st);
			}
		}
		for(GPublicStock s:ratio){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				ratios.add(st);
			}
		}
		for(GPublicStock s:cb){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				cbs.add(st);
			}
		}
        
        
        
        List<Stock> indexs=new ArrayList<Stock>();
        //List<Stock> nonIndexs=new ArrayList<Stock>();
        for(Stock o1:result){
        	if(o1.getSymbol().equals("sh000001")||o1.getSymbol().equals("sz399001")||o1.getSymbol().equals("sz399006")||o1.getSymbol().equals("sz399101")){
        		indexs.add(o1);
        		o1.setHtName(o1.getName());
        	}/*else{
        		nonIndexs.add(o1);
        	}*/
        }
		/*if(nonIndexs.size()>0){
			Collections.sort(nonIndexs,new Comparator.RateDescComparator());
		}
		*/
		
		if(indexs.size()>0){
			Collections.sort(indexs,new Comparator.RateDescComparator());
			//Collections.reverse(tps);
		}
		if(acvus.size()>0){
			Collections.sort(acvus,new Comparator.RateDescComparator());
			Collections.reverse(acvus);
		}
		if(av5s.size()>0){
			Collections.sort(av5s,new Comparator.RateDescComparator());
			Collections.reverse(av5s);
		}
		if(av10s.size()>0){
			Collections.sort(av10s,new Comparator.RateDescComparator());
			Collections.reverse(av10s);
		}
		if(bigs.size()>0){
			Collections.sort(bigs,new Comparator.RateDescComparator());
			Collections.reverse(bigs);
		}
		if(ratios.size()>0){
			Collections.sort(ratios,new Comparator.RateDescComparator());
			Collections.reverse(ratios);
		}
		if(tps.size()>0){
			Collections.sort(tps,new Comparator.RateDescComparator());
			Collections.reverse(tps);
		}
		if(cbs.size()>0){
			Collections.sort(cbs,new Comparator.RateDescComparator());
			Collections.reverse(cbs);
		}
		
		
		
		result.clear();
		result.addAll(indexs);
		result.addAll(acvus);
		if(result.size()>0){
			result.get(0).setPosition("head");
			result.get(result.size()-1).setPosition("tail");
		}
		
		
		
		request.setAttribute("pool", store.publicStockMap);
		request.setAttribute("acvu", result);
		request.setAttribute("av5", av5s);
		request.setAttribute("av10", av10s);
		request.setAttribute("big", bigs);
		request.setAttribute("tp", tps);
		request.setAttribute("ratio", ratios);
		request.setAttribute("cb", cbs);
		request.setAttribute("set", set);
		
		request.setAttribute("acvuSize", result.size());
		request.setAttribute("av5Size", av5s.size());
		request.setAttribute("av10Size", av10s.size());
		request.setAttribute("bigSize", bigs.size());
		request.setAttribute("tpSize", tps.size());
		request.setAttribute("ratioSize", ratios.size());
		request.setAttribute("cbSize", cbs.size());
		request.setAttribute("set", set);
		return "publicPool";
	}
	
	
	public void setHighLight(GPublicStock s,Stock st){
		if(s.getConcern()!=null){
			if(s.getConcern()==1){
				st.setName("<font color=red><b>"+st.getName()+"</b></font>");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/operate.do", method = RequestMethod.GET)
	public String operate(HttpServletRequest request) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		String currentCat=request.getParameter("currentCat");
		String currentSymbol=request.getParameter("currentSymbol");
		logger.info("request:view public pool");
		request.setAttribute("currentCat", currentCat);
		request.setAttribute("currentSymbol", currentSymbol);
		List<GPublicStock> all=store.publicStock;
		
		List<GPublicStock> acvu=new ArrayList<GPublicStock>();
		List<GPublicStock> av5=new ArrayList<GPublicStock>();
		List<GPublicStock> av10=new ArrayList<GPublicStock>();
		List<GPublicStock> big=new ArrayList<GPublicStock>();
		List<GPublicStock> tp=new ArrayList<GPublicStock>();
		List<GPublicStock> ratio=new ArrayList<GPublicStock>();
		List<GPublicStock> cb=new ArrayList<GPublicStock>();
		List<GPublicStock> other=new ArrayList<GPublicStock>();
		
		//Set<String> s=store.publicPool.keySet();
		Set<String> set=new HashSet<String>();
		List<String> symbolList=new ArrayList<String>();
	
		for(GPublicStock s:all){
		    String type=s.getType();
		    if(type==null){
		    	String symbol=s.getSymbol();
		    	if(!(symbol.equals("sh000001"))&&!(symbol.equals("sz399001"))&&!(symbol.equals("sz399006"))&&!(symbol.equals("sz399101"))){
		    		other.add(s);
		    	}
		    }else{
		    	if(type.equals("acvu")){
		    		acvu.add(s);
		    	}
		    	if(type.equals("av5")){
		    		av5.add(s);
		    	}
		    	if(type.equals("av10")){
		    		av10.add(s);
		    	}
		    	if(type.equals("big")){
		    		big.add(s);
		    	}
		    	if(type.equals("tp")){
		    		tp.add(s);
		    	}
		    	if(type.equals("ratio")){
		    		ratio.add(s);
		    	}
		    	if(type.equals("cb")){
		    		cb.add(s);
		    	}
		    }
			symbolList.add(s.getSymbol());
		}
		set.addAll(symbolList);
		String symbols=StringUtils.join(set,",");
		List<Stock> newL=Fetch_ServeralStock_Sina.fetch(symbols,Store.getProxy());
		
		
		/*List<List<Object>> symbolTaskList=ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList=new ArrayList<Callable<Object>>();
		for(List<Object> sys:symbolTaskList){
			callList.add(new RealTask(sys));
		}
        List<Object> r=threadService.service(callList);		
        List<Stock> result=new ArrayList<Stock>();
        for(Object l:r){
        	List<Stock> slice=(List<Stock>)l;
        	result.addAll(slice);
        }*/
        
		List<Stock> result=newL;
        Map<String,Stock> allR=new HashMap<String,Stock>();
        for(Stock s:result){
        	allR.put(s.getSymbol(), s);
        }
        List<Stock> acvus=new ArrayList<Stock>();
		List<Stock> av5s=new ArrayList<Stock>();
		List<Stock> av10s=new ArrayList<Stock>();
		List<Stock> bigs=new ArrayList<Stock>();
		List<Stock> tps=new ArrayList<Stock>();
		List<Stock> ratios=new ArrayList<Stock>();
		List<Stock> cbs=new ArrayList<Stock>();

		acvu.addAll(other);
		
		for(GPublicStock s:acvu){
			Stock st=allR.get(s.getSymbol());
			
			if(st!=null){
				if(s.getConcern()!=null){
					
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				acvus.add(st);
			}
		}
		for(GPublicStock s:av5){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				av5s.add(st);
			}
		}
		for(GPublicStock s:av10){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				av10s.add(st);
			}
		}
		for(GPublicStock s:big){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				bigs.add(st);
			}
		}
		for(GPublicStock s:tp){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				tps.add(st);
			}
		}
		for(GPublicStock s:ratio){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				ratios.add(st);
			}
		}
		for(GPublicStock s:cb){
			Stock st=allR.get(s.getSymbol());
			if(st!=null){
				if(s.getConcern()!=null){
					st.setHtName(st.getName());
					if(s.getConcern()==1){
						st.setHtName("<b>"+st.getName()+"</b>");
					}
					if(s.getConcern()==2){
						st.setHtName("<font color=red><b>"+st.getName()+"</b></font>");
					}
					st.setConcern(s.getConcern());
				}else{
					st.setConcern((byte)0);
					st.setHtName(st.getName());
				}
				cbs.add(st);
			}
		}
        
        
        
        List<Stock> indexs=new ArrayList<Stock>();
        //List<Stock> nonIndexs=new ArrayList<Stock>();
        for(Stock o1:result){
        	if(o1.getSymbol().equals("sh000001")||o1.getSymbol().equals("sz399001")||o1.getSymbol().equals("sz399006")||o1.getSymbol().equals("sz399101")){
        		indexs.add(o1);
        		o1.setHtName(o1.getName());
        	}/*else{
        		nonIndexs.add(o1);
        	}*/
        }
		/*if(nonIndexs.size()>0){
			Collections.sort(nonIndexs,new Comparator.RateDescComparator());
		}
		*/
		
		if(indexs.size()>0){
			Collections.sort(indexs,new Comparator.RateDescComparator());
		}
		if(acvus.size()>0){
			Collections.sort(acvus,new Comparator.RateDescComparator());
			Collections.reverse(acvus);
		}
		if(av5s.size()>0){
			Collections.sort(av5s,new Comparator.RateDescComparator());
			Collections.reverse(av5s);
		}
		if(av10s.size()>0){
			Collections.sort(av10s,new Comparator.RateDescComparator());
			Collections.reverse(av10s);
		}
		if(bigs.size()>0){
			Collections.sort(bigs,new Comparator.RateDescComparator());
			Collections.reverse(bigs);

		}
		if(ratios.size()>0){
			Collections.sort(ratios,new Comparator.RateDescComparator());
			Collections.reverse(ratios);

		}
		if(tps.size()>0){
			Collections.sort(tps,new Comparator.RateDescComparator());
			Collections.reverse(tps);

		}
		if(cbs.size()>0){
			Collections.sort(cbs,new Comparator.RateDescComparator());
			Collections.reverse(cbs);
		}
		
		
		
		if(bigs.size()>0){
			bigs.get(0).setPosition(bigs.get(0).getPosition()+"head");
			bigs.get(bigs.size()-1).setPosition(bigs.get(bigs.size()-1).getPosition()+" tail");
		}
		if(acvus.size()>0){
			acvus.get(0).setPosition(acvus.get(0).getPosition()+"head");
			acvus.get(acvus.size()-1).setPosition(acvus.get(acvus.size()-1).getPosition()+" tail");
		}
		if(av5s.size()>0){
			av5s.get(0).setPosition(av5s.get(0).getPosition()+"head");
			av5s.get(av5s.size()-1).setPosition(av5s.get(av5s.size()-1).getPosition()+" tail");
		}
		if(av10s.size()>0){
			av10s.get(0).setPosition(av10s.get(0).getPosition()+"head");
			av10s.get(av10s.size()-1).setPosition(av10s.get(av10s.size()-1).getPosition()+" tail");
		}
		if(cbs.size()>0){
			cbs.get(0).setPosition(cbs.get(0).getPosition()+"head");
			cbs.get(cbs.size()-1).setPosition(cbs.get(cbs.size()-1).getPosition()+" tail");
		}
		if(ratios.size()>0){
			ratios.get(0).setPosition(ratios.get(0).getPosition()+"head");
			ratios.get(ratios.size()-1).setPosition(ratios.get(ratios.size()-1).getPosition()+" tail");
		}
		if(tps.size()>0){
			tps.get(0).setPosition(tps.get(0).getPosition()+"head");
			tps.get(tps.size()-1).setPosition(tps.get(tps.size()-1).getPosition()+" tail");
		}
		
		
		
		
		result.clear();
		result.addAll(indexs);
		result.addAll(acvus);
		result.get(0).setPosition("head");
		result.get(result.size()-1).setPosition("tail");
		
		
		
		request.setAttribute("acvu", result);
		request.setAttribute("av5", av5s);
		request.setAttribute("av10", av10s);
		request.setAttribute("big", bigs);
		request.setAttribute("tp", tps);
		request.setAttribute("ratio", ratios);
		request.setAttribute("cb", cbs);
		request.setAttribute("set", set);
		
		request.setAttribute("acvuSize", result.size());
		request.setAttribute("av5Size", av5s.size());
		request.setAttribute("av10Size", av10s.size());
		request.setAttribute("bigSize", bigs.size());
		request.setAttribute("tpSize", tps.size());
		request.setAttribute("ratioSize", ratios.size());
		request.setAttribute("cbSize", cbs.size());
		request.setAttribute("set", set);
		return "operate";
	}
	
	
	@RequestMapping(value = "/check.do", method = RequestMethod.GET)
	public String check(HttpServletRequest request) {
		logger.info("request:check");
		return "check";
	}
	
	@RequestMapping(value = "/allIndex.do", method = RequestMethod.GET)
	public String allIndex(HttpServletRequest request) {
		logger.info("request:check");
		return "allIndex";
	}
	
	
	@RequestMapping(value = "/removeFromPublicPool.do", method = RequestMethod.GET)
	public String removeFromPublicPool(HttpServletRequest request,HttpServletResponse response, @RequestParam String symbol) throws IOException {
		String currentCat=request.getParameter("currentCat");
		String currentSymbol=request.getParameter("currentSymbol");
		if(StringUtils.isBlank(currentCat)){
			currentCat="";
		}
        if(currentCat.equals("undifined")){
        	currentCat="";
		}
        
        if(StringUtils.isBlank(currentSymbol)){
        	currentSymbol="";
		}
        if(currentSymbol.equals("undifined")){
        	currentSymbol="";
		}
        
		logger.info("request:remove from public pool");
        GPublicStock ps=this.gPublicStockService.findRecordByProperty("symbol", symbol);
        if(ps!=null){
        	ps.setHold((byte)0);
        	ps.setRemoveDate(new Date());
        	this.gPublicStockService.update(ps);
        	this.store.removeFromPublic(symbol);
        	this.store.reloadPublicStock();
        }
        response.sendRedirect(request.getContextPath() + "/operate.do?currentCat="+currentCat+"&currentSymbol="+currentSymbol); 
		return null;
	}
	
	@RequestMapping(value = "/delFromPublicPool.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delFromPublicPool(HttpServletRequest request,HttpServletResponse response, @RequestParam String symbol) throws IOException {
		Map<String,Object> m=new HashMap<String,Object>();	
		logger.info("request:remove from public pool");
        GPublicStock ps=this.gPublicStockService.queryStockInPool(symbol);
        if(ps!=null){
        	this.gPublicStockService.delete(ps);
        	this.store.reloadPublicStock();
        }
		return m;
	}
	
	
	@RequestMapping(value = "/indexReal.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> indexReal(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String,Object> m=new HashMap<String,Object>();	
		logger.info("request:remove from public pool");
		
		List<Stock> l=Fetch_ServeralStock_Sina.fetch("sh000001,sz399001,sz399101,sz399006",Store.getProxy());
        Stock sh=l.get(0);
        Stock sz=l.get(1);
        Stock zh=l.get(2);
        Stock ch=l.get(3);
        
        String shs="";
        String szs="";
        String zhs="";
        String chs="";
        
        if(sh.getRealRate()>0){
        	shs="<b><font color=red>&nbsp;"+FetchUtil.formatRatePercent(sh.getRate())+"</font></b><img src='resources/pic/up2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }else{
        	shs="<b><font color=green>&nbsp;"+FetchUtil.formatRatePercent(sh.getRate())+"</font></b><img src='resources/pic/dn2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }
        
        if(sz.getRealRate()>0){
        	szs="<b><font color=red>&nbsp;"+FetchUtil.formatRatePercent(sz.getRate())+"</font></b><img src='resources/pic/up2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }else{
        	szs="<b><font color=green>&nbsp;"+FetchUtil.formatRatePercent(sz.getRate())+"</font></b><img src='resources/pic/dn2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }
        
        if(zh.getRealRate()>0){
        	zhs="<b><font color=red>&nbsp;"+FetchUtil.formatRatePercent(zh.getRate())+"</font></b><img src='resources/pic/up2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }else{
        	zhs="<b><font color=green>&nbsp;"+FetchUtil.formatRatePercent(zh.getRate())+"</font></b><img src='resources/pic/dn2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }
        
        if(ch.getRealRate()>0){
        	chs="<b><font color=red>&nbsp;"+FetchUtil.formatRatePercent(ch.getRate())+"</font></b><img src='resources/pic/up2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }else{
        	chs="<b><font color=green>&nbsp;"+FetchUtil.formatRatePercent(ch.getRate())+"</font></b><img src='resources/pic/dn2.png' style='width:12px;hight:8px'>&nbsp;&nbsp;";
        }

        
        m.put("sh", shs);
        m.put("sz", szs);
        m.put("zh", zhs);
        m.put("ch", chs);
		return m;
	}
	
	
	@RequestMapping(value = "/setConcern.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> setConcern(HttpServletRequest request,@RequestParam String symbol){
		logger.info("request:set concern");
		String value=request.getParameter("value");
		Map<String,Object> m=new HashMap<String,Object>();
		if(symbol!=null){
			GPublicStock st=store.publicStockMap.get(symbol);
			if(st!=null){
				if(StringUtils.isNotBlank(value)){
					st.setConcern(Byte.parseByte(value));
				}else{
					if(st.getConcern()==null){
						st.setConcern((byte)1);
					}else{
						if(st.getConcern()==0){
							st.setConcern((byte)1);
						}else if(st.getConcern()==1){
							st.setConcern((byte)2);
						}else if(st.getConcern()==2){
							st.setConcern((byte)0);
						}
					}
				}
				
				this.gPublicStockService.update(st);
			}
		}
		return m;
	}
	
	@RequestMapping(value = "/addPublicPool.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addPublicPool(@RequestParam String symbols,
			@RequestParam String replace){
		logger.info("request:add stock to public pool");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "ok");
		if(replace.equals("1")){
			store.publicPool.clear();
		}
		if(StringUtils.isNotBlank(symbols)){
			String[] syms=StringUtils.split(symbols,",");			
			List<GPublicStock> l=new ArrayList<GPublicStock>();
			for(String s:syms){
				GPublicStock ps=new GPublicStock();
				String symbol=null;
				String type="";
				if(s.contains("-")){
					String[] datas=StringUtils.split(s,"-");
					symbol=datas[0];
					type=datas[1];
				}else{
					symbol=s;
				}
				if(store.publicStockMap.containsKey(symbol)){
					//continue;
				}
				if(StringUtils.isNotBlank(symbol)){
					if(StringUtils.isNumeric(symbol)){
						if(s.length()==6){
							if(s.startsWith("6")){
								s="sh"+s;
							}else{
								s="sz"+s;
							}
						}
						if(s.length()==7){
							if(s.startsWith("0")){
								symbol=s.replaceFirst("0", "sz");
							}
							if(s.startsWith("1")){
								symbol=s.replaceFirst("1", "sh");
							}
						}
					}else if(s.startsWith("sh")||s.startsWith("sz")){
						
					}else{
						symbol=Fetch_AllStock.nameMap.get(symbol);
					}
					
					Stock st=Fetch_AllStock.map.get(symbol);
					if(st!=null){
						ps.setName(st.getName());
					}
					ps.setSymbol(symbol);
					ps.setType(type);
					ps.setAddDate(new Date());
					l.add(ps);
				}
			}
			this.gPublicStockService.add(l);
			this.store.reloadPublicStock();
		}
		return map;
	}
	
	
	@RequestMapping(value = "/help.do", method = RequestMethod.GET)
	public String help(HttpServletRequest request) {
		return "help";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/choose.do", method = RequestMethod.GET)
	public String choose(HttpServletRequest request) {
		logger.info("request:choose stock");
		if(!store.containsKey("acvu")){
			store.ananyse();
		}
	
		
		List<String> big=(List<String>)store.get("big");
		List<String> acvu=(List<String>)store.get("acvu");
		List<String> av5=(List<String>)store.get("av5");
		List<String> av10=(List<String>)store.get("av10");
		List<String> cb=(List<String>)store.get("cb");
		List<String> ratio=(List<String>)store.get("ratio");
		List<String> tp=(List<String>)store.get("tp");
		List<String> cb2=(List<String>)store.get("cb2");
		
		
		List<Stock> bigs=new ArrayList<Stock>();
		List<Stock> acvus=new ArrayList<Stock>();
		List<Stock> av5s=new ArrayList<Stock>();
		List<Stock> av10s=new ArrayList<Stock>();
		List<Stock> cbs=new ArrayList<Stock>();
		List<Stock> ratios=new ArrayList<Stock>();
		List<Stock> tps=new ArrayList<Stock>();
		List<Stock> cb2s=new ArrayList<Stock>();
		
		for(String s:big){
			Stock st=new Stock();
			st.setSymbol(s);
			Stock stt=store.recent.get(s);
			if(stt!=null){
				st.setName(stt.getName());
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
			}
			bigs.add(st);
		}
		for(String s:acvu){
			Stock st=new Stock();
			st.setSymbol(s);
			if(s.equals("sh600602")){
				s.length();
			}
			Stock stt=store.recent.get(s);
			if(stt!=null){
				st.setName(stt.getName());
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
			}
			
			acvus.add(st);
		}
		for(String s:av5){
			Stock st=new Stock();
			st.setSymbol(s);
			
			Stock stt=store.recent.get(s);
			if(stt!=null){
				st.setName(stt.getName());
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
			}
			
			av5s.add(st);
		}
		for(String s:av10){
			Stock st=new Stock();
			st.setSymbol(s);
			
			Stock stt=store.recent.get(s);
			if(stt!=null){
				st.setName(stt.getName());
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
			}
			av10s.add(st);
		}
		
		for(String s:cb){
			Stock st=new Stock();
			st.setSymbol(s);
			
			Stock stt=store.recent.get(s);
			if(stt!=null){
				st.setName(stt.getName());
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
				if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
					st.setTing(true);
				}else{
					st.setTing(false);
				}
			}
			cbs.add(st);
		}
		
		if(ratio!=null){
			for(String s:ratio){
				Stock st=new Stock();
				st.setSymbol(s);
				
				Stock stt=store.recent.get(s);
				if(stt!=null){
					st.setName(stt.getName());
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
				}
				ratios.add(st);
			}
		}
		
		if(tp!=null){
			for(String s:tp){
				Stock st=new Stock();
				st.setSymbol(s);
				
				Stock stt=store.recent.get(s);
				if(stt!=null){
					st.setName(stt.getName());
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
				}
				tps.add(st);
			}
		}
		if(cb2!=null){
			for(String s:cb2){
				Stock st=new Stock();
				st.setSymbol(s);
				
				Stock stt=store.recent.get(s);
				if(stt!=null){
					st.setName(stt.getName());
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
					if(stt.getStartPrice().equals("0")||stt.getStartPrice().equals("0.00")){
						st.setTing(true);
					}else{
						st.setTing(false);
					}
				}
				cb2s.add(st);
			}
		}
		
		
		
		List<Stock> all=new ArrayList<Stock>();
		all.addAll(acvus);
		all.addAll(av5s);
		all.addAll(av10s);
		all.addAll(bigs);
		all.addAll(cbs);
		all.addAll(ratios);
		all.addAll(tps);
		all.addAll(cb2s);
		
		
		
		for(int i=0;i<store.kdata2.size();i++){
			String symbol=(String)store.kdata2.get(i).get("symbol");
			for(Stock st:all){
				if(st.getSymbol().equals(symbol)){
					st.setIndex(i);
				}
			}
		}
		
		
		
		if(bigs.size()>0){
			bigs.get(0).setPosition("head");
			bigs.get(bigs.size()-1).setPosition(bigs.get(bigs.size()-1).getPosition()+" tail");
		}
		if(acvus.size()>0){
			acvus.get(0).setPosition("head");
			acvus.get(acvus.size()-1).setPosition(acvus.get(acvus.size()-1).getPosition()+" tail");
		}
		if(av5s.size()>0){
			av5s.get(0).setPosition("head");
			av5s.get(av5s.size()-1).setPosition(av5s.get(av5s.size()-1).getPosition()+" tail");
		}
		if(av10s.size()>0){
			av10s.get(0).setPosition("head");
			av10s.get(av10s.size()-1).setPosition(av10s.get(av10s.size()-1).getPosition()+" tail");
		}
		if(cbs.size()>0){
			cbs.get(0).setPosition("head");
			cbs.get(cbs.size()-1).setPosition(cbs.get(cbs.size()-1).getPosition()+" tail");
		}
		if(cb2s.size()>0){
			cb2s.get(0).setPosition("head");
			cb2s.get(cb2s.size()-1).setPosition(cb2s.get(cb2s.size()-1).getPosition()+" tail");
		}
		if(ratios.size()>0){
			ratios.get(0).setPosition("head");
			ratios.get(ratios.size()-1).setPosition(ratios.get(ratios.size()-1).getPosition()+" tail");
		}
		if(tps.size()>0){
			tps.get(0).setPosition("head");
			tps.get(tps.size()-1).setPosition(tps.get(tps.size()-1).getPosition()+" tail");
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
		if(cb.size()>size){
			size=cb.size();
		}
		if(cb2.size()>size){
			size=cb2.size();
		}
		if(ratio.size()>size){
			size=ratio.size();
		}
		if(tp.size()>size){
			size=tp.size();
		}

		request.setAttribute("size", size);
		request.setAttribute("big", bigs);
		request.setAttribute("acvu", acvus);
		request.setAttribute("av5", av5s);
		request.setAttribute("av10", av10s);
		request.setAttribute("cb", cbs);
		request.setAttribute("cb2", cb2s);
		request.setAttribute("tp", tps);
		request.setAttribute("ratio", ratios);
		
		request.setAttribute("bigSize", big.size());
		request.setAttribute("acvuSize", acvu.size());
		request.setAttribute("av5Size", av5.size());
		request.setAttribute("av10Size", av10.size());
		request.setAttribute("cbSize", cb.size());
		request.setAttribute("cb2Size", cb2.size());
		request.setAttribute("ratioSize", ratio.size());
		request.setAttribute("tpSize", tp.size());
		request.setAttribute("cb2Size", cb2.size());
		
		request.setAttribute("bigStr", "'"+StringUtils.join(big,"','")+"'");
		request.setAttribute("acvuStr",  "'"+StringUtils.join(acvu,"','")+"'");
		request.setAttribute("av5Str",  "'"+StringUtils.join(av5,"','")+"'");
		request.setAttribute("av10Str", "'"+ StringUtils.join(av10,"','")+"'");
		request.setAttribute("cbStr",  "'"+StringUtils.join(cb,"','")+"'");
		request.setAttribute("cb2Str",  "'"+StringUtils.join(cb2,"','")+"'");
		request.setAttribute("ratioStr",  "'"+StringUtils.join(ratio,"','")+"'");
		request.setAttribute("tpStr", "'"+ StringUtils.join(tp,"','")+"'");
		
			
		return "stockPool";
	}
	
	
	
	@RequestMapping(value = "/kData.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> kData(@RequestParam String symbol) throws IOException, ParseException {
		logger.info("request:get k data");
		if(symbol.startsWith("sz")||symbol.startsWith("sh")){
			
		}else if(symbol.length()==6&&StringUtils.isNumeric(symbol)){
			if(symbol.startsWith("6")){
				symbol="sh"+symbol;
			}else{
				symbol="sz"+symbol;
			}
		}else{
			symbol=Fetch_AllStock.nameMap.get(symbol);
		}
		
		Boolean download=false;
		if(Store.downloaded==2){
			download=true;
		}
		logger.info("name to symbol:"+symbol);

		Map<String,Object> map=dataService.getKData2(symbol,Store.workingDay,download,store);
		return map;
	}	
	
	
	@RequestMapping(value = "/allkData.do", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> allkData() throws IOException, ParseException {
		Map<String,Stock> map=Fetch_AllStock.map;
		Boolean download=false;
		if(Store.downloaded==2){
			download=true;
		}
		
		List<Stock> holder = new Holder_Choose_MultiThread(store).choose();
		Collections.sort(holder,new Comparator.HoldereComparator());
		
		List<Map<String,Object>> r=new ArrayList<Map<String,Object>>();
		for(Stock s:holder){
			Map<String,Object> m=dataService.getKData3(s.getSymbol(),Store.workingDay,download,store);
			r.add(m);
		}
		
		
		
		
		return r;
	}	
	
	@RequestMapping(value = "/getAll.do", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getAll() throws IOException, ParseException {
		List<Map<String,Object>> map=store.kdata2;
		return map;
	}	
	
	
	@RequestMapping(value = "/his.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> history(@RequestParam String symbol,@RequestParam String name,
			@RequestParam long start,
			@RequestParam long end) throws IOException, ParseException {
		Map<String,Object> map=new HashMap<String,Object>();
		GHis h=new GHis();
		h.setSymbol(symbol);
		h.setName(name);
		h.setStart(new Date(start));
		h.setEnd(new Date(end));
		
		this.gHisService.insert(h);
		this.store.reloadHistoryStock();
		return map;
	}	
	
	
	
	@RequestMapping(value = "/canonHistory.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> canonHistory(@RequestParam Integer hisId) throws IOException, ParseException {
		logger.info("request:get k data");
		Calendar c=Calendar.getInstance();
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int minits=c.get(Calendar.MINUTE);

		
		GHis h=store.hisMap.get(hisId);
		boolean shi=true;
		if(hour<9&&hour>=15){
			shi=false;
		}
		if(hour==9&&minits<30){
			shi=false;
		}
		
		//List<GPublicStock> his=this.gPublicStockService.queryHistory(symbol);
		Map<String,Object> map=DataService.canonHistory(h.getSymbol(),Store.workingDay,shi,h);
		return map;
	}	
	
	
	
	@RequestMapping(value = "/setType.do", method = RequestMethod.GET)
	public String setType(HttpServletRequest request,HttpServletResponse response,@RequestParam String symbol,@RequestParam String type) throws IOException, ParseException {
		this.gPublicStockService.setType(symbol, type);
		response.sendRedirect(request.getContextPath() + "/publicPool.do?currentCat=&currentSymbol="); 
		return null;
	}
	
	
	
	public static void main(String args[]){
		System.out.println(StringUtils.isNumeric("002333"));
	}
}
