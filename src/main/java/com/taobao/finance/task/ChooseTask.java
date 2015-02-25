package com.taobao.finance.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.common.Store;
import com.taobao.finance.util.FetchUtil;

public class ChooseTask extends Thread{

	@Autowired
	public Store store;
	public ChooseTask(Store store){
		this.store=store;
	}
	public void run(){
		
		Boolean workingDay=FetchUtil.checkWorkingDay();
		store.workingDay=workingDay;
		/**
		 * 下载
		 */
		if(workingDay){
			Date d=new Date();
			store.setDownloading(d);
			store.updateHistory();
			store.updateTmp();
			store.setDownloaded(d);
			
			/**
			 * 分析
			 */
			store.setChoosing(d);
			store.ananyse();
			store.setChoosed(d);
		}
	}
}
