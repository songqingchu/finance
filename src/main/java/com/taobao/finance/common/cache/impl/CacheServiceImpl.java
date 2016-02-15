package com.taobao.finance.common.cache.impl;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.taobao.finance.common.cache.ICacheService;


//@Component("cacheService")
public class CacheServiceImpl implements ICacheService {

	@Autowired
	private MemcachedClient memcachedClient;

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	@Override
	public Object get(String key) {
		try {
			return memcachedClient.get(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void set(String key, Object object) {
		this.set(key, object, 3600 * 24 * 7);
	}

	@Override
	public void set(String key, Object value, int exp) {
		if (exp <= 0)
			exp = 3600 * 24 * 7;

		try {
			memcachedClient.set(key, exp, value);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String generateKey(String app, String key) {
		return CACHE_KEY_BASE + "_" + app + "_" + key;
	}

	@Override
	public Boolean contains(String key) {
		Object o = null;
		try {
			o = memcachedClient.get(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return o != null;
	}

	@Override
	public boolean delete(String key) {
		try {
			return memcachedClient.delete(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return true;
	}

}
