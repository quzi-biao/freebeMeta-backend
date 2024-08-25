package com.freebe.code.business.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 项目
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_project", 
	indexes = { @Index(columnList = "Id")})
public class Project extends BaseEntity {
	
	/**
	 * 项目所有者（项目主理人）
	 */
	private Long ownerId;
	
	/**
	 * 项目类型
	 */
	private Integer projectType;
	
	/**
	 * 项目总预算
	 */
	private String preAmount;
	
	/**
	 * 项目实际支出
	 */
	private String realAmount;
	
	/**
	 * 结算货币
	 */
	private Integer currency;
	
	/**
	 * 项目图片（支持多张）
	 */
	@Column(columnDefinition = "TEXT")
	private String pictures;
	
	/**
	 * 项目状态
	 */
	private Integer state;
	
	/**
	 * 项目标签，数组
	 */
	@Column(columnDefinition = "TEXT")
	private String tags;
	
	/**
	 * 项目介绍
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 项目开始时间
	 */
	private Long startTime;
	
	/**
	 * 项目完成时间
	 */
	private Long doneTime;
	
	/**
	 * 项目结束时间
	 */
	private Long endTime;
	
	/**
	 * 项目结算时间
	 */
	private Long billTime;
	
	/**
	 * 项目结算状态
	 */
	private Integer billState;
}
