package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.common.ObjectCaches;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;
import com.freebe.code.business.meta.entity.TaskType;
import com.freebe.code.business.meta.service.TaskTypeService;
import com.freebe.code.business.meta.vo.TaskTypeVO;
import com.freebe.code.business.meta.controller.param.TaskTypeParam;
import com.freebe.code.business.meta.controller.param.TaskTypeQueryParam;
import com.freebe.code.business.meta.repository.TaskTypeRepository;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class TaskTypeServiceImpl extends BaseServiceImpl<TaskType> implements TaskTypeService {
	@Autowired
	private TaskTypeRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public TaskTypeVO findById(Long id) throws CustomException {
		TaskTypeVO ret = this.objectCaches.get(id, TaskTypeVO.class);
		if(null == ret){
			Optional<TaskType> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public TaskTypeVO createOrUpdate(TaskTypeParam param) throws CustomException {
		TaskType e = this.getUpdateEntity(param);


		e = repository.save(e);

		TaskTypeVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<TaskTypeVO> queryPage(TaskTypeQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<TaskType> example = buildSpec(param);

		Page<TaskType> page = repository.findAll(example, request);
		List<TaskTypeVO> retList = new ArrayList<>();

		for(TaskType e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<TaskTypeVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<TaskType> buildSpec(TaskTypeQueryParam param) throws CustomException {
		return new Specification<TaskType>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<TaskType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<TaskType> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private TaskTypeVO toVO(TaskType e) throws CustomException {
		TaskTypeVO vo = new TaskTypeVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());


		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, TaskTypeVO.class);
		super.softDelete(id);
	}

}
