package com.freebe.code.business.base.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 键值对
 * @author xiezhengbiao
 *
 */
@Data
@Entity
@NoArgsConstructor
@Table(name="key_value")
public class KeyValue {
	/**
	 * 数据 ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * key
	 */
	private String itemKey;
	
	/**
	 * 值
	 */
	private String itemValue;
	
	/**
	 * 类型
	 */
	private String itemType;
}
