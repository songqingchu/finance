package com.taobao.finance.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.taobao.finance.constants.FundConstants;
import com.taobao.finance.dataobject.DailyData;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.dataobject.Store;

public class FetchUtil {

	public static DateFormat SHANGTOU_FORMAT = new SimpleDateFormat("yyyymmdd");
	public static DateFormat TIANTIAN_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat FILE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

	public static String HARDEN_TYPE_RATE7 = "���Ƿ�ƫ��ֵ�ﵽ7%";
	public static String HARDEN_TYPE_RATE_VERSE7 = "�յ���ƫ��ֵ�ﵽ7%";
	public static String HARDEN_TYPE_RATE20 = "�Ƿ�ƫ��ֵ�ۼƴﵽ20%";
	public static String HARDEN_TYPE_RATE_VERSE20 = "����ƫ��ֵ�ۼƴﵽ20%";
	public static String HARDEN_TYPE_EXCHANGE20 = "�ջ����ʴﵽ20%";
	public static String HARDEN_TYPE_SWING_15 = "�����ֵ�ﵽ15%";
	public static String HARDEN_TYPE_RATE_ST12 = "�Ƿ�ƫ��ֵ�ۼƴﵽ12%��ST֤ȯ";
	public static String HARDEN_TYPE_RATE_VERSE_ST12 = "����ƫ��ֵ�ۼƴﵽ12%��ST֤ȯ";

	public static String FILE_RONGQUAN = "rongQuan.txt";
	public static String FILE_RONGQUAN_ZHU = "rongQuanZhu.txt";
	public static String FILE_RONGQUAN_ZHO = "rongQuanZho.txt";
	public static String FILE_RONGQUAN_CHU = "rongQuanChu.txt";

	public static String FILE_HOLDING = "holding.txt";
	public static String FILE_HOLDING_ZHU = "holdingZhu.txt";
	public static String FILE_HOLDING_ZHO = "holdingZho.txt";
	public static String FILE_HOLDING_CHU = "holdingChu.txt";

	// ����ģ��1�����ģ�ͣ������������Ʒ�ת��

	public static String FILE_NORMAL_CONVERT1 = "NormalConvert_1.txt";
	public static String FILE_NORMAL_CONVERT2 = "NormalConvert_2.txt";
	public static String FILE_NORMAL_CONVERT3 = "NormalConvert_3.txt";
	public static String FILE_NORMAL_CONVERT4 = "NormalConvert_4.txt";
	public static String FILE_NORMAL_CONVERT = "NormalConvert.txt";

	public static String FILE_HOLDING_CONVERT1 = "HoldingConvert_1.txt";
	public static String FILE_HOLDING_CONVERT2 = "HoldingConvert_2.txt";
	public static String FILE_HOLDING_CONVERT3 = "HoldingConvert_3.txt";
	public static String FILE_HOLDING_CONVERT4 = "HoldingConvert_4.txt";
	public static String FILE_HOLDING_CONVERT = "HoldingConvert.txt";

	public static String FILE_LOAN_CONVERT1 = "LoanConvert_1.txt";
	public static String FILE_LOAN_CONVERT2 = "LoanConvert_2.txt";
	public static String FILE_LOAN_CONVERT3 = "LoanConvert_3.txt";
	public static String FILE_LOAN_CONVERT4 = "LoanConvert_4.txt";
	public static String FILE_LOAN_CONVERT = "LoanConvert.txt";

	public static String FILE_HARDEN1 = "Harden_1.txt";
	public static String FILE_HARDEN2 = "Harden_2.txt";
	public static String FILE_HARDEN3 = "Harden_3.txt";
	public static String FILE_HARDEN4 = "Harden_4.txt";
	public static String FILE_HARDEN = "Harden.txt";

	public static String FILE_HARDEN_YOU1 = "HardenYou_1.txt";
	public static String FILE_HARDEN_YOU2 = "HardenYou_2.txt";
	public static String FILE_HARDEN_YOU3 = "HardenYou_3.txt";
	public static String FILE_HARDEN_YOU4 = "HardenYou_4.txt";
	public static String FILE_HARDEN_YOU = "HardenYou.txt";

	public static String FILE_DUO = "M1-C-D.txt";
	public static String FILE_DUO_X = "M1-C-D-X.txt";
	public static String FILE_KONG = "M1-C-K.txt";
	public static String FILE_FILT_DUO_1 = "M1-F-D1.txt";
	public static String FILE_FILT_DUO_2 = "M1-F-D2.txt";
	public static String FILE_FILT_DUO_3 = "M1-F-D3.txt";
	public static String FILE_FILT_DUO_4 = "M1-F-D4.txt";

