package com.taobao.finance.dataobject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyData {
	public static DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	private String fundCode;
	private String fundName;
	private String date;
	private Double valueToday;
	private Double valueAll;
	private Double buyIn;
	private Double rate;
	private Double totalRate;
	private String corp;
	
	public String getCorp() {
		return corp;
	}
	public void setCorp(String corp) {
		this.corp = corp;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getTotalRate() {
		return totalRate;
	}
	public void setTotalRate(Double totalRate) {
		this.totalRate = totalRate;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public Date getDate() {
		try {
			return format.parse(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setDate(Date date) {
		String str=format.format(date);
		this.date = str;
	}
	public Double getValueToday() {
		return valueToday;
	}
	public void setValueToday(Double valueToday) {
		this.valueToday = valueToday;
	}
	public Double getValueAll() {
		return valueAll;
	}
	public void setValueAll(Double valueAll) {
		this.valueAll = valueAll;
	}
	public Double getBuyIn() {
		return buyIn;
	}
	public void setBuyIn(Double buyIn) {
		this.buyIn = buyIn;
	}
	public String toString(){
		String s="";
		s="\t"+this.fundCode+"\t"+this.valueToday+"\t"+this.valueAll;
		if(this.buyIn==null){
			s+="\t--";
		}else{
			s+="\t"+this.buyIn;
		}
		return s+"\t"+this.fundName;
	}
	
}
