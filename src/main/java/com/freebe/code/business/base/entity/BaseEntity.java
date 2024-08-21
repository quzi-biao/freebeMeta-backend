package com.freebe.code.business.base.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends CodeEntity {
	
	/**
	 * 所有的实体都有 ID 和名字
	 */
	private String name;
	
	/**
	 * 是否使用
	 */
	private Boolean inUse;
	
	@CreatedDate
	@Column
	private Long createTime;
	
	@LastModifiedDate
	@Column
	private Long modifyTime;
}
