package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.finance.entity.GStock;
import com.taobao.finance.util.FetchUtil;

public class Fetch_Holders {

	public static String url = "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=GG&sty=GDRS&st=3&sr=1&p=page&ps=50&mkt=1&fd=date";

	public static String getUrl(String date, int page) {
		String newUrl = url.replace("page", page + "");
		newUrl = newUrl.replace("fd=date", "fd=" + date);
		return newUrl;
	}

	public static Map<String, Object> fetch(String newUrl) {
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
				String jsonStr = getMethod.getResponseBodyAsString();
				jsonStr = jsonStr.replace("(", "");
				jsonStr = jsonStr.replace(")", "");
				Map<String, Object> m = FetchUtil.parseHolders(jsonStr);
				return m;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDate() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		if (month >= 1 && month <= 3) {
			year = year - 1;
			month = 12;
			day = 31;
		}
		if (month >= 4 && month <= 6) {
			month = 3;
			day = 31;
		}
		if (month >= 7 && month <= 9) {
			month = 6;
			day = 30;
		}
		if (month >= 10 && month <= 12) {
			month = 9;
			day = 30;
		}
		String s = year + "-" + month + "-" + day;
		return s;
	}

	@SuppressWarnings("unchecked")
	public static List<GStock> getAll(String date) {
		List<GStock> l = new ArrayList<GStock>();
		if(date==null){
			date = getDate();
		}
		String url = "";
		Map<String, Object> m = null;
		List<GStock> li = null;

		for (int i = 1; i <= 100; i++) {
			url = getUrl(date, i);
			m = fetch(url);
			li = (List<GStock>) m.get("data");
			if (li.size() != 0) {
				l.addAll(li);
			}
			if (l.size() < 50) {
				break;
			}
		}
		return l;
	}
	
	public static Map<String,GStock> getAllLong() {
		Map<String,GStock> m=new HashMap<String,GStock>();
		List<String> l=new ArrayList<String>();
		/*l.add("2013-3-31");
		l.add("2013-6-30");
		l.add("2013-9-30");
		l.add("2013-12-31");
		l.add("2014-3-31");
		l.add("2014-6-30");
		l.add("2014-9-30");
		l.add("2014-12-31");
		l.add("2015-3-31");*/
		
		l.add("2015-6-30");
		
		for(int i=0;i<l.size();i++){
			String s=l.get(i);
			List<GStock> li=getAll(s);
			s=StringUtils.replace(s, "-3-", "-03-");
			s=StringUtils.replace(s, "-6-", "-06-");
			s=StringUtils.replace(s, "-9-", "-09-");
			for(GStock st:li){
				if(!m.containsKey(st.getSymbol())){
					String r=s+":"+st.getHolder();
					st.setRecord(r+";");
					m.put(st.getSymbol(), st);
				}else{
					GStock stOld=m.get(st.getSymbol());
					stOld.setHolder(st.getHolder());
					stOld.setChange(st.getChange());
					String r=s+":"+st.getHolder();
					if(StringUtils.isBlank(stOld.getRecord())){
						stOld.setRecord("");
					}
					stOld.setRecord(stOld.getRecord()+r+";");
				}
			}
		}
		return m;
	}
	

	public static void main(String args[]) {

		Map<String,GStock> m = getAllLong();
		m.size();
		

	}

}


class Holder_Task implements Callable<List<GStock>> {
	public static final Logger logger = Logger.getLogger("taskLogger");
	private String date;
	private int page;

	public Holder_Task(String date,int page) {
		this.date=date;
		this.page=page;
	}


	public List<GStock> call() throws Exception {
		List<GStock> l = new ArrayList<GStock>();
		

		return l;
	}
}
