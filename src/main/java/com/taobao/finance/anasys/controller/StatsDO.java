package com.taobao.finance.anasys.controller;

import java.text.DecimalFormat;
import java.util.Date;

public class StatsDO {
	public static DecimalFormat f = new DecimalFormat("0.0");
	
	public String date;
	public Double value;
	public Double r=1D;
	public Double rate=null;
	public Double v=null;
	
	public Double getV() {
		return v;
	}
	public void setV(Double v) {
		String s=f.format(v);
		this.v = Double.parseDouble(s);
	}
	public Double getR() {
		return r;
	}
	public void setR(Double r) {
		this.r = r;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
}
