package com.taobao.finance.choose;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.fetch.stock.Fetch_AllStock;
import com.taobao.finance.util.CheckUtil;

public class Base_Choose implements Choose{

	
	@Override
	public  List<Stock> chooseBV(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			//System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkBV(history.get(history.size()-2),history.get(history.size()-1));
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		
		Collections.sort(l,new Comparator<Stock>(){
			public int compare(Stock v1,Stock v2){
				if(v1.getVrate()-v2.getVrate()>0){
					return -1;
				}else{
					return 1;
				}
			}
		});
		
		return l;
	}
	
	
	
	public  List<Stock> chooseChannel(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			if(s.getCode().equals("002382")){
				s.getCode();
			}
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkChannel(history);
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		
		return l;
	}
	
	@Override
	public  List<Stock> choosePT(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkPT(history);
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		
		return l;
	}
	
	@Override
	public  List<Stock> chooseAve(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkAve(history);
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		
		return l;
	}
	
	@Override
	public  List<Stock> chooseAVCU(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkAVCU(history);
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		
		return l;
	}
	
	@Override
	public  List<Stock> chooseAV20(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkAV20(history,1.20F,15,13,14);
			if (match) {
				l.add(s);
			}
			i++;
		}
		
		return l;
	}
	
	
	@Override
	public  List<Stock> chooseP(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkP(history);
			if (match) {
				s.setVrate(history.get(history.size()-1).getVrate());
				l.add(s);
				
			}
			i++;
		}
		return l;
	}
	
	
	@Override
	public  List<Stock> chooseP_Buy(){
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			List<Stock> history = prepareData(s.getSymbol(), null);
			if(history==null){
				continue;
			}
			if(history.size()<2){
				continue;
			}
			boolean match = CheckUtil.checkP_Buy(history);
			if (match) {
				l.add(s);
			}
			history.remove(history.size()-1);
			match = CheckUtil.checkP_Buy(history);
			if (match) {
				l.add(s);
			}
			history.remove(history.size()-1);
			match = CheckUtil.checkP_Buy(history);
			if (match) {
				l.add(s);
			}
			
			i++;
		}
		return l;
	}
	
	@Override
	public List<Stock> chooseGoldenV() {
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
		Map<Stock, List<Date>> m = new HashMap<Stock, List<Date>>();
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			String symbol = s.getSymbol();
			if(s.getCode().equals("000519")){
				l.size();
			}
			List<Stock> history=Hisdata_Base.readHisDataMerge(symbol, null);
			if(history==null){
				continue;
			}
			if(history.size()<13){
				continue;
			}
			boolean match = CheckUtil.checkGoldenV(history);
			if (match) {
				l.add(s);	
			}
			i++;
		}
		return l;
	}
	

	@Override
	public List<Stock> choosePDVD() {
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			String symbol = s.getSymbol();
			List<Stock> history=prepareData(symbol, null);
			if(history==null){
				continue;
			}
			if(history.size()<13){
				continue;
			}
			boolean match = CheckUtil.checkPDVD(history);
			if (match) {
				l.add(s);	
			}
			i++;
		}
		return l;
	}


	@Override
	public List<Stock> prepareData(String symbol,Date d) {
		return Hisdata_Base.readHisDataMerge(symbol, d);
	}

	
	public  void  printSymbol(List<Stock> l){
		for (Stock st : l) {
			System.out.println(st.getCode());
		}
	}
	
    public  void  printDetails(List<Stock> l){
    	for (Stock st : l) {
			System.out.println(st.getName()+"   "+st.getVrate());
		}
	}

	@Override
	public void printAnanyseResult() {
		List<Stock> l=chooseBV();
		System.out.print("\n\n\n\n\n");
		for (Stock st : l) {
			System.out.println(st.getCode());
		}
		System.out.print("\n共计："+l.size());
	}

	@Override
	public void printAnanyseResultDetail() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Stock> chooseCLY() {
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			String symbol = s.getSymbol();
			List<Stock> history=prepareData(symbol, null);
			if(history==null){
				continue;
			}
			if(history.size()<13){
				continue;
			}
			boolean match = CheckUtil.checkCLY(history);
			if (match) {
				l.add(s);	
			}
			i++;
		}
		return l;
	}
	
	
	public List<Stock> chooseCB() {
		Map<String, Stock> allMap = Fetch_AllStock.map;
		int i = 1;
        List<Stock> l=new ArrayList<Stock>();
		Collection<Stock> ss = allMap.values();
		for (Stock s : ss) {
			System.out.println("处理：" + i);
			String symbol = s.getSymbol();
			List<Stock> history=prepareData(symbol, null);
			if(history==null){
				continue;
			}
			if(history.size()<13){
				continue;
			}
			boolean match = CheckUtil.checkCB(history);
			if (match) {
				l.add(s);	
			}
			i++;
		}
		return l;
	}



}
