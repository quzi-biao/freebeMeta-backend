package com.freebe.code.business.base.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.controller.param.BaseEntityParam;
import com.freebe.code.business.base.entity.BaseEntity;
import com.freebe.code.business.base.repository.CommonRepository;
import com.freebe.code.business.base.repository.Selections;
import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.MessageParam;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.MessageService;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.RoleVO;
import com.freebe.code.common.Constants;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.CodeUtils;

@Transactional
public class BaseServiceImpl<T> implements BaseService<T> {
	
	@Autowired
	private CommonRepository baseDao;

	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	private ConcurrentHashMap<Long, T> cache = null;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MessageService msgService;
	
	public CommonRepository getBaseDao() {
		return baseDao;
	}
	
	@SuppressWarnings({ "unchecked" })
	private <S extends T> Class<S> getClazz() {
		if (this.clazz == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (!(params[0] instanceof Class)) {
				return null;
			}
			return (Class<S>) params[0];
		}
		return this.clazz;
	}
	
	public String getCurrAddress() throws CustomException {
		return "";
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	public UserVO getCurrentUser() {
		ServletRequestAttributes requestAttributes = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		if(null == requestAttributes) {
			return null;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		UserVO ret = (UserVO) request.getAttribute(Constants.USER_INFO);
		return ret;
	}
	
	/**
	 * 发送消息
	 * @param sender
	 * @param reciever
	 * @param content
	 * @param msgType
	 * @throws CustomException
	 */
	public void sendMessage(Long sender, Long reciever, String content, int msgType) throws CustomException {
		MessageParam param = new MessageParam();
		param.setContent(content);
		param.setReciever(reciever);
		param.setSender(sender);
		param.getMessageType();
		
		this.msgService.createOrUpdate(param);
	}
	
	/**
	 * 发送消息
	 * @param sender
	 * @param reciever
	 * @param content
	 * @param msgType
	 * @throws CustomException
	 */
	public void sendMessage(Long sender, List<Long> recievers, String content, int msgType) throws CustomException {
		for(Long reciever : recievers) {
			MessageParam param = new MessageParam();
			param.setContent(content);
			param.setReciever(reciever);
			param.setSender(sender);
			param.getMessageType();
			this.msgService.createOrUpdate(param);
		}
	}
	
	/**
	 * 获取当前成员
	 * @return
	 * @throws CustomException
	 */
	public MemberVO getCurrentMemeber() throws CustomException {
		ServletRequestAttributes requestAttributes = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		if(null == requestAttributes) {
			throw new CustomException("上下文异常");
		}
		HttpServletRequest request = requestAttributes.getRequest();
		UserVO ret = (UserVO) request.getAttribute(Constants.USER_INFO);
		if(null == ret) {
			throw new CustomException("请先登录");
		}
		
		MemberVO member = this.memberService.findByUserId(ret.getId());
		
		if(null == member) {
			throw new CustomException("请还不是社区成员");
		}
		
		return member;
	}
	
	/**
	 * 权限检查
	 * @param permissionCode
	 * @throws CustomException
	 */
	public void checkPermssion(String permissionCode) throws CustomException {
		MemberVO member = this.getCurrentMemeber();
		
		if(null == member.getRoles() || member.getRoles().size() == 0) {
			throw new CustomException("请联系" + permissionCode + "执行此操作");
		}
		
		for(RoleVO role : member.getRoles()) {
			if(permissionCode.equals(role.getRoleCode())) {
				return;
			}
		}
		throw new CustomException("请联系" + permissionCode + "执行此操作");
	}
	
	/**
	 * 需要超级管理员
	 * @throws CustomException
	 */
	public void needSystemSuperManager() throws CustomException {
		// 只有超级管理员才有创建组织的权限
		UserVO user = getCurrentUser();
		if(null == user.getUserType() || user.getUserType().intValue() != Constants.SUPER_MANAGER) {
			throw new CustomException("创建组织失败：权限不足");
		}
	}
	
	/**
	 * 需要超级管理员
	 * @throws CustomException
	 */
	public boolean isSystemSuperManager() throws CustomException {
		// 只有超级管理员才有创建组织的权限
		UserVO user = getCurrentUser();
		if(null == user.getUserType() || user.getUserType().intValue() != Constants.SUPER_MANAGER) {
			return false;
		}
		return true;
	}
	
	/**
	 * 需要超级管理员
	 * @throws CustomException
	 */
	public boolean isSuperManager() throws CustomException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		UserVO user = (UserVO) request.getAttribute(Constants.USER_INFO);
		// 是否是系统级别的超级管理员
		if(null == user.getUserType() || user.getUserType().intValue() != Constants.SUPER_MANAGER) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查当前用户是否有当前记录的操作权限
	 * @param entityId
	 * @throws CustomException
	 */
	public void checkEntityPermission(Long entityId) throws CustomException {
		UserVO user = getCurrentUser();
		// 超级管理员拥有任何记录的操作权限
		if(null == user.getUserType() || user.getUserType().intValue() != Constants.SUPER_MANAGER) {
			return;
		}
		return;
	}
	
	public T getUpdateEntity(BaseEntityParam param) throws CustomException {
		return getUpdateEntity(param, true);
	}
	
	@SuppressWarnings("unchecked")
	public T getUpdateEntity(BaseEntityParam param, boolean checkName, String errMsg) throws CustomException {
		BaseEntity entity = (BaseEntity) getById(param.getId());
		
		if(checkName) {
			BaseEntity nameExist = (BaseEntity) getByName(param.getName());
			if(null != nameExist) {
				if(null == entity || entity.getId().longValue() != nameExist.getId().longValue()) {
					throw new CustomException(errMsg);
				}
			}
		}
		
		if(null == entity) {
			try {
				entity = (BaseEntity) this.getClazz().getDeclaredConstructor().newInstance();
				if(StringUtils.isEmpty(entity.getCode())) {
					entity.setCode(CodeUtils.generateCode(getClazz()));
				}
			} catch (Exception e) {
				throw new CustomException("创建更新对象异常", e);
			}
		}else {
			if(StringUtils.isEmpty(entity.getCode())) {
				try {
					entity.setCode(CodeUtils.generateCode(getClazz()));
				} catch (Exception e) {
					throw new CustomException(e.getMessage(), e);
				}
			}
		}
		
		entity.setIsDelete(false);
		entity.setInUse(true);
		entity.setId(param.getId());
		entity.setName(param.getName());
		
		return (T) entity;
	}
	
	public T getUpdateEntity(BaseEntityParam param, boolean checkName) throws CustomException {
		return this.getUpdateEntity(param, checkName, "名称已存在");
	}
	
	@Override
	public T baseExample() {
		try {
			if (getClazz() == null) {
				return null;
			}
			T t = this.getClazz().getDeclaredConstructor().newInstance();
			BaseEntity entity = (BaseEntity)t;
			entity.setIsDelete(false);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public T getByCode(String code) {
		try {
			if (getClazz() == null) {
				return null;
			}
			T t = this.getClazz().getDeclaredConstructor().newInstance();
			BaseEntity entity = (BaseEntity)t;
			entity.setIsDelete(false);
			entity.setCode(code);
			return baseDao.find(this.getClazz(), Example.of(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public T getById(Long id) {
		if(null == id) {
			return null;
		}
		return this.getReference(id);
	}
	
	public T getByName(String name) {
		try {
			if (getClazz() == null) {
				return null;
			}
			T t = this.getClazz().getDeclaredConstructor().newInstance();
			BaseEntity entity = (BaseEntity)t;
			entity.setIsDelete(false);
			entity.setName(name);
			return baseDao.find(this.getClazz(), Example.of(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Long id) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), id);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param example
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Example<T> example) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), example);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Example<T> example, Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), example, selections);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(T entity) {
		return find(Example.of(entity));
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(T entity, Selections<T> selections) {
		return find(Example.of(entity), selections);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Specification<T> spec) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), spec);
	}
	
	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Specification<T> spec, Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), spec, selections);
	}

	/**
	 * 立即加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @param lockMode
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Long id, LockModeType lockMode) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.find(getClazz(), id, lockMode);
	}

	/**
	 * 延迟加载
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public T getReference(Long id) {
		if(null == id) {
			return null;
		}
		if (getClazz() == null) {
			return null;
		}
		return baseDao.getReference(getClazz(), id);
	}

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param clazz
	 * @return
	 * @throws CustomException 
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() throws CustomException {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz());
	}
	
	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Selections<T> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), selections);
	}

	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), sort);
	}
	
	/**
	 * 加载列表
	 *
	 * @author qc_zhong
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll(Selections<T> selections, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), selections, sort);
	}

	/**
	 * 根据查询条件加载列表
	 * 
	 * @author qc_zhong(钟其纯)
	 * @param example
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example) {
		return baseDao.findAll(example);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Selections<S> selections) {
		return baseDao.findAll(example, selections);
	}

	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author qc_zhong
	 * @param example
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return baseDao.findAll(example, sort);
	}
	
	/**
	 * 根据查询条件加载列表并排序
	 *
	 * @author qc_zhong
	 * @param example
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Example<S> example, Selections<S> selections, Sort sort) {
		return baseDao.findAll(example, selections, sort);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, sort);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, selections);
	}
	
	/**
	 * 根据查询条件加载列表
	 *
	 * @author qc_zhong
	 * @param spec
	 * @param selections
	 * @param sort
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public <S extends T> List<S> findAll(Specification<S> spec, Selections<S> selections, Sort sort) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.findAll(getClazz(), spec, selections, sort);
	}

	/**
	 * 持久化新建态实体
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public <S extends T> S save(S entity) {
		if (getClazz() == null) {
			return null;
		}
		return baseDao.save(getClazz(), entity);
	}

	/**
	 * 批量保存
	 *
	 * @author qc_zhong
	 * @param entities
	 * @return
	 */
	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();
		if (entities == null) {
			return result;
		}
		for (S entity : entities) {
			result.add(save(entity));
		}
		return result;
	}

	/**
	 * 保存flush
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public <S extends T> S saveAndFlush(S entity) {
		S result = save(entity);
		flush();
		return result;
	}

	/**
	 * 删除
	 *
	 * @author qc_zhong
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		T entity = find(id);
		if (entity != null) {
			baseDao.delete(entity);
		}
	}

	/**
	 * 删除
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public void delete(T entity) {
		baseDao.delete(entity);
	}

	/**
	 * 胶管
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	public void detach(T entity) {
		baseDao.detach(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity) {
		baseDao.refresh(entity);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, LockModeType lockMode) {
		baseDao.refresh(entity, lockMode);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param properties
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, Map<String, Object> properties) {
		baseDao.refresh(entity, properties);
	}

	/**
	 * 刷新
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 * @param properties
	 */
	@Override
	@Transactional(readOnly = true)
	public void refresh(T entity, LockModeType lockMode, Map<String, Object> properties) {
		baseDao.refresh(entity, lockMode, properties);
	}

	/**
	 * 加锁
	 *
	 * @author qc_zhong
	 * @param entity
	 * @param lockMode
	 */
	@Override
	public void lock(T entity, LockModeType lockMode) {
		baseDao.lock(entity, lockMode);
	}

	/**
	 * 获取实体的锁模式
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	public LockModeType getLockMode(T entity) {
		return baseDao.getLockMode(entity);
	}
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Long count(T entity) {
		if (getClazz() == null) {
			return 0L;
		}
		return baseDao.count(getClazz(), Example.of(entity));
	}
	
	/**
	 * 统计
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Long count(Specification<T> spec) {
		if (getClazz() == null) {
			return 0L;
		}
		return baseDao.count(getClazz(), spec);
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), id);
	}

	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param entity
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(T entity) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), Example.of(entity));
	}
	
	/**
	 * 是否存在
	 *
	 * @author qc_zhong
	 * @param spec
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(Specification<T> spec) {
		if (getClazz() == null) {
			return false;
		}
		return baseDao.exists(getClazz(), spec);
	}

	/**
	 * flush
	 *
	 * @author qc_zhong
	 */
	@Override
	public void flush() {
		baseDao.flush();
	}
	
	/**
	 * 更新
	 *
	 * @author qc_zhong
	 * @param id
	 * @param entity
	 * @return
	 */
	@Override
	public int update(Long id, T entity) {
		if (getClazz() == null) {
			return 0;
		}
		return baseDao.update(getClazz(), id, entity);
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		T t = this.getById(id);
		if(null != t) {
			this.baseDao.updateDelete(getClazz(), id, true);
		}
		if(null != id && null != cache) {
			cache.remove(id);
		}
	}
	
	@Override
	public void start(Long id) throws CustomException {
		baseDao.updateInUse(getClazz(), id, true);
	}

	@Override
	public void stop(Long id) throws CustomException {
		baseDao.updateInUse(getClazz(), id, false);
	}

	public ConcurrentHashMap<Long, T> getCache() {
		return cache;
	}

	public void setCache(ConcurrentHashMap<Long, T> cache) {
		this.cache = cache;
	}
	
	/**
	 * 转字符串
	 * @param v
	 * @return
	 */
	public String toStr(Object v) {
		if(null == v) {
			return null;
		}
		return JSONObject.toJSONString(v);
	}
	
	/**
	 * 字符串转数组
	 * @param input
	 * @return
	 */
	public List<String> toList(String input) {
		if(null == input || input.length() == 0) {
			return null;
		}
		
		return JSONObject.parseArray(input, String.class);
	}
	
	/**
	 * 
	 * @param <E>
	 * @param input
	 * @param clazz
	 * @return
	 */
	public <E> List<E> toList(String input, Class<E> clazz) {
		if(null == input || input.length() == 0) {
			return null;
		}
		
		return JSONObject.parseArray(input, clazz);
	}
	
	/**
	 * 货币转数值
	 * @param v
	 * @return
	 */
	public Double numbericCurrency(String v) {
		if(null == v) {
			return 0D;
		}
		BigDecimal d = new BigDecimal(v);
		d = d.divide(new BigDecimal(100000));
		return d.doubleValue();
	}
	
	/**
	 * 货币转数值
	 * @param v
	 * @return
	 */
	public Double numbericCurrency(Long v) {
		if(null == v) {
			return 0D;
		}
		BigDecimal d = new BigDecimal(v);
		d = d.divide(new BigDecimal(100000));
		return d.doubleValue();
	}
	
	/**
	 * 数值数组
	 * @param v
	 * @return
	 */
	public String currencyStr(Double v) {
		if(null == v) {
			return null;
		}
		BigDecimal d = new BigDecimal(v);
		d = d.multiply(new BigDecimal(100000));
		return d.toString();
	}
}
