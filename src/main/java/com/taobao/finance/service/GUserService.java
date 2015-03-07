package com.taobao.finance.service;

import org.springframework.stereotype.Component;

import com.taobao.finance.common.service.BaseService;
import com.taobao.finance.entity.GUser;

@Component
public class GUserService extends BaseService<GUser>{

	public GUser queryByName(String name){
		GUser user=null;
		user=this.findRecordByProperty("userName", name);
		return user;
	}
}
