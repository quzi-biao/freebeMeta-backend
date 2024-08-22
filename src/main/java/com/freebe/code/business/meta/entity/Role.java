package com.freebe.code.business.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_role", 
	indexes = { @Index(columnList = "Id")})
public class Role extends BaseEntity {
	
	/**
	 * 关联的合约地址
	 */
	private String contractAddress;
	
	/**
	 * 角色图标
	 */
	private String icon;
	
	/**
	 * 角色描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 角色回报
	 */
	private String reward;
	
	/**
	 * 人数
	 */
	private Integer number;
	
	/**
	 * 角色图片
	 */
	private String picture;
	
	/**
	 * 角色编码
	 */
	private String roleCode;
}
