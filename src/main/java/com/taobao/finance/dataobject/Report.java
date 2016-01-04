package com.taobao.finance.dataobject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report implements Comparable<Report>{

	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private int type;
	private int pingNum;
	private String title;
	private String link;
	private Date date;
	private String dateString;
	private String html;
	
	
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Date getDate() {
		return date;
	}
	
	public String getDateFormat() {
		return df.format(date);
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public int getPingNum() {
		return pingNum;
	}
	public void setPingNum(int pingNum) {
		this.pingNum = pingNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public int compareTo(Report o) {
		if(this.getDate().after(o.getDate())){
			return -1;
		}else{
			return 1;
		}
	}
}
