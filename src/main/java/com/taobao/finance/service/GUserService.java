package com.taobao.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GUserDAO;
import com.taobao.finance.entity.GUser;


@Component
public class GUserService extends BaseService<GUser>{

	@Autowired
	private GUserDAO gUserDAO;
	@Autowired
	public void setDao(GUserDAO gUserDAO) {
		super.setDao(gUserDAO);
	}
	public GUserDAO getDao() {
		return (GUserDAO)super.getDao();
	}
	
	
	public GUser queryByName(String name){
		GUser user=null;
		user=this.findRecordByProperty("userName", name);
		return user;
	}
}
