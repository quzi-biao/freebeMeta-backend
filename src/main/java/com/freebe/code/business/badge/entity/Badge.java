package com.freebe.code.business.badge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 徽章
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_badge", 
	indexes = { @Index(columnList = "Id")})
public class Badge extends BaseEntity {
	
	/**
	 * 徽章创建者
	 */
	private Long createMemeber;
	
	/**
	 * 图标
	 */
	private String icon;
	
	/**
	 * 展示图
	 */
	private String picture;
	
	/**
	 * 描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 获取条件
	 */
	@Column(columnDefinition = "TEXT")
	private String getCondition;
	
	/**
	 * 自动获取的认定器
	 */
	private Long autoGetIdentity;
	
	/**
	 * 徽章持有者数量
	 */
	private Integer holderNumber;
	
	/**
	 * 徽章合约地址
	 */
	private String contractAddress;
}
