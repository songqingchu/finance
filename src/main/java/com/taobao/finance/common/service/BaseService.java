package com.taobao.finance.common.service;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.taobao.finance.common.dao.BaseHibernateDao;


@Transactional
public class BaseService<T> {

	private BaseHibernateDao<T> dao;

	public void setDao(BaseHibernateDao<T> dao) {
		this.dao = dao;
	}
	
	protected BaseHibernateDao<T> getDao() {
		return this.dao;
	}


	public List<T> findAllRecords() {
		return dao.findAllRecords();
	}

	public List<T> findRecordsByProperty(String property, Object value) {
		return dao.findRecordsByProperty(property,value);
	}


	public T findRecordByProperty(String property, Object value) {
		return dao.findRecordByProperty(property,value);
	}


	public int countByProperty(String property, Object value) {
		return dao.countByProperty(property,value);
	}

	
	public T insert(T entity) {
		return dao.insert(entity);
	}


	public void saveOrUpdateAll(List<T> list) {
		dao.saveOrUpdateAll(list);
	}


	public T saveOrUpdate(T entity) {
		return dao.saveOrUpdate(entity);
	}

	public T update(T entity) {
		return dao.update(entity);
	}


	public T delete(T entity) {
		return dao.delete(entity);
	}


	public T deleteById(Serializable id) {
		return dao.deleteById(id);
	}

	public void deleteAllByIds(Serializable[] ids) {
		dao.deleteAllByIds(ids);
	}	
}