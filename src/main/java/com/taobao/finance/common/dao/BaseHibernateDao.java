package com.taobao.finance.common.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.finance.util.SystemUtil;


/**
 * Dao基类(所有业务Dao必须继承该类)
 * @author 	堵成杰
 * @since	2.0
 * 
 */
public class BaseHibernateDao<T>{

	private Class<T> entityClass;

	@Autowired
	private SessionFactory sessionFactory;
	
	public BaseHibernateDao() {
		entityClass = SystemUtil.getClassGenricType(getClass());
	}
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	public Session openSession() {
		return sessionFactory.openSession();
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
		return findByCriteria();
	}

	/**
	 * 根据ID得到唯一实体
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Class<T> c, Serializable id) {
		return (T) getSession().get(c, id);
	}
	
	/**
	 * 根据某个属性值取得相应数据
	 * @param property	属性名称
	 * @param value		属性值
	 * @return		
	 */
	@SuppressWarnings("unchecked")
	public List<T> findRecordsByProperty(String property, Object value) {
		return createCriteria(Restrictions.eq(property, value)).list();
	}

	/**
	 * 根据某个属性值取得第一条数据
	 * @param property	属性名称
	 * @param value		属性值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findRecordByProperty(String property, Object value) {
		
		List<T> list = createCriteria(Restrictions.eq(property, value)).list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据hql取得数据
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findRecordsByHql(String hql, Object... values) {
		return createQuery(hql, values).list();
	}
	
	/**
	 * 根据hql取得数据 (lik)
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findRecordsByHql(Class<E> dtoClass, String hql, Object... values) {
		return createQuery(hql, values).setResultTransformer(new AliasToBeanResultTransformer(dtoClass)).list();
	}
	
	
	/**
	 * 根据hql取得Dto数据
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findDtoRecordsByHql(Class<E> dtoClass, String hql, Object... values) {
		
		return (List<E>) createQuery(hql, values).setResultTransformer(new AliasToBeanResultTransformer(dtoClass)).list();
	}

	/**
	 * 根据sql取得DTO数据
	 * @param dtoClass
	 * @param sql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findDtoRecordsBySql(Class<E> dtoClass, String sql, Object... values){
		
		return (List<E>)createSQLQuery(sql, values).setResultTransformer(new AliasToBeanResultTransformer(dtoClass)).list();
	}
	
	/**
	 *  
	 * 据hql取得数据  (DTO分页显示)
	 * @Title: findDtoRecordsByHql   
	 * @Description:   
	 * @param @param <E>
	 * @param @param dtoClass  class
	 * @param @param hql   hql
	 * @param @param offset 开始取数据的下标
	 * @param @param length 读取数据记录数
	 * @param @param values 参数数组
	 * @param @return      
	 * @return List<E>     
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findDtoRecordsByHql(Class<E> dtoClass, String hql,int offset,int length, Object... values) {
			return createQuery(hql, values).setResultTransformer(new AliasToBeanResultTransformer(dtoClass)).setFirstResult(offset).setMaxResults(length).list();
	}
	/**
	 * 根据hql取得数据
	 * @param hql		hql
	 * @param offset	开始取数据的下标
	 * @param length	读取数据记录数
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findRecordsByHql(String hql,int offset,int length, Object... values) {
		return createQuery(hql, values).setFirstResult(offset).setMaxResults(length).list();
	}
	/**
	 * 根据hql取得第一条数据
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	public T findRecordByHql(String hql,Object...values) {
		List<T> list = findRecordsByHql(hql,values);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据hql取得第一条数据 (lik)
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	public <E> E findRecordByHql(Class<E> dtoClass, String hql,Object...values) {
		List<E> list = findRecordsByHql(dtoClass, hql,values);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据sql取得数据
	 * @param sql		sql
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findRecordsBySql(String sql, Object... values) {
		return createSQLQuery(sql, values).addEntity(entityClass).list();
	}
	
	/**
	 * 根据Sql取得第一条数据
	 * @param Sql		Sql
	 * @param values	参数数组
	 * @return
	 */
	public T findRecordBySql(String hql,Object...values) {
		List<T> list = findRecordsBySql(hql,values);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据sql取得数据 (lik)
	 * @param className
	 * @param sql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findRecordsBySql(Class<E> className, String sql, Object... values) {
		return createSQLQuery(sql, values).setResultTransformer(className==null?Transformers.ALIAS_TO_ENTITY_MAP:Transformers.aliasToBean(className)).list();
	}
	
	/**
	 * 根据sql取得数据 (lik)
	 * @param sql		sql
	 * @param values	参数数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findRecordsBySql(Class<E> className,String sql, int offset,int pageSize,Object...params) {
		return createSQLQuery(sql,params).setResultTransformer(className==null?Transformers.ALIAS_TO_ENTITY_MAP:Transformers.aliasToBean(className)).setFirstResult(offset).setMaxResults(pageSize).list();
	}

	/**
	 * 根据某个属性值得到相应实体数量
	 * @param property	属性名称
	 * @param value		属性值
	 * @return
	 */
	public int countByProperty(String property, Object value) {
		return ((Number) (createCriteria(Restrictions.eq(property, value))
				.setProjection(Projections.rowCount()).uniqueResult()))
				.intValue();
	}

	/**
	 * 根据HQL取得实体数量
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	public int countByHql(String hql,Object...values) {
		List<?> list = createQuery(hql, values).list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return ((Long) list.get(0)).intValue();
		} else
			return 0;
	}
	
	/**
	 * 根据SQL取得实体数量
	 * @param sql		sql
	 * @param values	参数数组
	 * @return
	 */
	public int countBySql(String sql) {
		Object obj = this.getSession().createSQLQuery(sql).list().get(0);
		if(obj instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal)obj;
			return bd.intValue();
		}
		return ((BigInteger) this.getSession().createSQLQuery(sql).list().get(0)).intValue();
	}
	
