package com.taobao.finance.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.anasys.controller.StatsDO;
import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.check.impl.Check_AV5;
import com.taobao.finance.check.impl.Check_AVCU;
import com.taobao.finance.check.impl.Check_BigTrend;
import com.taobao.finance.common.Store;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GStock;
import com.taobao.finance.fetch.impl.Fetch_AllStock;
import com.taobao.finance.fetch.impl.Fetch_SingleStock_Sina;
import com.taobao.finance.util.FetchUtil;

@Component
public class DataService {

	public static String getSymbol(String symbol) {
		if (symbol.startsWith("sz") || symbol.startsWith("sh")) {

		} else if (symbol.length() == 6 && StringUtils.isNumeric(symbol)) {
			if (symbol.startsWith("6")) {
				symbol = "sh" + symbol;
			} else {
				symbol = "sz" + symbol;
			}
		} else {
			symbol = Fetch_AllStock.nameMap.get(symbol);
		}
		return symbol;
	}

	public static String getName(String symbol) {
		String name = null;
		Stock s = Fetch_AllStock.map.get(symbol);
		if (s == null) {
			return null;
		}
		name = s.getName();
		return name;
	}

	public static Map<String, Object> mockStats(Integer id) throws IOException,
			ParseException {
		Map<String, Object> m = new HashMap<String, Object>();
		List<StatsDO> mine = readChart(id);
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		if (mine.size() > 0) {
			Date startDate = df.parse(mine.get(0).getDate());
			List<StatsDO> sh = readIndex(startDate, null, "sh000001");
			List<StatsDO> sz = readIndex(startDate, null, "sz399001");
			List<StatsDO> ch = readIndex(startDate, null, "sz399006");
			List<StatsDO> zh = readIndex(startDate, null, "sz399101");

			List<String> dList = new ArrayList<String>();
			List<Float> mL = new ArrayList<Float>();
			List<Float> shL = new ArrayList<Float>();
			List<Float> szL = new ArrayList<Float>();
			List<Float> year = new ArrayList<Float>();
			List<Double> chL = new ArrayList<Double>();
			List<Double> zhL = new ArrayList<Double>();

			List<Float> sRate = new ArrayList<Float>();
			List<Float> r = new ArrayList<Float>();
			List<Float> pRate = new ArrayList<Float>();
			List<Float> yRate = new ArrayList<Float>();
			List<Float> yyRate = new ArrayList<Float>();
			List<Float> ysRate = new ArrayList<Float>();

			for (StatsDO s : mine) {
				dList.add(s.getDate());
				mL.add(s.getvRate() / 1F - 100);

				Date endDate = StatsDO.df.parse(s.getDate());
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				long time1 = c.getTimeInMillis();
				c.setTime(endDate);
				long time2 = c.getTimeInMillis();
				long dayCount = (time2 - time1) / (1000 * 3600 * 24);
				if (dayCount == 0) {
					year.add(20F);
				} else {
					Float yearV = (s.getvRate() / 1.0F - 100) * 365 / dayCount;
					year.add(yearV);
				}

				sRate.add(s.getsRate() / 100F);
				r.add(s.getR() / 100F);
				pRate.add(s.getpRate() / 100F);
				yRate.add(s.getyRate() / 100F);
				yyRate.add(s.getYyRate() / 100F);
				ysRate.add(s.getYsRate() / 100F);
			}
			for (StatsDO s : sh) {
				shL.add(s.getvRate() / 100F);
			}
			for (StatsDO s : sz) {
				szL.add(s.getvRate() / 100F);
			}
			for (StatsDO s : ch) {
				chL.add(s.getV());
			}
			for (StatsDO s : zh) {
				zhL.add(s.getV());
			}

			m.put("series", dList);
			m.put("mine", mL);
			m.put("sh", shL);
			m.put("sz", szL);
			m.put("ch", chL);
			m.put("zh", zhL);
			m.put("year", year);

			m.put("sRate", sRate);
			m.put("rRate", r);
			m.put("pRate", pRate);

			m.put("yRate", yRate);
			m.put("yyRate", yyRate);
			m.put("ysRate", ysRate);
		}

		m.put("data", mine);
		// m.put("r", r);
		return m;
	}

