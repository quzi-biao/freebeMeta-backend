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
 * 文件
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_file", 
	indexes = { @Index(columnList = "Id")})
public class FreeFile extends BaseEntity {
	
	/**
	 * 文件标题
	 */
	@Column(columnDefinition = "TEXT")
	private String title;
	
	/**
	 * 文件描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 文件大小(B)
	 */
	private Long fileSize;
	
	/**
	 * 文件链接
	 */
	private String url;
	
	/**
	 * 文件类型
	 */
	private String fileType;
	
	/**
	 * 公开属性
	 */
	private Integer publicType;
	
	/**
	 * 文件所有者
	 */
	private Long ownerId;
	
	/**
	 * 文件定价
	 */
	private String price;
	
	/**
	 * 定价策略：次/不限次
	 */
	private Integer priceCategory;
}
