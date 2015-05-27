package com.taobao.finance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "public_stock")
public class GPublicStock {
	private Integer id;
	private String symbol;
	private String name;
	private Date addDate;
	private Date removeDate;
	private Byte hold=1;
	private String type;
	private String position;
	private Byte concern;
	
	@Transient
	public String getPosition() {
		return position;
	}
	
	@Column(name = "concern")
	public Byte getConcern() {
		return concern;
	}
	public void setConcern(Byte concern) {
		this.concern = concern;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "hold")
	public Byte getHold() {
		return hold;
	}
	public void setHold(Byte hold) {
		this.hold = hold;
	}
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	
	@Column(name = "add_date")
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
	@Column(name = "remove_date")
	public Date getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}
}
