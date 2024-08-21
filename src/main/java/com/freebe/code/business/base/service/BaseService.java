package com.freebe.code.business.base.service;

import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.freebe.code.business.base.repository.Selections;
import com.freebe.code.common.CustomException;

/**
 * 基础服务
 * @author xiezhengbiao
 *
 * @param <E> 对象类型
 * @param <ID> 对象 ID 的类型
 */
public interface BaseService<T> {
	
	void start(Long id) throws CustomException;
	
	void stop(Long id) throws CustomException;
	
	/**
	 * 构建包含基本参数的查询例
	 * @return
	 */
	T baseExample();
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param id
	 * @return
	 */
	T find(Long id);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param example
	 * @return
	 */
	T find(Example<T> example);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param example
	 * @param selections
	 * @return
	 */
	T find(Example<T> example, Selections<T> selections);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	T find(T entity);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @param selections
	 * @return
	 */
	T find(T entity, Selections<T> selections);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @return
	 */
	T find(Specification<T> spec);
	
	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @param selections
	 * @return
	 */
	T find(Specification<T> spec, Selections<T> selections);

	/**
	 * 立即加载
	 *
	 * @author xiezhengbiao
	 * @param id
	 * @param lockMode
	 * @return
	 */
	T find(Long id, LockModeType lockMode);

	/**
	 * 延迟加载
	 *
	 * @author xiezhengbiao
	 * @param id
	 * @return
	 */
	T getReference(Long id);

	/**
	 * 加载列表
	 *
	 * @author xiezhengbiao
	 * @param clazz
	 * @return
	 * @throws CustomException 
	 */
	List<T> findAll() throws CustomException;
	
	/**
	 * 加载列表
	 *
	 * @author xiezhengbiao
	 * @param selections
	 * @return
	 */
	List<T> findAll(Selections<T> selections);

	/**
	 * 加载列表
	 *
	 * @author xiezhengbiao
	 * @param sort
	 * @return
	 */
	List<T> findAll(Sort sort);
	
	/**
	 * 加载列表
	 *
	 * @author xiezhengbiao
	 * @param selections
	 * @param sort
	 * @return
	 */
	List<T> findAll(Selections<T> selections, Sort sort);

	/**
	 * 根据查询条件加载列表
	 * 
	 * @author xiezhengbiao(钟其纯)
	 * @param example
	 * @return
	 */
	<S extends T> List<S> findAll(Example<S> example);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author xiezhengbiao
	 * @param example
	 * @param selections
	 * @return
	 */
	<S extends T> List<S> findAll(Example<S> example, Selections<S> selections);
	
	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author xiezhengbiao
	 * @param example
	 * @param sort
	 * @return
	 */
	<S extends T> List<S> findAll(Example<S> example, Sort sort);
	
	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author xiezhengbiao
	 * @param example
	 * @param selections
	 * @param sort
	 * @return
	 */
	<S extends T> List<S> findAll(Example<S> example, Selections<S> selections, Sort sort);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @return
	 */
	<S extends T> List<S> findAll(Specification<S> spec);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @param sort
	 * @return
	 */
	<S extends T> List<S> findAll(Specification<S> spec, Sort sort);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @param selections
	 * @return
	 */
	<S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections);
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @param selections
	 * @param sort
	 * @return
	 */
	<S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections, Sort sort);

	/**
	 * 持久化新建态实体
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	<S extends T> S save(S entity);

	/**
	 * 批量保存
	 *
	 * @author xiezhengbiao
	 * @param entities
	 * @return
	 */
	<S extends T> List<S> save(Iterable<S> entities);

	/**
	 * 保存flush
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	<S extends T> S saveAndFlush(S entity);

	/**
	 * 删除
	 *
	 * @author xiezhengbiao
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 删除
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	void delete(T entity);

	/**
	 * 脱管
	 *
	 * @author xiezhengbiao
	 * @param entity
	 */
	void detach(T entity);

	/**
	 * 刷新
	 *
	 * @author xiezhengbiao
	 * @param entity
	 */
	void refresh(T entity);
	
	/**
	 * 标记位删除
	 * @param id
	 * @throws CustomException 
	 */
	void softDelete(Long id) throws CustomException;

	/**
	 * 刷新
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @param lockMode
	 */
	void refresh(T entity, LockModeType lockMode);

	/**
	 * 刷新
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @param properties
	 */
	void refresh(T entity, Map<String, Object> properties);

	/**
	 * 刷新
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @param lockMode
	 * @param properties
	 */
	void refresh(T entity, LockModeType lockMode, Map<String, Object> properties);

	/**
	 * 加锁
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @param lockMode
	 */
	void lock(T entity, LockModeType lockMode);

	/**
	 * 获取实体的锁模式
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	LockModeType getLockMode(T entity);
	
	/**
	 * 统计
	 *
	 * @author xiezhengbiao
	 * @param entity
	 * @return
	 */
	Long count(T entity);
	
	/**
	 * 统计
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @return
	 */
	Long count(Specification<T> spec);

	/**
	 * 是否存在
	 *
	 * @author xiezhengbiao
	 * @param clazz
	 * @param id
	 * @return
	 */
	boolean exists(Long id);

	/**
	 * 是否存在
	 *
	 * @author xiezhengbiao
	 * @param clazz
	 * @param entity
	 * @return
	 */
	boolean exists(T entity);
	
	/**
	 * 是否存在
	 *
	 * @author xiezhengbiao
	 * @param spec
	 * @return
	 */
	boolean exists(Specification<T> spec);

	/**
	 * flush
	 *
	 * @author xiezhengbiao
	 */
	void flush();
	
	/**
	 * 更新
	 *
	 * @author xiezhengbiao
	 * @param id
	 * @param entity
	 * @return
	 */
	int update(Long id, T entity);
	
	default String format(Double input) {
		if(input == null) {
			return "0";
		}
		return String.format("%.1f", input);
	}
}
