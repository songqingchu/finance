package com.taobao.finance.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock;

public class FetchUtil {

	public static DateFormat SHANGTOU_FORMAT = new SimpleDateFormat("yyyymmdd");
	public static DateFormat TIANTIAN_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat FILE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

	
	public Store store;
	public  void setFILE_STOCK_ANASYS_BASE(String fILE_STOCK_ANASYS_BASE) {
		FILE_STOCK_ANASYS_BASE = fILE_STOCK_ANASYS_BASE;
	}
	public  void setFILE_STOCK_HISTORY_BASE(String fILE_STOCK_HISTORY_BASE) {
		FILE_STOCK_HISTORY_BASE = fILE_STOCK_HISTORY_BASE;
	}
	public  void setFILE_STOCK_CHOOSE_BASE(String fILE_STOCK_CHOOSE_BASE) {
		FILE_STOCK_CHOOSE_BASE = fILE_STOCK_CHOOSE_BASE;
	}
	public  void setFILE_USER_STATS_BASE(String fILE_USER_STATS_BASE) {
		FILE_USER_STATS_BASE = fILE_USER_STATS_BASE;
	}

	public  String getFILE_STOCK_ANASYS_BASE() {
		return FILE_STOCK_ANASYS_BASE;
	}
	public  String getFILE_STOCK_HISTORY_BASE() {
		return FILE_STOCK_HISTORY_BASE;
	}
	public  String getFILE_USER_STATS_BASE() {
		return FILE_USER_STATS_BASE;
	}
	public  String getFILE_STOCK_CHOOSE_BASE() {
		return FILE_STOCK_CHOOSE_BASE;
	}


	public static String FILE_STOCK_ANASYS_BASE ;
	public static String FILE_STOCK_HISTORY_BASE ;
	public static String FILE_USER_STATS_BASE ;
	public static String FILE_STOCK_TMP_BASE ;
	public static String FILE_STOCK_CHOOSE_BASE ;

	
	public void init(){
		boolean working=checkWorkingDay();
		store.workingDay=working;
	}
	
