package com.freebe.code.business.base.repository;

import java.util.List;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * jpa select colomns interface
 * 
 * @author xiezhengbiao
 * @param <T>
 */
@FunctionalInterface
public interface Selections<T> {
	List<Selection<?>> toSelection(Root<T> root);
}
