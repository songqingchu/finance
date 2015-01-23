package com.taobao.finance.fetch.impl;

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

public class Fetch_M2_Exam {

	public static String url = "http://hq.sinajs.cn/list=";

	public static Map<String, Stock> map = new HashMap<String, Stock>();

	public static List<String> one = new ArrayList<String>();
	public static List<String> two = new ArrayList<String>();
	public static List<String> three = new ArrayList<String>();
	public static List<String> four = new ArrayList<String>();

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
		map.putAll(FetchUtil.readFileMapAbsolute(dateDir + FetchUtil.FILE_HOLDING_F_ZB));
		map.putAll(FetchUtil.readFileMapAbsolute(dateDir + FetchUtil.FILE_HOLDING_F_ZXB));
		
		one.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZB1));
		one.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZXB1));

		two.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZB2));
		two.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZXB2));

		three.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZB3));
		three.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZXB3));

		four.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZB4));
		four.addAll(FetchUtil.readFileListAbsolute(dateDir+ FetchUtil.FILE_HOLDING_F_ZXB4));

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
		Float totalRate=0F;
		for (String code : codes) {
			Stock s = fetch(code);
			totalRate=totalRate+s.getRate();
			
			if(one.size()>4||two.size()>4){
				if (s.getRate() > 0) {
					missing.add(s);
				}
			}else{
				if (s.getRate() > 0 && !four.contains(s.getSymbol())) {
					missing.add(s);
				}
			}
			
			map.put(code, s);
		}

		System.out.println("机选正确率："+FetchUtil.formatRate((float)missing.size()/(float)map.size()));
		System.out.println("机选收益率："+FetchUtil.formatRate(totalRate/map.size()));
		System.out.println("\n\n错失Stock：");
		java.util.Collections.sort(missing, new Stock.RateDescComparator());
		List<String> content=new ArrayList<String>();
		for (Stock s : missing) {
			System.out.println(s.getSymbol() + ":\t" + s.getName() + "\t"
					+ s.getRateString());
			content.add(s.toExamString());
		}
		String file=FetchUtil.getSaveExamFile(FetchUtil.FILE_HOLDING_E);
		FetchUtil.save(file, content);
	}





	public static void main(String args[]) {
		exam();
	}
}

