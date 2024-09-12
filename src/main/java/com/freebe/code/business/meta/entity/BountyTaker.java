package com.freebe.code.business.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 悬赏认领
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_bounty_taker", 
	indexes = { @Index(columnList = "Id")})
public class BountyTaker extends BaseEntity {
	/**
	 * 悬赏 ID
	 */
	private Long bountyId;
	
	/**
	 * 悬赏认领时间
	 */
	private Long takeTime;
	
	/**
	 * 悬赏认领者
	 */
	private Long taker;
	
	/**
	 * 悬赏完成时间
	 */
	private Long doneTime;
	
	/**
	 * 悬赏耗时
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
	 * 悬赏奖励
	 */
	private Long freeBe;
	
	/**
	 * 悬赏结算交易信息
	 */
	private Long transactionId;
	
	/**
	 * 放弃原因
	 */
	private String giveoutReason;
	
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
