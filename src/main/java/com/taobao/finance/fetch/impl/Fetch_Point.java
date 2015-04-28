package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015Äê4ÔÂ16ÈÕ
 */
public class Fetch_Point {
	public static String url1 = "http://api.map.baidu.com/ag/coord/convert?x=";
	public static String url2 = "&y=";
	public static String url3 = "&from=0&to=2&mode=1";
	
	public static String getUrl(String lat,String lng) {
		return url1+lat+url2+lng+url3;
	}


	public static String fetch(String lat,String lng) {
		HttpClient client = new HttpClient();
		String s = null;
		String newUrl = getUrl(lat,lng);
		//newUrl="http://api.map.baidu.com/ag/coord/convert?x=121.583140,121.583140&y=31.341174,31.341174&from=0&to=2&mode=1";
		HttpMethod getMethod = new GetMethod(newUrl);
		getMethod.setFollowRedirects(false);
		getMethod
				.addRequestHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");

		try {
			
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				List<String> l1=new ArrayList<String>();
				List<String> l2=new ArrayList<String>();
				String jsonStr = getMethod.getResponseBodyAsString();
				JSONArray array=(JSONArray)JSON.parse(jsonStr);
				int size=array.size();
				for(int i=0;i<size;i++){
					JSONObject o=(JSONObject)array.get(i);
					l1.add(o.get("x").toString());
					l2.add(o.get("y").toString());
				}
				s=getString(l1,l2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	
	public static String getString(List<String> l1,List<String>l2){
		String r="";
		int size=l1.size();
		List<String> l=new ArrayList<String>();
		for(int i=0;i<size;i++){
			l.add(getFromBase64(l1.get(i))+","+getFromBase64(l2.get(i)));
		}
		r=StringUtils.join(l,";");
		return r;
	}
	
	public static String getFromBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
	

	public static void main(String args[]) {

      fetch("121.583140","31.341174");
      
	}

}
