package com.taobao.finance.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.choose.local.thread.AV10_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AV5_Trend_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.AVCU_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.TP_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.other.BigTrend_Choose_MultiThread;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GTask;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.service.DataService;
import com.taobao.finance.service.GPublicStockService;
import com.taobao.finance.service.GTaskService;
import com.taobao.finance.service.ThreadService;
import com.taobao.finance.task.HisDataTask;
import com.taobao.finance.task.UnformalDataTask;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

@Component
@DependsOn("fetchUtil")
public class Store {
	private static final Logger logger = Logger.getLogger("taskLogger");
	public Map<String, Object> store = new HashMap<String, Object>();
	public Map<String, Integer> download = new HashMap<String, Integer>();
	public Map<String, Integer> choose = new HashMap<String, Integer>();
	public Map<String, Stock> publicPool = new HashMap<String, Stock>();
	public Map<String, Boolean> checkWorkingRecord = new HashMap<String, Boolean>();
	public List<GPublicStock> publicStock = new ArrayList<GPublicStock>();
	public List<GPublicStock> history = new ArrayList<GPublicStock>();
	public Map<String,Stock> recent=new HashMap<String,Stock>();
	public Map<String,List<Stock>> hot=new HashMap<String,List<Stock>>();
	public Map<String,Object> kdata=new HashMap<String,Object>();
	public List<Map<String,Object>> kdata2=new ArrayList<Map<String,Object>>();

	public static Boolean workingDay = null;
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");
	public static DateFormat DF2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public static int DOWNLOAD_STATUS_DOWNLOADING = 1;
	public static int DOWNLOAD_STATUS_DOWNLOADED = 2;
	public static int CHOOSEN_STATUS_CHOOSING = 1;
	public static int CHOOSEN_STATUS_CHOOSEN = 2;
	public static int downloaded = 0;
	public static int choosen = 0;

	@Autowired
	private ThreadService threadService;

	@Autowired
	private GTaskService gTaskService;
	
	@Autowired
	private DataService dataService;

	private GTask today;

	public Store() {

	}

