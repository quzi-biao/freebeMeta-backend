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
 * 内容草稿箱
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_content_draft", 
	indexes = { @Index(columnList = "Id") })
public class ContentDraft extends BaseEntity {
	/**
	 * 草稿更新时间
	 */
	public Long updateTime;
	
	/**
	 * 发布时间
	 */
	public Long deployTime;
	
	/**
	 * 发布者 ID
	 */
	private Long ownerId;
	
	/**
	 * 发布的内容ID
	 */
	private Long contentId;
	
	/**
	 * 本文做为另一文章的回应
	 */
	private Long repplyContent;
	
	/**
	 * 项目 ID
	 */
	private Long projectId;
	
	/**
	 * 内容类型
	 */
	private Integer contentType;
	
	/**
	 * 内容分类
	 */
	private Integer category;
	
	/**
	 * 内容标题
	 */
	@Column(columnDefinition = "TEXT")
	private String title;
	
	/**
	 * 内容 key，关联到 contentData
	 */
	private String contentKey;
	
	/**
	 * 内容封面
	 */
	@Column(columnDefinition = "TEXT")
	private String picture;
	
	/**
	 * 内容摘要
	 */
	@Column(columnDefinition = "TEXT")
	private String contentAbstract;
	
	/**
	 * 内容发布状态（审核中，已发布）
	 */
	private Integer status;
	
	/**
	 * 内容审核员
	 */
	private Long auditor;
	
	/**
	 * 审核建议
	 */
	private String auditorComment;
}
