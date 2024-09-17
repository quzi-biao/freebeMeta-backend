package com.freebe.code.business.badge.service.impl;


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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.badge.controller.param.BadgeActionParam;
import com.freebe.code.business.badge.controller.param.BadgeHoldParam;
import com.freebe.code.business.badge.controller.param.BadgeHoldQueryParam;
import com.freebe.code.business.badge.entity.BadgeHold;
import com.freebe.code.business.badge.repository.BadgeHoldRepository;
import com.freebe.code.business.badge.service.BadgeActionRecordService;
import com.freebe.code.business.badge.service.BadgeHoldService;
import com.freebe.code.business.badge.service.BadgeService;
import com.freebe.code.business.badge.type.BadgeActionType;
import com.freebe.code.business.badge.vo.BadgeHoldVO;
import com.freebe.code.business.badge.vo.BadgeVO;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class BadgeHoldServiceImpl extends BaseServiceImpl<BadgeHold> implements BadgeHoldService {
	@Autowired
	private BadgeHoldRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private BadgeService badgeService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BadgeActionRecordService actionRecordService;

	@Override
	public BadgeHoldVO findById(Long id) throws CustomException {
		BadgeHold ret = this.objectCaches.get(id, BadgeHold.class);
		if(null == ret){
			Optional<BadgeHold> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return toVO(ret);
	}
	
	@Transactional
	@Override
	public BadgeHoldVO awardBadge(BadgeActionParam param) throws CustomException {
		if(null == param.getBadgeId() || null == param.getMemberId()) {
			throw new CustomException("徽章发放参数错误");
		}
		
		checkPermission();
		
		MemberVO member = this.memberService.findById(param.getMemberId());
		if(null == member) {
			throw new CustomException("请先获得成员身份");
		}
		
		BadgeVO badge = this.badgeService.findById(param.getBadgeId());
		if(null == badge) {
			throw new CustomException("徽章不存在");
		}
		
		param.setActionType(BadgeActionType.AWARD);
		this.actionRecordService.createOrUpdate(param);
		
		BadgeHoldParam holdParam = new BadgeHoldParam();
		holdParam.setBadgeId(param.getBadgeId());	
		holdParam.setMemberId(param.getMemberId());		
		
		BadgeHoldVO ret = this.createOrUpdate(holdParam);
		
		this.badgeService.incHolderNumber(param.getBadgeId());
		
		return ret;
	}

	@Override
	public BadgeHoldVO takeBackBadge(BadgeActionParam param) throws CustomException {
		if(null == param.getBadgeId() || null == param.getMemberId()) {
			throw new CustomException("徽章回收参数错误");
		}
		
		checkPermission();
		
		BadgeHold hold = this.getBadge(param.getBadgeId(), param.getMemberId());
		if(null == hold) {
			throw new CustomException("他(她)没有这个徽章");
		}
		
		param.setActionType(BadgeActionType.RETAKE);
		this.actionRecordService.createOrUpdate(param);
		
		hold.setInHold(false);
		hold = this.repository.save(hold);
		this.badgeService.decHolderNumber(param.getBadgeId());
		
		objectCaches.put(hold.getId(), hold);
		
		return toVO(hold);
	}

	@Override
	public BadgeHoldVO giveOutBadge(BadgeActionParam param) throws CustomException {
		if(null == param.getBadgeId() || null == param.getMemberId()) {
			throw new CustomException("徽章放弃参数错误");
		}
		
		param.setMemberId(this.getCurrentMemeber().getId());
		
		BadgeHold hold = this.getBadge(param.getBadgeId(), param.getMemberId());
		if(null == hold) {
			throw new CustomException("你没有这个徽章");
		}
		
		param.setActionType(BadgeActionType.GIVEOUT);
		this.actionRecordService.createOrUpdate(param);
		
		hold.setInHold(false);
		hold = this.repository.save(hold);
		this.badgeService.decHolderNumber(param.getBadgeId());
		
		objectCaches.put(hold.getId(), hold);
		
		return toVO(hold);
	}

	@Override
	public BadgeHoldVO createOrUpdate(BadgeHoldParam param) throws CustomException {
		if(null == param.getMemberId() || null == param.getBadgeId()) {
			throw new CustomException("徽章持有参数错误");
		}
		
		MemberVO member = this.memberService.findById(param.getMemberId());
		if(null == member) {
			throw new CustomException("成员信息不存在");
		}
		
		BadgeVO badge = this.badgeService.findById(param.getBadgeId());
		if(null == badge) {
			throw new CustomException("徽章不存在");
		}
		
		BadgeHold e = this.getBadge(param.getBadgeId(), param.getMemberId());
		if(null != e) {
			throw new CustomException(member.getName() + "已经持有该徽章");
		}
		
		e = this.getUpdateEntity(param, false);

		e.setBadgeId(param.getBadgeId());
		e.setMemberId(param.getMemberId());
		e.setInHold(true);

		e = repository.save(e);

		BadgeHoldVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return vo;
	}

	@Override
	public List<BadgeHoldVO> queryHold(BadgeHoldQueryParam param) throws CustomException {
		Specification<BadgeHold> example = buildSpec(param);

		List<BadgeHold> list = repository.findAll(example);
		List<BadgeHoldVO> retList = new ArrayList<>();

		for(BadgeHold e:  list) {
			retList.add(toVO(e));
		}
		return retList;
	}

	private Specification<BadgeHold> buildSpec(BadgeHoldQueryParam param) throws CustomException {
		return new Specification<BadgeHold>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<BadgeHold> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<BadgeHold> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("inHold", true);
				
				builder.addEqual("badgeId", param.getBadgeId());
				
				if(null != param.getMemberIds() && param.getMemberIds().size() > 0) {
					builder.addIn("memberId", param.getMemberIds());
				}else {
					builder.addEqual("memberId", param.getMemberId());
				}

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private BadgeHoldVO toVO(BadgeHold e) throws CustomException {
		BadgeHoldVO vo = new BadgeHoldVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setBadge(badgeService.findById(e.getBadgeId()));
		vo.setMember(memberService.findById(e.getMemberId()));
		vo.setInHold(e.getInHold());

		return vo;
	}
	
	private BadgeHold getBadge(Long badgeId, Long memberId) throws CustomException {
		BadgeHold e = new BadgeHold();
		e.setMemberId(memberId);
		e.setBadgeId(badgeId);
		e.setInHold(true);
		
		List<BadgeHold> holds = this.repository.findAll(Example.of(e));
		if(null == holds || holds.size() == 0) {
			return null;
		}
		
		return holds.get(0);
	}


	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, BadgeHoldVO.class);
		super.softDelete(id);
	}
	

	private void checkPermission() throws CustomException {
		this.checkPermssion(BADGE_MANAGER);
	}

}
