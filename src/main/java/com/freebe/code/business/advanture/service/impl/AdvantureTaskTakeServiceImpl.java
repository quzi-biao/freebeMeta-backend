package com.freebe.code.business.advanture.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.advanture.controller.param.AdvantureCardParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeAuditParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeQueryParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeSubmitParam;
import com.freebe.code.business.advanture.entity.AdvantureTaskTake;
import com.freebe.code.business.advanture.repository.AdvantureTaskTakeRepository;
import com.freebe.code.business.advanture.service.AdvantureCardService;
import com.freebe.code.business.advanture.service.AdvantureTaskService;
import com.freebe.code.business.advanture.service.AdvantureTaskTakeService;
import com.freebe.code.business.advanture.type.AdvantureTaskTakeState;
import com.freebe.code.business.advanture.type.Constant;
import com.freebe.code.business.advanture.vo.AdvantureCardVO;
import com.freebe.code.business.advanture.vo.AdvantureTaskTakeVO;
import com.freebe.code.business.advanture.vo.AdvantureTaskVO;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.business.meta.vo.RoleVO;
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
public class AdvantureTaskTakeServiceImpl extends BaseServiceImpl<AdvantureTaskTake> implements AdvantureTaskTakeService {
	@Autowired
	private AdvantureTaskTakeRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdvantureTaskService advantureTaskService;
	
	@Autowired
	private AdvantureCardService advantureCardService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private ObjectCaches objectCaches;
	
	private Object createLock = new Object();

