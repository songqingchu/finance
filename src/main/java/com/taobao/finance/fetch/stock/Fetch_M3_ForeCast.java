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
import com.taobao.finance.util.FetchUtil;

public class Fetch_M3_ForeCast {
	public static String url = "http://hq.sinajs.cn/list=";

	public static Map<String, Stock> map = new HashMap<String, Stock>();

	public static List<String> one = new ArrayList<String>();
	public static List<String> two = new ArrayList<String>();
	public static List<String> three = new ArrayList<String>();
	public static List<String> four = new ArrayList<String>();
	
	public static List<String> oneYou = new ArrayList<String>();
	public static List<String> twoYou = new ArrayList<String>();
	public static List<String> threeYou = new ArrayList<String>();
	public static List<String> fourYou = new ArrayList<String>();

	static {
		Date d = new Date();
		String dir = "";
		for (int i = -1; i > -10; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_ANASYS_BASE + FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			if (f.exists()) {
				d = dd;
				break;
			}
		}
		String dateDir = dir + "\\";
		map = FetchUtil.readFileMapAbsolute(dateDir + FetchUtil.FILE_HARDEN_F);
		one = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F1);
		two = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F2);
		three = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F3);
		four = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F4);
		
		oneYou = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F_YOU1);
		twoYou = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F_YOU2);
		threeYou = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F_YOU3);
		fourYou = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F_YOU4);
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

	public static void exam() {
		Set<String> codes = map.keySet();
		List<Stock> missing = new ArrayList<Stock>();
		List<Stock> mistake = new ArrayList<Stock>();
		List<Stock> giveUp = new ArrayList<Stock>();
		Float totalRate=0F;
		for (String code : codes) {
			Stock s = fetch(code);
			totalRate=totalRate+s.getRate();
			
			if(one.size()>4||two.size()>4){
				if (s.getRate() > 0) {
					missing.add(s);
				}
			}else{
				if (s.getRate() > 0 && !four.contains(s.getSymbol())&& !fourYou.contains(s.getSymbol())) {
					missing.add(s);
				}
				if(s.getRate()<0){
					mistake.add(s);
				}
				if(s.getRate() > 0 && !four.contains(s.getSymbol())&& !fourYou.contains(s.getSymbol())){
					giveUp.add(s);
				}
			}
			
			map.put(code, s);
		}

		System.out.println("机选正确率："+FetchUtil.formatRate((float)missing.size()/(float)map.size()));
		System.out.println("机选收益率："+FetchUtil.formatRate(totalRate/map.size()));
		System.out.println("\n\n错失Stock：");
		java.util.Collections.sort(missing, new Stock.RateDescComparator());
		for (Stock s : missing) {
			System.out.println(s.getSymbol() + ":\t" + s.getName() + "\t"
					+ s.getRateString());
		}
		
		System.out.println("\n\n涨停后下跌Stock，请分析原因：");
		java.util.Collections.sort(mistake, new Stock.RateDescComparator());
		for (Stock s : mistake) {
			System.out.println(s.getSymbol() + ":\t" + s.getName() + "\t"
					+ s.getRateString());
		}
		
		System.out.println("\n\n放弃继续看强势涨停Stock，请分析原因：");
		java.util.Collections.sort(giveUp, new Stock.RateDescComparator());
		for (Stock s : giveUp) {
			System.out.println(s.getSymbol() + ":\t" + s.getName() + "\t"
					+ s.getRateString());
		}
		print();
	}

	public static void print() {

		
		System.out.println("\n\n无量涨停模型");
		if(one.size()>4||two.size()>4){
			System.out.println("您昨日未完成筛选工作！");
			return;
		}
		System.out.println("进取隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(one, map)));
		for (String code : one) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("稳健隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(two, map)));
		for (String code : two) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("保险隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(three, map)));
		for (String code : three) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("保守隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(four, map)));
		for (String code : four) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}
		
		
		
		
		
		
		System.out.println("\n\n有量涨停模型");
		if(one.size()>4||two.size()>4){
			System.out.println("您昨日未完成筛选工作！");
			//return;
		}
		System.out.println("进取隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(oneYou, map)));
		for (String code : oneYou) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("稳健隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(twoYou, map)));
		for (String code : twoYou) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("保险隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(threeYou, map)));
		for (String code : threeYou) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

		System.out.println("保守隔夜盈利率："
				+ FetchUtil.formatRate(countTodayRate(fourYou, map)));
		for (String code : fourYou) {
			System.out.println(code + ":\t" + map.get(code).getName() + "\t"
					+ map.get(code).getRateString());
		}

	}

	public static Float countTodayRate(List<String> code, Map<String, Stock> map) {

		Float sum = 0F;
		for (String s : code) {
			Stock stock = map.get(s);
			sum = sum + stock.getRate();
		}
		return sum / code.size();
	}

	public static void main(String args[]) {
		exam();
		
	}
}