	public static String FILE_FILT_KONG_1 = "M1-F-K1.txt";
	public static String FILE_FILT_KONG_2 = "M1-F-K2.txt";
	public static String FILE_FILT_KONG_3 = "M1-F-K3.txt";
	public static String FILE_FILT_KONG_4 = "M1-F-K4.txt";

	public static String FILE_FILT_DUO = "M1-F-D.txt";
	public static String FILE_FILT_KONG = "M1-F-K.txt";

	public static String FILE_EXAM_DUO = "M1-E-D.txt";
	public static String FILE_EXAM_KONG = "M1-E-K.txt";

	// ����ģ��2��TOP����ֲ֡�
	public static String FILE_HOLDING_C = "M3-C-HOLDING.txt";

	public static String FILE_HOLDING_C_ZB = "M2-F-HOLDING-ZB.txt";
	public static String FILE_HOLDING_C_ZXB = "M2-F-HOLDING-ZXB.txt";
	public static String FILE_HOLDING_C_CB = "M2-F-HOLDING-CB.txt";

	public static String FILE_HOLDING_E = "M2-E-HOLDING.txt";

	public static String FILE_HOLDING_F_ZB = "M2-F-HOLDING-ZB.txt";
	public static String FILE_HOLDING_F_ZXB = "M2-F-HOLDING-ZXB.txt";
	public static String FILE_HOLDING_F_CB = "M2-F-HOLDING-CB.txt";

	public static String FILE_HOLDING_F_ZB1 = "M2-F-HOLDING-ZB1.txt";
	public static String FILE_HOLDING_F_ZXB1 = "M2-F-HOLDING-ZXB1.txt";
	public static String FILE_HOLDING_F_CB1 = "M2-F-HOLDING-CB1.txt";

	public static String FILE_HOLDING_F_ZB2 = "M2-F-HOLDING-ZB2.txt";
	public static String FILE_HOLDING_F_ZXB2 = "M2-F-HOLDING-ZXB2.txt";
	public static String FILE_HOLDING_F_CB2 = "M2-F-HOLDING-CB2.txt";

	public static String FILE_HOLDING_F_ZB3 = "M2-F-HOLDING-ZB3.txt";
	public static String FILE_HOLDING_F_ZXB3 = "M2-F-HOLDING-ZXB3.txt";
	public static String FILE_HOLDING_F_CB3 = "M2-F-HOLDING-CB3.txt";

	public static String FILE_HOLDING_F_ZB4 = "M2-F-HOLDING-ZB4.txt";
	public static String FILE_HOLDING_F_ZXB4 = "M2-F-HOLDING-ZXB4.txt";
	public static String FILE_HOLDING_F_CB4 = "M2-F-HOLDING-CB4.txt";

	// ����ģ��3��������ͣģ�͡�
	public static String FILE_HARDEN_C = "M3-C-HARDEN.txt";
	public static String FILE_HARDEN_F1 = "M3-F-HARDEN1.txt";
	public static String FILE_HARDEN_F2 = "M3-F-HARDEN2.txt";
	public static String FILE_HARDEN_F3 = "M3-F-HARDEN3.txt";
	public static String FILE_HARDEN_F4 = "M3-F-HARDEN4.txt";

	public static String FILE_HARDEN_F_YOU1 = "M3-F-HARDEN-YOU1.txt";
	public static String FILE_HARDEN_F_YOU2 = "M3-F-HARDEN-YOU2.txt";
	public static String FILE_HARDEN_F_YOU3 = "M3-F-HARDEN-YOU3.txt";
	public static String FILE_HARDEN_F_YOU4 = "M3-F-HARDEN-YOU4.txt";

	public static String FILE_HARDEN_F = "M3-F-HARDEN.txt";
	public static String FILE_HARDEN_E = "M3-E-HARDEN.txt";

	// public static String FILE_STOCK_ANASYS_BASE =
	// "/home/songhong.ljy/finance/anasys/";
	// public static String FILE_STOCK_STATIS_BASE =
	// "/home/songhong.ljy/finance/statis/";

	public static String FILE_STOCK_ANASYS_BASE = "E:\\stock\\anasys\\";
	public static String FILE_STOCK_STATIS_BASE = "E:\\stock\\statis\\";

	public static int DATE_FORMAT_TYPE_COMPU_WRITE = 1;
	public static int DATE_FORMAT_TYPE_OTHER = 2;

