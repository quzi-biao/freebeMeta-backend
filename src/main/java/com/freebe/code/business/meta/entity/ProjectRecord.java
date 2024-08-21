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
 * 项目记录
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_project_record", 
	indexes = { @Index(columnList = "Id")})
public class ProjectRecord extends BaseEntity {
	
	/**
	 * 项目ID
	 */
	private Long projectId;
	
	/**
	 * 成员 ID
	 */
	private Long userId;
	
	/**
	 * 回复的记录ID
	 */
	private Long frontRecordId;
	
	/**
	 * 回复的记录发表人
	 */
	private Long frontUserId;
	
	/**
	 * 记录内容
	 */
	@Column(columnDefinition = "TEXT")
	private String pictures;
	
	/**
	 * 记录内容
	 */
	@Column(columnDefinition = "TEXT")
	private String content;

	/**
	 * 附加文件（文件 ID）
	 */
	@Column(columnDefinition = "TEXT")
	private String appendFiles;
	
	/**
	 * 项目角色
	 */
	private String projectRole;
	
	/**
	 * 记录类型
	 */
	private Integer reocrdType;
	
}
