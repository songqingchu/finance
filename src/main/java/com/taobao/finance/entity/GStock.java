package com.taobao.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "g_stock_2")
public class GStock {
	private String symbol;
	private String name;
	private Integer holder;
	private Integer change;
	private String record;
	private Integer id;
	private String liuTong;
	
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name = "record")
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	
	@Column(name = "liutong")
	public String getLiuTong() {
		return liuTong;
	}
	public void setLiuTong(String liuTong) {
		this.liuTong = liuTong;
	}

	@Column(name = "symbol")
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "holder")
	public Integer getHolder() {
		return holder;
	}
	public void setHolder(Integer holder) {
		this.holder = holder;
	}
	
	@Column(name = "modify")
	public Integer getChange() {
		return change;
	}
	public void setChange(Integer change) {
		this.change = change;
	}
}
