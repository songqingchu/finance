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

public class Fetch_M3_Exam {
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
		map = FetchUtil.readFileMapAbsolute(dateDir + FetchUtil.FILE_HARDEN_F);
		one = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F1);
		two = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F2);
		three = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F3);
		four = FetchUtil.readFileListAbsolute(dateDir
				+ FetchUtil.FILE_HARDEN_F4);
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
		List<Stock> r = new ArrayList<Stock>();
		for (String code : codes) {
			Stock s = fetch(code);		
			map.put(code, s);
			r.add(s);
		}

	
		java.util.Collections.sort(r, new Stock.RateDescComparator());
		List<String> content=new ArrayList<String>();
		for (Stock s : r) {
			System.out.println(s.getSymbol()  + "\t"
					+ s.getRateString());
			content.add(s.toExamString());
		}
		String file=FetchUtil.getSaveExamFile(FetchUtil.FILE_HARDEN_E);
		FetchUtil.save(file, content);
	}





	public static void main(String args[]) {
		exam();
	}
}

