package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 文件权限
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_file_permission", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "fileId")})
public class FilePermission extends BaseEntity {
	/**
	 * 文件 ID
	 */
	private Long fileId;
	
	/**
	 * 成员 ID
	 */
	private Long userId;
	
	/**
	 * 关联的交易 ID
	 */
	private Long transactionId;
	
	/**
	 * 权限状态
	 */
	private Integer permissionType;
	
	/**
	 * 权限允许的次数
	 */
	private Long permissionTimes;
	
	/**
	 * 查看次数
	 */
	private Long viewTimes;
}