	public  String getFILE_STOCK_TMP_BASE() {
		return FILE_STOCK_TMP_BASE;
	}
	public  void setFILE_STOCK_TMP_BASE(String fILE_STOCK_TMP_BASE) {
		FILE_STOCK_TMP_BASE = fILE_STOCK_TMP_BASE;
	}
	public static Stock parseTodayStockFromSina(String s, String code) {

		Stock stock = null;
		try {
			String ss[] = s.split("=");
			if (ss.length == 2) {
				String data[] = ss[1].split(",");
				stock = new Stock();
				stock.setStartPrice(data[1]);

				stock.setEndPrice(data[3]);
				stock.setLastEndPrice(data[2]);
				stock.setHighPrice(data[4]);
				stock.setLowPrice(data[5]);
				Float rate = Float.parseFloat(data[3])
						/ Float.parseFloat(data[2]) - 1;
				Float realRate = (Float.parseFloat(data[3]) - Float
						.parseFloat(data[1])) / Float.parseFloat(data[2]);
				stock.setRealRate(realRate);
				stock.setRate(rate);
				stock.setSymbol(code);
				stock.setName(data[0].replace("\"", ""));
				stock.setTradeNum(Long.parseLong(data[8]));
			}
		} catch (Exception e) {
			return null;
		}
		return stock;

	}

	

	
	public static List<Stock> parseStockHistoryFromCompass(String s, String code)
			throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Stock> r = new ArrayList<Stock>();
		try {

			Parser parser = new Parser(s);
			NodeList nodes = parser.parse(new TagNameFilter("tr"));
			if (nodes.size() > 0) {
				for (int i = 4; i < nodes.size(); i++) {
					Stock stock = new Stock();
					stock.setSymbol(code);
					org.htmlparser.Node tr = nodes.elementAt(i);
					NodeList child = tr.getChildren();

					Node dNode = child.elementAt(0).getFirstChild();
					Node sNode = child.elementAt(1);
					Node hNode = child.elementAt(2);
					Node lNode = child.elementAt(3);
					Node eNode = child.elementAt(4);
					Node num = child.elementAt(5);

					String dStr = dNode.toHtml();
					String sStr = sNode.getFirstChild().toHtml();
					String hStr = hNode.getFirstChild().toHtml();
					String lStr = lNode.getFirstChild().toHtml();
					String eStr = eNode.getFirstChild().toHtml();
					String numStr = num.getFirstChild().toHtml();
					numStr = numStr.replace(",", "");
					numStr = numStr.replace(".", "");

					
					stock.setDate(format.parse(dStr));
					stock.setStartPrice(sStr);
					stock.setHighPrice(hStr);
					stock.setLowPrice(lStr);
					stock.setEndPrice(eStr);
					stock.setTradeNum(Long.parseLong(numStr) * 100);
					r.add(stock);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}


	

	

	



	/**
	 * Parser parser = new Parser(s); AndFilter filter = new AndFilter( new
	 * TagNameFilter("div"), new HasAttributeFilter("id","change") ); NodeList
	 * nodes = parser.parse(filter); if(nodes.size()>0){ Stock stock=new
	 * Stock(); stock.setSymbol(code); org.htmlparser.Node d=nodes.elementAt(0);
	 * 
	 * String codeStr=d.toHtml(); codeStr=codeStr.replace("<div>", "");
	 * codeStr=codeStr.replace("</div>", ""); return stock; }else{ return null;
	 * }
	 */

	public static List<Stock> parseSinaStock2(String s) {
		List<Stock> list = new ArrayList<Stock>();
		s = s.replace("var quote_123={rank:[\"", "");
		s = s.replace("var quote_123={\"],pages:1}", "");

		String[] dd = s.split("\",\"");
		for (String k : dd) {
			String[] kk = k.split(",");
			Stock st = new Stock();
			if (kk[1].startsWith("6")) {
				st.setSymbol("sh" + kk[1]);
				st.setName(kk[2]);
			} else {
				st.setSymbol("sz" + kk[1]);
				st.setName(kk[2]);
			}
			list.add(st);
		}
		return list;

	}

	

	
	
	
	public static String format0Right(float num) {
		String format = "%07d";
		return String.format(format, num);
	}

	
	

	
	
	

	

	public static boolean saveAbsolute(String file, List<String> content) {
		File f = new File(file);
		if (f.exists()) {
			f.delete();
		} else {
			createFile(file);
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String line : content) {
				bw.write(line + "\r\n");
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void createFile(String file) {
		String dir = "";
		if (file.contains("/")) {
			String[] ids = file.split("/");
			for (int i = 0; i < ids.length - 1; i++) {
				dir = dir + "/" + ids[i];
			}
		} else {
			String[] ids = StringUtils.split(file, "\\");
			for (int i = 0; i < ids.length - 1; i++) {
				if (i == ids.length - 2) {
					dir = dir + ids[i];
				} else {
					dir = dir + ids[i] + "\\";
				}
			}

		}
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
			f.canExecute();
		}
		f = new File(file);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	public static Map<String, Stock> readFileMapAbsolute(String file) {
		Map<String, Stock> map = new HashMap<String, Stock>();
		File f = new File(file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			while (line != null) {
				String data[] = line.split("\t");
				Stock s = new Stock();
				s.setSymbol(data[0]);
				if (data.length > 1) {
					s.setName(data[1]);
				}
				map.put(data[0], s);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	
	
	

	
	
	

	public static String formatRate(Float rate) {
		String str = new DecimalFormat("0.000").format(rate);
		if (!str.startsWith("-")) {
			str = " " + str;
		}
		return str;
	}

	public static String formatExchange(Float rate) {
		String str = new DecimalFormat("00000.00").format(rate);

		String prefix = "0";

		if (str.startsWith("00706")) {
			str.length();
		}

		if (!str.startsWith("-")) {
			str = "+" + str;
		}

		prefix = "+0";
		String placement = "+ ";
		while (str.startsWith(prefix)) {
			str = str.replace(prefix, placement);
			prefix = placement + "0";
			placement = placement + " ";
		}
		str = str.replace("+", " ");
		prefix = "-0";
		placement = "- ";
		while (str.startsWith(prefix)) {
			str = str.replace(prefix, placement);
			prefix = placement + "0";
			placement = placement + " ";
		}
		return str;
	}

	public static String formatRatePercent(Float rate) {
		String str = new DecimalFormat("0.00").format(rate * 100);
		if (!str.startsWith("-")) {
			if (str.length() == 5) {
				str = "+" + str;
			} else {
				str = "+ " + str;
			}
		} else {
			if (str.length() == 6) {
				// str = " " + str;
			} else {
				str = str.replace("-", "- ");
			}
		}
		return str + "%";
	}
	
    public static boolean checkWorkingDay(){
    	String sh="sh000001";
    	String sz="sz399001";
    	Stock todaySh = Fetch_SingleStock.fetch(sh);
    	Stock todaySz = Fetch_SingleStock.fetch(sz);
    	Stock tmpSh = getTmp(sh);
    	Stock tmpSz = getTmp(sz);
    	if(!todaySh.getStartPrice().equals(tmpSh.getStartPrice())){
    		return true;
    	}
    	if(!todaySh.getEndPrice().equals(tmpSh.getEndPrice())){
    		return true;
    	}
    	if(!todaySz.getStartPrice().equals(tmpSz.getStartPrice())){
    		return true;
    	}
    	if(!todaySz.getEndPrice().equals(tmpSz.getEndPrice())){
    		return true;
    	}
    	return false;
    }
    
    public static Stock getTmp(String symbol){
    	String url=FetchUtil.FILE_STOCK_TMP_BASE+symbol+".txt";
    	String s=FetchUtil.FILE_STOCK_HISTORY_BASE;
        File f=new File(url);
        Stock st=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
            if(line != null) {
			  st=Hisdata_Base.parseTmpData(symbol, line);
            }
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return st;
    }

    public static void main(String args[]) throws IOException, ParseException{
		checkWorkingDay();
	}
}
