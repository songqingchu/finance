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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Achieve;
import com.taobao.finance.dataobject.Introduce;
import com.taobao.finance.dataobject.Report;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.dataobject.Tick;
import com.taobao.finance.entity.GStock;
import com.taobao.finance.entity.Proxy;
import com.taobao.finance.fetch.impl.Fetch_SingleStock_Sina;

public class FetchUtil {

	public static DateFormat SHANGTOU_FORMAT = new SimpleDateFormat("yyyymmdd");
	public static DateFormat TIANTIAN_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static DateFormat FILE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

	public Store store;

	public void setFILE_STOCK_ANASYS_BASE(String fILE_STOCK_ANASYS_BASE) {
		FILE_STOCK_ANASYS_BASE = fILE_STOCK_ANASYS_BASE;
	}

	public void setFILE_STOCK_HISTORY_BASE(String fILE_STOCK_HISTORY_BASE) {
		FILE_STOCK_HISTORY_BASE = fILE_STOCK_HISTORY_BASE;
	}

	public void setFILE_STOCK_CHOOSE_BASE(String fILE_STOCK_CHOOSE_BASE) {
		FILE_STOCK_CHOOSE_BASE = fILE_STOCK_CHOOSE_BASE;
	}

	public void setFILE_USER_STATS_BASE(String fILE_USER_STATS_BASE) {
		FILE_USER_STATS_BASE = fILE_USER_STATS_BASE;
	}

	public String getFILE_STOCK_ANASYS_BASE() {
		return FILE_STOCK_ANASYS_BASE;
	}

	public String getFILE_STOCK_HISTORY_BASE() {
		return FILE_STOCK_HISTORY_BASE;
	}

	public String getFILE_USER_STATS_BASE() {
		return FILE_USER_STATS_BASE;
	}

	public String getFILE_STOCK_CHOOSE_BASE() {
		return FILE_STOCK_CHOOSE_BASE;
	}

	public static String FILE_STOCK_ANASYS_BASE = "E:\\stock\\anasys\\";
	public static String FILE_STOCK_HISTORY_BASE = "E:\\stock\\history\\";
	public static String FILE_USER_STATS_BASE = "E:\\stock\\stats\\";
	public static String FILE_STOCK_TMP_BASE = "E:\\stock\\tmp\\";
	public static String FILE_STOCK_CHOOSE_BASE = "E:\\stock\\choose\\";

	public void init() {
		/*
		 * boolean working=checkWorkingDay(); store.workingDay=working;
		 */
	}

	public String getFILE_STOCK_TMP_BASE() {
		return FILE_STOCK_TMP_BASE;
	}

	public void setFILE_STOCK_TMP_BASE(String fILE_STOCK_TMP_BASE) {
		FILE_STOCK_TMP_BASE = fILE_STOCK_TMP_BASE;
	}

