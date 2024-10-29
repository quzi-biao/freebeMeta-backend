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
 * 市场供应方
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_markert_provider", 
	indexes = {@Index(columnList = "Id")})
public class MarketProvide extends InteractionEntity {
	/**
	 * 供应所有者
	 */
	private Long ownerId;
	
	/**
	 * 创建者
	 */
	private Long creator;
	
	/**
	 * 供应内容
	 */
	private String title;
	
	/**
	 * 宣传展示图
	 */
	private String picture;
	
	/**
	 * 描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 最高价格
	 */
	private Long maxPrice;
	
	/**
	 * 最低价格
	 */
	private Long minPrice;
	
	/**
	 * 价格描述
	 */
	@Column(columnDefinition = "TEXT")
	private String priceDescription;
	
	/**
	 * 供应者
	 */
	@Column(columnDefinition = "TEXT")
	private String providers;
	
	/**
	 * 标签
	 */
	@Column(columnDefinition = "TEXT")
	private String tags;
	
	/**
	 * 是否审核通过
	 */
	private Boolean audit;
	
	/**
	 * 联系方式
	 */
	@Column(columnDefinition = "TEXT")
	private String contact;
}
