package com.freebe.code.common;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.entity.KeyValue;
import com.freebe.code.business.base.repository.KeyValueRepository;

@Component
public class KVStorage {
	@Autowired
	private KeyValueRepository keyValueRepository;
	
	/**
	 * 保存一对 keyvalue
	 * @param key
	 * @param value
	 */
	public synchronized void save(String key, Object value) {
		if(null == value || null == key) {
			return;
		}
		KeyValue kv = this.getByKey(key);
		if(null == kv) {
			kv = new KeyValue();
		}
		
		kv.setItemKey(key);
		kv.setItemValue(JSONObject.toJSONString(value));
		kv.setItemType(value.getClass().getName());
		
		keyValueRepository.save(kv);
	}
	
	/**
	 * 获取字符串值
	 * @param key
	 * @return
	 */
	public String get(String key) {
		KeyValue kv = this.getByKey(key);
		if(null == kv) {
			return null;
		}
		
		return kv.getItemValue();
	}
	
	/**
	 * 获取对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T get(String key, Class<T> clazz) {
		KeyValue kv = this.getByKey(key);
		if(null == kv) {
			return null;
		}
		if(StringUtils.isEmpty(kv.getItemValue())) {
			return null;
		}
		
		return JSONObject.parseObject(kv.getItemValue(), clazz);
	}
	
	private KeyValue getByKey(String key) {
		KeyValue example = new KeyValue();
		example.setItemKey(key);
		
		Optional<KeyValue> optional = this.keyValueRepository.findOne(Example.of(example));
		if(!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}
}
