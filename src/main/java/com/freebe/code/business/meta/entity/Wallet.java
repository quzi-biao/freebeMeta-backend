package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 钱包
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_wallet", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "userId"), @Index(columnList = "address")})
public class Wallet extends BaseEntity {
	/**
	 * 成员 ID
	 */
	private Long userId;
	
	/**
	 * 钱包地址，一般和 member 中的 address 一致
	 */
	private String address;
	
	/**
	 * 积分余额
	 */
	private Long freeBe;
	
	/**
	 * U 的余额
	 */
	private String usdt;
	
	/**
	 * 人民币余额
	 */
	private String cny;
}
