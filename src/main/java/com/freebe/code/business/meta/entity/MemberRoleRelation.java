package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户角色关系表
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_member_role_relation", 
	indexes = {@Index(columnList = "roleId"), @Index(columnList = "memberId")})
public class MemberRoleRelation extends BaseEntity {
	/**
	 * 角色ID
	 */
	private Long roleId;
	
	/**
	 * 成员 ID
	 */
	private Long memberId;
}
