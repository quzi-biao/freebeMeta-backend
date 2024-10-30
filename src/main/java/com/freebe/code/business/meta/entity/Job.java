package com.freebe.code.business.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.InteractionEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 岗位
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_job", 
	indexes = { @Index(columnList = "Id") })
public class Job extends InteractionEntity {
	/**
	 * 岗位发布者
	 */
	private Long ownerId;
	
	/**
	 * 归属的项目
	 */
	private Long projectId;
	
	/**
	 * 岗位标题
	 */
	@Column(columnDefinition = "TEXT")
	private String title;
	
	/**
	 * 内容 key，关联到 contentData
	 */
	private String descriptionKey;
	
	/**
	 * 岗位回报说明
	 */
	@Column(columnDefinition = "TEXT")
	private String rewardDescription;
	
	/**
	 * 申请者数量
	 */
	private Long applier;
	
	/**
	 * 关联的徽章 ID
	 */
	private Long badgeId;
	
	/**
	 * 关联的问卷 ID
	 */
	private Long questionaireId;
	
	/**
	 * 关联的任务类型 ID
	 */
	private Long taskTypeId;
	
	/**
	 * 招聘人数
	 */
	private Integer headCount;
	
	/**
	 * 招募截止时间
	 */
	private Long deadLine;
	
	/**
	 * 已招募的人数
	 */
	private Integer currHead;
}
