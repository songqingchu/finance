package com.taobao.finance.fetch;

import java.util.List;
import java.util.Map;

import com.taobao.finance.dataobject.DailyData;

public interface FetchService {

	/**
	 * �Ӹ�����վץȡ��������
	 * @return
	 */
	List<DailyData> fetchAgo();
	
	/**
	 * ��������ץȡ��������
	 * @return
	 */
	List<DailyData> fetchToday(Map<String,Boolean> codeMap);
	
	/**
	 * ��ȡȫ����Ʊ��������,����
	 * @return
	 */
	List<DailyData> fetchAllFunds();
}
