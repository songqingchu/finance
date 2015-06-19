package com.taobao.finance.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GStockDAO;
import com.taobao.finance.entity.GStock;


@Component
public class GStockService extends BaseService<GStock>{

	@Autowired
	private GStockDAO gStockDAO;
	
	@Autowired
	public void setDao(GStockDAO gStockDAO) {
		super.setDao(gStockDAO);
	}
	public GStockDAO getDao() {
		return (GStockDAO)super.getDao();
	}
	
	
	public List<GStock> queryAll(){
		String hql="FROM GStock";
		List<GStock> l=this.getDao().findRecordsByHql(hql);
		return l;
	}
}
