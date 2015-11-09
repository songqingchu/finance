package com.taobao.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GHisDAO;
import com.taobao.finance.dao.GProxyDAO;
import com.taobao.finance.entity.GHis;
import com.taobao.finance.entity.Proxy;

@Component
public class GProxyService  extends BaseService<Proxy>{

	@Autowired
	private GProxyDAO gProxyDAO;
	
	@Autowired
	public void setDao(GProxyDAO gProxyDAO) {
		super.setDao(gProxyDAO);
	}
	public GProxyDAO getDao() {
		return (GProxyDAO)super.getDao();
	}
	
	public List<Proxy> queryHistory(){
		String sql="select * FROM g_proxy_server";
		List<Proxy> l=this.getDao().findRecordsBySql(sql);
		return l;
	}

}
