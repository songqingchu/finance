package com.taobao.finance.fetch.stock;

import java.io.File;
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
import com.taobao.finance.util.FetchUtil;


/**
 * trace前几天数据
 * 1只普涨天数：
 * 1只普涨幅度：
 * @author songhong.ljy
 */
public class Fetch_Trace10Day {
	public static String url = "http://hq.sinajs.cn/list=";

	public static Map<String, Stock> map = new HashMap<String, Stock>();

	

	static List<List<String>> d=new ArrayList<List<String>>();
	static List<List<String>> dL=new ArrayList<List<String>>();
	
/*	static List<List<String>> h=new ArrayList<List<String>>();
	static List<List<String>> hL=new ArrayList<List<String>>();
	
	static List<List<String>> hy=new ArrayList<List<String>>();
	static List<List<String>> hyL=new ArrayList<List<String>>();*/

	static {
		for(int i=0;i<10;i++){
		    d.add(new ArrayList<String>());
		    dL.add(new ArrayList<String>());
		   /* h.add(new ArrayList<String>());
		    hL.add(new ArrayList<String>());
		    hy.add(new ArrayList<String>());
		    hyL.add(new ArrayList<String>());*/
		}

		Date dd=null;
		int realCount=0;
		for(int count=-1;count>-30;count--){
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DATE,count);
			dd=c.getTime();
			String dir=FetchUtil.FILE_STOCK_ANASYS_BASE+FetchUtil.FILE_FORMAT.format(dd);
			File f=new File(dir);
			if(f.exists()){
				List<String> contains=FetchUtil.readFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_4);
				d.get(realCount).addAll(contains);
				List<String> containsAllLine=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_FILT_DUO_4);
				dL.get(realCount).addAll(containsAllLine);
				dL.get(realCount).add(dir+"\\"+FetchUtil.FILE_FILT_DUO_4);
				
				/*contains=FetchUtil.readFileListAbsolute(dir+"\\"+FetchUtil.FILE_HARDEN_F4);
				h.get(realCount).addAll(contains);
				containsAllLine=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_HARDEN_F4);
				hL.get(realCount).addAll(containsAllLine);
				hL.get(realCount).add(dir+"\\"+FetchUtil.FILE_HARDEN_F4);
				
				contains=FetchUtil.readFileListAbsolute(dir+"\\"+FetchUtil.FILE_HARDEN_F_YOU4);
				hy.get(realCount).addAll(contains);
				containsAllLine=FetchUtil.readAllLineFileListAbsolute(dir+"\\"+FetchUtil.FILE_HARDEN_F_YOU4);
				hyL.get(realCount).addAll(containsAllLine);
				hyL.get(realCount).add(dir+"\\"+FetchUtil.FILE_HARDEN_F_YOU4);*/
						
				realCount++;
				if(realCount==10){
					break;
				}
			}
		}
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

	public static void trace() {
		for(int i=0;i<10;i++){
			List<String> symbolD=d.get(i);
			List<String> allLineD=dL.get(i);
			if(symbolD!=null&&symbolD.size()!=0){
				System.out.println("跟踪前"+(i+1)+"天数据！");
				for(int j=0;j<symbolD.size();j++){
					String all=allLineD.get(j);
					if(all.endsWith("-")){
						continue;
					}else{
						String s=symbolD.get(j);
						Stock st=fetch(s);
						if(st.getRate()>0){
							all=all+"\t"+st.getRateString();
						}else{
							all=all+"\t"+st.getRateString()+"\t-";
						}
						allLineD.set(j, all);
					}
				}
				String dir=allLineD.remove(allLineD.size()-1);
				FetchUtil.save(dir, allLineD);
				update(dir,allLineD);
			}
			
			
			/*List<String> symbolH=d.get(i);
			List<String> allLineH=dL.get(i);
			if(symbolH!=null&&symbolH.size()!=0){
				for(int j=0;j<symbolH.size();j++){
					String all=allLineH.get(j);
					if(all.endsWith("-")){
						continue;
					}else{
						String s=symbolH.get(j);
						Stock st=fetch(s);
						if(st.getRate()>0){
							all=all+"\t"+st.getRateString();
						}else{
							all=all+"\t"+st.getRateString()+"\t-";
						}
						allLineH.set(j, all);
					}
				}
				String dir=allLineH.remove(allLineH.size()-1);
				FetchUtil.save(dir, allLineH);
				update(dir,allLineH);
			}
			
			
			List<String> symbolHY=d.get(i);
			List<String> allLineHY=dL.get(i);
			if(symbolHY!=null&&symbolHY.size()!=0){
				for(int j=0;j<symbolHY.size();j++){
					String all=allLineHY.get(j);
					if(all.endsWith("-")){
						continue;
					}else{
						String s=symbolHY.get(j);
						Stock st=fetch(s);
						if(st.getRate()>0){
							all=all+"\t"+st.getRateString();
						}else{
							all=all+"\t"+st.getRateString()+"\t-";
						}
						allLineHY.set(j, all);
					}
				}
				String dir=allLineHY.remove(allLineHY.size()-1);
				FetchUtil.save(dir, allLineHY);
				update(dir,allLineHY);
			}*/
		}
	}
	
	public static void update(String dir,List<String> content){
		for(int i=1;i<4;i++){
			String newDir=dir.replace("4.txt", i+".txt");
			List<String> l=FetchUtil.readFileListAbsolute(newDir);
			List<String> newContent=new ArrayList<String>();
			for(String code:l){
				for(String s:content){
					if(s.startsWith(code)){
						newContent.add(s);
					}
				}
			}
			FetchUtil.save(newDir, newContent);
		}
	}

	public static void main(String args[]) {
		trace();
	}
}

