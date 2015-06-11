package com.taobao.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GXingDAO;
import com.taobao.finance.entity.GPublicStock;
import com.taobao.finance.entity.GUser;
import com.taobao.finance.entity.GXing;

@Service
public class GXingService extends BaseService<GXing>{

	@Autowired
	private GXingDAO gXingDAO;
	
	
	@Autowired
	public void setDao(GXingDAO gXingDAO) {
		super.setDao(gXingDAO);
	}
	public GXingDAO getDao() {
		return (GXingDAO)super.getDao();
	}
	
	public GXing queryById(Integer id){
		GXing xing=null;
		xing=this.findRecordByProperty("id", id);
		return xing;
	}
	
	
	public List<GXing> queryAll(){
		String hql="FROM GXing order by  date";
		List<GXing> l=this.getDao().findRecordsByHql(hql);
		return l;
	}
}
