package com.freebe.code.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.ObjectUtils;

public class QueryUtils {
	/**
	 * 添加相等条件
	 * @param <T>
	 * @param root
	 * @param field
	 * @param value
	 * @param predicates
	 * @param criteriaBuilder
	 */
	public static <T> void addEqualPredicates(Root<T> root, String field, Object value, List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
		if(ObjectUtils.isEmpty(value)) {
			return;
		}
		predicates.add(criteriaBuilder.equal(root.get(field), value));
	}
	
	/**
	 * 添加 like 条件
	 * @param <T>
	 * @param root
	 * @param field
	 * @param value
	 * @param predicates
	 * @param criteriaBuilder
	 */
	public static <T> void addLikePredicates(Root<T> root, String field, String value, List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
		if(ObjectUtils.isEmpty(value)) {
			return;
		}
		predicates.add(criteriaBuilder.like(root.get(field), "%" + value + "%"));
	}
	
	
	/**
	 * 添加 in 条件
	 * @param <T>
	 * @param root
	 * @param field
	 * @param value
	 * @param predicates
	 * @param criteriaBuilder
	 */
	public static <T> void addInPredicates(Root<T> root, String field, List<?> values, List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
		if(null == values || values.size() == 0) {
			return;
		}
		predicates.add(criteriaBuilder.in(root.get(field)).value(values));
	}
	
	/**
	 * 添加之间的条件
	 * @param <T>
	 * @param root
	 * @param field
	 * @param max
	 * @param min
	 * @param predicates
	 * @param criteriaBuilder
	 */
	public static <T> void addBetweenPredicates(Root<T> root, String field, Number min, Number max, List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
		if(ObjectUtils.isEmpty(max)&& ObjectUtils.isEmpty(min)) {
			return;
		}
		if(ObjectUtils.isEmpty(max)) {
			predicates.add(criteriaBuilder.ge(root.get(field), min));
		}else {
			if(ObjectUtils.isEmpty(min)) {
				predicates.add(criteriaBuilder.le(root.get(field), max));
			}else {
				predicates.add(criteriaBuilder.between(root.get(field), min.longValue(), max.longValue()));
			}
			
		}
		
	}
	
	public static class QueryBuilder<T> {
		private Root<T> root;
		
		private CriteriaBuilder criteriaBuilder;
		
		private List<Predicate> predicates = new ArrayList<>();
		
		public QueryBuilder(Root<T> root, CriteriaBuilder criteriaBuilder) {
			this.root = root;
			this.criteriaBuilder = criteriaBuilder;
		}
		
		public void addEqual(String field, Object value) {
			addEqualPredicates(root, field, value, predicates, criteriaBuilder);
		}
		
		public void addBetween(String field, Number min, Number max) {
			addBetweenPredicates(root, field, min, max, predicates, criteriaBuilder);
		}
		
		public void addLike(String filed, String value) {
			addLikePredicates(root, filed, value, predicates, criteriaBuilder);
		}
		
		public void addIn(String filed, List<?> values) {
			addInPredicates(root, filed, values, predicates, criteriaBuilder);
		}
		
		public Predicate getPredicate() {
			Predicate[] predicateArray = predicates.toArray(new Predicate[predicates.size()]);
			Predicate ands = criteriaBuilder.and(predicateArray);
			return ands;
		}

	}
	
	
}
