package com.taobao.finance.fetch.stock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_GetLastWorkDay;
import com.taobao.finance.util.FetchUtil;

public class Fetch_StockInfo {

	public static DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	public static String url = "http://vip.stock.finance.sina.com.cn/corp/view/vCB_AllBulletin.php?Page=1&stockid=";

	public static Map<String, Stock> map = new HashMap<String, Stock>();
	public static List<String> l = new ArrayList<String>();
	static {
		l.add("重组");
		l.add("资产");
		l.add("并购");
		l.add("组建");
		l.add("重大");
		l.add("复牌");
		l.add("项目");
		l.add("增发");
		l.add("新增股份");
	}

	public static String getUrl(String code) {
		return url + code;
	}

	public static List<String> fetch(String code) {
		List<String> list = null;
		HttpClient client = new HttpClient();
		String newUrl = getUrl(code);
		HttpMethod getMethod = new GetMethod(newUrl);
		client.getParams().setContentCharset("GBK");
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				list = FetchUtil.parseStockInfoFromSina(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String filter(String code,Date d) throws ParseException {
		List<String> s = fetch(code);
		String infoStr = "";
  
		for(int i=0;i<3;i++){
			boolean contain=false;
			try{
				String dateStr=s.get(i).split("&nbsp;")[0].trim();
				Date infoDate=format.parse(dateStr);
				if(!infoDate.before(d)){
					for (String key : l) {
						if(s.get(i).contains("延期复牌")){
							contain=false;
							break;
						}
						if(s.get(i).contains("停牌")){
							contain=false;
							break;
						}
						if(s.get(i).contains("重组")&&s.get(i).contains("进展")){
							contain=false;
							break;
						}
						if (s.get(i).contains(key)) {
							s.set(i, s.get(i).replace(key, "<b>"+key+"</b>"));
		                   contain=true;
						}
					}
					if(contain){
						infoStr=infoStr+s.get(i).trim()+"<br>";
					}
				}
			}catch (Exception e){
				continue ;
			}
			
		}
		infoStr=infoStr.replace("\n", "");
		return infoStr;
	}


	public static Map<String,Stock> getGreateIssue(){
		Date d=Fetch_GetLastWorkDay.getLastWorkDay();
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		//c.add(Calendar.DATE, -1);
		
		Map<String,Stock> m=new HashMap<String,Stock>();
		int index=0;
		int size=Fetch_AllStock.map.size();
		for(String symbol:Fetch_AllStock.map.keySet()){
			index++;
			Stock s=new Stock();
			s.setSymbol(symbol);
			s.setName(Fetch_AllStock.map.get(symbol).getName());
			symbol=symbol.replace("sh", "");
			symbol=symbol.replace("sz", "");
			String info;
			try {
				info = filter(symbol,c.getTime());
				s.setInfo(info);
				if(info!=null&&info!=""){
					s.setChongzhu(true);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println(index+"/"+size);
			m.put(s.getSymbol(), s);
			/*if(index>5){
				break;
			}*/
		}
		return m;
	}
	
	
	public static void main(String args[]) throws ParseException {
	  //  System.out.println(filter("002399"));	
	}

}
