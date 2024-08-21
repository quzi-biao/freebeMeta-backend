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
 * 消息
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_message", 
	indexes = { @Index(columnList = "Id")})
public class Message extends BaseEntity {
	
	/**
	 * 消息发送着
	 */
	private Long sender;
	
	/**
	 * 消息接收者
	 */
	private Long reciever;
	
	/**
	 * 消息类型
	 */
	private Integer messageType;
	
	/**
	 * 消息状态
	 */
	private Integer state;
	
	/**
	 * 消息内容
	 */
	@Column(columnDefinition = "TEXT")
	private String content;
	
	/**
	 * 读消息时间
	 */
	private Long readTime;
}
