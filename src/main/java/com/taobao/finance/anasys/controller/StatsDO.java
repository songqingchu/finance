package com.taobao.finance.anasys.controller;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class StatsDO {
	public static DecimalFormat f = new DecimalFormat("0.0");
	public static DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	
	public String date;
	public Double value;
	public Integer r=1;
	public Integer sRate=0;
	public Integer yRate=0;
	public Integer back=0;
	public Double v=null;
	
	public Integer yCount;
	public Integer sCount;
	
	public Double aY;
	public Double aS;
	public Double nY;
	public Double nS;
	
	
	public static DecimalFormat getF() {
		return f;
	}
	public static void setF(DecimalFormat f) {
		StatsDO.f = f;
	}
	public Integer getyCount() {
		return yCount;
	}
	public void setyCount(Integer yCount) {
		this.yCount = yCount;
	}
	public Integer getsCount() {
		return sCount;
	}
	public void setsCount(Integer sCount) {
		this.sCount = sCount;
	}
	public Double getaY() {
		return aY;
	}
	public void setaY(Double aY) {
		this.aY = aY;
	}
	public Double getaS() {
		return aS;
	}
	public void setaS(Double aS) {
		this.aS = aS;
	}
	public Double getnY() {
		return nY;
	}
	public void setnY(Double nY) {
		this.nY = nY;
	}
	public Double getnS() {
		return nS;
	}
	public void setnS(Double nS) {
		this.nS = nS;
	}
	public Double getV() {
		return v;
	}
	public void setV(Double v) {
		String s=f.format(v);
		this.v = Double.parseDouble(s);
	}
	public Integer getR() {
		return r;
	}
	public void setR(Integer r) {
		this.r = r;
	}
	public Integer getsRate() {
		return sRate;
	}
	public void setsRate(Integer sRate) {
		this.sRate = sRate;
	}
	public Integer getyRate() {
		return yRate;
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