	@Override
	public AdvantureTaskTakeVO findById(Long id) throws CustomException {
		AdvantureTaskTake ret = this.objectCaches.get(id, AdvantureTaskTake.class);
		if(null == ret){
			Optional<AdvantureTaskTake> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return toVO(ret);
	}

	@Override
	public AdvantureTaskTakeVO createOrUpdate(AdvantureTaskTakeParam param) throws CustomException {
		Long userId = this.getCurrentUser().getId();
		AdvantureTaskTake take = new AdvantureTaskTake();
		take.setTakerId(userId);
		take.setTaskId(param.getTaskId());
		take.setIsDelete(false);
		take.setState(AdvantureTaskTakeState.DOING);
		
		List<AdvantureTaskTake> ret = this.repository.findAll(Example.of(take));
		if(null != ret && ret.size() > 0) {
			throw new CustomException("您已经领取了该任务");
		}
		
		AdvantureTaskTake e = this.getUpdateEntity(param, false);
		
		// 如果领取任务时发现没有经验卡，则创建
		AdvantureCardVO card = this.advantureCardService.findByUserId(userId);
		if(null == card) {
			synchronized (createLock) {
				card = this.advantureCardService.findByUserId(userId);
				if(null == card) {
					card = this.advantureCardService.createOrUpdate(new AdvantureCardParam());
				}
			}
		}
		
		if(System.currentTimeMillis() - card.getEndTime() > 0) {
			throw new CustomException("您的冒险旅程已经结束，请等待下一次冒险之旅的开始");
		}
		
		// 通过线为 600
		if(card.getExperience() >= Constant.EXPERIENCE) {
			throw new CustomException("您已经通过考核，不需要再领取任务了");
		}
		
		e.setTakerId(userId);
		e.setTaskId(param.getTaskId());
		e.setTakeTime(System.currentTimeMillis());
		e.setState(AdvantureTaskTakeState.DOING);

		e = repository.save(e);

		AdvantureTaskTakeVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		return vo;
	}
	

	@Override
	public AdvantureTaskTakeVO submit(AdvantureTaskTakeSubmitParam param) throws CustomException {
		AdvantureTaskTake e = this.getById(param.getTakeId());
		if(null == e) {
			throw new CustomException("您还未领取任务");
		}
		
		if(e.getTakerId().longValue() != this.getCurrentUser().getId().longValue()) {
			throw new CustomException("您无此操作权限");
		}
		
		if(e.getState() != AdvantureTaskTakeState.DOING) {
			throw new CustomException("状态异常");
		}
		
		e.setSubmitTime(System.currentTimeMillis());
		e.setSubmitDescription(param.getDescription());
		
		e.setState(AdvantureTaskTakeState.WAIT_AUDIT);
		e = this.repository.save(e);
		
		AdvantureTaskTakeVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		return vo;
	}

	@Override
	public AdvantureTaskTakeVO audit(AdvantureTaskTakeAuditParam param) throws CustomException {
		AdvantureTaskTake e = this.getById(param.getTakeId());
		if(null == e) {
			throw new CustomException("不存在");
		}
		
		checkPermssion();
		
		if(e.getState() != AdvantureTaskTakeState.WAIT_AUDIT) {
			throw new CustomException("状态异常");
		}
		
		AdvantureTaskVO task = this.advantureTaskService.findById(e.getTaskId());
		
		e.setAuditTime(System.currentTimeMillis());
		if(param.getPass()) {
			e.setState(AdvantureTaskTakeState.DONE);
			this.advantureCardService.addExperience(e.getTakerId(), task.getExperience());
		}else {
			e.setState(AdvantureTaskTakeState.FAILED);
		}
		
		e = this.repository.save(e);
		
		AdvantureTaskTakeVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		return vo;
	}

	@Override
	public AdvantureTaskTakeVO giveOut(AdvantureTaskTakeSubmitParam param) throws CustomException {
		AdvantureTaskTake e = this.getById(param.getTakeId());
		if(null == e) {
			throw new CustomException("您还未领取任务");
		}
		
		if(e.getTakerId().longValue() != this.getCurrentUser().getId().longValue()) {
			throw new CustomException("您无此操作权限");
		}
		
		if(e.getState() != AdvantureTaskTakeState.DOING) {
			throw new CustomException("状态异常");
		}
		
		e.setSubmitTime(System.currentTimeMillis());
		e.setState(AdvantureTaskTakeState.GIVEOUT);
		e = this.repository.save(e);
		
		AdvantureTaskTakeVO vo = toVO(e);
		objectCaches.put(vo.getId(), e);
		return vo;
	}


	@Override
	public Page<AdvantureTaskTakeVO> queryPage(AdvantureTaskTakeQueryParam param) throws CustomException {
		param.setOrder("id");
		
		PageRequest request = PageUtils.toPageRequest(param);
		
		if(!StringUtils.isEmpty(param.getTakerName())) {
			List<Long> takerIds = this.userService.queryUserByName(param.getTakerName());
			if(null == takerIds || takerIds.size() == 0) {
				return new PageImpl<AdvantureTaskTakeVO>(new ArrayList<>(), request, 0);
			}
			param.setTakerIds(takerIds);
		}

		Specification<AdvantureTaskTake> example = buildSpec(param);

		Page<AdvantureTaskTake> page = repository.findAll(example, request);
		List<AdvantureTaskTakeVO> retList = new ArrayList<>();

		for(AdvantureTaskTake e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<AdvantureTaskTakeVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<AdvantureTaskTake> buildSpec(AdvantureTaskTakeQueryParam param) throws CustomException {
		return new Specification<AdvantureTaskTake>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<AdvantureTaskTake> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<AdvantureTaskTake> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("state", param.getState());
				if(null != param.getTakerIds() && param.getTakerIds().size()> 0){
					builder.addIn("takerId", param.getTakerIds());
				}else {
					builder.addEqual("takerId", param.getTakerId());
				}
				
				builder.addEqual("taskId", param.getTaskId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private AdvantureTaskTakeVO toVO(AdvantureTaskTake e) throws CustomException {
		AdvantureTaskTakeVO vo = new AdvantureTaskTakeVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setTakerId(e.getTakerId());
		vo.setTaskId(e.getTaskId());
		vo.setTakeTime(e.getTakeTime());
		vo.setSubmitTime(e.getSubmitTime());
		vo.setAuditTime(e.getAuditTime());
		vo.setSubmitDescription(e.getSubmitDescription());
		vo.setAuditDescription(e.getAuditDescription());
		vo.setState(e.getState());
		
		vo.setTaker(userService.getUser(e.getTakerId()));
		vo.setTask(advantureTaskService.findById(vo.getTaskId()));

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, AdvantureTaskTakeVO.class);
		super.softDelete(id);
	}
	

	private void checkPermssion() throws CustomException {
		Long currUser = this.getCurrentUser().getId();
		MemberVO member = this.memberService.findByUserId(currUser);
		
		if(null == member.getRoles() || member.getRoles().size() == 0) {
			throw new CustomException("请联系冒险者审核官执行此操作");
		}
		
		for(RoleVO role : member.getRoles()) {
			if(TransactionService.ROLE_ADVANTURE_AUDITOR_CODE.equals(role.getRoleCode())) {
				return;
			}
		}
		throw new CustomException("请联系冒险者审核官执行此操作");
	}
}
