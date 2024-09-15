package com.freebe.code.business.badge.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 徽章持有记录
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_badge_hold", indexes = { @Index(columnList = "Id")})
public class BadgeHold extends BaseEntity {
	/**
	 * 徽章ID
	 */
	private Long badgeId;
	
	/**
	 * 成员 ID，非成员，仅有 userId 不能获取徽章
	 */
	private Long memberId;
	
	/**
	 * 是否持有中
	 */
	private Boolean inHold;
}
