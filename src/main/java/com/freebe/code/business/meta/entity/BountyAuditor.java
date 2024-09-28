package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 项目悬赏专员
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_bounty_auditor", 
	indexes = { @Index(columnList = "Id")})
public class BountyAuditor extends BaseEntity {
	
	/**
	 * 悬赏专员 ID
	 */
	private Long userId;
	
	/**
	 * 悬赏 ID
	 */
	private Long bountyId;
}
