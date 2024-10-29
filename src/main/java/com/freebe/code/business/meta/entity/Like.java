package com.freebe.code.business.meta.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 喜爱
 * @author xiezhengbiao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_like")
@Cacheable
public class Like extends BaseEntity{
	/**
	 * 用户 ID
	 */
	private Long userId;
	
	/**
	 * 类型 ID
	 */
	private Long typeId;
	
	/**
	 * 对象 ID
	 */
	private Long entityId;
}
