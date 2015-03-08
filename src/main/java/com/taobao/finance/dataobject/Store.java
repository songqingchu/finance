package com.taobao.finance.dataobject;

public class Store {

	private String name;
	private int times;
	private Float all;
	private String code;
	private Float sell;
	private Float pureBuy;
	
	
	public String toString(){
	    return name+"\n"+times+"\n"+all+"\n"+code+"\n"+sell+"\n"+pureBuy+"\n";
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Float getAll() {
		return all;
	}
	public void setAll(Float all) {
		this.all = all;
	}
	private Float buy;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public Float getBuy() {
		return buy;
	}
	public void setBuy(Float buy) {
		this.buy = buy;
	}
	public Float getSell() {
		return sell;
	}
	public void setSell(Float sell) {
		this.sell = sell;
	}
	public Float getPureBuy() {
		return pureBuy;
	}
	public void setPureBuy(Float pureBuy) {
		this.pureBuy = pureBuy;
	}
	
}
