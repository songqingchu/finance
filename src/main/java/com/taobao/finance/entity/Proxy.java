package com.taobao.finance.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "g_proxy")
public class Proxy implements Comparable<Proxy> ,Serializable{

	public static DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
	private Integer id;
	private String ip;
	private Integer port;
	private String location;
	private Long lastTtl;
	private Long avgTtl;
	private Integer rank=0;
	private Date includeDate;
	private Date addDate;
	private Date delDate;
	
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "add_date")
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	@Column(name = "del_date")
	public Date getDelDate() {
		return delDate;
	}
	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}
	
	@Column(name = "ip")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "port")
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	@Column(name = "location")
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Column(name = "last_ttl")
	public Long getLastTtl() {
		return lastTtl;
	}
	public void setLastTtl(Long lastTtl) {
		this.lastTtl = lastTtl;
	}
	
	@Column(name = "avg_tt")
	public Long getAvgTtl() {
		return avgTtl;
	}
	public void setAvgTtl(Long avgTtl) {
		this.avgTtl = avgTtl;
	}
	
	@Column(name = "rank")
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	@Column(name = "include_date")
	public Date getIncludeDate() {
		return includeDate;
	}
	public void setIncludeDate(Date includeDate) {
		this.includeDate = includeDate;
	}
	
	public String toString(){
		String ipStr=ip;
		if(ip.length()<15){
			for(int i=0;i<15-ip.length();i++){
				ipStr=ipStr+" ";
			}
		}
		String portString=port.toString();
		if(portString.length()<5){
			for(int i=0;i<5-port.toString().length();i++){
				portString=portString+" ";
			}
		}
		String r="";
		r="地址："+ipStr+", 端口:"+portString+"， 收录时间："+df.format(includeDate)+",  ttl:"+lastTtl;
		return r;
	}
	@Override
	public int compareTo(Proxy o) {
		// TODO Auto-generated method stub
		return (int)(this.getLastTtl()-o.getLastTtl());
	}
}