package com.taobao.finance.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.finance.base.Hisdata_Base;
import com.taobao.finance.common.Store;

public class ChooseTask extends Thread{

	@Autowired
	public Store store;
	public ChooseTask(Store store){
		this.store=store;
	}
	public void run(){
		Date d=new Date();
		store.setDownloading(d);
		Hisdata_Base.updateDataHistoryAll();
		Hisdata_Base.updateDataHistoryDataUnFormal();
		store.setDownloaded(d);
		
		store.setChoosing(d);
		store.ananyse();
		store.setChoosed(d);
	}
}
