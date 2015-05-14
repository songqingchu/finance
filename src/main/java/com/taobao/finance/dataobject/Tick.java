package com.taobao.finance.dataobject;

import java.util.Date;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015Äê5ÔÂ14ÈÕ
 */
public class Tick {

	private String timeStr;
	private Date time;
	private Float price;
	private String priceStr;
	private Float rate;
	private String rateStr;
	private Float d;
	private String dStr;
	private String symbol;
	private String code;
	private Integer num;
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getPriceStr() {
		return priceStr;
	}
	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
		this.price=Float.parseFloat(priceStr);
		
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public String getRateStr() {
		return rateStr;
	}
	public void setRateStr(String rateStr) {
		this.rateStr = rateStr;
		this.rate=Float.parseFloat(rateStr);
	}
	public Float getD() {
		return d;
	}
	public void setD(Float d) {
		this.d = d;
	}
	public String getdStr() {
		return dStr;
	}
	public void setdStr(String dStr) {
		this.dStr = dStr;
		this.d=Float.parseFloat(dStr);
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
		String code=symbol.replace("sh", "");
		code=code.replace("sz", "");
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
