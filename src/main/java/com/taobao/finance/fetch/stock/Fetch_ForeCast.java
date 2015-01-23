package com.taobao.finance.fetch.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_CheckIncludeRecent;
import com.taobao.finance.fetch.impl.Fetch_SecuritiesLoan;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.util.FetchUtil;

public class Fetch_ForeCast {
	public static Date lastWorkDay=null;
	
	public static String url = "http://hq.sinajs.cn/list=";

	public static Map<String, Stock> map = new HashMap<String, Stock>();

	public static List<String> normal1 = new ArrayList<String>();
	public static List<String> normal2 = new ArrayList<String>();
	public static List<String> normal3 = new ArrayList<String>();
	public static List<String> normal4 = new ArrayList<String>();
	public static List<String> normal = new ArrayList<String>();
	
	public static List<String> holding1 = new ArrayList<String>();
	public static List<String> holding2 = new ArrayList<String>();
	public static List<String> holding3 = new ArrayList<String>();
	public static List<String> holding4 = new ArrayList<String>();
	public static List<String> holding = new ArrayList<String>();

	public static List<String> rongquan1 = new ArrayList<String>();
	public static List<String> rongquan2 = new ArrayList<String>();
	public static List<String> rongquan3 = new ArrayList<String>();
	public static List<String> rongquan4 = new ArrayList<String>();
	public static List<String> rongquan = new ArrayList<String>();

	public static List<String> harden1 = new ArrayList<String>();
	public static List<String> harden2 = new ArrayList<String>();
	public static List<String> harden3 = new ArrayList<String>();
	public static List<String> harden4 = new ArrayList<String>();
	public static List<String> harden = new ArrayList<String>();
	
	public static List<String> hardenyou1 = new ArrayList<String>();
	public static List<String> hardenyou2 = new ArrayList<String>();
	public static List<String> hardenyou3 = new ArrayList<String>();
	public static List<String> hardenyou4 = new ArrayList<String>();
	public static List<String> hardenyou = new ArrayList<String>();
	
	public static List<String> chongzhu = new ArrayList<String>();

	static {
		loadData();
	}

