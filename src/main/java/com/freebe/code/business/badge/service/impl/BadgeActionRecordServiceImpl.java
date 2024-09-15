package com.freebe.code.business.badge.service.impl;


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

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.badge.controller.param.BadgeActionParam;
import com.freebe.code.business.badge.controller.param.BadgeActionRecordQueryParam;
import com.freebe.code.business.badge.entity.BadgeActionRecord;
import com.freebe.code.business.badge.repository.BadgeActionRecordRepository;
import com.freebe.code.business.badge.service.BadgeActionRecordService;
import com.freebe.code.business.badge.service.BadgeService;
import com.freebe.code.business.badge.vo.BadgeActionRecordVO;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.service.MemberService;
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
public class BadgeActionRecordServiceImpl extends BaseServiceImpl<BadgeActionRecord> implements BadgeActionRecordService {
	@Autowired
	private BadgeActionRecordRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BadgeService badgeService;

	@Override
	public BadgeActionRecordVO findById(Long id) throws CustomException {
		BadgeActionRecordVO ret = this.objectCaches.get(id, BadgeActionRecordVO.class);
		if(null == ret){
			Optional<BadgeActionRecord> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public BadgeActionRecordVO createOrUpdate(BadgeActionParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("请输入操作理由");
		}
		
		BadgeActionRecord e = this.getUpdateEntity(param, false);

		e.setOperator(this.getCurrentUser().getId());
		e.setBadgeId(param.getBadgeId());
		e.setMemberId(param.getMemberId());
		e.setDescription(param.getDescription());
		e.setActionTime(System.currentTimeMillis());
		
		e.setActionType(param.getActionType());

		e = repository.save(e);

		BadgeActionRecordVO vo = toVO(e);

		return vo;
	}

	@Override
	public Page<BadgeActionRecordVO> queryPage(BadgeActionRecordQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<BadgeActionRecord> example = buildSpec(param);

		Page<BadgeActionRecord> page = repository.findAll(example, request);
		List<BadgeActionRecordVO> retList = new ArrayList<>();

		for(BadgeActionRecord e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<BadgeActionRecordVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<BadgeActionRecord> buildSpec(BadgeActionRecordQueryParam param) throws CustomException {
		return new Specification<BadgeActionRecord>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<BadgeActionRecord> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<BadgeActionRecord> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("actionType", param.getActionType());
				builder.addEqual("badgeId", param.getBadgeId());
				builder.addEqual("memberId", param.getMemberId());
				builder.addEqual("operator", param.getOperator());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private BadgeActionRecordVO toVO(BadgeActionRecord e) throws CustomException {
		BadgeActionRecordVO vo = new BadgeActionRecordVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOperator(this.userService.getUser(e.getOperator()));
		vo.setBadge(this.badgeService.findById(e.getBadgeId()));
		vo.setMember(this.memberService.findById(e.getMemberId()));
		
		vo.setDescription(e.getDescription());
		vo.setActionTime(e.getActionTime());
		vo.setActionType(e.getActionType());
		vo.setTransactionHash(e.getTransactionHash());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, BadgeActionRecordVO.class);
		super.softDelete(id);
	}

}
