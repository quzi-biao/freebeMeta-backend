package com.freebe.code.business.advanture.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务领取
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_advanture_take", 
	indexes = { @Index(columnList = "Id")})
public class AdvantureTaskTake extends BaseEntity{
	/**
	 * 领取者 ID
	 */
	private Long takerId;
	
	/**
	 * 领取的任务 ID
	 */
	private Long taskId;
	
	/**
	 * 领取时间
	 */
	private Long takeTime;
	
	/**
	 * 提交时间
	 */
	private Long submitTime;
	
	/**
	 * 审核时间
	 */
	private Long auditTime;
	
	/**
	 * 提交说明
	 */
	private String submitDescription;
	
	/**
	 * 审核说明
	 */
	private String auditDescription;
	
	/**
	 * 状态
	 */
	private Integer state;
}