	public static void loadData() {
		normal1.clear();
		normal2.clear();
		normal3.clear();
		normal4.clear();
		normal.clear();
		
		holding1.clear();
		holding2.clear();
		holding3.clear();
		holding4.clear();
		holding.clear();
		
		rongquan1.clear();
		rongquan2.clear();
		rongquan3.clear();
		rongquan4.clear();
		rongquan.clear();
		
		harden1.clear();
		harden2.clear();
		harden3.clear();
		harden4.clear();
		harden.clear();
		
		
		hardenyou1.clear();
		hardenyou2.clear();
		hardenyou3.clear();
		hardenyou4.clear();
		hardenyou.clear();
		
		chongzhu.clear();
		
		Calendar c = Calendar.getInstance();
		Date dd = c.getTime();
		String dir = FetchUtil.FILE_STOCK_ANASYS_BASE
				+ FetchUtil.FILE_FORMAT.format(dd);
		File f = new File(dir);

		for (int i = -1; i > -10; i--) {
			c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_ANASYS_BASE
					+ FetchUtil.FILE_FORMAT.format(dd);
			f = new File(dir);
			if (f.exists()) {
				lastWorkDay=dd;
				normal1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT1);
				normal2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT2);
				normal3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT3);
				normal4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT4);
				

				holding1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT1);
				holding2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT2);
				holding3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT3);
				holding4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT4);

				rongquan1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT1);
				rongquan2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT2);
				rongquan3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT3);
				rongquan4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT4);

				harden1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN1);
				harden2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN2);
				harden3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN3);
				harden4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN4);
				harden = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN);

				hardenyou1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU1);
				hardenyou2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU2);
				hardenyou3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU3);
				hardenyou4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU4);
				hardenyou = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU);
				
				chongzhu = FetchUtil.readAllLineFileListAbsolute(dir + "\\" + "chongzhu.txt");
				break;
			}
		}
	}
	
	public static void loadData(String date) {
		normal1.clear();
		normal2.clear();
		normal3.clear();
		normal4.clear();
		normal.clear();
		
		holding1.clear();
		holding2.clear();
		holding3.clear();
		holding4.clear();
		holding.clear();
		
		rongquan1.clear();
		rongquan2.clear();
		rongquan3.clear();
		rongquan4.clear();
		rongquan.clear();
		
		harden1.clear();
		harden2.clear();
		harden3.clear();
		harden4.clear();
		harden.clear();
		
		
		hardenyou1.clear();
		hardenyou2.clear();
		hardenyou3.clear();
		hardenyou4.clear();
		hardenyou.clear();
		
		chongzhu.clear();
		

		String dir = FetchUtil.FILE_STOCK_ANASYS_BASE
				+ date;
		File f = new File(dir);
				normal1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT1);
				normal2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT2);
				normal3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT3);
				normal4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_NORMAL_CONVERT4);
				

				holding1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT1);
				holding2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT2);
				holding3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT3);
				holding4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HOLDING_CONVERT4);

				rongquan1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT1);
				rongquan2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT2);
				rongquan3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT3);
				rongquan4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_LOAN_CONVERT4);

				harden1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN1);
				harden2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN2);
				harden3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN3);
				harden4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN4);
				harden = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN);

				hardenyou1 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU1);
				hardenyou2 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU2);
				hardenyou3 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU3);
				hardenyou4 = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU4);
				hardenyou = FetchUtil.readAllLineFileListAbsolute(dir + "\\"
						+ FetchUtil.FILE_HARDEN_YOU);
				
				chongzhu = FetchUtil.readAllLineFileListAbsolute(dir + "\\" + "chongzhu.txt");
	}
	
	public static Map<String,Stock> getData(boolean refresh){
		if(refresh){
			loadData();
		}else{
			Calendar c = Calendar.getInstance();
			Date dd = c.getTime();
			String dir = FetchUtil.FILE_STOCK_ANASYS_BASE
					+ FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			for (int i = -1; i > -10; i--) {
				c = Calendar.getInstance();
				c.add(Calendar.DATE, i);
				dd = c.getTime();
				dir = FetchUtil.FILE_STOCK_ANASYS_BASE
						+ FetchUtil.FILE_FORMAT.format(dd) + "\\"
						+ FetchUtil.FILE_DUO_X;
				f = new File(dir);
				if (f.exists()) {
					break;
				}
			}
			//if(!(dd.getYear()==lastWorkDay.getYear()&&dd.getMonth()==lastWorkDay.getMonth()&&dd.getDate()==lastWorkDay.getDate())){
				loadData();
			//}
		}
		
		
		List<String> all=new ArrayList<String>();
		all.addAll(normal4);
		all.addAll(holding4);
		all.addAll(rongquan4);
		all.addAll(harden);
		all.addAll(hardenyou);
		
		all.addAll(chongzhu);
		
		Map<String,Stock> m=new HashMap<String,Stock>();
		
		for(String s:all){
			if(s.contains("600606")){
				s.hashCode();
			}
			Stock st=fetch(s);
            if(Fetch_SecuritiesLoan.all.contains(s)){
			   st.setRongquan(true);	
			}
          //  Fetch_M3_Harden.setHardenTimes(st, true);
            if(s.contains("sh600225")){
				s.length();
			}
            setHardenTimes(st);
			m.put(s, st);
		}
		return m;
	}
	
	public static void setHardenTimes(Stock s){
		if(s==null){
			return;
		}
		Stock st=Fetch_BaseData.map.get(s.getSymbol());
		if(st!=null){
			s.setHardenTimes(st.getHardenTimes());
		}
	}
	
	public static Map<String,Stock> loadBaseData(String date){
		String file=FetchUtil.FILE_STOCK_ANASYS_BASE+date+"\\"+FetchUtil.FILE_DUO_X;
		Map<String,Stock> m=FetchUtil.readFileListAbsoluteX(file);
		return m;
	}
	
	public static void setHardenTimes(String date,Stock s){
		String file=FetchUtil.FILE_STOCK_ANASYS_BASE+date+"\\"+FetchUtil.FILE_DUO_X;
		Map<String,Stock> m=FetchUtil.readFileListAbsoluteX(file);
		if(s==null){
			return;
		}
		Stock st=m.get(s.getSymbol());
		if(st!=null){
			s.setHardenTimes(st.getHardenTimes());
		}
	}
	
	public static Map<String,Stock> getData(boolean refresh,String date){
		loadData(date);
	
		List<String> all=new ArrayList<String>();
		//all.addAll(normal4);
		//all.addAll(holding4);
		//all.addAll(rongquan4);
		all.addAll(harden);
		all.addAll(hardenyou);
		
		Map<String,Stock> m=new HashMap<String,Stock>();
		Map<String,Stock> base=loadBaseData(date);
		
		for(String s:all){
			
			Stock st=fetch(s);
            if(Fetch_SecuritiesLoan.all.contains(s)){
			   st.setRongquan(true);	
			}
           // setHardenTimes(date,st);
            Stock sst=base.get(s);
            if(sst!=null){
            	st.setHardenTimes(sst.getHardenTimes());
            }
			m.put(s, st);
		}
		return m;
	}
	
	
	
	
	public static String getUrl(String stock) {
		return url + stock;
	}
	
	/**
	 * 抓取数据
	 * 
	 * @param url
	 * @return
	 */
	public static Stock fetch(String code) {
		HttpClient client = new HttpClient();
		Stock s = null;
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				s = FetchUtil.parseTodayStockFromSina(jsonStr, code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}


	public static Float countTodayRate(List<String> code, Map<String, Stock> map) {

		if(code.size()==0){
			return 0F;
		}
		Float sum = 0F;
		for (String s : code) {
			Stock stock = map.get(s);
			/*if(stock.getRate()<-0.1F){
				continue;
			}*/
			sum = sum + stock.getRealRate();
		}
		String s=FetchUtil.format0Right((Float)(sum / code.size()));
		return Float.parseFloat(s);
	}
	
	public static Float countTotalRate(List<String> code, Map<String, Stock> map) {

		if(code.size()==0){
			return 0F;
		}
		Float sum = 0F;
		for (String s : code) {
			Stock stock = map.get(s);
			sum = sum + stock.getTotalRate();
		}
		String s=FetchUtil.format0Right4(sum / code.size());
		return Float.parseFloat(s);
	}
	

	public static void main(String args[]) {
		 Map<String,Stock> m=Fetch_ForeCast.getData(true);
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.normal1, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.normal2, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.normal3, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.normal4, m));
		 
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.holding1, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.holding2, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.holding3, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.holding4, m));
		 
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.rongquan1, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.rongquan2, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.rongquan3, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.rongquan4, m));
		 
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.harden1, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.harden2, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.harden3, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.harden4, m));
		 
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.hardenyou1, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.hardenyou2, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.hardenyou3, m));
		 System.out.println(Fetch_ForeCast.countTodayRate(Fetch_ForeCast.hardenyou4, m));
		 
		 
	}
}
