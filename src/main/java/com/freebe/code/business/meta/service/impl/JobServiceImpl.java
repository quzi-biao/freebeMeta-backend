package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.badge.service.BadgeService;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.JobParam;
import com.freebe.code.business.meta.controller.param.JobQueryParam;
import com.freebe.code.business.meta.entity.Job;
import com.freebe.code.business.meta.repository.JobRepository;
import com.freebe.code.business.meta.service.CollectService;
import com.freebe.code.business.meta.service.ContentDataService;
import com.freebe.code.business.meta.service.JobService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.type.ContentType;
import com.freebe.code.business.meta.type.InteractionEntityType;
import com.freebe.code.business.meta.vo.JobVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class JobServiceImpl extends BaseServiceImpl<Job> implements JobService {
	@Autowired
	private JobRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private ContentDataService contentDataService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BadgeService bdgeService;
	
	@Autowired
	private CollectService collectService;
	
	@Override
	public JobVO findById(Long id) throws CustomException {
		Job ret = getEntity(id);
		return toVO(ret);
	}

	
	@Transactional
	@Override
	public synchronized void incApply(Long id) {
		Job job = this.getEntity(id);
		if(null == job.getApplier()) {
			job.setApplier(0L);
		}
		job.setApplier(job.getApplier() + 1);
		this.repository.save(job);
		this.objectCaches.put(job.getId(), job);
	}
	
	@Transactional
	@Override
	public synchronized void incHead(Long id) {
		Job job = this.getEntity(id);
		if(null == job.getCurrHead()) {
			job.setCurrHead(0);
		}
		job.setCurrHead(job.getCurrHead() + 1);
		this.repository.save(job);
		this.objectCaches.put(job.getId(), job);
	}

	@Transactional
	@Override
	public JobVO createOrUpdate(JobParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("请输入内容标题");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("请输入工作内容描述");
		}
		
		if(StringUtils.isEmpty(param.getRewardDescription())) {
			throw new CustomException("请输入工作回报说明");
		}
		
		
		Job e = this.getUpdateEntity(param, false);
		
		Long currId = getCurrentUser().getId();
		
		if(null != e.getOwnerId() && e.getOwnerId().longValue() != currId.longValue()) {
			throw new CustomException("您无权修改本内容");
		}
		

		e.setOwnerId(getCurrentUser().getId());
		e.setProjectId(param.getProjectId());
		e.setTitle(param.getTitle());
		e.setRewardDescription(param.getRewardDescription());
		e.setBadgeId(param.getBadgeId());
		e.setQuestionaireId(param.getQuestionaireId());
		e.setTaskTypeId(param.getTaskTypeId());
		
		String key = this.contentDataService.updateContent(e.getDescriptionKey(), param.getDescription(), ContentType.NORMAL);
		e.setDescriptionKey(key);

		e.setApplier(0L);
		e.setFavorite(0L);
		e.setCollect(0L);
		e.setShare(0L);
		e.setComment(0L);
		
		e.setHeadCount(param.getHeadCount());
		e.setDeadLine(param.getDeadLine());
		
		e = repository.save(e);

		JobVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return vo;
	}

	@Override
	public Page<JobVO> queryPage(JobQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Job> example = buildSpec(param);

		Page<Job> page = repository.findAll(example, request);
		List<JobVO> retList = new ArrayList<>();

		for(Job e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<JobVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Job> buildSpec(JobQueryParam param) throws CustomException {
		return new Specification<Job>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Job> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("badgeId", param.getBadgeId());
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("questionaireId", param.getQuestionaireId());
				builder.addEqual("taskTypeId", param.getTaskTypeId());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private JobVO toVO(Job e) throws CustomException {
		JobVO vo = new JobVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(this.userService.getUser(e.getOwnerId()));
		vo.setProjectId(e.getProjectId());
		vo.setTitle(e.getTitle());
		vo.setDescription(this.contentDataService.findByKey(e.getDescriptionKey()));
		vo.setRewardDescription(e.getRewardDescription());
		vo.setApplier(e.getApplier());
		vo.setBadge(this.bdgeService.findById(e.getBadgeId()));
		vo.setQuestionaireId(e.getQuestionaireId());
		vo.setTaskTypeId(e.getTaskTypeId());

		vo.setLike(e.getFavorite());
		vo.setCollect(e.getCollect());
		vo.setShare(e.getShare());
		vo.setComment(e.getComment());
		
		vo.setHeadCount(e.getHeadCount());
		vo.setDeadLine(e.getDeadLine());	
		vo.setCurrHead(e.getCurrHead());
		if(null != e.getProjectId()) {
			vo.setProject(this.projectService.findById(e.getProjectId()));
		}
		
		vo.setCollected(this.collectService.isCollect(InteractionEntityType.JOB, vo.getId()));

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, JobVO.class);
		super.softDelete(id);
	}
	

	private Job getEntity(Long id) {
		Job ret = this.objectCaches.get(id, Job.class);
		if(null == ret){
			Optional<Job> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
}
