package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

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
public class ContentDraft extends Content {
	/**
	 * 草稿更新时间
	 */
	public Long updateTime;
	
	/**
	 * 发布时间
	 */
	public Long deployTime;
}
