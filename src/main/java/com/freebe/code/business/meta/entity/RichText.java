package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 问卷问题
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_rich_text", 
	indexes = { @Index(columnList = "Id")})
public class RichText extends BaseEntity {
	/**
	 * 所有者 ID
	 */
	private Long ownerId;
	
	/**
	 * 所有者类型
	 */
	private Integer ownerType;
	
	/**
	 * 二进制内容
	 */
	private byte[] byteContent;
	
	/**
	 * 文本内容
	 */
	private String textContent;
	
	/**
	 * 内容类型
	 */
	private Integer contentType;
}