	@PostConstruct
	public void init() {

		if (workingDay == null) {
			workingDay = FetchUtil.checkWorkingDayUsusal();
		}
		logger.info("\n\n\n");
		logger.info("*******************************************************");
		logger.info("system start,normal check workingday:" + workingDay);
		today = gTaskService.queryLastTask();
		Set<String> sSet=new HashSet<String>();
		if (StringUtils.isNoneBlank(today.getAcvu())) {
			String[] ids = StringUtils.split(today.getAcvu(), ",");
			List<String> l = new ArrayList<String>();
			l.addAll(Arrays.asList(ids));
			store.put("acvu", l);
			sSet.addAll(l);
		}
		if (StringUtils.isNoneBlank(today.getBig())) {
			String[] ids = StringUtils.split(today.getBig(), ",");
			List<String> l = new ArrayList<String>();
			l.addAll(Arrays.asList(ids));
			store.put("big", l);
			sSet.addAll(l);
		}
		if (StringUtils.isNoneBlank(today.getAv5())) {
			String[] ids = StringUtils.split(today.getAv5(), ",");
			List<String> l = new ArrayList<String>();
			l.addAll(Arrays.asList(ids));
			store.put("av5", l);
			sSet.addAll(l);
		}
		if (StringUtils.isNoneBlank(today.getAv10())) {
			String[] ids = StringUtils.split(today.getAv10(), ",");
			List<String> l = new ArrayList<String>();
			l.addAll(Arrays.asList(ids));
			store.put("av10", l);
			sSet.addAll(l);
		}
		
		for(String s:Fetch_AllStock.map.keySet()){
			Stock st=Hisdata_Base.readTmpData(s);
			if(st!=null){
				st.setName(Fetch_AllStock.map.get(s).getName());
				this.recent.put(s,st);
			}
		}
		
		for(String s:sSet){
			List<Stock> l=Hisdata_Base.readHisDataMerge(s, null);
			List<Stock> lNew=new ArrayList<Stock>();
			if(l.size()>300){
				for(int i=l.size()-300;i<l.size();i++){
					lNew.add(l.get(i));
				}
			}else{
				lNew=l;
			}
			hot.put(s, lNew);
		}
		
		for(String s:sSet){			
			Boolean down=false;
			if(Store.downloaded==2){
				down=true;
			}
			try {
				Map<String,Object> m=dataService.getKData2(s, this.workingDay, down, this);
				this.kdata.put(s, m);
				this.kdata2.add(m);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		for(String s:Fetch_AllStock.map.keySet()){
			Stock st=Hisdata_Base.readTmpData(s);
			if(st!=null){
				st.setName(Fetch_AllStock.map.get(s).getName());
				this.recent.put(s,st);
			}
		}
		
		logger.info("system start,load anasys result");

		if (today != null) {
			if (workingDay) {
				Date d = new Date();
				if (today.getDate().getDate() == d.getDate()) {
					downloaded = today.getDownload();
					choosen = today.getChoose();
				} else {
					downloaded = 0;
					choosen = 0;
				}
			} else {
				Date d = new Date();
				if (today.getDate().getDate() == d.getDate()) {

				} else {
					downloaded = today.getDownload();
					choosen = today.getChoose();
				}
			}
		} else {
			downloaded = 0;
			choosen = 0;
		}
		logger.info("*******************************************************");
		
		
		
		publicStock = this.gPublicStockService.queryAll();
		history = this.gPublicStockService.queryHistory();
		Thread d = new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {

				while (true) {
					try {
						logger.info("\n\n");
						logger.info("heart beat check---------------------------------------");
						today = gTaskService.queryLastTask();
						if (today.getDate().getDate() == new Date().getDate()) {
							if (today.getWorking() == 1) {
								workingDay = true;
							}
						} else {
							workingDay = false;
						}

						Date d = new Date();
						String endDateStr = DF.format(d) + " 15:00:00";
						String beginDateStr = DF.format(d) + " 09:30:00";
						String beginDateStr3 = DF.format(d) + " 10:30:00";
						Date closeTime = DF2.parse(endDateStr);
						Date beginTime = DF2.parse(beginDateStr);
						Date beginTime3 = DF2.parse(beginDateStr3);

						if (d.after(beginTime) && d.before(beginTime3)) {

							workingDay = FetchUtil.checkWorkingDay2();
							logger.info("routin check workingday:" + workingDay);
							if (workingDay) {
								today = gTaskService.queryLastTask();
								if (today.getDate().getDate() != new Date()
										.getDate()) {
									GTask t = new GTask();
									Date dd = new Date();
									String dstr = DF.format(dd);
									try {
										t.setDate(DF.parse(dstr));
									} catch (ParseException e) {
										e.printStackTrace();
									}
									t.setDownload(GTask.NON_DOWNLOAD);
									t.setWorking(GTask.WORKING);
									t.setChoose(GTask.NON_CHOOSE);
									t.setInsDate(new Date());
									logger.info("now task:"+today);
									t = gTaskService.insert(t);
									logger.info("insert task");
								}
							}

							if (workingDay) {
								downloaded = 0;
								choosen=0;
							}
						}


						if (d.after(closeTime)) {
							boolean canDownload = false;
							if (workingDay) {
								if (today != null) {
									if (today.getDate().getDate() == d.getDate()) {
										if (today.getDownload() == 1|| today.getDownload() == 2) {
											canDownload = false;
										}else{
											canDownload = true;
										}
									} else {
										canDownload = true;
									}
								} else {
									canDownload = true;
								}
								logger.info(today);
								logger.info("decide need download:" + canDownload);
							}

							if (canDownload) {
								logger.info("routin download begin");
								today.setDownload(GTask.DOWNLOADING);
								today.setWorking(GTask.WORKING);
								today.setChoose(GTask.NON_CHOOSE);
								today = gTaskService.update(today);								
								
								downloaded = DOWNLOAD_STATUS_DOWNLOADING;
								updateHistory();
								updateTmp();
								downloaded = DOWNLOAD_STATUS_DOWNLOADED;
								logger.info("routin download end");								
								
								logger.info("routin ananyse begin");
								choosen = CHOOSEN_STATUS_CHOOSING;
								ananyse();
								choosen = CHOOSEN_STATUS_CHOOSEN;	
								logger.info("routin ananyse end");
								
								today.setDownload(GTask.DOWNLOADED);
								today.setChoose(GTask.CHOOSEN);
								gTaskService.update(today);
							}
						}
						logger.info("check        end---------------------------------------");
						logger.info("\n\n");
						Thread.sleep(60 * 1000 * 15);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};
		d.setName("check_thread");
		d.start();
	}

	
	
	@Autowired
	private GPublicStockService gPublicStockService;

	public void removeFromPublic(String symbol) {
		List<GPublicStock> l = new ArrayList<GPublicStock>();
		for (GPublicStock s : this.publicStock) {
			if (!s.getSymbol().equals(symbol)) {
				l.add(s);
			}
		}
		this.publicStock = l;
	}

	public void reloadPublicStock() {
		publicStock = this.gPublicStockService.queryAll();
	}

	public void reloadHistoryStock() {
		history = this.gPublicStockService.queryHistory();
	}

	public void reloadStatus() {
		// List<GTask> l=this.gTaskService.queryLastTenTask();
	}

	public void updateTmp() {
		Set<String> s = Fetch_AllStock.map.keySet();
		List<String> symbolList = new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList = ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList = new ArrayList<Callable<Object>>();
		for (List<Object> sys : symbolTaskList) {
			UnformalDataTask t = new UnformalDataTask(sys);
			callList.add(t);
		}
		List<Object> r = threadService.service(callList);
		r.size();
	}

	public void updateHistory() {
		Fetch_AllStock.getData();
		Set<String> s = Fetch_AllStock.map.keySet();
		List<String> symbolList = new ArrayList<String>();
		symbolList.addAll(s);
		List<List<Object>> symbolTaskList = ThreadUtil.divide(symbolList, 16);
		List<Callable<Object>> callList = new ArrayList<Callable<Object>>();
		for (List<Object> sys : symbolTaskList) {
			HisDataTask t = new HisDataTask(sys, false);
			callList.add(t);
		}
		List<Object> r = threadService.service(callList);
		r.size();
	}

	public void ananyse() {
		try{
		logger.info("anaysys big trend");
		List<Stock> big = new BigTrend_Choose_MultiThread().choose();
		logger.info("anaysys acvu");
		List<Stock> acvu = new AVCU_Choose_MultiThread().choose();
		logger.info("anaysys av5");
		List<Stock> av5 = new AV5_Trend_Choose_MultiThread().choose();
		logger.info("anaysys av10");
		List<Stock> av10 = new AV10_Trend_Choose_MultiThread().choose();
		logger.info("anaysys tp");
		List<Stock> tp = new TP_Choose_MultiThread().choose();

		List<String> bigs = new ArrayList<String>();
		for (Stock s : big) {
			bigs.add(s.getSymbol());
		}

		List<String> acvus = new ArrayList<String>();
		for (Stock s : acvu) {
			acvus.add(s.getSymbol());
		}

		List<String> av5s = new ArrayList<String>();
		for (Stock s : av5) {
			av5s.add(s.getSymbol());
		}

		List<String> av10s = new ArrayList<String>();
		for (Stock s : av10) {
			av10s.add(s.getSymbol());
		}

		List<String> tps = new ArrayList<String>();
		for (Stock s : tp) {
			tps.add(s.getSymbol());
		}

		if (this.today != null) {
			GTask t = today;
			if (bigs.size() > 0) {
				t.setBig(StringUtils.join(bigs, ","));
			}
			if (av5.size() > 0) {
				t.setAv5(StringUtils.join(av5s, ","));
			}
			if (av10.size() > 0) {
				t.setAv10(StringUtils.join(av10s, ","));
			}
			if (tp.size() > 0) {
				t.setTp(StringUtils.join(tps, ","));
			}
			if (acvu.size() > 0) {
				t.setAcvu(StringUtils.join(acvus, ","));
			}
			t.setChoose(GTask.CHOOSEN);
			t.setUpDate(new Date());
			logger.info("update anasys result");
			gTaskService.update(t);
		}

		store.put("big", bigs);
		store.put("acvu", acvus);
		store.put("av5", av5s);
		store.put("av10", av10s);
		store.put("tp", tps);
		
		store.put("bigs", big);
		store.put("acvus", acvu);
		store.put("av5s", av5);
		store.put("av10s", av10);
		store.put("tps", tp);
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean containsKey(String key) {
		return store.containsKey(key);
	}

	public Object get(String key) {
		return store.get(key);
	}

	public void put(String key, List<String> l) {
		store.put(key, l);
	}

	public int getDownloadStatus() {
		return downloaded;
	}

	public void setDownloading() {
		downloaded = 1;
	}

	public void setDownloaded() {
		downloaded = 2;
	}

	public int getChooseStatus() {
		return choosen;
	}

	public void status(Date d) {
		String dateStr = DF.format(d);
		choose.put(dateStr, 1);
	}

	public void setChoosed() {
		choosen = 2;
	}

	public void setChoosing() {
		choosen = 1;
	}
}
