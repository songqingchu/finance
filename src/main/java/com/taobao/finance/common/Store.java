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
import com.taobao.finance.choose.local.thread.CB2_Choose_MultiThread;
import com.taobao.finance.choose.local.thread.CB_Choose_MultiThread;
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
	public Map<String,GPublicStock> publicStockMap = new HashMap<String,GPublicStock>();
	public List<GPublicStock> history = new ArrayList<GPublicStock>();
	public Map<String, Stock> recent = new HashMap<String, Stock>();
	public Map<String, List<Stock>> hot = new HashMap<String, List<Stock>>();
	public Map<String, Object> kdata = new HashMap<String, Object>();
	public List<Map<String, Object>> kdata2 = new ArrayList<Map<String, Object>>();

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

	
	public void reloadPublicPool(){
		logger.info("reload public pool");
		publicStock = this.gPublicStockService.queryAll();
		publicStockMap.clear();
		for(GPublicStock p:publicStock){
			publicStockMap.put(p.getSymbol(), p);
		}
		logger.info("reload public pool end\n");
	}
	public void reloadHot(Set<String> sSet) {
		logger.info("reload k-data");
		hot.clear();
		for (String s : sSet) {
			List<Stock> l = Hisdata_Base.readHisDataMerge(s, null);
			List<Stock> lNew = new ArrayList<Stock>();
			if (l.size() > 200) {
				for (int i = l.size() - 200; i < l.size(); i++) {
					lNew.add(l.get(i));
				}
			} else {
				lNew = l;
			}
			hot.put(s, lNew);
		}
		logger.info("reload k-data end\n");
	}

	public void reloadRecent() {
		logger.info("reload tmp-data");
		for (String s : Fetch_AllStock.map.keySet()) {
			Stock st = Hisdata_Base.readTmpData(s);
			if (st != null) {
				st.setName(Fetch_AllStock.map.get(s).getName());
				this.recent.put(s, st);
			}
		}
		logger.info("reload tmp-data end\n");
	}

	public void reloadKdata(Set<String> sSet) {
		logger.info("reload symbols");
		this.kdata.clear();
		this.kdata2.clear();
		for (String s : sSet) {
			Boolean down = false;
			if (Store.downloaded == 2) {
				down = true;
			}
			try {
				Map<String, Object> m = dataService.getKData2(s,this.workingDay, down, this);
				this.kdata.put(s, m);
				this.kdata2.add(m);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		logger.info("reload symbols end\n");
	}

	@PostConstruct
	public void init() {

		if (workingDay == null) {
			workingDay = FetchUtil.checkWorkingDayUsusal();
		}
		logger.info("\n\n\n");
		logger.info("*******************************************************");
		logger.info("system start:    normal check workingday:" + workingDay);
		today = gTaskService.queryLastTask();
		GTask lastDay=gTaskService.queryLast2Task();

		Set<String> sSet = new HashSet<String>();
		
		reloadPublicPool();
		
		String[] ids =null;
		if (StringUtils.isNotBlank(today.getAcvu())) {
			ids = StringUtils.split(today.getAcvu(), ",");
		}else{
			ids = StringUtils.split(lastDay.getAcvu(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("acvu", l);
			sSet.addAll(l);
		}else{
			store.put("acvu", new ArrayList<String>());
		}
		
		
		
		ids =null;
		if (StringUtils.isNotBlank(today.getBig())) {
			ids = StringUtils.split(today.getBig(), ",");
		}else{
			ids = StringUtils.split(lastDay.getBig(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("big", l);
			sSet.addAll(l);
		}else{
			store.put("big", new ArrayList<String>());
		}
		
		
		
		
		ids =null;
		if (StringUtils.isNotBlank(today.getAv5())) {
			ids = StringUtils.split(today.getAv5(), ",");
		}else{
			ids = StringUtils.split(lastDay.getAv5(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("av5", l);
			sSet.addAll(l);
		}else{
			store.put("av5", new ArrayList<String>());
		}
		
		ids =null;
		if (StringUtils.isNotBlank(today.getAv10())) {
			ids = StringUtils.split(today.getAv10(), ",");
		}else{
			ids = StringUtils.split(lastDay.getAv10(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("av10", l);
			sSet.addAll(l);
		}else{
			store.put("av10", new ArrayList<String>());
		}
		
		
		ids =null;
		if (StringUtils.isNotBlank(today.getCb())) {
			ids = StringUtils.split(today.getCb(), ",");
		}else{
			ids = StringUtils.split(lastDay.getCb(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("cb", l);
			sSet.addAll(l);
		}else{
			store.put("cb", new ArrayList<String>());
		}
		
		
		ids =null;
		if (StringUtils.isNotBlank(today.getRatio())) {
			ids = StringUtils.split(today.getRatio(), ",");
		}else{
			ids = StringUtils.split(lastDay.getRatio(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("ratio", l);
			sSet.addAll(l);
		}else{
			store.put("ratio", new ArrayList<String>());
		}
		
		
		ids =null;
		if (StringUtils.isNotBlank(today.getTp())) {
			ids = StringUtils.split(today.getTp(), ",");
		}else{
			ids = StringUtils.split(lastDay.getTp(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("tp", l);
			sSet.addAll(l);
		}else{
			store.put("tp", new ArrayList<String>());
		}
		
		ids =null;
		if (StringUtils.isNotBlank(today.getCb2())) {
			ids = StringUtils.split(today.getCb2(), ",");
		}else{
			ids = StringUtils.split(lastDay.getCb2(), ",");
		}
		if(ids!=null){
			List<String> l = new ArrayList<String>();
			for(String id:ids){
				if(!publicStockMap.containsKey(id)){
					l.add(id);
				}
			}
			store.put("cb2", l);
			sSet.addAll(l);
		}else{
			store.put("cb2", new ArrayList<String>());
		}
		
		
		

		logger.info("system start:    load anasys result");

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
		
		logger.info("system start:    load hot kdata");
        reloadHot(sSet);
        logger.info("system start:    load hot kdata end\n");
        
        logger.info("system start:    load tmp data");
		reloadRecent();
		logger.info("system start:    load tmp data end\n");
		
		logger.info("system start:    ananyse model mark");
		reloadKdata(sSet);
		logger.info("system start:    ananyse model mark end\n");
		
		logger.info("system start:    system start end" );
		logger.info("*******************************************************");

		
		history = this.gPublicStockService.queryHistory();
		Thread d = new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {

				while (true) {
					try {
						logger.info("\n\n");
						logger.info("----------------------------------------------------------");
						logger.info("heart beat check:         start");
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
							logger.info("heart beat check:         workingday," + workingDay);
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
									logger.info("heart beat check:         now task," + today);
									t = gTaskService.insert(t);
									logger.info("heart beat check:         insert task");
								}
							}

							if (workingDay) {
								downloaded = 0;
								choosen = 0;
							}
						}

						if (d.after(closeTime)) {
							boolean canDownload = false;
							if (workingDay) {
								if (today != null) {
									if (today.getDate().getDate() == d
											.getDate()) {
										if (today.getDownload() == 1
												|| today.getDownload() == 2) {
											canDownload = false;
										} else {
											canDownload = true;
										}
									} else {
										canDownload = true;
									}
								} else {
									canDownload = true;
								}
								logger.info("heart beat check:         "+today);
								logger.info("heart beat check:         decide need download:"
										+ canDownload);
							}

							if (canDownload) {
								
								today.setDownload(GTask.DOWNLOADING);
								today.setWorking(GTask.WORKING);
								today.setChoose(GTask.NON_CHOOSE);
								today = gTaskService.update(today);
								
								logger.info("update all stock name begin");
								Fetch_AllStock.getData();
								logger.info("update all stock name end");
								
								
								downloaded = DOWNLOAD_STATUS_DOWNLOADING;
								download();
								downloaded = DOWNLOAD_STATUS_DOWNLOADED;
								

								
								choosen = CHOOSEN_STATUS_CHOOSING;
								ananyse();
								choosen = CHOOSEN_STATUS_CHOOSEN;
								

								today.setDownload(GTask.DOWNLOADED);
								today.setChoose(GTask.CHOOSEN);
								gTaskService.update(today);
								
								
							}
						}
						logger.info("heart beat check:         end");
						logger.info("----------------------------------------------------------");
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
		if(publicStock!=null){
			publicStockMap.clear();
			for(GPublicStock g:publicStock){
				publicStockMap.put(g.getSymbol(), g);
			}
		}
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
	
	public void download(){
		logger.info("routin download begin");
		updateHistory();
		updateTmp();
		logger.info("routin download end\n");
	}

	public void ananyse() {
		try {
			logger.info("routin ananyse begin");
			
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
			logger.info("anaysys cb");
			List<Stock> cb = new CB_Choose_MultiThread().choose();
			logger.info("anaysys cb2");
			List<Stock> cb2 = new CB2_Choose_MultiThread().choose();
			
			List<Stock> ratio =new ArrayList<Stock>(); 
					
					
			logger.info("routin ananyse end\n");

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
			
			List<String> cbs = new ArrayList<String>();
			for (Stock s : cb) {
				cbs.add(s.getSymbol());
			}
			
			List<String> cb2s = new ArrayList<String>();
			for (Stock s : cb2) {
				cb2s.add(s.getSymbol());
			}
			
			List<String> ratios = new ArrayList<String>();
			/*for (Stock s : cb) {
				cbs.add(s.getSymbol());
			}*/

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
				if (cbs.size() > 0) {
					t.setCb(StringUtils.join(cbs, ","));
				}
				if (cb2s.size() > 0) {
					t.setCb2(StringUtils.join(cb2s, ","));
				}
				if (ratios.size() > 0) {
					t.setCb(StringUtils.join(ratios, ","));
				}
				
				t.setChoose(GTask.CHOOSEN);
				t.setDownload(GTask.DOWNLOADED);
				t.setUpDate(new Date());
				logger.info("update anasys result\n");
				gTaskService.update(t);
			}

			store.put("big", bigs);
			store.put("acvu", acvus);
			store.put("av5", av5s);
			store.put("av10", av10s);
			store.put("tp", tps);
			store.put("cb", cbs);
			store.put("cb2", cb2s);
			store.put("ratio", ratios);

			store.put("bigs", big);
			store.put("acvus", acvu);
			store.put("av5s", av5);
			store.put("av10s", av10);
			store.put("tps", tp);
			store.put("cbs", cb);
			store.put("cb2s", cb2);
			store.put("ratios", ratio);
			
			Set<String> sSet = new HashSet<String>();
			if (StringUtils.isNoneBlank(today.getAcvu())) {
				String[] ids = StringUtils.split(today.getAcvu(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getBig())) {
				String[] ids = StringUtils.split(today.getBig(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getAv5())) {
				String[] ids = StringUtils.split(today.getAv5(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getAv10())) {
				String[] ids = StringUtils.split(today.getAv10(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getCb())) {
				String[] ids = StringUtils.split(today.getCb(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getCb2())) {
				String[] ids = StringUtils.split(today.getCb2(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getTp())) {
				String[] ids = StringUtils.split(today.getTp(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}
			if (StringUtils.isNoneBlank(today.getRatio())) {
				String[] ids = StringUtils.split(today.getRatio(), ",");
				List<String> l = new ArrayList<String>();
				l.addAll(Arrays.asList(ids));
				sSet.addAll(l);
			}

			logger.info("routin reload begin");
			reloadHot(sSet);
			reloadKdata(sSet);
			reloadRecent();
			logger.info("routin reload end\n");
			
		} catch (Exception e) {
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
