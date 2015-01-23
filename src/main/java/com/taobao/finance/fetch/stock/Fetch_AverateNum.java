package com.taobao.finance.fetch.stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.comparator.Comparator;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_StockHistory;
import com.taobao.finance.util.FetchUtil;

public class Fetch_AverateNum {

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String urlToday = "http://money.finance.sina.com.cn/quotes_service/view/vMS_tradedetail.php?";

	public static String getUrl(String symbol, Date d, int page) {
		String newUrl = "";
		newUrl = urlToday + "date=" + format.format(d) + "&symbol="
					+ symbol + "&page=" + page;
		return newUrl;
	}

	public static List<Integer> fetch(String s) {
		List<Integer> list = null;
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(s);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				jsonStr.hashCode();
				list = FetchUtil.parseNumber(jsonStr, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Integer> getOneDay(Date d, String symbol) {
		List<Integer> l = new ArrayList<Integer>();
		String u = "";
		for (int i = 1; i <= 23; i++) {
			u = getUrl(symbol, d, i);
			List<Integer> r = fetch(u);
			l.addAll(r);
		}
		return l;
	}

	public static Integer getAvgNum(String code, Date d) throws ParseException {
		List<Integer> l = getOneDay(d, code);
		int sum = 0;
		for (Integer i : l) {
			sum = sum + i;
		}
		return sum / l.size();
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
		List<Stock> l = Fetch_StockHistory.fetch("sz300304");
		List<Date> dList = new ArrayList<Date>();
		dList.add(new Date());

		for (int i = 0; i < 5; i++) {
			dList.add(l.get(i).getDate());
		}

		List<Stock> sList = getTop();
		List<String> sss = new ArrayList<String>();
		int j = 0;
		for (Stock s : sList) {
			j++;
			if (j > 5) {
				break;
			}
			List<Integer> num = new ArrayList<Integer>();

			for (Date d : dList) {
				num.add(getAvgNum(s.getSymbol(), d));
			}
			sss.add(s.getSymbol() + "\t" + s.getName() + "\t" + num);
		}
		for (String s : sss) {
			System.out.println(s);
		}

	}

	public static void main(String args[]) throws ParseException {
		List<Date> dList = new ArrayList<Date>();
		dList.add(new Date());
		List<Integer> num = new ArrayList<Integer>();

		for (Date d : dList) {
			num.add(getAvgNum("sz300304", d));
		}
		System.out.println(num);

	}
}
