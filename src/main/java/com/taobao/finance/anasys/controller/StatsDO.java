package com.taobao.finance.anasys.controller;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class StatsDO {
	public static DecimalFormat f = new DecimalFormat("0.0");
	public static DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	public static DecimalFormat nf=new DecimalFormat("0.00");
	public static String SPLIT=",";
	
	public String date;
	public Integer value;
	public Integer change;
	
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
	
	
	
	public Double v=null;
	
	public Integer ayCount;
	public Integer asCount;
	public Integer nyCount;
	public Integer nsCount;
	
	public Integer ayValue;
	public Integer asValue;
	public Integer nyValue;
	public Integer nsValue;
	
	public Integer ayPosition;
	public Integer asPosition;
	public Integer nyPosition;
	public Integer nsPosition;
	
	public Integer ayRate;
	public Integer asRate;
	public Integer nyRate;
	public Integer nsRate;
	
    public Boolean last=false;
	
	public Boolean getLast() {
		return last;
	}



	public void setLast(Boolean last) {
		this.last = last;
	}



	public String toFileString(){
		String result="";
		result+=this.date+SPLIT+
				this.getValue()+SPLIT+
				this.getChange()+SPLIT+
				this.getvRate()+SPLIT+
				this.getAyCount()+SPLIT+
				this.getAsCount()+SPLIT+
				this.getNyCount()+SPLIT+
				this.getNsCount()+SPLIT+
				this.getAyValue()+SPLIT+
				this.getAsValue()+SPLIT+
				this.getNyValue()+SPLIT+
				this.getNsValue()+SPLIT+
				this.getAyPosition()+SPLIT+
				this.getAsPosition()+SPLIT+
				this.getNyPosition()+SPLIT+
				this.getNsPosition()+SPLIT+
				this.getAyRate()+SPLIT+
				this.getAsRate()+SPLIT+
				this.getNyRate()+SPLIT+
				this.getNsRate();
		return result;
	}
	
	
	
	public Integer getvRate() {
		return vRate;
	}
	public void setvRate(Integer vRate) {
		this.vRate = vRate;
	}
	public Integer getpRate() {
		Integer f=1;
		if(this.asPosition+this.nsPosition*100/this.value==0){
			return 100;
		}else{
			f=(this.ayPosition+this.nyPosition)*100/(this.asPosition+this.nsPosition);
		}
		return f;
	}
	public void setpRate(Integer pRate) {
		this.pRate = pRate;
	}
	public static DecimalFormat getF() {
		return f;
	}
	public static void setF(DecimalFormat f) {
		StatsDO.f = f;
	}
	public static DateFormat getDf() {
		return df;
	}
	public static void setDf(DateFormat df) {
		StatsDO.df = df;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getChange() {
		return change;
	}
	public void setChange(Integer change) {
		this.change = change;
	}
	public Integer getR() {
		Integer f=1;
		if(this.asValue+this.nsValue==0){
			return 1;
		}else{
			f=(this.ayValue+this.nyValue)*100/(this.asValue+this.nsValue);
		}
		return f;
	}
	public void setR(Integer r) {
		this.r = r;
	}
	public Integer getsRate() {
		Integer f=1;
		if(this.asCount+this.ayCount+this.nsCount+this.nyCount==0){
			return 5000;
		}else{
			f=(this.ayCount+this.nyCount)*10000/(this.asCount+this.ayCount+this.nsCount+this.nyCount);
		}
		return f;
	}
	public void setsRate(Integer sRate) {
		this.sRate = sRate;
	}
	public Integer getyRate() {
		Integer f=1;
		if(this.asRate+this.ayRate+this.nsRate+this.nyRate==0){
			return 0;
		}else{
			f=(this.asRate+this.ayRate+this.nsRate+this.nyRate)/(this.asCount+this.ayCount+this.nsCount+this.nyCount);
		}
		return f;
	}
	
	
	public Integer getYyRate() {
		Integer f=1;
		if(this.ayRate+this.nyRate==0){
			return 0;
		}else{
			f=(this.ayRate+this.nyRate)/(this.ayCount+this.nyCount);
		}
		return f;
	}
	public void setYyRate(Integer yyRate) {
		this.yyRate = yyRate;
	}
	public Integer getYsRate() {
		Integer f=1;
		if(this.asRate+this.nsRate==0){
			return 0;
		}else{
			f=(this.asRate+this.nsRate)/(this.asCount+this.nsCount);
		}
		return f;
	}
	public void setYsRate(Integer ysRate) {
		this.ysRate = ysRate;
	}
	public void setyRate(Integer yRate) {
		this.yRate = yRate;
	}
	public Integer getBack() {
		return back;
	}
	public void setBack(Integer back) {
		this.back = back;
	}
	public Double getV() {
		return v;
	}
	public void setV(Double v) {
		this.v = v;
	}
	public Integer getAyCount() {
		return ayCount;
	}
	public void setAyCount(Integer ayCount) {
		this.ayCount = ayCount;
	}
	public Integer getAsCount() {
		return asCount;
	}
	public void setAsCount(Integer asCount) {
		this.asCount = asCount;
	}
	public Integer getNyCount() {
		return nyCount;
	}
	public void setNyCount(Integer nyCount) {
		this.nyCount = nyCount;
	}
	public Integer getNsCount() {
		return nsCount;
	}
	public void setNsCount(Integer nsCount) {
		this.nsCount = nsCount;
	}
	public Integer getAyValue() {
		return ayValue;
	}
	public void setAyValue(Integer ayValue) {
		this.ayValue = ayValue;
	}
	public Integer getAsValue() {
		return asValue;
	}
	public void setAsValue(Integer asValue) {
		this.asValue = asValue;
	}
	public Integer getNyValue() {
		return nyValue;
	}
	public void setNyValue(Integer nyValue) {
		this.nyValue = nyValue;
	}
	public Integer getNsValue() {
		return nsValue;
	}
	public void setNsValue(Integer nsValue) {
		this.nsValue = nsValue;
	}
	public Integer getAyPosition() {
		return ayPosition;
	}
	public void setAyPosition(Integer ayPosition) {
		this.ayPosition = ayPosition;
	}
	public Integer getAsPosition() {
		return asPosition;
	}
	public void setAsPosition(Integer asPosition) {
		this.asPosition = asPosition;
	}
	public Integer getNyPosition() {
		return nyPosition;
	}
	public void setNyPosition(Integer nyPosition) {
		this.nyPosition = nyPosition;
	}
	public Integer getNsPosition() {
		return nsPosition;
	}
	public void setNsPosition(Integer nsPosition) {
		this.nsPosition = nsPosition;
	}
	
	
	
	public Integer getAyRate() {
		return ayRate;
	}
	public String getAyRateFormat() {
		return nf.format(ayRate/100F);
	}
	public void setAyRate(Integer ayRate) {
		this.ayRate = ayRate;
	}
	
	
	
	public Integer getAsRate() {
		return asRate;
	}
	public String getAsRateFormat() {
		return nf.format(asRate/100F);
	}
	public void setAsRate(Integer asRate) {
		this.asRate = asRate;
	}
	
	
	
	public Integer getNyRate() {
		return nyRate;
	}
	public String getNyRateFormat() {
		return nf.format(nyRate/100F);
	}
	public void setNyRate(Integer nyRate) {
		this.nyRate = nyRate;
	}
	
	
	public Integer getNsRate() {
		return nsRate;
	}
	public String getNsRateFormat() {
		return nf.format(nsRate/100F);
	}
	public void setNsRate(Integer nsRate) {
		this.nsRate = nsRate;
	}
}
