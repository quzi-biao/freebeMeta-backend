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
 * 内容数据，做为各个对象富文本等内容的存储
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_content_data", 
	indexes = { @Index(columnList = "Id") })
public class ContentData extends BaseEntity {
	/**
	 * 内容的文本
	 */
	@Column(columnDefinition = "TEXT")
	private String content;
	
	/**
	 * 内容的二进制数据（如果是压缩保存）
	 */
	@Column(columnDefinition = "BLOB")
	private byte[] binaryData;
	
	/**
	 * 内容是否压缩
	 */
	private Integer commpressType;
	
	/**
	 * 是否是加密内容
	 */
	private Integer cryptoType;
	
	/**
	 * 内容类型
	 */
	private Integer contentType;
	
	/**
	 * 内容所有者
	 */
	private Long ownerId;
	
	/**
	 * 内容的 key
	 */
	private String contentKey;
}
