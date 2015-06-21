package com.taobao.finance.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.dao.GRecordDAO;
import com.taobao.finance.entity.GRecord;

@Component
public class GRecordService extends BaseService<GRecord>{

	@Autowired
	private GRecordDAO gRecordDAO;
	
	@Autowired
	public void setDao(GRecordDAO gRecordDAO) {
		super.setDao(gRecordDAO);
	}
	public GRecordDAO getDao() {
		return (GRecordDAO)super.getDao();
	}
	
	
	public List<GRecord> queryAll(Integer userId){
		String hql="FROM GRecord where user="+userId+" order by date asc";
		List<GRecord> l=this.getDao().findRecordsByHql(hql);
		return l;
	}
}
