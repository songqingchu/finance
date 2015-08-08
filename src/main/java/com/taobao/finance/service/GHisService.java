package com.taobao.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GHisDAO;
import com.taobao.finance.entity.GHis;
import com.taobao.finance.entity.GPublicStock;


@Component
public class GHisService  extends BaseService<GHis>{

	@Autowired
	private GHisDAO gHisDAO;
	
	@Autowired
	public void setDao(GHisDAO gHisDAO) {
		super.setDao(gHisDAO);
	}
	public GHisDAO getDao() {
		return (GHisDAO)super.getDao();
	}
	
	public List<GHis> queryHistory(){
		String hql="FROM GHis";
		List<GHis> l=this.getDao().findRecordsByHql(hql);
		return l;
	}

}
