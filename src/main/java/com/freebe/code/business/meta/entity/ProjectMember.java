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
 * 项目成员
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_project_member", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "projectId"), @Index(columnList = "memberId")})
public class ProjectMember extends BaseEntity {
	
	/**
	 * 项目 ID
	 */
	private Long projectId;
	
	/**
	 * 成员 ID
	 */
	private Long memberId;
	
	/**
	 * 加入时间
	 */
	private Long joinTime;
	
	/**
	 * 退出时间
	 */
	private Long dimissionTime;
	
	/**
	 * 承担的角色
	 */
	private String role;
	
	/**
	 * 成员状态
	 */
	private Integer projectMemberState;
	
	/**
	 * 任务完成状态
	 */
	private Integer workState;
	
	/**
	 * 成员结算状态
	 */
	private Integer billState;
	
	/**
	 * 预期支付金额
	 */
	private String preAmount;
	
	/**
	 * 实际支付金额
	 */
	private String realAmount;
	
	/**
	 * 工作评价
	 */
	@Column(columnDefinition = "TEXT")
	private String evaluation;
	
	/**
	 * 退出原因
	 */
	private String dismissionReason;
	
	/**
	 * 任务完成时间
	 */
	private Long doneTime;
	
	/**
	 * 结算交易ID
	 */
	private Long transactionId;
	
	/**
	 * 结算时间
	 */
	private Long billTime;
	
	/**
	 * 结算完成时间
	 */
	private Long billEndTime;
}
