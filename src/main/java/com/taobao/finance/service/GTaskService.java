package com.taobao.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GTaskDAO;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GTask;


@Component
public class GTaskService extends BaseService<GTask>{

	@Autowired
	private GTaskDAO gTaskDAO;
	
	@Autowired
	public void setDao(GTaskDAO gTaskDAO) {
		super.setDao(gTaskDAO);
	}
	public GTaskDAO getDao() {
		return (GTaskDAO)super.getDao();
	}
	
	
	public GTask queryLastTask(){
		String hql="FROM GTask order by date desc limit 1";
		GTask l=this.getDao().findRecordByHql(hql);
		return l;
	}
	
	public GTask queryLast2Task(){
		String hql="select * FROM g_task order by date desc limit 2";
		List<GTask> l=this.getDao().findRecordsBySql(hql,new Object[]{});
		if(l.size()==2){
			return l.get(1);
		}
		return null;
	}
	

}