	public static Stock parseTodayStockFromSina(String s) {

		s = StringUtils.replace(s, "var hq_str_", "");
		if (s.contains("\n")) {
			s = s.substring(1, s.length());
		}
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
				stock.setSymbol(ss[0]);
				stock.setName(data[0].replace("\"", ""));
				stock.setTradeNum(Long.parseLong(data[8]));
			}
		} catch (Exception e) {
			return null;
		}
		return stock;

	}

	public static List<Proxy> parseProxy(String s) {

		List<Proxy> l = new ArrayList<Proxy>();
		try {
			Parser parser = new Parser(s);
			NodeList nodes = parser.parse(new AndFilter(
					new TagNameFilter("tr"), new HasAttributeFilter("class",
							"odd")));

			for (int i = 0; i < nodes.size(); i++) {
				Node tr = nodes.elementAt(i);
				NodeList tds = tr.getChildren();
				List<Node> tdList = new ArrayList<Node>();
				int k = 0;
				for (int j = 0; j < tds.size(); j++) {
					Node td = tds.elementAt(j);
					if (td.getText().contains("td")) {
						tdList.add(td);
						k++;
						if (k >= 10) {
							break;
						}
					}
				}

				if (tdList.size() >= 10) {
					String ip = tdList.get(2).getChildren().elementAt(0)
							.getText();
					String port = tdList.get(3).getChildren().elementAt(0)
							.getText();
					// String
					// address=tdList.get(4).getChildren().elementAt(1).getChildren().elementAt(0).getText();
					String date = tdList.get(9).getChildren().elementAt(0)
							.getText();
					Proxy p = new Proxy();
					p.setIp(ip);
					p.setPort(Integer.parseInt(port));
					// p.setLocation(address);
					DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
					p.setIncludeDate(df.parse(date));
					l.add(p);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;

	}

	public static Map<String, String> parseShiZhi(String s) {
		DecimalFormat df = new DecimalFormat("#.00");
		Map<String, String> m = new HashMap<String, String>();
		try {
			JSONArray a = (JSONArray) JSON.parse(s);
			int size = a.size();
			for (int i = 0; i < size; i++) {
				JSONObject o = (JSONObject) a.get(i);
				String symbol = o.getString("symbol");
				String value = o.getString("nmc");
				Float f = Float.parseFloat(value);
				f = f / 10000;
				String newValue = df.format(f);
				m.put(symbol, newValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;

	}

	public static Map<String, Object> parseHolders(String s) {
		Map<String, Object> m = new HashMap<String, Object>();
		List<GStock> l = new ArrayList<GStock>();

		JSONArray array = (JSONArray) JSON.parse(s);
		for (int i = 0; i < array.size(); i++) {
			String d = (String) array.get(i);
			GStock st = new GStock();
			String[] datas = d.split(",");
			String symbol = datas[0];
			String name = datas[1];
			String holder = datas[2];
			String change = datas[3];
			if (symbol.startsWith("60")) {
				symbol = "sh" + symbol;
			} else {
				symbol = "sz" + symbol;
			}
			st.setSymbol(symbol);
			st.setName(name);
			st.setHolder(Integer.parseInt(holder));
			st.setChange((int) (Float.parseFloat(change) * 100));
			l.add(st);
		}
		m.put("data", l);

		return m;

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

	public static Stock parseProfit(String s, String code) throws Exception {
		Map<String, String> m = new HashMap<String, String>();
		try {
			Parser parser = new Parser(s);
			parser.setEncoding("GBK");
			NodeList nodes = parser.parse(new HasAttributeFilter("id",
					"FundHoldSharesTable"));
			NodeList trs = nodes.elementAt(0).getChildren();
			if (nodes.size() > 0) {
				int size = nodes.elementAt(0).getChildren().size();
				for (int i = 0; i < size; i++) {
					List<Node> l = new ArrayList<Node>();
					Node n = trs.elementAt(i);
					System.out
							.println("-==========================================");

					System.out.println(n.toHtml());
					if (n.toHtml().contains("鍏憡鏃ユ湡")) {
						l.add(n);
						String s1 = n.getChildren().elementAt(0).getChildren()
								.elementAt(1).getFirstChild().toHtml();
						System.out.println(s1);
					}
					if (n.toHtml().contains("鍑�鍒╂鼎")) {
						l.add(n);
						String s2 = n.getChildren().elementAt(1)
								.getFirstChild().toHtml();
						System.out.println(s2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Introduce parseInfo(String s) {
		Date d = new Date();
		List<Report> l = new ArrayList<Report>();
		Parser parser = null;
		try {
			parser = new Parser(s);
			NodeList nodes = parser.parse(new AndFilter(new HasAttributeFilter(
					"id", "Table0"), new TagNameFilter("table")));
			NodeList trs = nodes.elementAt(0).getChildren();
			try {
				Introduce i = new Introduce();
				if (trs.elementAt(3).getChildren().size() > 3) {
					Node name = trs.elementAt(3).getChildren().elementAt(3);
					if (name.getChildren() != null) {
						String nameStr = name.getChildren().elementAt(0)
								.getText();
						i.setName(nameStr);
					}
				}
				
				if (trs.elementAt(12).getChildren().size() > 3) {
					Node address = trs.elementAt(12).getChildren().elementAt(3);
					if (address.getChildren() != null) {
						String add = address.getChildren().elementAt(0)
								.getText();
						i.setAddress(add);
					}
				}
				if (trs.elementAt(13).getChildren().size() > 1) {
					Node province = trs.elementAt(13).getChildren()
							.elementAt(1);
					if (province.getChildren() != null) {
						String pro = province.getChildren().elementAt(0)
								.getText();
						i.setProvince(pro);
					}
				}
				if (trs.elementAt(17).getChildren().size() > 1) {
					Node introduce = trs.elementAt(17).getChildren()
							.elementAt(1);
					if (introduce.getChildren() != null) {
						String intr = introduce.getChildren().elementAt(0)
								.getText();
						i.setIntroduce(intr);
					}
				}
				if (trs.elementAt(18).getChildren().size() > 1) {
					Node range = trs.elementAt(18).getChildren().elementAt(1);
					if (range.getChildren() != null) {
						String ran = range.getChildren().elementAt(0).getText();
						i.setRange(ran);
					}
				}
				if (trs.elementAt(6).getChildren().size() > 3) {
					Node field = trs.elementAt(6).getChildren().elementAt(3);
					if (field.getChildren() != null) {
						String fie = field.getChildren().elementAt(0).getText();
						i.setField(fie);
					}
				}
				return i;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(trs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<Report> parseReport(String s) {
		Date d = new Date();
		List<Report> l = new ArrayList<Report>();
		Parser parser = null;
		try {
			parser = new Parser(s);
			NodeList nodes = parser.parse(new AndFilter(new HasAttributeFilter(
					"class", "l3"), new TagNameFilter("span")));
			for (int i = 1; i < nodes.size(); i++) {
				try {
					Node n = nodes.elementAt(i).getChildren().elementAt(0);
					Node dNode = nodes.elementAt(i).getNextSibling()
							.getNextSibling();
					Node reNode = nodes.elementAt(i).getPreviousSibling();
					String replay = reNode.getChildren().elementAt(0).getText();
					String date = dNode.getChildren().elementAt(0).getText();

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = df.parse(d.getYear() + "-" + date);

					LinkTag link = (LinkTag) n;
					String href = link.getAttribute("href");
					String text = n.getChildren().elementAt(0).getText();
					if (text.contains("权益变动") || text.contains("要约")) {
						Report r = new Report();
						r.setType(1);
						r.setTitle(text);
						r.setLink("http://guba.eastmoney.com" + href);
						r.setDate(dd);
						r.setPingNum(Integer.parseInt(replay));
						r.setDateString(d.getYear() + "-" + date);
						l.add(r);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (ParserException e) {
			e.printStackTrace();
		}

		return l;
	}

	public static List<Report> parseAchievement(String s) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		List<Report> l = new ArrayList<Report>();
		Parser parser = null;
		try {
			String[] tokens = StringUtils.split(s, "\n");
			if (tokens.length >= 0) {
				for (String ss : tokens) {
					if (ss.contains("defjson")) {

						ss = ss.replace("defjson: {pages:1,data:[\"", "");
						ss = ss.replace("\"]},", "");
						ss = ss.replace(" ", "");
						ss = ss.replace("\"", "aa");

						String[] ttt = ss.split("aa,aa");
						if (ttt.length > 0) {
							for (String r : ttt) {
								String[] tttt = StringUtils.split(r, ",");
								if (tttt.length == 18) {
									Achieve a = new Achieve();
									a.setCode(tttt[0]);
									a.setEarningStr(tttt[2]);
									a.setRevenueStr(tttt[4]);
									a.setRevenueTzStr(tttt[5]);
									a.setRevenueHzStr(tttt[6]);

									a.setProfitStr(tttt[7]);
									a.setProfitTzStr(tttt[8]);
									a.setProfitHzStr(tttt[9]);

									a.setAssetStr(tttt[10]);
									a.setAssetRateStr(tttt[11]);
									a.setCashFlowStr(tttt[12]);

									a.setDateStr(tttt[16]);
									a.setReportDateStr(tttt[15]);
									a.setDate(df.parse(tttt[16]));
									a.setReportDate(df.parse(tttt[15]));
								}
							}

						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return l;
	}

	public static List<Tick> parseZhubi(String s, String code, String dateStr,
			String filter) throws Exception {
		List<Tick> l = new ArrayList<Tick>();
		try {
			Parser parser = new Parser(s);
			NodeList nodes = parser.parse(new AndFilter(new HasAttributeFilter(
					"id", "datatbl"),
					new HasAttributeFilter("class", "datatbl")));
			NodeList trs = nodes.elementAt(0).getChildren();

			boolean firstTr = true;
			if (trs.size() > 0) {
				int size = trs.size();
				;
				for (int i = 0; i < size; i++) {
					Node n = trs.elementAt(i);
					boolean containsTr = false;
					containsTr = n.toHtml().contains("<tr");
					if (containsTr && firstTr) {
						firstTr = false;
						continue;
					}
					if (containsTr) {
						if (!firstTr) {
							NodeList tds = n.getChildren();
							if (tds.size() > 6) {
								List<Node> ll = new ArrayList<Node>();
								for (int j = 0; j < tds.size(); j++) {
									if (tds.elementAt(j).toHtml()
											.contains("<th")
											|| tds.elementAt(j).toHtml()
													.contains("<td")) {
										ll.add(tds.elementAt(j));
									}
								}

								String time = ll.get(0).getChildren()
										.elementAt(0).getText();
								String price = ll.get(1).getChildren()
										.elementAt(0).getText();
								String rate = ll.get(2).getChildren()
										.elementAt(0).getText();
								String d = ll.get(3).getChildren().elementAt(0)
										.getText();
								String num = ll.get(4).getChildren()
										.elementAt(0).getText();

								if (filter != null) {
									SimpleDateFormat df2 = new SimpleDateFormat(
											"yyyy.MM.dd HH:mm:ss");
									if (!df2.parse(dateStr + " " + time).after(
											df2.parse(filter))) {
										continue;
									}
								}

								rate = rate.replace("%", "");
								rate = rate.replace("+", "");
								if (d.equals("--")) {
									d = "0";
								}
								d = d.replace("+", "");

								Tick t = new Tick();
								t.setSymbol(code);
								t.setPriceStr(price);
								t.setTimeStr(dateStr + " " + time);
								t.setRateStr(rate);
								t.setdStr(d);
								t.setNum(Integer.parseInt(num));
								l.add(t);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (l.size() > 0) {
			Collections.reverse(l);
		}
		return l;
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
				if (StringUtils.isNotBlank(line)) {
					String data[] = line.split("\t");
					Stock s = new Stock();
					s.setSymbol(data[0]);
					if (data.length > 1) {
						s.setName(data[1]);
					}
					map.put(data[0], s);
				}
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

	public static String formatRate2(Float rate) {
		String str = new DecimalFormat("0.00").format(rate);
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

	public static boolean checkWorkingDay() {
		String sh = "sh000001";
		String sz = "sz399001";
		Stock todaySh = Fetch_SingleStock_Sina.fetch(sh, null, null);
		Stock todaySz = Fetch_SingleStock_Sina.fetch(sz, null, null);
		Stock tmpSh = getTmp(sh);
		Stock tmpSz = getTmp(sz);
		if (!todaySh.getStartPrice().equals(tmpSh.getStartPrice())) {
			return true;
		}
		if (!todaySh.getEndPrice().equals(tmpSh.getEndPrice())) {
			return true;
		}
		if (!todaySz.getStartPrice().equals(tmpSz.getStartPrice())) {
			return true;
		}
		if (!todaySz.getEndPrice().equals(tmpSz.getEndPrice())) {
			return true;
		}
		return false;
	}

	public static boolean checkWorkingDayUsusal() {
		Calendar cal = Calendar.getInstance();
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		if (w >= 1 && w <= 5) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkWorkingDay2() {
		String sh = "sh000001";
		String sz = "sz399001";
		Stock todaySh = Fetch_SingleStock_Sina.fetch(sh, null, null);
		Stock todaySz = Fetch_SingleStock_Sina.fetch(sz, null, null);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Stock todaySh2 = Fetch_SingleStock_Sina.fetch(sh, null, null);
		Stock todaySz2 = Fetch_SingleStock_Sina.fetch(sz, null, null);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Stock todaySh3 = Fetch_SingleStock_Sina.fetch(sh, null, null);
		Stock todaySz3 = Fetch_SingleStock_Sina.fetch(sz, null, null);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Stock todaySh4 = Fetch_SingleStock_Sina.fetch(sh, null, null);
		Stock todaySz4 = Fetch_SingleStock_Sina.fetch(sz, null, null);

		if (!todaySh.getStartPrice().equals(todaySh2.getStartPrice())) {
			return true;
		}
		if (!todaySh.getEndPrice().equals(todaySh3.getEndPrice())) {
			return true;
		}
		if (!todaySh.getEndPrice().equals(todaySh4.getEndPrice())) {
			return true;
		}
		if (!todaySz.getStartPrice().equals(todaySz2.getStartPrice())) {
			return true;
		}
		if (!todaySz.getEndPrice().equals(todaySz3.getEndPrice())) {
			return true;
		}
		if (!todaySz.getEndPrice().equals(todaySz4.getEndPrice())) {
			return true;
		}

		if (!todaySh.getStartPrice().equals(todaySh3.getStartPrice())) {
			return true;
		}
		if (!todaySh.getEndPrice().equals(todaySh3.getEndPrice())) {
			return true;
		}
		if (!todaySz.getStartPrice().equals(todaySz3.getStartPrice())) {
			return true;
		}
		if (!todaySz.getEndPrice().equals(todaySz3.getEndPrice())) {
			return true;
		}

		if (!todaySh.getStartPrice().equals(todaySh4.getStartPrice())) {
			return true;
		}
		if (!todaySh.getEndPrice().equals(todaySh4.getEndPrice())) {
			return true;
		}
		if (!todaySz.getStartPrice().equals(todaySz4.getStartPrice())) {
			return true;
		}
		if (!todaySz.getEndPrice().equals(todaySz4.getEndPrice())) {
			return true;
		}
		return false;
	}

	public static Stock getTmp(String symbol) {
		String url = FetchUtil.FILE_STOCK_TMP_BASE + symbol + ".txt";
		String s = FetchUtil.FILE_STOCK_HISTORY_BASE;
		File f = new File(url);
		Stock st = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			if (line != null) {
				st = Hisdata_Base.parseTmpData(symbol, line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return st;
	}

	public static void main(String args[]) throws IOException, ParseException {
		checkWorkingDay2();
	}
}
