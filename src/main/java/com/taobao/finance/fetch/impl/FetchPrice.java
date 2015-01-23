package com.taobao.finance.fetch.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class FetchPrice {
	public static  String url="http://detail.tmall.com/item.htm?id=40934251337";
	public static  String base="http://detail.tmall.com/item.htm?";
	public static  String ajax="http://mdskip.taobao.com/core/initItemDetail.htm?addressLevel=3&tryBeforeBuy=false&notAllowOriginPrice=false&isForbidBuyItem=false&sellerPreview=false&isUseInventoryCenter=false&tmallBuySupport=true&itemId=40934251337&itemTags=775,843,907,1163,1291,1478,1483,1803,1867,2049,2059,2251,2443,2507,2635,3974,4166,4555,4811,5323,7809,28802,53954,56130,56194&isApparel=false&isSecKill=false&isAreaSell=false&sellerUserTag3=144185556822163584&sellerUserTag4=1152921508937483651&household=false&sellerUserTag=34672672&progressiveSupport=false&service3C=true&showShopProm=false&isRegionLevel=true&sellerUserTag2=18020085046183936&isIFC=false&offlineShop=false&cartEnable=true&tgTag=false&queryMemberRight=true&callback=setMdskip&timestamp=1421154971184&areaId=330100&cat_id=2&ref=http%3A%2F%2Flist.tmall.com%2Fsearch_product.htm%3Fq%3Diphone6%2B%25CA%25D6%25BB%25FA%25BF%25C7%26click_id%3Diphone6%2B%25CA%25D6%25BB%25FA%25BF%25C7%26from%3Dmallfp..pc_1.0_hq%26spm%3D3.7396704.a1z5h.1.ZC0UET";
	public static List<Stock> fetch(String uri) {
		String id=getId(uri);
		List<Stock> list = null;
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(ajax);
		getMethod.setFollowRedirects(false);
		getMethod.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		getMethod.addRequestHeader("Referer",url);
		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				File f=new File("E:\\aaa.txt");
				if(!f.exists()){
					FetchUtil.createFile("E:\\aaa.txt");
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(jsonStr);
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String getId(String url){
		String [] s=url.split("\\?");
		if(s.length==2){
			String [] params=s[1].split("&");
			if(params!=null){
				for(String ss:params){
					if(ss.startsWith("id=")){
						return ss;
					}
						
				}
			}
		}
		return null;
	}
	
	public static void main(String args[]){
		System.out.println(getId(url));
	}
}


