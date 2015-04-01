package com.taobao.finance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "g_task")
public class GTask {
	
	private Integer id;
	private Date date;
	private Byte download;
	private Byte choose;
	private String av5;
	private String av10;
	private String acvu;
	private String big;
	private String tp;
	private Byte working;
	private Date insDate;
	private Date upDate;
	
	
	@Column(name = "ins_tm")
	public Date getInsDate() {
		return insDate;
	}
	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}
	
	@Column(name = "update_tm")
	public Date getUpDate() {
		return upDate;
	}
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}
	public static byte NON_DOWNLOAD=(byte)0;
	public static byte DOWNLOADING=(byte)1;
	public static byte DOWNLOADED=(byte)2;
	
	
	public static byte NON_CHOOSE=(byte)0;
	public static byte CHOOSEING=(byte)1;
	public static byte CHOOSEN=(byte)2;
	
	public static byte WORKING=(byte)1;
	public static byte NON_WORKING=(byte)0;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name = "download")
	public Byte getDownload() {
		return download;
	}
	public void setDownload(Byte download) {
		this.download = download;
	}
	
	@Column(name = "choose")
	public Byte getChoose() {
		return choose;
	}
	public void setChoose(Byte choose) {
		this.choose = choose;
	}
	
	
	@Column(name = "av5")
	public String getAv5() {
		return av5;
	}
	public void setAv5(String av5) {
		this.av5 = av5;
	}
	
	@Column(name = "av10")
	public String getAv10() {
		return av10;
	}
	public void setAv10(String av10) {
		this.av10 = av10;
	}
	
	
	@Column(name = "acvu")
	public String getAcvu() {
		return acvu;
	}
	public void setAcvu(String acvu) {
		this.acvu = acvu;
	}
	
	@Column(name = "big")
	public String getBig() {
		return big;
	}
	public void setBig(String big) {
		this.big = big;
	}
	
	@Column(name = "tp")
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	
	
	@Column(name = "working")
	public Byte getWorking() {
		return working;
	}
	public void setWorking(Byte working) {
		this.working = working;
	}
}
