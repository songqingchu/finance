package com.taobao.finance.choose;

import java.util.Date;
import java.util.List;

import com.taobao.finance.dataobject.Stock;

public interface Choose {

	/**
	 * 选择倍量
	 * @return
	 */
	public  List<Stock> chooseBV();
	
	public  List<Stock> chooseAV20();
	
	public  List<Stock> chooseChannel();
	
	/**
	 * 选择黄金柱
	 * @return
	 */
	public  List<Stock> chooseGoldenV();
	
	/**
	 * 选择价增量减
	 * @return
	 */
	public  List<Stock> choosePDVD();
	
	/**
	 * 检查连阳
	 * @return
	 */
	public  List<Stock> chooseCLY();
	
	/**
	 * 检查平台已突破
	 * @return
	 */
	public  List<Stock> choosePT();
	
	/**
	 * 检查平台待突破
	 * @return
	 */
	public  List<Stock> chooseP();
	
	public  List<Stock> chooseP_Buy();
	
	public  List<Stock> chooseAve();
	
	public  List<Stock> chooseAVCU();
	
	/**
	 * 检查连续涨停第二波
	 * @return
	 */
	public  List<Stock> chooseCB();
	
	/**
	 * 实时接口和本地接口实现
	 * @param symbol
	 * @param d
	 * @return
	 */
	public  List<Stock> prepareData(String symbol,Date d);
	
	/**
	 * 给出股票池
	 */
	public  void printAnanyseResult();
	
	/**
	 * 给出
	 */
	public  void printAnanyseResultDetail();
}
