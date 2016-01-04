package com.taobao.finance.dataobject;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Achieve {

	
	private String symbol;
	private String code;
	//截止日期
	private Date date;
	private String dateStr;
	//公告日期
	private Date reportDate;
	private String reportDateStr;
	//每股收益
	private String earningStr;
	private Double earning;
	//营收
	private String revenueStr;
	private Double revenue;
	//营收同比
	private String revenueTzStr;
	private Double revenueTz;
	//营收环比
	private String revenueHzStr;
	private Double revenueHz;
	//净利
	private String profitStr;
	private Double profit;
	//净利同比
    private String profitTzStr;
	private Double profitTz;
    //净利环比
	private String profitHzStr;
	
	private Double profitHz;
	//净资产
	private String assetStr;
	private Double asset;
	//净资产收益率
	private String assetRateStr;
	private Double assetRate;
	//现金流
	private String cashFlowStr;
	private Double cashFlow;
	
	
	
	public String getCashFlowStr() {
		return cashFlowStr;
	}
	public void setCashFlowStr(String cashFlowStr) {
		this.cashFlowStr = cashFlowStr;
		this.cashFlow=Double.parseDouble(cashFlowStr);
	}
	
	
	public Double getCashFlow() {
		return cashFlow;
	}
	public void setCashFlow(Double cashFlow) {
		this.cashFlow = cashFlow;
	}
	
	
	public String getSymbol() {
		if(StringUtils.isNotBlank(symbol)){
			return symbol;
		}else{
			String s="";
			if(code.startsWith("600")){
				s="sh"+code;
			}else{
				s="sz"+code;
			}
			return s;
		}
		
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCode() {
		if(StringUtils.isNotBlank(code)){
			return code;
		}else{
			String s=StringUtils.replace(symbol, "sh","");
			s=StringUtils.replace(symbol, "sz","");
			return s;
		}
		
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportDateStr() {
		return reportDateStr;
	}
	public void setReportDateStr(String reportDateStr) {
		this.reportDateStr = reportDateStr;
	}
	
	
	public String getEarningStr() {
		return earningStr;
	}
	public void setEarningStr(String earningStr) {
		this.earningStr = earningStr;
		this.earning=Double.parseDouble(earningStr);
	}
	
	public Double getEarning() {
		return earning;
	}
	public void setEarning(Double earning) {
		this.earning = earning;
	}
	
	public String getRevenueStr() {
		return revenueStr;
	}
	public void setRevenueStr(String revenueStr) {
		this.revenueStr = revenueStr;
		this.revenue=Double.parseDouble(revenueStr);
	}
	
	
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	
	
	public String getRevenueTzStr() {
		return revenueTzStr;
	}
	public void setRevenueTzStr(String revenueTzStr) {
		this.revenueTzStr = revenueTzStr;
		this.revenueTz=Double.parseDouble(revenueTzStr);
	}
	
	
	public Double getRevenueTz() {
		return revenueTz;
	}
	public void setRevenueTz(Double revenueTz) {
		this.revenueTz = revenueTz;
	}
	
	
	public String getRevenueHzStr() {
		return revenueHzStr;
	}
	public void setRevenueHzStr(String revenueHzStr) {
		this.revenueHzStr = revenueHzStr;
		this.revenueHz=Double.parseDouble(revenueHzStr);
	}
	
	
	public Double getRevenueHz() {
		return revenueHz;
	}
	public void setRevenueHz(Double revenueHz) {
		this.revenueHz = revenueHz;
	}
	
	
	public String getProfitStr() {
		return profitStr;
	}
	public void setProfitStr(String profitStr) {
		this.profitStr = profitStr;
		this.profit=Double.parseDouble(profitStr);
	}
	
	
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	
	
	public String getProfitTzStr() {
		return profitTzStr;
	}
	public void setProfitTzStr(String profitTzStr) {
		this.profitTzStr = profitTzStr;
		this.profitTz=Double.parseDouble(profitTzStr);
	}
	
	
	public Double getProfitTz() {
		return profitTz;
	}
	public void setProfitTz(Double profitTz) {
		this.profitTz = profitTz;
	}
	
	
	public String getProfitHzStr() {
		return profitHzStr;
	}
	public void setProfitHzStr(String profitHzStr) {
		this.profitHzStr = profitHzStr;
		this.profitHz=Double.parseDouble(profitHzStr);
	}
	
	
	public Double getProfitHz() {
		return profitHz;
	}
	public void setProfitHz(Double profitHz) {
		this.profitHz = profitHz;
	}
	
	
	public String getAssetStr() {
		return assetStr;
	}
	public void setAssetStr(String assetStr) {
		this.assetStr = assetStr;
		this.asset=Double.parseDouble(assetStr);
	}
	
	
	public Double getAsset() {
		return asset;
	}
	public void setAsset(Double asset) {
		this.asset = asset;
	}
	
	
	public String getAssetRateStr() {
		return assetRateStr;
	}
	public void setAssetRateStr(String assetRateStr) {
		this.assetRateStr = assetRateStr;
		this.assetRate=Double.parseDouble(assetRateStr);
	}
	
	
	public Double getAssetRate() {
		return assetRate;
	}
	public void setAssetRate(Double assetRate) {
		this.assetRate = assetRate;
	}
	
}
