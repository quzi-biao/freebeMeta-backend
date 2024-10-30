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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.advanture.service.AdvantureCardService;
import com.freebe.code.business.advanture.type.CardState;
import com.freebe.code.business.advanture.vo.AdvantureCardVO;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.JobApplyParam;
import com.freebe.code.business.meta.controller.param.JobApplyQueryParam;
import com.freebe.code.business.meta.entity.JobApply;
import com.freebe.code.business.meta.entity.QuestionnaireAnswer;
import com.freebe.code.business.meta.repository.JobApplyRepository;
import com.freebe.code.business.meta.service.JobApplyService;
import com.freebe.code.business.meta.service.JobService;
import com.freebe.code.business.meta.service.QuestionnaireAnswerService;
import com.freebe.code.business.meta.type.ApplyStatus;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.vo.JobApplyVO;
import com.freebe.code.business.meta.vo.JobVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;
import com.freebe.code.util.S;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class JobApplyServiceImpl extends BaseServiceImpl<JobApply> implements JobApplyService {
	@Autowired
	private JobApplyRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private QuestionnaireAnswerService answerService;
	
	@Autowired
	private AdvantureCardService advantureCardService;

	@Override
	public JobApplyVO findById(Long id) throws CustomException {
		JobApply ret = getEntity(id);
		
		return toVO(ret);
	}
	
	@Override
	public JobApplyVO findApply(Long jobId) throws CustomException {
		Long userId = this.getCurrentUser().getId();
		JobApply ret = this.objectCaches.get(S.c("u", jobId, "-", userId), JobApply.class);
		if(null == ret){
			JobApply probe = new JobApply();
			probe.setOwnerId(userId);
			probe.setJobId(jobId);
			
			List<JobApply> op = this.repository.findAll(Example.of(probe));
			if(null == op || op.size() == 0){
				ret = new JobApply();
			}else {
				ret = op.get(0);
			}
		}
		
		objectCaches.put(S.c("u", jobId, "-", userId), ret);
		
		return toVO(ret);
	}

	@Transactional
	@Override
	public JobApplyVO createOrUpdate(JobApplyParam param) throws CustomException {
		if(null == param.getJobId()) {
			throw new CustomException("参数错误");
		}
		
		JobVO job = jobService.findById(param.getJobId());
		if(null == job) {
			throw new CustomException("岗位不存在");
		}
		
		if(job.getDeadLine() != null && job.getDeadLine() > 0 && System.currentTimeMillis() > job.getDeadLine().longValue()) {
			throw new CustomException("招聘已过截止时间");
		}
		
		if(null != job.getHeadCount() && job.getHeadCount() > 0) {
			if(job.getCurrHead() != null) {
				if(job.getCurrHead().intValue() >= job.getHeadCount().intValue()) {
					throw new CustomException("已招满");
				}
			}
		}
		
		UserVO curr = this.getCurrentUser();
		
		JobApply probe = new JobApply();
		probe.setOwnerId(curr.getId());
		probe.setJobId(param.getJobId());
		
		JobApply e = null;
		List<JobApply> es = this.repository.findAll(Example.of(probe));
		if(null == es || es.size() == 0) {
			e = this.getUpdateEntity(param, false);
		}else {
			e = es.get(0);
		}
		
		// 允许修改申请信息
		e.setApplyInfo(param.getApplyInfo());
		
		if(null == e.getApplyStatus()) {
			if(null == job.getQuestionaireId() && null == job.getTaskTypeId()) {
				// 没有任务和问卷，申请直接进入审核阶段
				e.setApplyStatus(ApplyStatus.REVIEW);
			}else {
				// 没有问卷，申请进入到任务阶段
				if(null == job.getQuestionaireId()) {
					e.setApplyStatus(ApplyStatus.DO_TASK);
				}else {
					e.setApplyStatus(ApplyStatus.ANSWER);
				}
			}
			this.sendMessage(curr.getId(), job.getOwnerId(), S.c("您发布的工作有新的申请: ",  job.getTitle(), ", 申请人: ", curr.getName()), MessageType.JOB_APPLY_MSG);
			e.setApplyTime(System.currentTimeMillis());
			e.setOwnerId(curr.getId());
			e.setJobId(param.getJobId());
		}
		
		e = this.saveAndCache(e);
		this.jobService.incApply(e.getJobId());
		
		return toVO(e);
	}
	
	@Transactional
	@Override
	public JobApplyVO questionnaireEnd(Long applyId) throws CustomException {
		if(null == applyId) {
			throw new CustomException("参数错误");
		}
		
		JobApply apply = this.getEntity(applyId);
		if(null == apply) {
			throw new CustomException("申请不存在");
		}
		
		if(apply.getApplyStatus().intValue() != ApplyStatus.ANSWER) {
			throw new CustomException("状态异常");
		}
		
		Long currId = this.getCurrentUser().getId();
		if(currId.longValue() != apply.getOwnerId()) {
			throw new CustomException("您无权执行此操作");
		}
		JobVO job = this.jobService.findById(apply.getJobId());
		
		QuestionnaireAnswer probe = new QuestionnaireAnswer();
		probe.setUserId(currId);
		probe.setQuestionnaireId(job.getQuestionaireId());
		List<QuestionnaireAnswer> retList = this.answerService.findAll(Example.of(probe));
		if(null == retList || retList == null) {
			throw new CustomException("未答题");
		}
		
		apply.setAnwserId(retList.get(0).getId());
		apply.setAnswerTime(System.currentTimeMillis());
		apply.setApplyStatus(ApplyStatus.ANSWER_AUDIT);
		
		apply = this.repository.save(apply);	
		
		this.sendMessage(apply.getOwnerId(), job.getOwnerId(), S.c("申请者[", this.getCurrentUser().getName(), "]完成问卷回答, 请及时审核"), MessageType.JO_APPLY_ANSWER_DONE_MSG);
		
		apply = this.saveAndCache(apply);
		
		return toVO(apply);
	}
	
	@Transactional
	@Override
	public JobApplyVO answerAudit(AuditParam param) throws CustomException {
		if(null == param.getId() || null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		JobApply apply = this.getEntity(param.getId());
		if(null == apply) {
			throw new CustomException("申请不存在");
		}
		
		if(apply.getApplyStatus().intValue() != ApplyStatus.ANSWER_AUDIT) {
			throw new CustomException("状态异常");
		}
		
		JobVO job = this.jobService.findById(apply.getJobId());
		
		UserVO curr = this.getCurrentUser();
		if(curr.getId().longValue() != job.getOwnerId().longValue()) {
			throw new CustomException("岗位所有者才有审核权限");
		}
		
		
		apply.setAnserAuditTime(System.currentTimeMillis());
		apply.setAnswerComment(param.getEvaluate());
		
		if(param.getPass()) {
			if(job.getTaskTypeId() != null) {
				apply.setApplyStatus(ApplyStatus.DO_TASK);
				this.sendMessage(curr.getId(), apply.getOwnerId(), "您的问卷回答审核已通过, 请开始考察任务吧", MessageType.JO_APPLY_ANSWER_AUDIT_MSG);
			}else {
				apply.setApplyStatus(ApplyStatus.REVIEW);
				this.sendMessage(curr.getId(), apply.getOwnerId(), "您的问卷回答审核已通过, 请等待审核", MessageType.JO_APPLY_ANSWER_AUDIT_MSG);
			}
		}else {
			if(StringUtils.isEmpty(param.getEvaluate())) {
				throw new CustomException("您应该说明不通过的原因");
			}
			apply.setApplyStatus(ApplyStatus.ANSWER_REJECT);
			this.sendMessage(curr.getId(), apply.getOwnerId(), S.c("您的问卷回答审核未通过: ", param.getEvaluate()), MessageType.JO_APPLY_ANSWER_AUDIT_MSG);
		}
		
		apply = this.saveAndCache(apply);
		
		return toVO(apply);
	}

	@Transactional
	@Override
	public JobApplyVO taskEnd(Long applyId) throws CustomException {
		if(null == applyId) {
			throw new CustomException("参数错误");
		}
		
		JobApply apply = this.getEntity(applyId);
		if(null == apply) {
			throw new CustomException("申请不存在");
		}
		
		if(apply.getApplyStatus().intValue() != ApplyStatus.DO_TASK) {
			throw new CustomException("状态异常");
		}
		
		Long currId = this.getCurrentUser().getId();
		if(currId.longValue() != apply.getOwnerId()) {
			throw new CustomException("您无权执行此操作");
		}
		
		JobVO job = this.jobService.findById(apply.getJobId());
		Long taskTypeId = job.getTaskTypeId();
		
		AdvantureCardVO taskCard = this.advantureCardService.findByUserId(currId, taskTypeId);
		if(null == taskCard) {
			throw new CustomException("您还未开始任务");
		}
		
		if(taskCard.getState() != CardState.PASS) {
			throw new CustomException("您还未通过任务考察");
		}
		
		apply.setTaskEndTime(System.currentTimeMillis());
		apply.setApplyStatus(ApplyStatus.TASK_AUDIT);
		
		this.sendMessage(apply.getOwnerId(), job.getOwnerId(), S.c("申请者[", taskCard.getUser().getName(), "]完成任务, 请及时审核"), MessageType.JO_APPLY_ANSWER_DONE_MSG);
		
		apply = saveAndCache(apply);
		
		return toVO(apply);
	}

	@Transactional
	@Override
	public JobApplyVO taskAudit(AuditParam param) throws CustomException {
		if(null == param.getId() || null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		JobApply apply = this.getEntity(param.getId());
		if(null == apply) {
			throw new CustomException("申请不存在");
		}
		
		if(apply.getApplyStatus().intValue() != ApplyStatus.TASK_AUDIT) {
			throw new CustomException("状态异常");
		}
		
		JobVO job = this.jobService.findById(apply.getJobId());
		
		UserVO curr = this.getCurrentUser();
		if(curr.getId().longValue() != job.getOwnerId().longValue()) {
			throw new CustomException("岗位所有者才有审核权限");
		}
		
		apply.setTaskAuditTime(System.currentTimeMillis());
		apply.setTaskComment(param.getEvaluate());
		
		if(param.getPass()) {
			apply.setApplyStatus(ApplyStatus.REVIEW);
			this.sendMessage(curr.getId(), apply.getOwnerId(), "您的任务考察已通过, 请等待审核", MessageType.JO_APPLY_TASK_AUDIT_MSG);
		}else {
			if(StringUtils.isEmpty(param.getEvaluate())) {
				throw new CustomException("您应该说明不通过的原因");
			}
			apply.setApplyStatus(ApplyStatus.TASK_REJECT);
			this.sendMessage(curr.getId(), apply.getOwnerId(), S.c("您的任务审核未通过: ", param.getEvaluate()), MessageType.JO_APPLY_TASK_AUDIT_MSG);
		}
		
		apply = saveAndCache(apply);
		
		return toVO(apply);
	}

	@Transactional
	@Override
	public JobApplyVO review(AuditParam param) throws CustomException {
		if(null == param.getId() || null == param.getPass()) {
			throw new CustomException("参数错误");
		}
		
		JobApply apply = this.getEntity(param.getId());
		if(null == apply) {
			throw new CustomException("申请不存在");
		}
		
		if(apply.getApplyStatus().intValue() != ApplyStatus.REVIEW) {
			throw new CustomException("状态异常");
		}
		
		JobVO job = this.jobService.findById(apply.getJobId());
		
		UserVO curr = this.getCurrentUser();
		if(curr.getId().longValue() != job.getOwnerId().longValue()) {
			throw new CustomException("岗位所有者才有审核权限");
		}
		
		apply.setReviewTime(System.currentTimeMillis());
		apply.setReviewComment(param.getEvaluate());
		
		if(param.getPass()) {
			apply.setApplyStatus(ApplyStatus.REVIEW_PASS);
			this.jobService.incHead(job.getId());
			this.sendMessage(curr.getId(), apply.getOwnerId(), S.c("恭喜您, 你应聘的岗位[", job.getName() ,"]申请已通过"), MessageType.JO_APPLY_REVIEW_MSG);
		}else {
			if(StringUtils.isEmpty(param.getEvaluate())) {
				throw new CustomException("您应该说明不通过的原因");
			}
			apply.setApplyStatus(ApplyStatus.REVIEW_REJECT);
			this.sendMessage(curr.getId(), apply.getOwnerId(), S.c("您的申请未通过: ", param.getEvaluate()), MessageType.JO_APPLY_REVIEW_MSG);
		}
		
		apply = this.saveAndCache(apply);
		
		return toVO(apply);
	}

	@Override
	public Page<JobApplyVO> queryPage(JobApplyQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<JobApply> example = buildSpec(param);

		Page<JobApply> page = repository.findAll(example, request);
		List<JobApplyVO> retList = new ArrayList<>();

		for(JobApply e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<JobApplyVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<JobApply> buildSpec(JobApplyQueryParam param) throws CustomException {
		return new Specification<JobApply>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<JobApply> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<JobApply> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("jobId", param.getJobId());
				builder.addEqual("ownerId", param.getOwnerId());
				
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private JobApplyVO toVO(JobApply e) throws CustomException {
		JobApplyVO vo = new JobApplyVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(userService.getUser(e.getOwnerId()));
		vo.setJobId(e.getJobId());
		vo.setApplyInfo(e.getApplyInfo());
		vo.setApplyTime(e.getApplyTime());
		vo.setAnwserId(e.getAnwserId());
		vo.setAnswerTime(e.getAnswerTime());
		vo.setAnswerComment(e.getAnswerComment());
		vo.setAnserAuditTime(e.getAnserAuditTime());
		vo.setApplyStatus(e.getApplyStatus());
		vo.setTaskCardId(e.getTaskCardId());
		vo.setTaskEndTime(e.getTaskEndTime());
		vo.setTaskComment(e.getTaskComment());
		vo.setTaskAuditTime(e.getTaskAuditTime());
		vo.setReviewerId(e.getReviewerId());
		vo.setReviewTime(e.getReviewTime());
		vo.setReviewComment(e.getReviewComment());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, JobApplyVO.class);
		super.softDelete(id);
	}
	

	private JobApply saveAndCache(JobApply apply) {
		apply = this.repository.save(apply);		
		objectCaches.put(apply.getId(), apply);
		objectCaches.put(S.c("u", apply.getJobId(), "-", apply.getOwnerId()), apply);
		return apply;
	}
	
	private JobApply getEntity(Long id) {
		JobApply ret = this.objectCaches.get(id, JobApply.class);
		if(null == ret){
			Optional<JobApply> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

}
