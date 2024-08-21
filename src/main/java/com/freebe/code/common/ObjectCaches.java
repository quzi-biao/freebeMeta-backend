package com.freebe.code.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ObjectCaches {
	
	private Map<Class<?>, Map<Object, Object>> caches = new ConcurrentHashMap<>();
	
	public void put(Object key, Object value) {
		if(null == value || null == key) {
			return;
		}
		Class<?> clazz = value.getClass();
		Map<Object, Object> cache = caches.get(clazz);
		if(null == cache) {
			cache = new ConcurrentHashMap<>();
			caches.put(clazz, cache);
		}
		cache.put(key, value);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T get(Object id, Class<?> clazz) {
		if(null == id || null == clazz) {
			return null;
		}
		Map<Object, Object> cache = caches.get(clazz);
		if(null == cache) {
			return null;
		}
		return (T) cache.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> get(Class<T> clazz) {
		@SuppressWarnings("rawtypes")
		Map cache = caches.get(clazz);
		if(null == cache) {
			return null;
		}
		return cache;
	}
	
	public void clear(Class<?> clazz) {
		caches.remove(clazz);
	}


	public void delete(Object id, Class<?> clazz) {
		Map<Object, Object> cache = caches.get(clazz);
		if(null == cache) {
			return;
		}
		cache.remove(id);
	}
}
