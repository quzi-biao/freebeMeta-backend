package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 交易
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_transaction", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "srcWalletId"), @Index(columnList = "dstWalletId")})
public class Transaction extends BaseEntity {
	/**
	 * 转出地址
	 */
	private Long srcWalletId;
	
	/**
	 * 转入地址
	 */
	private Long dstWalletId;
	
	/**
	 * 交易金额
	 */
	private String amount;
	
	/**
	 * 货币
	 */
	private Integer currency;
	
	/**
	 * 交易确认时间(上链时间)
	 */
	private Long confirmTime;
	
	/**
	 * 交易 hash
	 */
	private String hash;
	
	/**
	 * 交易类型
	 */
	private Integer transactionType;
	
	/**
	 * 交易状态
	 */
	private Integer state;
	
	/**
	 * 交易备注
	 */
	private String mark;
	
	/**
	 * 关联的项目 ID
	 */
	private Long projectId;
	
	/**
	 * 公示开始时间
	 */
	private Long publicStartTime;
	
	/**
	 * 失败原因
	 */
	private String failedReason;
}
