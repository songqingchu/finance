package com.taobao.finance.choose;

import java.util.Date;
import java.util.List;

import com.taobao.finance.dataobject.Stock;


public interface Choose {

	/**
	 * ѡ����
	 * @return
	 */
	public  List<Stock> chooseBV();
	
	public  List<Stock> chooseAV20();
	
	public  List<Stock> chooseChannel();
	
	/**
	 * ѡ��ƽ���
	 * @return
	 */
	public  List<Stock> chooseGoldenV();
	
	/**
	 * ѡ���������
	 * @return
	 */
	public  List<Stock> choosePDVD();
	
	/**
	 * �������
	 * @return
	 */
	public  List<Stock> chooseCLY();
	
	/**
	 * ���ƽ̨��ͻ��
	 * @return
	 */
	public  List<Stock> choosePT();
	
	/**
	 * ���ƽ̨��ͻ��
	 * @return
	 */
	public  List<Stock> chooseP();
	
	public  List<Stock> chooseP_Buy();
	
	public  List<Stock> chooseAve();
	
	public  List<Stock> chooseAVCU();
	
	/**
	 * ���������ͣ�ڶ���
	 * @return
	 */
	public  List<Stock> chooseCB();
	
	/**
	 * ʵʱ�ӿںͱ��ؽӿ�ʵ��
	 * @param symbol
	 * @param d
	 * @return
	 */
	public  List<Stock> prepareData(String symbol,Date d);
	
	/**
	 * ������Ʊ��
	 */
	public  void printAnanyseResult();
	
	/**
	 * ����
	 */
	public  void printAnanyseResultDetail();
}
