package com.freebe.code.business.website.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 网页模版
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_website_template")
public class WebsiteTemplateEntity extends BaseEntity {
	
	/**
	 * 模版描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 模版所有者
	 */
	private Long owner;
	
	/**
	 * demo 链接
	 */
	@Column(columnDefinition = "TEXT")
	private String demoUrl;
	
	/**
	 * 模版图片
	 */
	@Column(columnDefinition = "TEXT")
	private String picture;
	
	/**
	 * 定价
	 */
	private Long price;
	
	/**
	 * 模版资源路径
	 */
	@Column(columnDefinition = "TEXT")
	private String templateUrl;
	
	/**
	 * 模版内容
	 */
	@Column(columnDefinition = "TEXT")
	private String content;
}
