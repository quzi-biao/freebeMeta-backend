package com.freebe.code.business.meta.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 收藏
 * @author xiezhengbiao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_collect")
@Cacheable
public class Collect extends BaseEntity {
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
