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
 * 市场供应方
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_markert_provider_user", 
	indexes = {@Index(columnList = "Id")})
public class MarketProvideUser extends BaseEntity {
	/**
	 * 供应 ID
	 */
	private Long provideId;
	
	/**
	 * 用户 ID
	 */
	private Long userId;
	
	/**
	 * 经验说明
	 */
	@Column(columnDefinition = "TEXT")
	private String experience;
	
	/**
	 * 能力证明
	 */
	@Column(columnDefinition = "TEXT")
	private String evidence;
	
	/**
	 * 服务时间
	 */
	@Column(columnDefinition = "TEXT")
	private String serviceTime;
}
