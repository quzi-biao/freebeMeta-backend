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
 * 任务认领
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_task_taker", 
	indexes = { @Index(columnList = "Id")})
public class TaskTaker extends BaseEntity {
	/**
	 * 任务 ID
	 */
	private Long taskId;
	
	/**
	 * 任务认领时间
	 */
	private Long takeTime;
	
	/**
	 * 任务认领者
	 */
	private Long taker;
	
	/**
	 * 任务完成时间
	 */
	private Long doneTime;
	
	/**
	 * 任务耗时
	 */
	private Long costTime;
	
	/**
	 * 完成状态
	 */
	private Integer state;
	
	/**
	 * 完成评价
	 */
	private String evaluate;
	
	/**
	 * 任务奖励
	 */
	private Long freeBe;
	
	/**
	 * 任务结算交易信息
	 */
	private Long transactionId;
	
	/**
	 * 交付文件
	 */
	@Column(columnDefinition = "TEXT")
	private String submitFiles;
	
	/**
	 * 交付文件
	 */
	@Column(columnDefinition = "TEXT")
	private String submitPictures;
	
	/**
	 * 交付描述
	 */
	@Column(columnDefinition = "TEXT")
	private String submitDescription;
}
