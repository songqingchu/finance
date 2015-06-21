package com.taobao.finance.entity;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.taobao.finance.util.FetchUtil;

@Entity
@Table(name = "g_record")
public class GRecord {
	
	public static DecimalFormat nf=new DecimalFormat("0.00");
	 public Boolean last=false;
	 
	@Transient
	public Boolean getLast() {
		return last;
	}
	public void setLast(Boolean last) {
		this.last = last;
	}

		//增长率
		public Integer vRate;
		//胜率
		public Integer sRate=0;
		//损益比
		public Integer r=1;
		//仓位率
		public Integer pRate;
	    //平均盈利率
		public Integer yRate=0;
		//平均盈利率(盈利)
		public Integer yyRate;
		//平均盈利率(损失)
		public Integer ysRate;
		//最大回撤
		public Integer back=0;
		
		
		@Transient
		public Integer getR() {
			Integer f=1;
			if(this.asv+this.nsv==0){
				return 1;
			}else{
				f=(this.ayv+this.nyv)*100/(this.asv+this.nsv);
			}
			return f;
		}
		public void setR(Integer r) {
			this.r = r;
		}
		
		@Transient
		public Integer getsRate() {
			Integer f=1;
			if(this.asc+this.ayc+this.nsc+this.nyc==0){
				return 5000;
			}else{
				f=(this.ayc+this.nyc)*10000/(this.asc+this.ayc+this.nsc+this.nyc);
			}
			return f;
		}
		public void setsRate(Integer sRate) {
			this.sRate = sRate;
		}
		@Transient
		public Integer getyRate() {
			Integer f=1;
			if(this.asr+this.ayr+this.nsr+this.nyr==0){
				return 0;
			}else{
				f=(this.asr+this.ayr+this.nsr+this.nyr)/(this.asc+this.ayc+this.nsc+this.nyc);
			}
			return f;
		}
		
		@Transient
		public Integer getYyRate() {
			Integer f=1;
			if(this.ayc+this.nyc==0){
				return 0;
			}
			if(this.ayr+this.nyr==0){
				return 0;
			}else{
				f=(this.ayr+this.nyr)/(this.ayc+this.nyc);
			}
			return f;
		}
		public void setYyRate(Integer yyRate) {
			this.yyRate = yyRate;
		}
		@Transient
		public Integer getYsRate() {
			Integer f=1;
			if(this.asc+this.nsc==0){
				return 0;
			}
			if(this.asr+this.nsr==0){
				return 0;
			}else{
				f=(this.asr+this.nsr)/(this.asc+this.nsc);
			}
			return f;
		}
		@Transient
		public Integer getpRate() {
			Integer f=1;
			if(this.asp+this.nsp*100/this.money==0){
				return 100;
			}else{
				f=(this.ayp+this.nyp)*100/(this.asp+this.nsp);
			}
			return f;
		}
		
	private Integer id;
	private Integer rate;
	private Integer user;
	
	@Column(name = "user")
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	@Column(name = "rate")
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	private Date date;
	private Integer money;
	private Integer modi;
	private Integer ayc;
	private Integer asc;
	private Integer nyc;
	private Integer nsc;
	private Integer ayv;
	private Integer asv;
	private Integer nyv;
	private Integer nsv;
	private Integer ayp;
	private Integer asp;
	private Integer nyp;
	private Integer nsp;
	private Integer ayr;
	private Integer asr;
	private Integer nyr;
	private Integer nsr;
	
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
	
	@Transient
	public String getDateFormat() {
		return FetchUtil.FILE_FORMAT.format(date);
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name = "money")
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	
	@Column(name = "modi")
	public Integer getModi() {
		return modi;
	}
	public void setModi(Integer modi) {
		this.modi = modi;
	}
	
	@Column(name = "ayc")
	public Integer getAyc() {
		return ayc;
	}
	public void setAyc(Integer ayc) {
		this.ayc = ayc;
	}
	
	@Column(name = "as_count")
	public Integer getAsc() {
		return asc;
	}
	public void setAsc(Integer asc) {
		this.asc = asc;
	}
	
	@Column(name = "nyc")
	public Integer getNyc() {
		return nyc;
	}
	public void setNyc(Integer nyc) {
		this.nyc = nyc;
	}
	
	@Column(name = "nsc")
	public Integer getNsc() {
		return nsc;
	}
	public void setNsc(Integer nsc) {
		this.nsc = nsc;
	}
	
	@Column(name = "ayv")
	public Integer getAyv() {
		return ayv;
	}
	public void setAyv(Integer ayv) {
		this.ayv = ayv;
	}
	
	@Column(name = "asv")
	public Integer getAsv() {
		return asv;
	}
	public void setAsv(Integer asv) {
		this.asv = asv;
	}
	
	@Column(name = "nyv")
	public Integer getNyv() {
		return nyv;
	}
	public void setNyv(Integer nyv) {
		this.nyv = nyv;
	}
	
	@Column(name = "nsv")
	public Integer getNsv() {
		return nsv;
	}
	public void setNsv(Integer nsv) {
		this.nsv = nsv;
	}
	
	@Column(name = "ayp")
	public Integer getAyp() {
		return ayp;
	}
	public void setAyp(Integer ayp) {
		this.ayp = ayp;
	}
	
	@Column(name = "asp")
	public Integer getAsp() {
		return asp;
	}
	public void setAsp(Integer asp) {
		this.asp = asp;
	}
	
	@Column(name = "nyp")
	public Integer getNyp() {
		return nyp;
	}
	public void setNyp(Integer nyp) {
		this.nyp = nyp;
	}
	
	@Column(name = "nsp")
	public Integer getNsp() {
		return nsp;
	}
	public void setNsp(Integer nsp) {
		this.nsp = nsp;
	}
	
	@Column(name = "ayr")
	public Integer getAyr() {
		return ayr;
	}
	@Transient
	public String getAyRateFormat() {
		return nf.format(ayr/100F);
	}
	public void setAyr(Integer ayr) {
		this.ayr = ayr;
	}
	
	@Column(name = "asr")
	public Integer getAsr() {
		return asr;
	}
	@Transient
	public String getAsRateFormat() {
		return nf.format(asr/100F);
	}
	public void setAsr(Integer asr) {
		this.asr = asr;
	}
	
	@Column(name = "nyr")
	public Integer getNyr() {
		return nyr;
	}
	@Transient
	public String getNyRateFormat() {
		return nf.format(nyr/100F);
	}
	public void setNyr(Integer nyr) {
		this.nyr = nyr;
	}
	
	@Column(name = "nsr")
	public Integer getNsr() {
		return nsr;
	}
	@Transient
	public String getNsRateFormat() {
		return nf.format(nsr/100F);
	}
	public void setNsr(Integer nsr) {
		this.nsr = nsr;
	}
}