	public static List<StatsDO> getRecords(Integer id) throws IOException,
			ParseException {
		List<StatsDO> mine = readChart(id);
		return mine;
	}

	public static Float getAve(List<Stock> l, int day, int start) {
		Float ave = 0F;
		Float total = 0F;
		for (int i = start; i >= start - day + 1; i--) {
			Float s = Float.parseFloat(l.get(i).getEndPrice());
			total = total + s;
		}
		ave = total / day;
		return ave;
	}

	public static Float getAve2(List<Stock> l, int day, int start) {
		if (start - day >= -1) {
			Float ave = 0F;
			Float total = 0F;
			for (int i = start; i >= start - day + 1; i--) {
				Float s = Float.parseFloat(l.get(i).getEndPrice());
				total = total + s;
			}
			ave = total / day;
			return ave;
		} else {
			return 0F;
		}

	}

	public static Map<String, Object> getKData(String symbol, Boolean working,
			Boolean downloaded) throws IOException, ParseException {
		Map<String, Object> m = new HashMap<String, Object>();

		List<Stock> l = Hisdata_Base.readHisDataMerge(symbol, null);

		if (working) {
			if (!downloaded) {
				Stock s = Fetch_SingleStock_Sina.fetch(symbol);
				l.add(s);
			}
		}

		Object[][] av5 = new Object[260][2];
		Object[][] av10 = new Object[260][2];
		Object[][] av20 = new Object[260][2];
		Object[][] data = new Object[260][5];
		Object[][] vol = new Object[260][2];

		if (l.size() > 300) {
			for (int i = 1; i < 261; i++) {
				Stock s = l.get(l.size() - i);
				Float v5 = getAve(l, 5, l.size() - i);
				Float v10 = getAve(l, 10, l.size() - i);
				Float v20 = getAve(l, 20, l.size() - i);

				Date d = s.getDate() == null ? new Date() : s.getDate();
				av5[260 - i][0] = d;
				av5[260 - i][1] = v5;

				av10[260 - i][0] = d;
				av10[260 - i][1] = v10;

				av20[260 - i][0] = d;
				av20[260 - i][1] = v20;

				vol[260 - i][0] = d;
				vol[260 - i][1] = s.getTradeNum();

				data[260 - i][0] = d;
				data[260 - i][1] = Float.parseFloat(s.getStartPrice());
				data[260 - i][2] = Float.parseFloat(s.getLowPrice());
				data[260 - i][3] = Float.parseFloat(s.getHighPrice());
				data[260 - i][4] = Float.parseFloat(s.getEndPrice());
			}
		}

		List<String> acvuDate = new Check_AVCU().check(symbol);
		List<String> av5Date = new Check_AV5().check(symbol);
		List<String> bigDate = new Check_BigTrend().check(symbol);

		List<Map<String, Object>> acvuTips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> av5Tips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bigTips = new ArrayList<Map<String, Object>>();

		for (String s : acvuDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "ACVU");
			acvuTips.add(mm);
		}
		for (String s : av5Date) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "AV5");
			av5Tips.add(mm);
		}
		for (String s : bigDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "BIG");
			bigTips.add(mm);
		}

		if (acvuTips.size() > 0) {
			Collections.reverse(acvuTips);
		}
		if (av5Tips.size() > 0) {
			Collections.reverse(av5Tips);
		}
		if (bigTips.size() > 0) {
			Collections.reverse(bigTips);
		}

		m.put("av5", av5);
		m.put("av10", av10);
		m.put("av20", av20);
		m.put("vol", vol);
		m.put("data", data);
		m.put("acvuTips", acvuTips);
		m.put("av5Tips", av5Tips);
		m.put("bigTips", bigTips);

		Stock s = l.get(l.size() - 1);
		Stock st = Fetch_AllStock.map.get(s.getSymbol());
		String name = null;
		if (st != null) {
			name = st.getName();
		} else {
			name = s.getSymbol();
		}

		m.put("name", name);
		m.put("start", s.getStartPriceFloat());
		m.put("high", s.getHighPriceFloat());
		m.put("low", s.getLowPriceFloat());
		m.put("end", s.getEndPriceFloat());
		return m;
	}

	public Map<String, Object> getKData2(String symbol, Boolean working,
			Boolean downloaded, Store store) throws IOException, ParseException {
		Map<String, Object> m = new HashMap<String, Object>();

		if (store.holderMap.containsKey(symbol)) {
			String record = store.holderMap.get(symbol).getRecord();
			if (StringUtils.isNotBlank(record)) {
				String content = "<b>股东户数<b>&nbsp;&nbsp;&nbsp;流通:"+store.holderMap.get(symbol).getLiuTong()+"亿<br><br><br>";
				String[] res = StringUtils.split(record, ";");
				Map<String, List<Integer>> ma = new HashMap<String, List<Integer>>();
				List<String> yearList = new ArrayList<String>();
				for (String rr : res) {
					if (StringUtils.isNotBlank(rr)) {
						String[] rrs = StringUtils.split(rr, ":");
						if (rrs.length == 2) {
							content = content + "<b>" + rrs[0] + ":</b>&nbsp;"
									+ rrs[1] + "<br>";
						}

						String year = rrs[0].substring(0, 4);
						if (!ma.containsKey(year)) {
							yearList.add(year);
							List<Integer> volList = new ArrayList<Integer>();
							volList.add(Integer.parseInt(rrs[1]));
							ma.put(year, volList);
						} else {
							List<Integer> volList = ma.get(year);
							volList.add(Integer.parseInt(rrs[1]));
							ma.put(year, volList);
						}
					}
				}

				List<Integer> l3 = new ArrayList<Integer>();
				List<Integer> l6 = new ArrayList<Integer>();
				List<Integer> l9 = new ArrayList<Integer>();
				List<Integer> l12 = new ArrayList<Integer>();

				for (String year : yearList) {
					List<Integer> vol = ma.get(year);
					if (vol.size() == 1) {
                        l3.add(vol.get(0));
                        l6.add(0);
						l9.add(0);
						l12.add(0);
					}
					if (vol.size() == 2) {
						l3.add(vol.get(0));
						l6.add(vol.get(1));
						l9.add(0);
						l12.add(0);
					}
					if (vol.size() == 3) {
						l3.add(vol.get(0));
						l6.add(vol.get(1));
						l9.add(vol.get(2));
						l12.add(0);
					}
					if (vol.size() == 4) {
						l3.add(vol.get(0));
						l6.add(vol.get(1));
						l9.add(vol.get(2));
						l12.add(vol.get(3));
					}
				}
				m.put("holder", content);
				m.put("years", yearList);
				m.put("v3", l3);
				m.put("v6", l6);
				m.put("v9", l9);
				m.put("v12", l12);

			}
		}

		List<Stock> l = null;
		if (store.hot.containsKey(symbol)) {
			l = store.hot.get(symbol);
		} else {
			l = Hisdata_Base.readHisDataMerge(symbol, null);
		}

		if (working) {
			if (!downloaded) {
				Stock s = Fetch_SingleStock_Sina.fetch(symbol);
				if (s != null) {
					l.add(s);
				}
			}
		}

		if (l.size() > 0) {
			Boolean ting = false;
			Stock s = l.get(l.size() - 1);
			if (s.getStartPrice() != null) {
				if (s.getStartPriceFloat() < 0.05F) {
					ting = true;
				}
			}
			if (s.getHighPrice() != null) {
				if (s.getHighPriceFloat() < 0.05F) {
					ting = true;
				}
			}
			if (s.getLowPrice() != null) {
				if (s.getLowPriceFloat() < 0.05F) {
					ting = true;
				}
			}
			if (s.getEndPrice() != null) {
				if (s.getEndPriceFloat() < 0.05F) {
					ting = true;
				}
			}

			if (ting) {
				l.remove(l.size() - 1);
			}
		}

		int size = l.size();
		Object[][] av5 = new Object[size - 1][2];
		Object[][] av10 = new Object[size - 1][2];
		Object[][] av20 = new Object[size - 1][2];
		Object[][] data = new Object[size + 1][6];
		Object[][] vol = new Object[size + 1][2];

		for (int i = 1; i <= size - 1; i++) {
			if (i == 56) {
				l.size();
			}
			Stock s = l.get(l.size() - i);
			Float v5 = getAve2(l, 5, l.size() - i);
			Float v10 = getAve2(l, 10, l.size() - i);
			Float v20 = getAve2(l, 20, l.size() - i);

			Date d = s.getDate() == null ? new Date() : s.getDate();
			av5[size - 1 - i][0] = d;
			av5[size - 1 - i][1] = v5;

			av10[size - 1 - i][0] = d;
			av10[size - 1 - i][1] = v10;

			av20[size - 1 - i][0] = d;
			av20[size - 1 - i][1] = v20;

			vol[size - 1 - i][0] = d;
			vol[size - 1 - i][1] = s.getTradeNum();

			data[size - 1 - i][0] = d;
			data[size - 1 - i][1] = Float.parseFloat(s.getStartPrice());
			data[size - 1 - i][2] = Float.parseFloat(s.getLowPrice());
			data[size - 1 - i][3] = Float.parseFloat(s.getHighPrice());
			data[size - 1 - i][4] = Float.parseFloat(s.getEndPrice());
			Stock yesterday = l.get(l.size() - i - 1);
			Float rate = Float.parseFloat(s.getEndPrice())
					/ Float.parseFloat(yesterday.getEndPrice()) * 100 - 100;
			data[size - 1 - i][5] = rate;
		}
		vol[size - 1][0] = vol[size - 2][0];
		vol[size - 1][1] = vol[size - 2][1];
		data[size - 1][0] = data[size - 2][0];
		data[size - 1][1] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size - 1][2] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size - 1][3] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size - 1][4] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size - 1][5] = 1;

		vol[size][0] = vol[size - 2][0];
		vol[size][1] = vol[size - 2][1];
		data[size][0] = data[size - 2][0];
		data[size][1] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size][2] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size][3] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size][4] = Float.parseFloat(data[size - 2][1].toString()) * 1.1;
		data[size][5] = 1;

		List<String> acvuDate = new Check_AVCU().check(symbol);
		List<String> av5Date = new Check_AV5().check(symbol);
		List<String> bigDate = new Check_BigTrend().check(symbol);

		List<Map<String, Object>> acvuTips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> av5Tips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bigTips = new ArrayList<Map<String, Object>>();

		for (String s : acvuDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "ACVU");
			acvuTips.add(mm);
		}
		for (String s : av5Date) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "AV5");
			av5Tips.add(mm);
		}
		for (String s : bigDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("x", d);
			mm.put("title", "BIG");
			bigTips.add(mm);
		}

		if (acvuTips.size() > 0) {
			Collections.reverse(acvuTips);
		}
		if (av5Tips.size() > 0) {
			Collections.reverse(av5Tips);
		}
		if (bigTips.size() > 0) {
			Collections.reverse(bigTips);
		}
		m.put("symbol", symbol);
		m.put("av5", av5);
		m.put("av10", av10);
		m.put("av20", av20);
		m.put("vol", vol);
		m.put("data", data);
		m.put("acvuTips", acvuTips);
		m.put("av5Tips", av5Tips);
		m.put("bigTips", bigTips);

		Stock s = l.get(l.size() - 1);
		Stock st = Fetch_AllStock.map.get(s.getSymbol());
		String name = null;
		if (st != null) {
			name = st.getName();
		} else {
			name = s.getSymbol();
		}

		m.put("name", name);
		m.put("start", s.getStartPriceFloat());
		m.put("high", s.getHighPriceFloat());
		m.put("low", s.getLowPriceFloat());
		m.put("end", s.getEndPriceFloat());
		return m;
	}

	public static Map<String, Object> canonHistory(String symbol,
			Boolean working, Boolean shi, List<GPublicStock> his)
			throws IOException, ParseException {
		Map<String, Object> m = new HashMap<String, Object>();

		List<Stock> l = Hisdata_Base.readHisDataMerge(symbol, null);

		if (working) {
			if (shi) {
				Stock s = Fetch_SingleStock_Sina.fetch(symbol);
				l.add(s);
			}
		}

		int size = l.size();

		Object[][] av5 = new Object[size - 1][2];
		Object[][] av10 = new Object[size - 1][2];
		Object[][] av20 = new Object[size - 1][2];
		Object[][] data = new Object[size - 1][6];
		Object[][] vol = new Object[size - 1][2];

		for (int i = 1; i <= size - 1; i++) {
			Stock s = l.get(l.size() - i);
			Float v5 = getAve2(l, 5, l.size() - i);
			Float v10 = getAve2(l, 10, l.size() - i);
			Float v20 = getAve2(l, 20, l.size() - i);

			Date d = s.getDate() == null ? new Date() : s.getDate();
			av5[size - 1 - i][0] = d;
			av5[size - 1 - i][1] = v5;

			av10[size - 1 - i][0] = d;
			av10[size - 1 - i][1] = v10;

			av20[size - 1 - i][0] = d;
			av20[size - 1 - i][1] = v20;

			vol[size - 1 - i][0] = d;
			vol[size - 1 - i][1] = s.getTradeNum();

			data[size - 1 - i][0] = d;
			data[size - 1 - i][1] = Float.parseFloat(s.getStartPrice());
			data[size - 1 - i][2] = Float.parseFloat(s.getLowPrice());
			data[size - 1 - i][3] = Float.parseFloat(s.getHighPrice());
			data[size - 1 - i][4] = Float.parseFloat(s.getEndPrice());

			Stock yesterday = l.get(l.size() - i - 1);
			Float rate = Float.parseFloat(s.getEndPrice())
					/ Float.parseFloat(yesterday.getEndPrice()) * 100 - 100;
			data[size - 1 - i][5] = rate;
		}

		List<String> acvuDate = new Check_AVCU().check(symbol);
		List<String> av5Date = new Check_AV5().check(symbol);
		List<String> bigDate = new Check_BigTrend().check(symbol);

		List<Map<String, Object>> acvuTips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> av5Tips = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bigTips = new ArrayList<Map<String, Object>>();

		Stock start = null;
		Date dd = new Date();
		for (String s : acvuDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			for (GPublicStock st : his) {
				if ((!d.before(st.getAddDate()) && (!d
						.after(st.getRemoveDate())))
						&& st.getType().equals("acvu")) {
					if (d.before(dd)) {
						dd = d;
					}
					Map<String, Object> mm = new HashMap<String, Object>();
					mm.put("x", d);
					mm.put("title", "ACVU");
					acvuTips.add(mm);
				}
			}
		}

		for (String s : av5Date) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			for (GPublicStock st : his) {
				if ((!d.before(st.getAddDate()) && (!d
						.after(st.getRemoveDate())))
						&& st.getType().equals("av5")) {
					if (d.before(dd)) {
						dd = d;
					}
					Map<String, Object> mm = new HashMap<String, Object>();
					mm.put("x", d);
					mm.put("title", "AV5");
					av5Tips.add(mm);
				}
			}
		}

		for (String s : bigDate) {
			Date d = FetchUtil.FILE_FORMAT.parse(s);
			for (GPublicStock st : his) {
				if ((!d.before(st.getAddDate()) && (!d
						.after(st.getRemoveDate())))
						&& st.getType().equals("oth")) {
					if (d.before(dd)) {
						dd = d;
					}
					Map<String, Object> mm = new HashMap<String, Object>();
					mm.put("x", d);
					mm.put("title", "BIG");
					bigTips.add(mm);
				}
			}
		}

		if (acvuTips.size() > 0) {
			Collections.reverse(acvuTips);
		}
		if (av5Tips.size() > 0) {
			Collections.reverse(av5Tips);
		}
		if (bigTips.size() > 0) {
			Collections.reverse(bigTips);
		}

		m.put("av5", av5);
		m.put("av10", av10);
		m.put("av20", av20);
		m.put("vol", vol);
		m.put("data", data);
		m.put("acvuTips", acvuTips);
		m.put("av5Tips", av5Tips);
		m.put("bigTips", bigTips);

		Stock s = l.get(l.size() - 1);
		Stock st = Fetch_AllStock.map.get(s.getSymbol());
		String name = null;
		if (st != null) {
			name = st.getName();
		} else {
			name = s.getSymbol();
		}
		int startIndex = 0;
		Stock startStock = null;
		for (Stock stock : l) {
			if (stock.getDate() != null) {
				if (stock.getDate().before(dd)) {
					startStock = stock;
				}
			}
		}
		if (startStock != null) {
			startIndex = l.indexOf(startStock);
			if (startIndex - 60 > 0) {
				startIndex = startIndex - 60;
			}
		}
		m.put("startIndex", startIndex);
		m.put("name", name);
		m.put("start", s.getStartPriceFloat());
		m.put("high", s.getHighPriceFloat());
		m.put("low", s.getLowPriceFloat());
		m.put("end", s.getEndPriceFloat());
		return m;
	}

	public static List<StatsDO> readChart(Integer id) throws IOException,
			ParseException {
		List<StatsDO> l = new ArrayList<StatsDO>();
		File f = new File(FetchUtil.FILE_USER_STATS_BASE + id + ".csv");
		if (f.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				if (StringUtils.isBlank(line)) {
					break;
				}
				String[] data = line.split(",");

				String date = data[0];
				String value = data[1];
				String change = data[2];
				String rate = data[3];

				String ayc = data[4];
				String asc = data[5];
				String nyc = data[6];
				String nsc = data[7];

				String ayv = data[8];
				String asv = data[9];
				String nyv = data[10];
				String nsv = data[11];

				String ayp = data[12];
				String asp = data[13];
				String nyp = data[14];
				String nsp = data[15];

				String ayr = data[16];
				String asr = data[17];
				String nyr = data[18];
				String nsr = data[19];

				if (StringUtils.isNotBlank(ayc)) {
					StatsDO s = new StatsDO();
					s.setDate(date);
					s.setValue(Integer.parseInt(value));
					s.setChange(Integer.parseInt(change));
					s.setvRate(Integer.parseInt(rate));

					s.setAyCount(Integer.parseInt(ayc));
					s.setAsCount(Integer.parseInt(asc));
					s.setNyCount(Integer.parseInt(nyc));
					s.setNsCount(Integer.parseInt(nsc));

					s.setAyValue(Integer.parseInt(ayv));
					s.setAsValue(Integer.parseInt(asv));
					s.setNyValue(Integer.parseInt(nyv));
					s.setNsValue(Integer.parseInt(nsv));

					s.setAyPosition(Integer.parseInt(ayp));
					s.setAsPosition(Integer.parseInt(asp));
					s.setNyPosition(Integer.parseInt(nyp));
					s.setNsPosition(Integer.parseInt(nsp));

					s.setAyRate(Integer.parseInt(ayr));
					s.setAsRate(Integer.parseInt(asr));
					s.setNyRate(Integer.parseInt(nyr));
					s.setNsRate(Integer.parseInt(nsr));

					l.add(s);
				}
			}
			br.close();
		}
		return l;
	}

	public static List<StatsDO> readIndex(Date startDate, Date endDate,
			String symbol) {
		List<StatsDO> r = new ArrayList<StatsDO>();
		List<Stock> l = Hisdata_Base.readHisDataMerge(symbol, startDate);
		Double start = Double.parseDouble(l.get(0).getEndPrice());
		for (Stock s : l) {
			StatsDO st = new StatsDO();
			// st.setDate(df.format(s.getDate()));
			Double price = Double.parseDouble(s.getEndPrice());
			st.setV(price * 100 / start - 100);
			st.setvRate((int) (price * 10000 / start - 10000));
			r.add(st);
		}
		return r;
	}

	public static void main(String args[]) throws IOException, ParseException {
		System.out.println(FetchUtil.checkWorkingDay2());
	}
}
