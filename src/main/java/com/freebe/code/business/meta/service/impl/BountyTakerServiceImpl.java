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

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.BountyDoneParam;
import com.freebe.code.business.meta.controller.param.BountyGiveoutParam;
import com.freebe.code.business.meta.controller.param.BountyTakerParam;
import com.freebe.code.business.meta.controller.param.BountyTakerQueryParam;
import com.freebe.code.business.meta.entity.Bounty;
import com.freebe.code.business.meta.entity.BountyTaker;
import com.freebe.code.business.meta.repository.BountyTakerRepository;
import com.freebe.code.business.meta.service.BountyService;
import com.freebe.code.business.meta.service.BountyTakerService;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.type.BountyState;
import com.freebe.code.business.meta.type.BountyTakeLimit;
import com.freebe.code.business.meta.type.BountyTakerState;
import com.freebe.code.business.meta.type.MessageType;
import com.freebe.code.business.meta.vo.BountyTakerVO;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.business.meta.vo.ProjectVO;
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
public class BountyTakerServiceImpl extends BaseServiceImpl<BountyTaker> implements BountyTakerService {
	@Autowired
	private BountyTakerRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private BountyService bountyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;

	@Override
	public BountyTakerVO findById(Long id) throws CustomException {
		BountyTakerVO ret = this.objectCaches.get(id, BountyTakerVO.class);
		if(null == ret){
			Optional<BountyTaker> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public BountyTaker save(BountyTaker e) {
		BountyTaker t = super.save(e);
		objectCaches.delete(e.getId(), BountyTakerVO.class);
		return t;
	}

	@Transactional
	@Override
	public BountyTakerVO createOrUpdate(BountyTakerParam param) throws CustomException {
		if(null == param.getBountyId()) {
			throw new CustomException("参数错误");
		}
		
		Bounty bounty = this.bountyService.getReference(param.getBountyId());
		if(null == bounty) {
			throw new CustomException("悬赏不存在");
		}
		if(bounty.getState() != BountyState.WAIT_TAKER) {
			throw new CustomException("悬赏不可认领");
		}
		
//		long waitTime = System.currentTimeMillis() - bounty.getCreateTime();
//		if(waitTime > bounty.getTakerWaitTime() * DateUtils.DAY_PERIOD) {
//			bounty.setState(BountyState.WAIT_TIMEOUT_FAILED);
//			this.bountyService.update(bounty.getId(), bounty);
//			throw new CustomException("悬赏已超时失败");
//		}
		
		if(null != bounty.getProjectId() && bounty.getProjectId() > 0) {
			ProjectVO project = this.projectService.findById(bounty.getProjectId());
			Integer takeLimit = project.getBountyTakeLimit();
			if(takeLimit == BountyTakeLimit.MEMBER) {
				if(!this.isMember(getCurrentUser().getId(), project.getMembers())) {
					throw new CustomException("只有项目组成员才能认领悬赏，请联系项目管理员");
				}
			}
		
		}
		
		BountyTaker e = this.getUpdateEntity(param, false);

		e.setBountyId(param.getBountyId());
		e.setTakeTime(System.currentTimeMillis());
		e.setTaker(getCurrentUser().getId());
		e.setState(BountyTakerState.NORMAL);

		e = repository.save(e);
		this.bountyService.updateTake(e.getBountyId(), e.getId());
		BountyTakerVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);
		
		this.sendMessage(e.getTaker(), bounty.getOwnerId(), S.c("您的悬赏[#", bounty.getId(), "]", bounty.getName(), "已被认领，认领人: ", this.getCurrentUser().getName()), MessageType.BOUNTY_TAKE_MSG);

		return vo;
	}
	

	@Transactional
	@Override
	public BountyTakerVO giveOut(BountyGiveoutParam param) throws CustomException {
		if(null == param.getTakeId()) {
			throw new CustomException("参数错误");
		}
		
		BountyTaker take = this.getById(param.getTakeId());
		Bounty bounty = this.bountyService.getReference(take.getBountyId());
		if(take.getState() != BountyTakerState.NORMAL) {
			throw new CustomException("悬赏状态异常");
		}
		
		checkBounty(take, bounty);
		
		take.setDoneTime(System.currentTimeMillis());
		take.setState(BountyTakerState.GIVE_OUT);
		take.setGiveoutReason(param.getReason());
		
		// 放弃认领后，悬赏可重新认领
		this.bountyService.restartBounty(bounty.getId());
		
		BountyTakerVO vo = toVO(take);
		objectCaches.put(vo.getId(), vo);
		
		this.sendMessage(take.getTaker(), bounty.getOwnerId(), S.c("认领人放弃了您的悬赏[", bounty.getId(), "]", bounty.getName(), "，您的悬赏可被重新认领"), MessageType.BOUNTY_SUBMIT_MSG);
		
		return vo;
	}

	@Transactional
	@Override
	public BountyTakerVO doneBounty(BountyDoneParam param) throws CustomException {
		if(null == param.getTakeId()) {
			throw new CustomException("参数错误");
		}
		
		BountyTaker take = this.getById(param.getTakeId());
		Bounty bounty = this.bountyService.getReference(take.getBountyId());
		if(take.getState() != BountyTakerState.NORMAL && 
				!(take.getState() == BountyTakerState.DONE && bounty.getState() == BountyState.WAIT_AUDIT)) {
			throw new CustomException("悬赏状态异常");
		}
		
		checkBounty(take, bounty);
		
		take.setSubmitPictures(toStr(param.getSubmitPictures()));
		take.setSubmitFiles(toStr(param.getSubmitFiles()));
		take.setSubmitDescription(param.getSubmitDescription());
		take.setDoneTime(System.currentTimeMillis());
		take.setState(BountyTakerState.DONE);
		take.setCostTime(take.getDoneTime() - take.getTakeTime());
		
		take = this.repository.save(take);
		
		this.bountyService.doneBounty(bounty.getId());
		BountyTakerVO vo = toVO(take);
		objectCaches.put(vo.getId(), vo);
		
		this.sendMessage(take.getTaker(), bounty.getOwnerId(), S.c(this.getCurrentUser().getName(), "完成了您的悬赏: [#", bounty.getId(), "]", bounty.getName()), MessageType.BOUNTY_SUBMIT_MSG);
		
		return vo;
	}

//	private boolean checkBountyTimeLimit(BountyTaker take, Bounty bounty) {
//		if(bounty.getState() == BountyState.TIMEOUT_FAILED) {
//			return false;
//		}
//		
//		long costTime = System.currentTimeMillis() - take.getTakeTime();
//		if(costTime > bounty.getLimitTime() * DateUtils.DAY_PERIOD) {
//			bounty.setState(BountyState.TIMEOUT_FAILED);
//			this.bountyService.update(bounty.getId(), bounty);
//			return false;
//		}
//		
//		return true;
//	}

	@Override
	public Page<BountyTakerVO> queryPage(BountyTakerQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<BountyTaker> example = buildSpec(param);

		Page<BountyTaker> page = repository.findAll(example, request);
		List<BountyTakerVO> retList = new ArrayList<>();

		for(BountyTaker e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<BountyTakerVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<BountyTaker> buildSpec(BountyTakerQueryParam param) throws CustomException {
		return new Specification<BountyTaker>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<BountyTaker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<BountyTaker> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("state", param.getState());
				builder.addEqual("taker", param.getTaker());
				builder.addEqual("bountyId", param.getBountyId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private BountyTakerVO toVO(BountyTaker e) throws CustomException {
		BountyTakerVO vo = new BountyTakerVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setBountyId(e.getBountyId());
		vo.setTakeTime(e.getTakeTime());
		vo.setTaker(userService.getUser(e.getTaker()));
		
		vo.setDoneTime(e.getDoneTime());
		vo.setState(e.getState());
		vo.setEvaluate(e.getEvaluate());
		vo.setFreeBe(e.getFreeBe());
		vo.setTransactionId(e.getTransactionId());
		vo.setSubmitFiles(toList(e.getSubmitFiles()));
		vo.setSubmitPictures(toList(e.getSubmitPictures()));
		vo.setSubmitDescription(e.getSubmitDescription());
		vo.setGiveoutReason(e.getGiveoutReason());
		
		return vo;
	}
	
	/**
	 * 检查悬赏的状态
	 * @param take
	 * @param bounty
	 * @throws CustomException
	 */
	private void checkBounty(BountyTaker take, Bounty bounty) throws CustomException {
//		if(!checkBountyTimeLimit(take, bounty)) {
//			take.setState(BountyTakerState.TIMEOUT_CANCEL);
//			take.setDoneTime(System.currentTimeMillis());
//			this.repository.save(take);
//			throw new CustomException("悬赏已超时");
//		}
		
		if(bounty.getState() != BountyState.RUNNING && bounty.getState() != BountyState.WAIT_AUDIT) {
			throw new CustomException("悬赏状态异常");
		}
		
		if(bounty.getTakerId().longValue() != getCurrentUser().getId()) {
			throw new CustomException("您不是悬赏认领者");
		}
	}
	
	private boolean isMember(Long id, List<ProjectMemberVO> members) {
		if(null == members || members.size() == 0) {
			return true;
		}
		
		for(ProjectMemberVO m : members) {
			if(m.getMember().getUser().getId().longValue() == id.longValue()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, BountyTakerVO.class);
		super.softDelete(id);
	}
}
