package com.taobao.finance.common.cache;

/**
 * 
 * @auther lijiayang
 * @date 2015年9月28日
 */
public interface ICacheService {

	public static String CACHE_KEY_BASE = "RED5";
	public static String CACHE_KEY_CONNECTION = "CONNECTION";

	public Object get(String key);

	public void set(String key, Object object);

	public Boolean contains(String key);

	public String generateKey(String app, String key);

	public boolean delete(String key);

	public void set(String key, Object value, int exp);

}
