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
 * 成员
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_member", 
	indexes = {@Index(columnList = "Id")})
public class Member extends BaseEntity {
	
	/**
	 * 用户 ID
	 */
	private Long userId;
	
	/**
	 * 简介
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 技能树
	 */
	@Column(columnDefinition = "TEXT")
	private String skills;
	
	/**
	 * 最近的登录时间
	 */
	private Long lastTime;
	
	/**
	 * 角色列表
	 */
	@Column(columnDefinition = "TEXT")
	private String roles;
	
	/**
	 * 社区贡献
	 */
	private Long contribution;
}
