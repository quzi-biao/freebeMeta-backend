package com.freebe.code.business.base.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户
 * @author xiezhengbiao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="user")
@Cacheable
public class User extends BaseEntity {
	
	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 头像
	 */
	private String avator;
	
	/**
	 * 最后登录时间
	 */
	private Long lastLogin;
	
	/**
	 * freeBeId
	 */
	private String freeBeId;
	
	/**
	 * 注册时用于验证的账号如邮箱手机号等，MD5值
	 */
	private String verifyInfo;
	
	/**
	 * 贡献分
	 */
	private Long contribution;
}
