package com.taobao.finance.fetch.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



/**
 * 获取基金持仓
 * @author songhong.ljy
 */
public class Fetch_FundHolding {
	private static String allFundsUrl = "http://fund.eastmoney.com/";

	private static String getUrl(String code){
		code=code.replace("sz", "");
		return allFundsUrl+code+".html";
	}
	public static List<String>  fetchAllFunds(String code) {
		String url=getUrl(code);
		List<String> list = new ArrayList<String>();
		HttpClient client = new HttpClient();
		HttpMethod getMethod = new GetMethod(url);

		try {
			client.executeMethod(getMethod);
			if (getMethod.getStatusCode() == 200) {
				String jsonStr = getMethod.getResponseBodyAsString();
				list=parseResult(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<String> parseResult(String s) throws ParserException {
        List<String> l=new ArrayList<String>();
		Parser parser;
		parser = new Parser(s);
		
		NodeList nodes;
		nodes = parser.parse(new StringFilter("var stocklist"));
		if (nodes.size() > 0) {
			org.htmlparser.Node d = nodes.elementAt(0);
			String codeStr = d.toHtml();
			codeStr=codeStr.replace("var stocklist=\"", ""); 
		    codeStr=codeStr.replace("\"", "");
		    String codes[]= codeStr.split(",");
		    for(String c:codes){
		    	c=c.substring(0,c.length()-1);
		    	l.add(c);
		    }
		} 
		
		/**
		 * 持仓占比
		 */
        parser = new Parser(s);
		nodes = parser.parse(
						new AndFilter(
								new TagNameFilter("li"),
								new HasAttributeFilter("class","cc")
								)
		);
		List<String> quota=new ArrayList<String>();
		if (nodes.size() > 0) {
			for (int i = 1; i < nodes.size(); i++) {
				org.htmlparser.Node d = nodes.elementAt(i);
				String codeStr = d.getFirstChild().getFirstChild().toHtml();
				codeStr = codeStr.replace("%", "");
				if (codeStr.contains(".")) {
					quota.add(codeStr);
				}
				d.toHtml();
			}
		} 
		List<String> result =new ArrayList<String>();
		for(int i=0;i<l.size();i++){
			result.add(l.get(i)+"\t"+quota.get(i));
		}
		return result;
	}

	public static void main(String args[]) {

		List<String> l=fetchAllFunds("630005");
		
	}

}
