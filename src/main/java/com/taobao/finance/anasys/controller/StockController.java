package com.taobao.finance.anasys.controller;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.common.Store;
import com.taobao.finance.util.FetchUtil;


@Controller
public class StockController {
	private static final Logger logger = Logger.getLogger("fileLogger");
	public static Map<String,Boolean> download=new HashMap<String,Boolean>();
	public static DateFormat DF = new SimpleDateFormat("yyyy.MM.dd");
	@Autowired
	private Store store;
	
	@RequestMapping(value = "/getToday.do", method = RequestMethod.GET)
	public String stats(HttpServletRequest request,@RequestParam Boolean force) {
		boolean workday=FetchUtil.checkWorkingDay();
		if(!workday){
			final Date d=new Date();
			Integer status=store.getDownloadStatus(d);
			if(status!=null){
				if(force!=null&&force==true&&status!=1){
					store.setDownloading(d);
					new Thread(){
						public void run(){
							Hisdata_Base.updateDataHistoryAll();
							Hisdata_Base.updateDataHistoryDataUnFormal();
							store.setDownloaded(d);
						}
					}.start();
					request.setAttribute("message", "开始下载！");
					return "choose";
				}
				
				if(status==1){
					request.setAttribute("message", "正在下载中！");
					return "download";
				}
				if(status==2){
					request.setAttribute("message", "今日数据已经下载完成！");
					return "download";
				}
			}else{
				store.setDownloading(d);
				new Thread(){
					public void run(){
						Hisdata_Base.updateDataHistoryAll();
						Hisdata_Base.updateDataHistoryDataUnFormal();
						store.setDownloaded(d);
					}
				}.start();
				request.setAttribute("message", "开始下载！");
			}
		}else{
			request.setAttribute("workday", workday);
			request.setAttribute("message", "非工作日不用下载！");
		}
		return "download";
	}
	
	
	@RequestMapping(value = "/ananyse.do", method = RequestMethod.GET)
	public String choose(HttpServletRequest request,@RequestParam Boolean force) {
		final Date d=new Date();
		Integer status=store.getChooseStatus(d);
		if(status!=null){
			if(force!=null&&force==true&&status!=1){
				store.setChoosing(d);
				new Thread(){
					public void run(){
						store.ananyse();
						store.setChoosed(d);
					}
				}.start();
				request.setAttribute("message", "开始分析！");
				return "choose";
			}
			if(status==1){
				request.setAttribute("message", "正在分析中！");
				return "choose";
			}
			if(status==2){
				request.setAttribute("message", "今日数据已经分析完成！");
				return "choose";
			}
		}else{
			store.setChoosing(d);
			new Thread(){
				public void run(){
					store.ananyse();
					store.setChoosed(d);
				}
			}.start();
			request.setAttribute("message", "开始分析！");
			
		}
		return "choose";
	}
}
