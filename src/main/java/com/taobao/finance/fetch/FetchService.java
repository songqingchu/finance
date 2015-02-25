package com.taobao.finance.fetch;

import java.util.List;
import java.util.Map;

import com.taobao.finance.dataobject.DailyData;

public interface FetchService {

	/**
	 * 从各个网站抓取以往数据
	 * @return
	 */
	List<DailyData> fetchAgo();
	
	/**
	 * 从天天网抓取当日数据
	 * @return
	 */
	List<DailyData> fetchToday(Map<String,Boolean> codeMap);
	
	/**
	 * 获取全部股票基金名字,代码
	 * @return
	 */
	List<DailyData> fetchAllFunds();
}