	/**
	 * 根据SQL取得实体数量
	 * @param sql		sql
	 * @param values	参数数组
	 * @return
	 */
	public int countBySql(String sql, Object...values) {
		return ((BigInteger) createSQLQuery(sql,values).list().get(0)). intValue();
	}

	/**
	 * 通过动态查询条件进行查询
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	/**
	 * 创建Query对象
	 * @param queryString	查询字符串
	 * @param values		参数数组
	 * @return
	 */
	public Query createQuery(String queryString, Object... values) {
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 创建SQLQuery对象
	 * @param queryString	查询字符串
	 * @param values		参数数组
	 * @return
	 */
	public SQLQuery createSQLQuery(String queryString, Object... values) {
		SQLQuery queryObject = getSession().createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 创建检索条件
	 * @param criterion
	 * @return
	 */
	protected Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
	/**	
	   #################################
	   ##########   保存功能   ##########
	   #################################
	*/

	/**
	 * 根据ID加载实体
	 * @param id
	 * @return
	 */
	public T load(Serializable id) {
		return load(id, false);
	}
	
	/**
	 * 根据ID加载实体
	 * @param id	id
	 * @param lock	是否上锁
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public T load(Serializable id, boolean lock) {
		T entity = null;
		if (lock) {
			entity = (T) getSession().load(entityClass, id,
					LockMode.UPGRADE);
		} else {
			entity = (T) getSession().load(entityClass, id);
		}
		return entity;
	}

	/**
	 * 添加实体
	 * @param entity	实体
	 * @return
	 */
	public T insert(T entity) {
		getSession().persist(entity);
		return entity;
	}

	/**
	 * 更新或插入（多个）
	 * @param list	实体列表
	 */
	public void saveOrUpdateAll(List<T> list) {
		for (T t : list) {
			getSession().saveOrUpdate(t);
		}
	}

	/**
	 * 更新1个实体
	 * @param entity	实体
	 * @return
	 */
	public T saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}
	/**
	 * 更新某个实体
	 * @param entity	实体
	 * @return
	 */
	public T update(T entity) {
		getSession().merge(entity);
		return entity;
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
		getSession().delete(entity);
		return entity;
	}

	/**
	 * 删除某个实体
	 * @param id
	 * @return
	 */
	public T deleteById(Serializable id) {
		T entity = load(id);
		getSession().delete(entity);	
		return entity;
	}

	/**
	 * 根据id数组删除数据
	 * @param ids	id数组
	 */
	public void deleteAllByIds(Serializable[] ids) {
		for (Serializable id : ids) {
			T entity = load(id);
			getSession().delete(entity);
		}
	}	


	/**
	 * 根据hql删除数据
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	public int removeByHql(String hql, Object... values) {
		Query q = createQuery(hql, values);
	    return q.executeUpdate() ;
	}
	
	/**
	 * 根据Sql删除数据
	 * @param hql		hql
	 * @param values	参数数组
	 * @return
	 */
	public int removeBySql(String sql, Object... values) {
		Query q = createSQLQuery(sql, values);
	    return q.executeUpdate() ;
	}
	
	 /**
   * 根据hql更新数据
   * @param hql   hql
   * @param values  参数数组
   * @return
   */
  public int updateByHql(String hql, Object... values) {
    Query q = createQuery(hql, values);
      return q.executeUpdate() ;
  }
  
	 /**
   * 根据Sql更新数据
   * @param Sql   Sql
   * @param values  参数数组
   * @return
   */
  public int updateBySql(String sql, Object... values) {
	  Query q = createSQLQuery(sql, values);
      return q.executeUpdate() ;
  }
}
