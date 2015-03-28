package com.taobao.finance.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GPublicStockDAO;
import com.taobao.finance.entity.GPublicStock;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015年3月28日
 */
@Component
public class GPublicStockService extends BaseService<GPublicStock>{

	@Autowired
	private GPublicStockDAO gPublicStockDAO;
	@Autowired
	public void setDao(GPublicStockDAO gPublicStockDAO) {
		super.setDao(gPublicStockDAO);
	}
	public GPublicStockDAO getDao() {
		return (GPublicStockDAO)super.getDao();
	}
	
	
	public void add(List<GPublicStock> l){
		for(GPublicStock s:l){
			 getDao().insert(s);
		}
	}
	
	
	public List<GPublicStock> queryAll(){
		String hql="FROM GPublicStock WHERE hold=1";
		List<GPublicStock> l=this.getDao().findRecordsByHql(hql);
		return l;
	}
	
	public List<GPublicStock> queryHistory(){
		String hql="FROM GPublicStock WHERE hold=0";
		List<GPublicStock> l=this.getDao().findRecordsByHql(hql);
		return l;
	}
}
