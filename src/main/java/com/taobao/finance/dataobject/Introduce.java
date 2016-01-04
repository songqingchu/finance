package com.taobao.finance.dataobject;

import org.apache.commons.lang3.StringUtils;

public class Introduce {

	private String symbol;
	private String province;
	private String address;
	private String introduce;
	private String range;
	private String field;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public String getText(){
		String content="";
		content=content+StringUtils.trim("上市名称："+this.getName())+"\n";
		content=content+StringUtils.trim("所在省份："+this.getProvince())+"\n";
		content=content+StringUtils.trim("行业领域："+this.getField())+"\n\n";
/*		content=content+StringUtils.trim("经营范围："+this.getRange())+"\n\n";
		content=content+StringUtils.trim("公司简介："+this.getIntroduce())+"\n\n\n";*/
		return content;
	}
}