	/**
	 * ����JSON����
	 * 
	 * @param s
	 * @return
	 */
	public static DailyData parse_TiantianFoud_To_DailyData(String s) {
		DailyData d = new DailyData();
		String[] tokens = StringUtils.split(s, ",");
		if (tokens.length > 10) {
			d.setDate(new Date());
			d.setFundCode(tokens[0]);
			d.setFundName(tokens[1]);
			d.setValueAll(Double.parseDouble(tokens[4]));
			d.setValueToday(Double.parseDouble(tokens[5]));
		} else {
			// System.out.println("���ݽ�������");
			// System.out.println(s);
			return null;
		}
		return d;
	}

	/**
	 * ����XML����
	 * 
	 * @param s
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DailyData> parse_TianTianFoud_To_History_DailyData(
			String s) {
		List<DailyData> list = new ArrayList<DailyData>();
		DailyData d = null;
		SAXBuilder builder = new SAXBuilder(false);
		Document document;
		try {
			document = builder.build(new ByteArrayInputStream(s
					.getBytes("UTF-8")));
			Element table = document.getRootElement();
			Element tbody = table.getChild("tbody");
			@SuppressWarnings("unchecked")
			List<Element> trs = tbody.getChildren();
			for (Element tr : trs) {
				List<Element> tds = tr.getChildren();
				d = new DailyData();
				d.setValueToday(Double.parseDouble(tds.get(1).getValue()));
				d.setValueAll(Double.parseDouble(tds.get(2).getValue()));
				try {
					String rateStr = tds.get(3).getValue().replace("%", "");
					if (StringUtils.isBlank(rateStr)) {
						d.setRate(-10000D);
					} else {
						d.setRate(Double.parseDouble(rateStr));
					}

				} catch (Exception e1) {
					e1.printStackTrace();

				}
				d.setDate(SHANGTOU_FORMAT.parse(tds.get(0).getValue()));
				list.add(d);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return list;
	}

	/**
	 * ����XML����
	 * 
	 * @param s
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DailyData> parse_ShangTouFoud_To_DailyData(String s) {
		List<DailyData> list = new ArrayList<DailyData>();
		DailyData d = null;
		SAXBuilder builder = new SAXBuilder(false);
		Document document;
		try {
			document = builder.build(new ByteArrayInputStream(s
					.getBytes("UTF-8")));
			Element root = document.getRootElement();
			Element graph = root.getChild("graphs");
			@SuppressWarnings("unchecked")
			List<Element> children = graph.getChildren();
			Element value = children.get(0);
			@SuppressWarnings("unchecked")
			List<Element> values = value.getChildren();
			Element valueAll = children.get(1);
			List<Element> valuesAll = valueAll.getChildren();
			int size = values.size();
			for (int i = 0; i < size; i++) {
				d = new DailyData();
				d.setValueToday(Double.parseDouble(values.get(i).getValue()));
				d.setValueAll(Double.parseDouble(valuesAll.get(i).getValue()));
				d.setFundCode(FundConstants.FOUND_CODE_SHANGTOU_XINGXINGDONGLI);
				d.setFundName(FundConstants.FOUND_NAME_SHANGTOU_XINGXINGDONGLI);
				d.setDate(SHANGTOU_FORMAT.parse(valuesAll.get(i)
						.getAttributeValue("xid")));
				list.add(d);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return list;
	}

	public static List<String> parse_Rongquan_To_DailyData(String s)
			throws ParserException {
		List<String> list = new ArrayList<String>();
		Parser parser = new Parser(s);
		NodeIterator it = parser.elements();
		org.htmlparser.Node table = it.nextNode();
		NodeList child = table.getChildren();

		for (int i = 1; i < child.size(); i++) {
			org.htmlparser.Node tr = child.elementAt(i);
			NodeList td = tr.getChildren();
			org.htmlparser.Node code = td.elementAt(0).getFirstChild();
			org.htmlparser.Node name = td.elementAt(1).getFirstChild();
			String codeStr = code.toHtml();
			String nameStr = name.toHtml();

			if (codeStr.startsWith("300")) {
				codeStr = "sz" + codeStr;
			}
			if (codeStr.startsWith("002")) {
				codeStr = "sz" + codeStr;
			}
			if (codeStr.startsWith("600")) {
				codeStr = "sh" + codeStr;
			}
			if (codeStr.startsWith("000")) {
				codeStr = "sz" + codeStr;
			}
			Stock stock = new Stock();
			stock.setSymbol(codeStr);
			stock.setName(nameStr);
			list.add(codeStr);
		}
		return list;
	}

	/**
	 * ������������ʵʱ���̺����ݣ����Σ� ���֣����̣����գ��ּۣ���߼ۣ���ͼ�
	 * 
	 * @param s
	 * @param code
	 * @return
	 */
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

