package com.freebe.code.business.badge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 徽章操作记录
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_badge_action_record", indexes = { @Index(columnList = "Id")})
public class BadgeActionRecord extends BaseEntity{
	/**
	 * 操作者
	 */
	private Long operator;
	
	/**
	 * 徽章ID
	 */
	private Long badgeId;
	
	/**
	 * 成员 ID，非成员，仅有 userId 不能获取徽章
	 */
	private Long memberId;
	
	/**
	 * 操作缘由
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 操作时间
	 */
	private Long actionTime;
	
	/**
	 * 操作类型: 1 发放, 2 回收, 3 放弃
	 */
	private Integer actionType;
	
	/**
	 * 交易 hash
	 */
	private String transactionHash;
}
