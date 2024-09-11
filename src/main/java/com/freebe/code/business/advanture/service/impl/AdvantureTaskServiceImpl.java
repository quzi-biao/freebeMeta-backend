package com.freebe.code.business.advanture.service.impl;


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
import com.freebe.code.business.advanture.entity.AdvantureTask;
import com.freebe.code.business.advanture.service.AdvantureTaskService;
import com.freebe.code.business.advanture.vo.AdvantureTaskVO;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskQueryParam;
import com.freebe.code.business.advanture.repository.AdvantureTaskRepository;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class AdvantureTaskServiceImpl extends BaseServiceImpl<AdvantureTask> implements AdvantureTaskService {
	@Autowired
	private AdvantureTaskRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public AdvantureTaskVO findById(Long id) throws CustomException {
		AdvantureTaskVO ret = this.objectCaches.get(id, AdvantureTaskVO.class);
		if(null == ret){
			Optional<AdvantureTask> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public AdvantureTaskVO createOrUpdate(AdvantureTaskParam param) throws CustomException {
		AdvantureTask e = this.getUpdateEntity(param, false);

		e.setTitle(param.getTitle());
		e.setDescription(param.getDescription());
		e.setExperience(param.getExperience());
		e.setTaskLevel(param.getTaskLevel());

		e = repository.save(e);

		AdvantureTaskVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<AdvantureTaskVO> queryPage(AdvantureTaskQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<AdvantureTask> example = buildSpec(param);

		Page<AdvantureTask> page = repository.findAll(example, request);
		List<AdvantureTaskVO> retList = new ArrayList<>();

		for(AdvantureTask e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<AdvantureTaskVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<AdvantureTask> buildSpec(AdvantureTaskQueryParam param) throws CustomException {
		return new Specification<AdvantureTask>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<AdvantureTask> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<AdvantureTask> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("taskLevel", param.getTaskLevel());
				builder.addEqual("status", param.getStatus());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private AdvantureTaskVO toVO(AdvantureTask e) throws CustomException {
		AdvantureTaskVO vo = new AdvantureTaskVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setTitle(e.getTitle());
		vo.setDescription(e.getDescription());
		vo.setExperience(e.getExperience());
		vo.setStatus(e.getStatus());
		vo.setCreator(e.getCreator());
		vo.setTaskLevel(e.getTaskLevel());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, AdvantureTaskVO.class);
		super.softDelete(id);
	}

}
