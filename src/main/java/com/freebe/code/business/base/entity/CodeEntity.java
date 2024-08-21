package com.freebe.code.business.base.entity;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class CodeEntity extends IdEntity {
	/**
	 * 编码 entity
	 */
	private String code;
}
