package com.taobao.finance.common.service;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.taobao.finance.common.dao.BaseHibernateDao;


/**
 * Service基类(所有业务Service必须继承该类)
 * @author du 
 * @date 2012-3-30
 * @since 1.0
 * @param <T>
 */
@Transactional
public class BaseService<T> {

	private BaseHibernateDao<T> dao;

	public void setDao(BaseHibernateDao<T> dao) {
		this.dao = dao;
	}
	
	protected BaseHibernateDao<T> getDao() {
		return this.dao;
	}

	/**	
	   #################################
	   ##########   查询功能   ##########
	   #################################
	*/

	/**
	 * 取得所有数据
	 * @return
	 */
	public List<T> findAllRecords() {
		return dao.findAllRecords();
	}

	/**
	 * 根据某个属性值取得相应数据
	 * @param property	属性名称
	 * @param value		属性值
	 * @return		
	 */
	public List<T> findRecordsByProperty(String property, Object value) {
		return dao.findRecordsByProperty(property,value);
	}

	/**
	 * 根据某个属性值取得第一条数据
	 * @param property	属性名称
	 * @param value		属性值
	 * @return
	 */
	public T findRecordByProperty(String property, Object value) {
		return dao.findRecordByProperty(property,value);
	}

	/**
	 * 根据某个属性值得到相应实体数量
	 * @param property	属性名称
	 * @param value		属性值
	 * @return
	 */
	public int countByProperty(String property, Object value) {
		return dao.countByProperty(property,value);
	}

	
	
	/**	
	   #################################
	   ##########   保存功能   ##########
	   #################################
	*/

	/**
	 * 添加实体
	 * @param entity	实体
	 * @return
	 */
	public T insert(T entity) {
		return dao.insert(entity);
	}

	/**
	 * 更新或插入（多个）
	 * @param list	实体列表
	 */
	public void saveOrUpdateAll(List<T> list) {
		dao.saveOrUpdateAll(list);
	}

	/**
	 * 更新1个实体
	 * @param entity	实体
	 * @return
	 */
	public T saveOrUpdate(T entity) {
		return dao.saveOrUpdate(entity);
	}
	/**
	 * 更新某个实体
	 * @param entity	实体
	 * @return
	 */
	public T update(T entity) {
		return dao.update(entity);
	}

	/**	
	   #################################
	   ##########   删除功能   ##########
	   #################################
	*/
	
	/**
	 * 删除某个实体
	 * @param entity	实体
	 * @return
	 */
	public T delete(T entity) {
		return dao.delete(entity);
	}

	/**
	 * 删除某个实体
	 * @param id
	 * @return
	 */
	public T deleteById(Serializable id) {
		return dao.deleteById(id);
	}

	/**
	 * 根据id数组删除数据
	 * @param ids	id数组
	 */
	public void deleteAllByIds(Serializable[] ids) {
		dao.deleteAllByIds(ids);
	}	
}