	public static Date parseLastDateFromSina(String s) {

		Date d = null;
		Stock stock = null;
		String ss[] = s.split("=");
		if (ss.length == 2) {
			String data[] = ss[1].split(",");
			String dateStr = data[data.length - 3];
			dateStr = dateStr.replace("-", ".");
			try {
				return FILE_FORMAT.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<Stock> parseHardenStockFromSina(String s) {
		List<Stock> r = new ArrayList<Stock>();
		JsonArray jsonArray = null;
		try {
			Stock stock = null;
			JsonReader g = new JsonReader(new StringReader(s));
			g.setLenient(true);
			JsonElement el = new JsonParser().parse(g);
			if (el.isJsonArray()) {
				jsonArray = el.getAsJsonArray();
				Iterator<JsonElement> it = jsonArray.iterator();

				while (it.hasNext()) {
					JsonElement e = it.next();
					Stock st = new Stock();
					JsonObject o = (JsonObject) e;
					st.setSymbol(o.getAsJsonPrimitive("symbol").getAsString());
					st.setRate(o.getAsJsonPrimitive("changepercent")
							.getAsFloat() / 100);
					st.setName(o.getAsJsonPrimitive("name").getAsString());
					st.setStartPrice(o.getAsJsonPrimitive("settlement")
							.getAsString());
					st.setEndPrice(o.getAsJsonPrimitive("high").getAsString());
					st.setHighPrice(o.getAsJsonPrimitive("low").getAsString());
					st.setLowPrice(o.getAsJsonPrimitive("trade").getAsString());

					r.add(st);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * ��ָ���������ʷ����30
	 * 
	 * @param s
	 * @param code
	 * @return
	 * @throws Exception
	 */
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

	public static List<String> parseStockInfoFromSina(String s)
			throws ParserException {
		List<String> l = new ArrayList<String>();
		Parser parser = new Parser(s);
		NodeList nodes = parser.parse(new AndFilter(new TagNameFilter("div"),
				new HasAttributeFilter("class", "datelist")));
		NodeList content = nodes.elementAt(0).getChildren().elementAt(0)
				.getChildren();
		int size = content.size() / 3;
		for (int i = 0; i < size; i++) {
			String datestr = content.elementAt(i * 3).getText();
			String infoStr = content.elementAt(i * 3 + 1).getFirstChild()
					.getText();
			String str = (datestr + infoStr).replace("&nbsp;", "-");
			l.add(str);
		}
		return l;
	}

	public static int parseStoreHistoryTotalPageFromHexun(String s)
			throws Exception {
		Parser parser = new Parser(s);
		NodeList nodes = parser.parse(new AndFilter(new TagNameFilter("li"),
				new HasAttributeFilter("class", "next")));
		Node totalPage = nodes.elementAt(0).getPreviousSibling()
				.getPreviousSibling().getPreviousSibling();
		String text = totalPage.getFirstChild().getFirstChild().getText();
		return Integer.parseInt(text);
	}

	public static List<Stock> parseStoreHistoryFromHexun(String s, String code,
			String store) throws Exception {
		List<Stock> l = new ArrayList<Stock>();
		Parser parser = new Parser(s);
		NodeList nodes = parser.parse(new AndFilter(new TagNameFilter("table"),
				new HasAttributeFilter("class", "dou_table")));
		NodeList trs = nodes.elementAt(0).getChildren()
				.extractAllNodesThatMatch(new TagNameFilter("tr"));
		for (int i = 1; i <= trs.size() - 1; i++) {
			Node tr = trs.elementAt(i);
			String name = tr.getText();
			Stock st = new Stock();
			st.setHardenStoreCode(code);
			st.setHardenStore(store);
			String codeStr = tr.getChildren().elementAt(0).getFirstChild()
					.getFirstChild().getText();
			String dateStr = tr.getChildren().elementAt(1).getFirstChild()
					.getText();
			String typeStr = tr.getChildren().elementAt(2).getFirstChild()
					.getText();
			String rationStr = tr.getChildren().elementAt(3).getFirstChild()
					.getText();
			String[] ids = codeStr.split(" ");
			if (ids[0].startsWith("600")) {
				ids[0] = "sh" + ids[0];
			} else {
				ids[0] = "sz" + ids[0];
			}
			dateStr = dateStr.replace("-", ".");
			String type = getHotMoneyType(typeStr);
			rationStr = rationStr.replace("(", "/");
			rationStr = rationStr.replace(")", "");
			rationStr = rationStr.replace("%", "");
			String nums[] = rationStr.split("/");

			st.setSymbol(ids[0]);
			st.setName(ids[1]);
			st.setHardenDate(FILE_FORMAT.parse(dateStr));
			st.setHardenType(type);
			st.setHardenMoney(Float.parseFloat(nums[0]));
			st.setTatalValue((Float.parseFloat(nums[1])));
			st.setHardenQuota((Float.parseFloat(nums[2]) / 100F));

			l.add(st);
		}

		return l;
	}

	public static String getHotMoneyType(String s) {
		if (s.contains("�Ƿ�") && s.contains("7")) {
			return HARDEN_TYPE_RATE7;
		}
		if (s.contains("����") && s.contains("7")) {
			return HARDEN_TYPE_RATE_VERSE7;
		}

		if (s.contains("�Ƿ�") && s.contains("20")) {
			return HARDEN_TYPE_RATE20;
		}
		if (s.contains("����") && s.contains("20")) {
			return HARDEN_TYPE_RATE_VERSE20;
		}
		if (s.contains("�Ƿ�") && s.contains("12") && s.contains("ST")) {
			return HARDEN_TYPE_RATE_ST12;
		}
		if (s.contains("����") && s.contains("12") && s.contains("ST")) {
			return HARDEN_TYPE_RATE_VERSE_ST12;
		}

		if (s.contains("���") && s.contains("15")) {
			return HARDEN_TYPE_SWING_15;
		}

		if (s.contains("������") && s.contains("20")) {
			return HARDEN_TYPE_EXCHANGE20;
		}
		return "";
	}

	public static List<Stock> parseStockRateSpeed(String s) throws Exception {
		List<Stock> list = new ArrayList<Stock>();
		JSONArray arr = JSON.parseArray(s);
		Object[] objs = arr.toArray();
		for (Object ok : objs) {
			JSONObject o = (JSONObject) ok;
			String symbol = o.getString("symbol");
			String name = o.getString("name");
			Float ch = o.getFloat("changepercent");
			Stock st = new Stock();
			st.setSymbol(symbol);
			st.setName(name);
			st.setRate(ch / 100);
			list.add(st);
		}
		return list;

	}

	public static List<Store> parseStoreFromHexun(String s) throws Exception {

		List<Store> r = new ArrayList<Store>();
		Parser parser = new Parser(s);
		NodeList nodes = parser
				.parse(new AndFilter(new TagNameFilter("tr"),
						new HasAttributeFilter("onmouseover",
								"this.className='tr_bg'")));

		for (int i = 0; i < nodes.size(); i++) {
			Node d = nodes.elementAt(i);
			Store st = new Store();
			NodeList tds = d.getChildren();
			st.setName(tds.elementAt(1).getFirstChild().getFirstChild()
					.getText());
			LinkTag t = (LinkTag) tds.elementAt(1).getFirstChild();
			String code = t.extractLink();
			code = code.replace("yyb", "");
			code = code.replace(".shtml", "");
			String times = tds.elementAt(2).getFirstChild().getText();
			String all = tds.elementAt(3).getFirstChild().getText();
			String buy = tds.elementAt(4).getFirstChild().getText();
			String sell = tds.elementAt(5).getFirstChild().getText();
			String pureBuy = tds.elementAt(6).getFirstChild().getText();
			st.setTimes(Integer.parseInt(times));
			st.setAll(Float.parseFloat(all));
			st.setBuy(Float.parseFloat(buy));
			st.setSell(Float.parseFloat(sell));
			// st.setPureBuy(Float.parseFloat(pureBuy));
			st.setCode(code);
			r.add(st);
		}
		return r;
	}

	public static List<Integer> parseNumber(String s, boolean today)
			throws Exception {

		List<Integer> l = new ArrayList<Integer>();
		Parser parser = new Parser(s);
		NodeList nodes = parser.parse(new AndFilter(new TagNameFilter("tr"),
				new HasParentFilter(new HasAttributeFilter("id", "datatbl"))));
		for (int i = 1; i < nodes.size(); i++) {
			Node d = nodes.elementAt(i);
			NodeList tds = d.getChildren();
			Node num = null;
			if (today) {
				num = tds.elementAt(4);
			} else {
				num = tds.elementAt(3);
			}
			try {
				String ss = num.getFirstChild().getText();
				if (StringUtils.isNumeric(ss)) {
					l.add(Integer.parseInt(ss));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

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

	public static List<Stock> parseSinaFieldStock(String s) {
		List<Stock> list = new ArrayList<Stock>();
		JSONArray arr = JSON.parseArray(s);
		Object[] objs = arr.toArray();
		for (Object ok : objs) {
			JSONObject o = (JSONObject) ok;
			String symbol = o.getString("symbol");
			String name = o.getString("name");
			Stock st = new Stock();
			st.setSymbol(symbol);
			st.setName(name);
			list.add(st);
		}
		return list;

	}

	public static List<Stock> parseSinaStock(String s) {
		List<Stock> list = new ArrayList<Stock>();
		JsonArray jsonArray = null;
		try {
			Stock stock = null;
			JsonReader g = new JsonReader(new StringReader(s));
			g.setLenient(true);
			JsonElement el = new JsonParser().parse(g);
			Gson gson = new Gson();
			if (el.isJsonArray()) {
				jsonArray = el.getAsJsonArray();
				Iterator it = jsonArray.iterator();

				while (it.hasNext()) {
					JsonElement e = (JsonElement) it.next();
					stock = gson.fromJson(e, Stock.class);
					list.add(stock);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ʽ����� �ַ��� [*]�����,�Ҳ��ո�
	 * 
	 * @param str
	 * @param min_length
	 *            : ��С�������
	 * @return
	 */
	public static String formatLeftS(String str) {
		String format = "%-10s";
		return String.format(format, str);
	}

	/**
	 * ��ʽ����� ���� [*]�Ҷ���,��0
	 * 
	 * @param num
	 * @param min_length
	 *            : ��С�������
	 * @return
	 */
	public static String format0Right(long num) {
		String format = "%07d";
		return String.format(format, num);
	}

	public static String format0Right(float num) {
		String format = "%07d";
		return String.format(format, num);
	}

	/**
	 * ��ʽ����� ������ [*]�Ҷ���,��0
	 * 
	 * @param d
	 * @param min_length
	 *            : ��С�������
	 * @param precision
	 *            : С�������λ��
	 * @return
	 */
	public static String format0Right(Float f) {
		String format = "% 2.3f";
		if (f >= 0) {
			return " " + String.format(format, f);
		} else {
			return String.format(format, f);
		}
	}

	public static String format0Right4(Float f) {
		String format = "% 2.4f";
		if (f >= 0) {
			return " " + String.format(format, f);
		} else {
			return String.format(format, f);
		}
	}

	public static void richPrint(List<Stock> list) {
		System.out.println("����������" + list.size());
		int i = 0;
		String s = "";

		for (Stock stock : list) {
			String title = stock.getSymbol();
			if (title.contains("sz")) {
				title = title.replace("sz", "");
				title = "SZHQ" + title;
			}
			if (title.contains("sh")) {
				title = title.replace("sh", "");
				title = "SHHQ" + title;
			}
			s = s + title + "\n";
			i++;
			if (i > 100) {
				break;
			}
			System.out.println(stock.getSymbol() + "\t"
					+ FetchUtil.format0Right(stock.getTwo()) + "\t"
					+ FetchUtil.format0Right(stock.getThree()) + "\t"
					+ FetchUtil.format0Right(stock.getFour()) + "\t"
					+ FetchUtil.format0Right(stock.getFive()) + "\t"
					+ FetchUtil.format0Right(stock.getTen()) + "\t"
					+ FetchUtil.format0Right(stock.getTwenty()) + "\t"
					+ FetchUtil.format0Right(stock.getMonth()) + "\t"
					+ FetchUtil.format0Right(stock.getTwoMonth()) + "\t"
					+ stock.getName());
		}
		System.out.println("\n\n");
		System.out.println(s);
	}

	public static void print(List<Stock> list) {
		// System.out.println("����������" + list.size());
		String s = "";
		int size = 0;
		for (Stock stock : list) {
			if (stock.isRongquan()) {
				size++;
				continue;
			}
			System.out.println(stock.getSymbol());
		}
		System.out.println("����������" + list.size() + "\t" + size);
		System.out.println("\n\n");
	}

	/**
	 * �����ļ�
	 * 
	 * @param file
	 * @param date
	 * @param content
	 * @return
	 */
	public static boolean save(String file, String date, List<String> content) {
		String fileUrl = FILE_STOCK_ANASYS_BASE + date + "\\" + file;
		String bakUrl = FILE_STOCK_ANASYS_BASE + date + "\\back\\" + file;
		saveAbsolute(fileUrl, content);
		saveAbsolute(bakUrl, content);
		return true;
	}

	public static boolean save(String file, Date date, List<String> content) {
		String dateStr = FILE_FORMAT.format(date);
		String fileUrl = FILE_STOCK_ANASYS_BASE + dateStr + "\\" + file;
		String dirUrl = FILE_STOCK_ANASYS_BASE + dateStr;
		File dir = new File(dirUrl);
		File f = new File(fileUrl);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (f.exists()) {
			f.delete();
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileUrl));
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

	public static boolean saveAnasys(String file, Date date,
			List<String> content) {
		String dateStr = FILE_FORMAT.format(date);
		String fileUrl = FILE_STOCK_ANASYS_BASE + dateStr + "\\" + file;
		String backFileUrl = FILE_STOCK_ANASYS_BASE + dateStr + "\\back\\"
				+ file;
		saveAbsolute(fileUrl, content);
		saveAbsolute(backFileUrl, content);
		return true;
	}

	public static boolean saveStatis(String file, Date date,
			List<String> content) {
		String dateStr = FILE_FORMAT.format(date);
		String fileUrl = FILE_STOCK_STATIS_BASE + dateStr + "\\" + file;
		String backFileUrl = FILE_STOCK_STATIS_BASE + dateStr + "\\back\\"
				+ file;
		saveAbsolute(fileUrl, content);
		saveAbsolute(backFileUrl, content);
		return true;
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

	/**
	 * �����ļ�������·��
	 * 
	 * @param file
	 * @param date
	 * @param content
	 * @return
	 */
	public static boolean save(String file, List<String> content) {
		File f = new File(file);
		if (!f.exists()) {
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

	public static List<String> readFileList(String file) {
		String fileUrl = FetchUtil.getFileUrl(file, DATE_FORMAT_TYPE_OTHER);
		List<String> l = new ArrayList<String>();
		File f = new File(fileUrl);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			while (line != null) {
				String[] sss = line.split("\t");
				l.add(sss[0]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static Map<String, Stock> readFileMap(String file) {
		Map<String, Stock> map = new HashMap<String, Stock>();
		String fileUrl = FetchUtil.getFileUrl(file, DATE_FORMAT_TYPE_OTHER);
		File f = new File(fileUrl);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			while (line != null) {
				String[] sss = line.split("\t");
				Stock s = new Stock();
				s.setSymbol(sss[0]);
				map.put(line, s);
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

	/**
	 * ����·��
	 * 
	 * @param file
	 * @return
	 */
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

	public static Map<String, Stock> readRong(String file) {
		Map<String, Stock> map = new HashMap<String, Stock>();
		File f = new File(file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			br.readLine();
			br.readLine();
			line = br.readLine();
			while (line != null) {
				String data[] = line.split(" ");
				List<String> ff = new ArrayList<String>();
				for (String d : data) {
					if (StringUtils.isNotBlank(d)) {
						if (StringUtils.equals("����", d)) {
							continue;
						}
						if (StringUtils.equals("��", d)) {
							continue;
						}
						d = d.replace(".0000", "");
						String ssss = d.replace(".", "");
						if (StringUtils.isNumeric(ssss)) {
							ff.add(d);
						} else {
							String s = ff.get(ff.size() - 1);
							if (StringUtils.isNumeric(s)) {
								s.length();
								ff.add(d);
							} else {
								s = s + d;
								ff.remove(ff.size() - 1);
								ff.add(s);
							}

						}
					}
				}
				String symbol = data[0];
				if (symbol.startsWith("600")) {
					symbol = "sh" + symbol;
				} else {
					symbol = "sz" + symbol;
				}
				Stock s = new Stock();
				s.setSymbol(symbol);
				s.setName(ff.get(1));
				s.setRongNum(Float.parseFloat(ff.get(3)));
				map.put(symbol, s);
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

	public static Map<String, Stock> readFileMapAbsoluteForHolding(String file) {
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
				if (data.length == 3) {
					s.setHoldByFound(Integer.parseInt(data[1]));
					s.setName(data[2]);
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

	public static Map<String, Stock> readFileMapAbsoluteForInfo(String file) {
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
				if (data.length >= 3) {
					s.setInfo(data[2]);
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

	public static Map<String, Stock> readFileMapAbsoluteForNormalConvert(
			String file) {
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
				if (data.length == 3) {
					s.setHoldByFound(Integer.parseInt(data[1]));
					s.setName(data[2]);
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

	public static List<String> readFileListAbsolute(String file) {
		List<String> l = new ArrayList<String>();
		File f = new File(file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			while (line != null) {
				String[] sss = line.split("\t");
				l.add(sss[0]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static Map<String, Stock> readFileListAbsoluteX(String file) {
		Map<String, Stock> m = new HashMap<String, Stock>();
		File f = new File(file);
		int error = 0;
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			line = br.readLine();
			while (line != null) {
				if (line.contains("sh600225")) {
					line.length();
				}
				String[] d = line.split("\t");
				if (d.length < 12) {
					line = br.readLine();
					continue;
				}
				Stock s = new Stock();
				s.setSymbol(d[0]);
				s.setName(d[1]);
				s.setStartPrice(d[2]);
				s.setEndPrice(d[3]);
				s.setStartPrice1(d[4]);
				s.setEndPrice1(d[5]);
				s.setStartPrice2(d[6]);
				s.setEndPrice2(d[7]);
				s.setStartPrice3(d[8]);
				s.setEndPrice3(d[9]);
				s.setStartPrice4(d[10]);
				s.setEndPrice4(d[11]);

				if (d[12].equals("null") || d[13].equals("null")
						|| d[14].equals("null") || d[15].equals("null")) {
					error++;
					line = br.readLine();
					continue;
				}
				count++;
				if (count == 948) {
					s.get_10changes();
				}
				System.out.println(count);
				s.setRate(Float.parseFloat(d[12]));
				s.setRate1(Float.parseFloat(d[13]));
				s.setRate2(Float.parseFloat(d[14]));
				s.setRate3(Float.parseFloat(d[15]));
				// s.setRate4(Float.parseFloat(d[15]));
				if (!d[18].equals("null")) {
					s.setMonth(Float.parseFloat(d[18]));

				}
				if (!d[17].equals("null")) {
					s.setTwoMonth(Float.parseFloat(d[17]));
					s.setStatis(false);
				}
				if (!d[19].equals("null")) {
					s.setHoldByFound(Integer.parseInt(d[19]));
				}
				if (!d[20].equals("null")) {
					s.setHardenTimes(Integer.parseInt(d[20]));
				}
				if (!d[21].equals("null")) {
					s.setStrictHarden(Boolean.parseBoolean(d[21]));
				}
				if (!d[22].equals("null")) {
					s.setRongquan(Boolean.parseBoolean(d[22]));
				}
				if (d.length >= 24) {
					s.setInfo(d[23]);
					if (d[23] != null && d[23] != "") {
						s.setChongzhu(true);
					}
				}

				m.put(s.getSymbol(), s);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("error:" + error);
		return m;
	}

	public static List<String> readAllLineFileListAbsolute(String file) {
		List<String> l = new ArrayList<String>();
		File f = new File(file);
		if (!f.exists()) {
			return l;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			line = br.readLine();
			while (line != null) {
				if (StringUtils.isBlank(line)) {
					line = br.readLine();
					continue;
				}
				l.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static String getFileUrl(String file, int type) {
		Date d = new Date();
		if (type == DATE_FORMAT_TYPE_OTHER) {
			Calendar c = java.util.Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			d = c.getTime();
		} else {
			Calendar c = java.util.Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			d = c.getTime();
		}

		String dateStr = FILE_FORMAT.format(d);
		return FILE_STOCK_ANASYS_BASE + dateStr + "\\" + file;
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

	public static String formatRate(String rate) {
		String str = new DecimalFormat("0.000").format(rate);
		if (!str.startsWith("-")) {
			str = " " + str;
		}
		return str;
	}

	public static String getSaveExamFile(String file) {
		String url = "";
		Date d = new Date();
		String dir = "";
		for (int i = -1; i > -10; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date dd = c.getTime();
			dir = FetchUtil.FILE_STOCK_ANASYS_BASE
					+ FetchUtil.FILE_FORMAT.format(dd);
			File f = new File(dir);
			if (f.exists()) {
				d = dd;
				break;
			}
		}
		url = FILE_STOCK_ANASYS_BASE + FILE_FORMAT.format(d) + "\\" + file;
		return url;
	}

	public static void main(String args) {
		Float a = 0.12344F;
		System.out.println("ss".split("\t").length);
	}
}
