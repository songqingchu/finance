package com.taobao.finance.fetch.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.comparator.Comparator;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_StockHistoryLong;

public class Fetch_HistoryAveNum {
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String url = "http://market.finance.sina.com.cn/downxls.php?";

	public static String getUrl(String symbol, Date d) {
		String newUrl = "";
		newUrl = url + "date=" + format.format(d) + "&symbol=" + symbol;
		return newUrl;
	}

	public static InputStream fetch(String newUrl) {
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				return getMethod.getResponseBodyAsStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Float> getAveNum(InputStream s) {
		try {
			List<Float> l = new ArrayList<Float>();
			InputStreamReader ir = new InputStreamReader(s);
			BufferedReader br = new BufferedReader(ir);
			String line = br.readLine();
			Float sum1 = 0f;
			Float sum2 = 0f;
			Float sumBuy = 0f;
			Float sumSell = 0f;
			Float batch = 0f;
			Float batchBuy=0f;
			Float batchSell=0f;
			Float lastNum = 0f;
			Float lastPrice=0f;
			while (line != null && line != "") {
				line = br.readLine();
				if (line == null) {
					continue;
				}
				String[] data = line.split("\t");
				lastPrice=Float.parseFloat(data[1]);
				sum1 = sum1 + Integer.parseInt(data[3]);
				lastNum = Float.parseFloat(data[3]);
				batch++;
				if(line.contains("ÂòÅÌ")){
					batchBuy++;
					sumBuy=sumBuy+Float.parseFloat(data[3]);
				}else if(line.contains("ÂôÅÌ")){
					batchSell++;
					sumSell=sumSell+Float.parseFloat(data[3]);
				}

			}
			br.close();
			sum2 = sum1 - lastNum;
			l.add(sum1 / (batch - 1));
			l.add(sum2 / (batch - 2));
			l.add(sumBuy / (batchBuy - 1));
			l.add(sumSell / (batchSell - 2));
			l.add(sum1/200);
			l.add(lastPrice*7);
			l.add((sumBuy/sumSell)*100);
			l.add((sumBuy-sumSell)/200+300);
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static List<Float> getAveNum2(InputStream s) {
		try {
			List<Float> l = new ArrayList<Float>();
			InputStreamReader ir = new InputStreamReader(s);
			BufferedReader br = new BufferedReader(ir);
			String line = br.readLine();
			Float sum1 = 0f;
			Float sum2 = 0f;
			Float sumBuy = 0f;
			Float sumSell = 0f;
			Float batch = 0f;
			Float batchBuy=0f;
			Float batchSell=0f;
			Float lastNum = 0f;
			Float lastPrice=0f;
			while (line != null && line != "") {
				line = br.readLine();
				if (line == null) {
					continue;
				}
				String[] data = line.split("\t");
				lastPrice=Float.parseFloat(data[1]);
				sum1 = sum1 + Integer.parseInt(data[3]);
				lastNum = Float.parseFloat(data[3]);
				batch++;
				if(line.contains("ÂòÅÌ")){
					batchBuy++;
					sumBuy=sumBuy+Float.parseFloat(data[3]);
				}else if(line.contains("ÂôÅÌ")){
					batchSell++;
					sumSell=sumSell+Float.parseFloat(data[3]);
				}

			}
			br.close();
			sum2 = sum1 - lastNum;
			l.add(sum1 / (batch - 1));
			l.add(sum2 / (batch - 2));
			l.add(sumBuy / (batchBuy - 1));
			l.add(sumSell / (batchSell - 2));
			l.add(sum1/200);
			l.add(lastPrice*7);
			l.add((sumBuy/sumSell)*100);
			l.add((sumBuy-sumSell)/200+300);
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Float> getData(String symbol, Date d) {
		String url = getUrl(symbol, d);
		InputStream s = fetch(url);
		return getAveNum(s);
	}
	
	public static List<Float> getData2(String symbol, Date d) {
		String url = getUrl(symbol, d);
		InputStream s = fetch(url);
		return getAveNum(s);
	}

	public static List<Stock> getTop() {
		List<Stock> s = Fetch_RateSpeed.fetch();
		List<Stock> r = new ArrayList<Stock>();
		for (Stock st : s) {
			if (st.getRate() > 0.095) {
				r.add(st);
			}
		}
		Collections.sort(r, new Comparator.RateDescComparator());
		return r;
	}

	public static void test() throws ParseException {
		List<Stock> l = getTop();
		for (Stock s : l) {

		}
	}

	public static Map<Date,List<Float>> get(String symbol){
		
		List<Stock> l = Fetch_StockHistoryLong.getData(symbol, 5);
		Collections.sort(l, new S());
		Map<Date,List<Float>> map = new HashMap<Date,List<Float>>();
		int ii=0;
		for (Stock s : l) {
			/*if(ii++>5){
				break;
			}*/
			List<Float> lll=getData(symbol, s.getDate());
			if(lll==null){
				continue;
			}
			if(lll.size()>3){
				map.put(s.getDate(),getData(symbol, s.getDate()));
			}
		}
		return map;
	}
	
	
	public void getAveNum(){
		String symbol = "sz300267";
		List<Stock> l = Fetch_StockHistoryLong.getData(symbol, 5);
		Collections.sort(l, new S());
		List<List<Float>> ll = new ArrayList<List<Float>>();
		int ii=0;
		for (Stock s : l) {
			ll.add(getData(symbol, s.getDate()));
		}
		int i = 0;
		for (List<Float> s : ll) {
			System.out.print(l.get(i).getDateString() + " ");
			try {
				System.out.print(s.get(0)+"\t");
				System.out.print(s.get(2)+"\t");
				System.out.print(s.get(3)+"\t");
				System.out.println(l.get(i).getTradeNum());
			} catch (Exception e) {
			}
			i++;
		}
	}
	
	public void getAveNum2(){
		String symbol = "sz300267";
		List<Stock> l = Fetch_StockHistoryLong.getData(symbol, 5);
		Collections.sort(l, new S());
		List<List<Float>> ll = new ArrayList<List<Float>>();
		int ii=0;
		for (Stock s : l) {
			ll.add(getData(symbol, s.getDate()));
		}
		int i = 0;
		for (List<Float> s : ll) {
			System.out.print(l.get(i).getDateString() + " ");
			try {
				System.out.print(s.get(0)+"\t");
				System.out.print(s.get(2)+"\t");
				System.out.print(s.get(3)+"\t");
				System.out.println(l.get(i).getTradeNum());
			} catch (Exception e) {
			}
			i++;
		}
	}
	
	
	public static void main(String args[]) throws IOException, ParseException {
		
	}
}

class S implements java.util.Comparator<Stock> {
	public int compare(Stock s1, Stock s2) {
		if (s1.getDate().before(s2.getDate())) {
			return -1;
		}
		if (s1.getDate().after(s2.getDate())) {
			return 1;
		}
		return 0;
	}
}